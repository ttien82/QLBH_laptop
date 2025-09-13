package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.SanPhamDAO;
import com.qlbh.qlbhlaptop.dialog.*;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class SanPhamPanel extends JPanel {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SanPhamPanel.class.getName());

    private SanPhamDAO dao = new SanPhamDAO();
    private StringBuilder sbDataSPModel = null;
    private DefaultTableModel model;

    public SanPhamPanel() {
        initComponents();
        InitTblSanPham();
        fillTableData();

        tblSanPham.setAutoCreateRowSorter(true);
        tblSanPham.setDefaultEditor(Object.class, null);
    }

    private void InitTblSanPham() {
        String[] columns = {"Mã SP", "Tên SP", "NCC", "Loại SP", "CPU", "RAM", "Ổ Cứng", "Card MH", "Giá Bán", "Số Lượng", "Hình Ảnh"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 10) return ImageIcon.class;
                return String.class;
            }
        };
        tblSanPham.setRowHeight(70);
        tblSanPham.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblSanPham.getSelectedRow();
                btnEdit.setEnabled(selectedRow != -1);
                btnDelete.setEnabled(selectedRow != -1);
            }
        });
    }

    private void fillTableData() {
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        model.setRowCount(0);

        sbDataSPModel = dao.getHienThiSP();
        String data = sbDataSPModel.toString();
        String[] rows = data.split(";");

        for (String row : rows) {
            row = row.replace("{", "").replace("}", "").trim();
            String[] fields = row.trim().split("\\s*,\\s*");

            String imagePath = "images/" + fields[10].trim();
            ImageIcon icon = null;
            if (fields[10].trim() != null) {
                icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            }

            model.addRow(new Object[]{
                fields[0].trim(), fields[1].trim(), fields[2].trim(), fields[3].trim(),
                fields[4].trim(), fields[5].trim(), fields[6].trim(), fields[7].trim(),
                fields[8].trim(), fields[9].trim(), icon
            });
        }
        tblSanPham.setModel(model);
        tblSanPham.getColumnModel().getColumn(10).setCellRenderer(new ImageRenderer());
    }

    private class ImageRenderer extends JLabel implements TableCellRenderer {
        public ImageRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setIcon(value instanceof ImageIcon ? (ImageIcon) value : null);
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Tải lại");
        jScrollPane1 = new JScrollPane();
        jScrollPane2 = new JScrollPane();
        tblSanPham = new JTable();
        filler1 = new Box.Filler(new java.awt.Dimension(0, 2), new java.awt.Dimension(0, 2), new java.awt.Dimension(32767, 2));

        btnAdd.addActionListener(evt -> btnAddActionPerformed(evt));
        btnEdit.addActionListener(evt -> btnEditActionPerformed(evt));
        btnDelete.addActionListener(evt -> btnDeleteActionPerformed(evt));
        btnRefresh.addActionListener(evt -> fillTableData());

        jPanel2.add(btnAdd);
        jPanel2.add(btnEdit);
        jPanel2.add(btnRefresh);
        jPanel2.add(btnDelete);

        tblSanPham.setModel(new DefaultTableModel(
            new Object[][]{{null, null, null, null, null}},
            new String[]{"Title 1", "Title 2", "Title 3", "Title 4", "Title 5"}
        ));
        jScrollPane1.setViewportView(tblSanPham);
        jScrollPane2.setViewportView(jScrollPane1);

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(jPanel1);
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        SanPham_Dialog_Them dialog = new SanPham_Dialog_Them(parent);
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
    }

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow != -1) {
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            SanPham_Dialog_Sua dialog = new SanPham_Dialog_Sua(parent, tblSanPham.getValueAt(selectedRow, 0).toString());
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
        }
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        int selectedRow = tblSanPham.getSelectedRow();
        if (result == JOptionPane.YES_OPTION && selectedRow != -1) {
            dao.delete(tblSanPham.getValueAt(selectedRow, 0).toString());
            fillTableData();
            JOptionPane.showMessageDialog(this, "Xóa thành công");
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Sản Phẩm");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new SanPhamPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private JButton btnAdd, btnDelete, btnEdit, btnRefresh;
    private Box.Filler filler1;
    private JPanel jPanel1, jPanel2;
    private JScrollPane jScrollPane1, jScrollPane2;
    private JTable tblSanPham;
}
