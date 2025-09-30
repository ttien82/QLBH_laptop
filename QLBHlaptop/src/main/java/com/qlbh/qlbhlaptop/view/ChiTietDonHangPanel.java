package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.ChiTietDonHangDAO;
import com.qlbh.qlbhlaptop.dto.ChiTietDonHangViewDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;


public class ChiTietDonHangPanel extends JPanel {
    private JTable tbl;
    private JButton btnSearch, btnAdd, btnEdit, btnDelete;
    private JTextField txtSearch;

    public ChiTietDonHangPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // --- Header (Tìm kiếm + nút thêm) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.setToolTipText("Nhập mã đơn hàng...");

        btnSearch = new JButton(" Tìm kiếm");
        styleButton(btnSearch, new Color(66, 133, 244));

        btnAdd = new JButton(" Thêm");
        styleButton(btnAdd, new Color(15, 157, 88));
        btnAdd.setVisible(false);
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnAdd);

        add(topPanel, BorderLayout.NORTH);

        // --- Bảng chi tiết ---
        String[] columnNames = {"Mã Đơn Hàng", "Mã Sản Phẩm", "Số lượng", "Đơn giá"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        tbl = new JTable(model);

        tbl.setFont(new Font("Arial", Font.PLAIN, 14));
        tbl.setRowHeight(28);

        JTableHeader header = tbl.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));

        // căn giữa header và dữ liệu số
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbl.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tbl.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tbl);
        add(scrollPane, BorderLayout.CENTER);

        // --- Footer (Cập nhật + Xóa) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        btnEdit = new JButton("️ Cập nhật");
        styleButton(btnEdit, new Color(52, 168, 83));
        btnEdit.setVisible(false);
        btnDelete = new JButton("️ Xóa");
        styleButton(btnDelete, new Color(234, 67, 53));

        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Hàm làm đẹp nút
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    // Load dữ liệu từ DAO
    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        model.setRowCount(0);

        ChiTietDonHangDAO dao = new ChiTietDonHangDAO();
        List<ChiTietDonHangViewDTO> list = dao.getAllWithDonHang();

        for (ChiTietDonHangViewDTO ct : list) {
            model.addRow(new Object[]{
                    ct.getMaDH(),
                    ct.getMaSP(),
                    ct.getSoLuong(),
                    ct.getDonGia()
            });
        }
    }

    // Getter cho Controller
    public JButton getBtnSearch() { return btnSearch; }
    public JTextField getTxtSearch() { return txtSearch; }
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnUpdate() { return btnEdit; }
    public JButton getBtnDelete() { return btnDelete; }
    public JTable getTblChiTiet() { return tbl; }
}
