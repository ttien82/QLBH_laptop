package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.NhanVienDAO;
import com.qlbh.qlbhlaptop.model.NhanVien;
import com.qlbh.qlbhlaptop.dialog.*;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.logging.Logger;

public class NhanVienPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(NhanVienPanel.class.getName());

    private NhanVienDAO dao = new NhanVienDAO();
    private List<NhanVien> ListNV;
    private DefaultTableModel model;

    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private JTable tblNhanVien;

    public NhanVienPanel() {
        initComponents();
        InitTblNhanVien();
        fillTableData();
    }

    private void InitTblNhanVien() {
        String[] columns = {"Mã NV", "Tên NV", "Điện thoại", "Địa chỉ"};
        model = new DefaultTableModel(columns, 0);

        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblNhanVien.getSelectedRow();
                btnEdit.setEnabled(selectedRow != -1);
                btnDelete.setEnabled(selectedRow != -1);
            }
        });

        tblNhanVien.setAutoCreateRowSorter(true);
        tblNhanVien.setDefaultEditor(Object.class, null);
    }

    private void fillTableData() {
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        model.setRowCount(0);

        ListNV = dao.getAll();
        for (NhanVien row : ListNV) {
            model.addRow(new Object[]{
                row.getMaNV(),
                row.getTenNV(),
                row.getDienThoai(),
                row.getDiaChi()
            });
        }
        tblNhanVien.setModel(model);
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Table
        tblNhanVien = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        add(scrollPane);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Tải lại");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        add(buttonPanel);

        // Event: Refresh
        btnRefresh.addActionListener(e -> fillTableData());

        // Event: Add
        btnAdd.addActionListener(e -> {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            NhanVien_Dialog_Them dialog = new NhanVien_Dialog_Them(parent);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    fillTableData();
                }
                public void windowClosing(WindowEvent e) {
                    fillTableData();
                }
            });
            dialog.setVisible(true);
        });

        // Event: Edit
        btnEdit.addActionListener(e -> {
            int selectedRow = tblNhanVien.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để sửa!");
                return;
            }
            String maNV = tblNhanVien.getValueAt(selectedRow, 0).toString();
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            NhanVien_Dialog_Sua dialog = new NhanVien_Dialog_Sua(parent, maNV);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    fillTableData();
                }
                public void windowClosing(WindowEvent e) {
                    fillTableData();
                }
            });
            dialog.setVisible(true);
        });

        // Event: Delete
        btnDelete.addActionListener(e -> {
            int selectedRow = tblNhanVien.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên để xóa!");
                return;
            }
            int result = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa nhân viên này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                dao.delete(tblNhanVien.getValueAt(selectedRow, 0).toString());
                fillTableData();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            }
        });
    }

    // Test Panel trong JFrame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý Nhân Viên");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new NhanVienPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
