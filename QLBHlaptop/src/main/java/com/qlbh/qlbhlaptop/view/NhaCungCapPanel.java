package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.NhaCungCapDAO;
import com.qlbh.qlbhlaptop.model.NhaCungCap;
import com.qlbh.qlbhlaptop.dialog.*;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class NhaCungCapPanel extends JPanel {

    private NhaCungCapDAO dao = new NhaCungCapDAO();
    private List<NhaCungCap> ListNCC;
    private DefaultTableModel model;

    private JButton btnAdd = new JButton("Thêm");
    private JButton btnDelete = new JButton("Xóa");
    private JButton btnEdit = new JButton("Sửa");
    private JButton btnRefresh = new JButton("Tải lại");
    private JTable tblNhaCungCap = new JTable();
    private JScrollPane jScrollPane1 = new JScrollPane(tblNhaCungCap);
    private JPanel jPanel2 = new JPanel();

    public NhaCungCapPanel() {
        initComponents();
        InitTblNhaCungCap();
        fillTableData();

        tblNhaCungCap.setAutoCreateRowSorter(true);
        tblNhaCungCap.setDefaultEditor(Object.class, null);
    }

    private void InitTblNhaCungCap() {
        String[] columns = {"Mã NCC", "Tên NCC", "Địa chỉ", "Điện thoại"};
        model = new DefaultTableModel(columns, 0);

        tblNhaCungCap.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblNhaCungCap.getSelectedRow();
                btnEdit.setEnabled(selectedRow != -1);
                btnDelete.setEnabled(selectedRow != -1);
            }
        });
    }

    private void fillTableData() {
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        model.setRowCount(0);

        ListNCC = dao.getAll();
        for (NhaCungCap ncc : ListNCC) {
            model.addRow(new Object[]{
                ncc.getMaNCC(),
                ncc.getTenNCC(),
                ncc.getDiaChi(),
                ncc.getDienThoai()
            });
        }
        tblNhaCungCap.setModel(model);
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Thêm bảng
        add(jScrollPane1);

        // Thêm các nút
        jPanel2.add(btnAdd);
        jPanel2.add(btnEdit);
        jPanel2.add(btnDelete);
        jPanel2.add(btnRefresh);
        add(jPanel2);

        // ======= EVENT BUTTONS =======
        btnRefresh.addActionListener(e -> fillTableData());

        // ADD
        btnAdd.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            NhaCungCap_Dialog_Them dialog = new NhaCungCap_Dialog_Them(parentFrame);
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

        // EDIT
        btnEdit.addActionListener(e -> {
            int selectedRow = tblNhaCungCap.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!");
                return;
            }

            String maNCC = tblNhaCungCap.getValueAt(selectedRow, 0).toString();
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            NhaCungCap_Dialog_Sua dialog = new NhaCungCap_Dialog_Sua(parentFrame, maNCC);
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

        // DELETE
        btnDelete.addActionListener(e -> {
            int selectedRow = tblNhaCungCap.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
                return;
            }

            int result = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa NCC này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                String maNCC = tblNhaCungCap.getValueAt(selectedRow, 0).toString();
                dao.delete(maNCC);
                fillTableData();
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
            }
        });
    }

    // Test JPanel riêng
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý Nhà Cung Cấp");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new NhaCungCapPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
