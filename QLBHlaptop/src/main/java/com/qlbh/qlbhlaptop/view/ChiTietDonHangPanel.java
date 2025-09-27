/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ChiTietDonHangPanel extends JPanel {

    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnUpdate, btnDelete;
    private JTable tblChiTiet;

    public ChiTietDonHangPanel() {
        setLayout(new BorderLayout());

        // --- Thanh tìm kiếm & nút thêm ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Tìm kiếm");
        btnAdd = new JButton("Thêm");
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnAdd);

        add(searchPanel, BorderLayout.NORTH);

        // --- Bảng chi tiết đơn hàng ---
        tblChiTiet = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Mã ĐH", "Mã SP", "Số lượng", "Đơn giá"}
        ));
        add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        // --- Các nút Update / Delete ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);

        add(actionPanel, BorderLayout.SOUTH);
    }

    // --- Getter để Controller có thể thao tác ---
    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JTable getTblChiTiet() {
        return tblChiTiet;
    }
}
