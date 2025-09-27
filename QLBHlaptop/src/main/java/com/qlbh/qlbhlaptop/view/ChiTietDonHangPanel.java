/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class ChiTietDonHangPanel extends JPanel {

    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnUpdate, btnDelete;
    private JTable tblChiTiet;

    public ChiTietDonHangPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding

        // --- Thanh tìm kiếm & nút thêm ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        txtSearch = new JTextField(25); // to hơn
        btnSearch = createButton("Tìm kiếm");
        btnAdd = createButton("Thêm");
        searchPanel.add(new JLabel("TÌM MÃ ĐƠN HÀNG:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnAdd);

        add(searchPanel, BorderLayout.NORTH);

        // --- Bảng chi tiết đơn hàng ---
        tblChiTiet = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"MÃ ĐƠN HÀNG", "MÃ SẢN PHẨM", "SỐ LƯỢNG", "ĐƠN GIÁ"}
        ));

        // Làm header bảng nổi bật
        JTableHeader header = tblChiTiet.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(200, 200, 200));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        tblChiTiet.setRowHeight(28);
        tblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        // --- Các nút Update / Delete ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnUpdate = createButton("Cập nhật");
        btnDelete = createButton("Xóa");
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);

        add(actionPanel, BorderLayout.SOUTH);
    }

    // --- Hàm tạo button có style ---
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(66, 135, 245));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    // --- Getter để Controller có thể thao tác ---
    public JTextField getTxtSearch() { return txtSearch; }
    public JButton getBtnSearch() { return btnSearch; }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnUpdate() { return btnUpdate; }
    public JButton getBtnDelete() { return btnDelete; }
    public JTable getTblChiTiet() { return tblChiTiet; }
}
