/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.dialog;
import com.qlbh.qlbhlaptop.dto.TopCustomerDTO;
import com.qlbh.qlbhlaptop.util.CsvExportUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author Luka
 */
public class KhachHang_TopCustomers_Dialog extends JDialog {
    private JTable table;

    public KhachHang_TopCustomers_Dialog(JFrame parent,
                                         List<TopCustomerDTO> data,
                                         LocalDate from, LocalDate to) {
        super(parent, "Top khách hàng toàn hệ thống", true);
        setSize(650, 440);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Top khách hàng (" + from + " → " + to + ")");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Mã KH", "Tên KH", "Số đơn", "Tổng doanh thu (VND)"};
        DefaultTableModel m = new DefaultTableModel(cols, 0){ public boolean isCellEditable(int r,int c){return false;} };
        for (TopCustomerDTO d : data) {
            m.addRow(new Object[]{
                    d.getMaKH(), d.getTenKH(), d.getSoDon(),
                    String.format("%,.0f", d.getTongDoanhThu())
            });
        }

        table = new JTable(m);
        table.setRowHeight(24);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnExport = new JButton("Xuất CSV");
        btnExport.addActionListener(e ->
                CsvExportUtil.exportTableToCSV(table, this, "top-khach-hang.csv"));

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnExport);
        south.add(btnClose);
        add(south, BorderLayout.SOUTH);
    }
}