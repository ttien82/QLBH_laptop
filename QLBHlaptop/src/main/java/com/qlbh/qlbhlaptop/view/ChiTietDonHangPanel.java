package com.qlbh.qlbhlaptop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChiTietDonHangPanel extends JPanel {

    private JTextField txtSearch;
    private JButton btnSearch, btnAdd, btnUpdate, btnDelete;
    private JTable tblChiTiet;

    public ChiTietDonHangPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding ngoài viền

        // --- Thanh tìm kiếm & nút thêm ---
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnSearch = createStyledButton("Tìm kiếm", new Color(0, 123, 255));
        btnAdd = createStyledButton("Thêm", new Color(0, 200, 180));

        leftPanel.add(txtSearch);
        leftPanel.add(btnSearch);
        rightPanel.add(btnAdd);

        searchPanel.add(leftPanel, BorderLayout.WEST);
        searchPanel.add(rightPanel, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);

        // --- Bảng chi tiết đơn hàng ---
        DefaultTableModel model = new DefaultTableModel(
                new Object[][]{
                        {"DH001", "SP001", 3, "15000000"},
                        {"DH002", "SP003", 1, "45000000"},
                        {"DH003", "SP002", 2, "28000000"}
                },
                new String[]{"Mã ĐH", "Mã SP", "Số lượng", "Đơn giá"}
        );
        tblChiTiet = new JTable(model);
        tblChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblChiTiet.setRowHeight(28);
        tblChiTiet.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        // --- Các nút Update / Delete ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnUpdate = createStyledButton("Cập nhật", new Color(40, 167, 69)); // xanh lá
        btnDelete = createStyledButton("Xóa", new Color(220, 53, 69));      // đỏ

        actionPanel.add(btnUpdate);
        actionPanel.add(btnDelete);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 36));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
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
