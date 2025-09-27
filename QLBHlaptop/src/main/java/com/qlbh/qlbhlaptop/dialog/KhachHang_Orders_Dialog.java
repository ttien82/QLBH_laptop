// com.qlbh.qlbhlaptop.dialog.KhachHang_Orders_Dialog
package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.dto.OrderOfCustomerDTO;
import com.qlbh.qlbhlaptop.util.CsvExportUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KhachHang_Orders_Dialog extends JDialog {
    private JTable table;

    public KhachHang_Orders_Dialog(JFrame parent, String tenKH, List<OrderOfCustomerDTO> data) {
        super(parent, "Đơn hàng của KH: " + tenKH, true);
        setSize(720, 460);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Đơn hàng (toàn thời gian) của: " + tenKH);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Ngày", "Mã đơn", "NV lập", "Tổng tiền (VND)"};
        DefaultTableModel m = new DefaultTableModel(cols, 0){ public boolean isCellEditable(int r,int c){return false;} };

        if (data == null || data.isEmpty()) {
            // không có đơn — vẫn hiển thị bảng rỗng
        } else {
            for (OrderOfCustomerDTO d : data) {
                m.addRow(new Object[]{
                        d.getNgayLap(),                 // LocalDate toString: yyyy-MM-dd
                        d.getMaDH(),
                        d.getMaNV() + " - " + d.getTenNV(),
                        String.format("%,.0f", d.getTongTien())
                });
            }
        }

        table = new JTable(m);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnExport = new JButton("Xuất CSV");
        btnExport.addActionListener(e ->
                CsvExportUtil.exportTableToCSV(table, this,
                        "don-hang-cua-" + tenKH.replaceAll("\\s+","_") + ".csv"));

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnExport);
        south.add(btnClose);
        add(south, BorderLayout.SOUTH);
    }
}
