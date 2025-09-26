// com.qlbh.qlbhlaptop.dialog.NhanVien_TopSale_Dialog
package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.dto.RevenueByEmployeeDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class NhanVien_TopSale_Dialog extends JDialog {
    public NhanVien_TopSale_Dialog(JFrame parent, List<RevenueByEmployeeDTO> data,
                                   LocalDate from, LocalDate to) {
        super(parent, "Top doanh thu nhân viên", true);
        setSize(650, 420);
        setLocationRelativeTo(parent);

        String subtitle = (from == null || to == null) ? "(toàn thời gian)"
                : "(" + from + " → " + to + ")";
        JLabel lbl = new JLabel("Top doanh thu nhân viên " + subtitle);
        lbl.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));

        String[] cols = {"Mã NV", "Tên NV", "Số đơn", "Doanh thu (VND)", "Giá trị TB/đơn"};
        DefaultTableModel m = new DefaultTableModel(cols, 0){
            public boolean isCellEditable(int r,int c){return false;}
        };
        for (RevenueByEmployeeDTO d : data) {
            m.addRow(new Object[]{
                d.getMaNV(), d.getTenNV(), d.getSoDonHang(),
                String.format("%,.0f", d.getTongDoanhThu()),
                String.format("%,.0f", d.getAvgOrderValue())
            });
        }
        JTable tbl = new JTable(m); tbl.setRowHeight(24);
        tbl.setAutoCreateRowSorter(true);

        add(lbl, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);

        JButton close = new JButton("Đóng");
        close.addActionListener(e -> dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(close);
        add(south, BorderLayout.SOUTH);
    }
}
