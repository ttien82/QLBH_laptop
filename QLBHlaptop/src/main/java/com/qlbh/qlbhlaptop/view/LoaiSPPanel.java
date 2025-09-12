package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.LoaiSPDAO;
import com.qlbh.qlbhlaptop.model.LoaiSP;
import com.qlbh.qlbhlaptop.dialog.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class LoaiSPPanel extends JPanel {

    private LoaiSPDAO dao = new LoaiSPDAO();
    private List<LoaiSP> ListLoaiSP;
    private DefaultTableModel model;

    private JTable tblLoaiSP;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    public LoaiSPPanel() {
        setLayout(new BorderLayout());
        initUI();
        initTable();
        fillTableData();
    }

    private void initUI() {
        // Table
        tblLoaiSP = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblLoaiSP);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Tải lại");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnDelete);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> fillTableData());
    }

    private void initTable() {
        String[] columns = {"Mã LoaiSP", "Tên LoaiSP"};
        model = new DefaultTableModel(columns, 0);
        tblLoaiSP.setModel(model);
        tblLoaiSP.setAutoCreateRowSorter(true);
        tblLoaiSP.setDefaultEditor(Object.class, null);

        tblLoaiSP.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean rowSelected = tblLoaiSP.getSelectedRow() != -1;
                btnEdit.setEnabled(rowSelected);
                btnDelete.setEnabled(rowSelected);
            }
        });

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    private void fillTableData() {
        model.setRowCount(0);
        ListLoaiSP = dao.getAll();

        for (LoaiSP lsp : ListLoaiSP) {
            model.addRow(new Object[]{lsp.getMaLoaiSP(), lsp.getTenLoaiSP()});
        }
    }

    private void openAddDialog() {
        LoaiSP_Dialog_Them dialog = new LoaiSP_Dialog_Them((JFrame)SwingUtilities.getWindowAncestor(this));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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

    private void openEditDialog() {
        int selectedRow = tblLoaiSP.getSelectedRow();
        if (selectedRow == -1) return;

        String maLoaiSP = tblLoaiSP.getValueAt(selectedRow, 0).toString();
        LoaiSP_Dialog_Sua dialog = new LoaiSP_Dialog_Sua((JFrame)SwingUtilities.getWindowAncestor(this), maLoaiSP);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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

    private void deleteSelected() {
        int selectedRow = tblLoaiSP.getSelectedRow();
        if (selectedRow == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa Loại SP này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dao.delete(tblLoaiSP.getValueAt(selectedRow, 0).toString());
            fillTableData();
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }

    // Hàm main chỉ để chạy thử JPanel này trong một JFrame
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Loại SP");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.add(new LoaiSPPanel());
            frame.setVisible(true);
        });
    }
}
