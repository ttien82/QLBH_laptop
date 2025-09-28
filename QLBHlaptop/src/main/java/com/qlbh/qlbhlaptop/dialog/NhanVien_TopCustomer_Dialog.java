
package com.qlbh.qlbhlaptop.dialog;
import com.qlbh.qlbhlaptop.dto.TopCustomerByEmpDTO;
import com.qlbh.qlbhlaptop.util.CsvExportUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class NhanVien_TopCustomer_Dialog extends JDialog {

    public NhanVien_TopCustomer_Dialog(JFrame parent, String tenNV,
                                       List<TopCustomerByEmpDTO> data,
                                       LocalDate from, LocalDate to) {
        super(parent, "Top khách hàng của NV: " + tenNV, true);
        setSize(600, 420);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // ===== Tiêu đề =====
        String sub = (from == null || to == null)
                ? "(Toàn thời gian)"
                : "(" + from + " → " + to + ")";
        JLabel title = new JLabel("Top khách hàng " + sub);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        add(title, BorderLayout.NORTH);

        // ===== Bảng dữ liệu =====
        String[] cols = {"Mã KH", "Tên KH", "Số đơn", "Doanh thu (VND)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (TopCustomerByEmpDTO d : data) {
            model.addRow(new Object[]{
                    d.getMaKH(),
                    d.getTenKH(),
                    d.getSoDon(),
                    String.format("%,.0f", d.getDoanhThu())
            });
        }
        
        // lấy từ jTable name để truyền vào exportcsv util
        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
        // export csv file
        JButton btnExport = new JButton("Xuất CSV");
        // truyền vào đủ tham số đầu vào (JTable (name), this, fname)
        btnExport.addActionListener(e -> {
            String fname = "top-khach-hang-" + tenNV.replaceAll("\\s+","_") + ".csv";
            CsvExportUtil.exportTableToCSV(table, this, fname);   // <— truyền JTable
        });
        // ===== export csv ==== //
        JButton btnClose = new JButton("Đóng");
           // ===== Nút đóng =====
        btnClose.addActionListener(e -> dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnExport);
        south.add(btnClose);
        add(south, BorderLayout.SOUTH);
    }
}
