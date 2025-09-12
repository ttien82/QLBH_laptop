package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.KhachHangDAO;
import com.qlbh.qlbhlaptop.model.KhachHang;
import com.qlbh.qlbhlaptop.dialog.KhachHang_Dialog_Them;
import com.qlbh.qlbhlaptop.dialog.KhachHang_Dialog_Sua;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class KhachHangPanel extends JPanel {

    private KhachHangDAO dao = new KhachHangDAO();
    private List<KhachHang> ListKH;
    private DefaultTableModel model;

    private JTable tblKhachHang;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    public KhachHangPanel() {
        setLayout(new BorderLayout(10, 10));
        initUI();
        initTable();
        fillTableData();
    }

    private void initUI() {
        // Bảng khách hàng
        tblKhachHang = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        add(scrollPane, BorderLayout.CENTER);

        // Panel nút chức năng
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Tải lại");

        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnRefresh);
        panelButtons.add(btnDelete);
        add(panelButtons, BorderLayout.SOUTH);

        // Gán sự kiện
        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnRefresh.addActionListener(e -> fillTableData());
    }

    private void initTable() {
        String[] columns = {"Mã KH", "Tên KH", "Điện thoại", "Email", "Địa chỉ"};
        model = new DefaultTableModel(columns, 0);
        tblKhachHang.setModel(model);
        tblKhachHang.setAutoCreateRowSorter(true);
        tblKhachHang.setDefaultEditor(Object.class, null);

        tblKhachHang.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean rowSelected = tblKhachHang.getSelectedRow() != -1;
                btnEdit.setEnabled(rowSelected);
                btnDelete.setEnabled(rowSelected);
            }
        });

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    private void fillTableData() {
        model.setRowCount(0);
        ListKH = dao.getAll();
        for (KhachHang kh : ListKH) {
            model.addRow(new Object[]{
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getDienThoai(),
                kh.getEmail(),
                kh.getDiaChi()
            });
        }
    }

    private void onAdd() {
        KhachHang_Dialog_Them dialog = new KhachHang_Dialog_Them((JFrame) (Frame) SwingUtilities.getWindowAncestor(this));
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                fillTableData();
            }
            public void windowClosing(WindowEvent e) {
                fillTableData();
            }
        });
        dialog.setVisible(true);
    }

    private void onEdit() {
        int row = tblKhachHang.getSelectedRow();
        if (row == -1) return;

        String maKH = tblKhachHang.getValueAt(row, 0).toString();
        KhachHang_Dialog_Sua dialog = new KhachHang_Dialog_Sua((JFrame) (Frame) SwingUtilities.getWindowAncestor(this), maKH);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                fillTableData();
            }            
            public void windowClosing(WindowEvent e) {
                fillTableData();
            }
        });
        dialog.setVisible(true);
    }

    private void onDelete() {
        int row = tblKhachHang.getSelectedRow();
        if (row == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa Khách Hàng này?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String maKH = tblKhachHang.getValueAt(row, 0).toString();
            dao.delete(maKH);
            fillTableData();
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }

    // Hàm main để chạy thử Panel này trong một JFrame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Khách Hàng");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new KhachHangPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
