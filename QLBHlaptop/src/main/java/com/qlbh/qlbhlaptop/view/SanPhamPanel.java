package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.SanPhamDAO;
import com.qlbh.qlbhlaptop.dialog.*;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class SanPhamPanel extends javax.swing.JPanel {

    private SanPhamDAO dao = new SanPhamDAO();
    private DefaultTableModel model;
    private StringBuilder sbDataSPModel = null;
        
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SanPhamPanel.class.getName());
    public SanPhamPanel() {
        initComponents();
        
        initTable();
        fillTableData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        //btnEdit.addActionListener(e -> fillTableData());
    }
    private void initTable() {
        String[] columns = {"Mã SP", "Tên SP", "NCC", "Loại SP", "CPU", "RAM", "Ổ Cứng", "Card MH", "Giá Bán", "Số Lượng", "Hình Ảnh"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 10 ? ImageIcon.class : String.class;
            }
        };
        tbl.setRowHeight(70);
        tbl.setModel(model);
        tbl.setDefaultEditor(Object.class, null);
        tbl.setAutoCreateRowSorter(true);

        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean selected = tbl.getSelectedRow() != -1;
                btnEdit.setEnabled(selected);
                btnDelete.setEnabled(selected);
            }
        });

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    private void fillTableData() {
        model.setRowCount(0);
        StringBuilder sb = dao.getHienThiSP();
        String[] rows = sb.toString().split(";");

        for (String row : rows) {
            row = row.replace("{", "").replace("}", "").trim();
            String[] fields = row.split("\\s*,\\s*");

            ImageIcon icon = null;
            if (!fields[10].trim().isEmpty()) {
                String imgPath = "images/" + fields[10].trim();
                icon = new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
            }

            model.addRow(new Object[]{
                fields[0], fields[1], fields[2], fields[3],
                fields[4], fields[5], fields[6], fields[7],
                fields[8], fields[9], icon
            });
        }

        tbl.getColumnModel().getColumn(10).setCellRenderer(new ImageRenderer());
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
    
    private void onAdd() {
        SanPham_Dialog_Them   dialog = new SanPham_Dialog_Them  ((JFrame) SwingUtilities.getWindowAncestor(this));
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
        int row = tbl.getSelectedRow();
        if (row == -1) return;

        String ma = tbl.getValueAt(row, 0).toString();
        SanPham_Dialog_Sua  dialog = new SanPham_Dialog_Sua ((JFrame) SwingUtilities.getWindowAncestor(this), ma);
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
        int row = tbl.getSelectedRow();
        if (row == -1) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa Sản Phẩm này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String maKH = tbl.getValueAt(row, 0).toString();
            dao.delete(maKH);
            fillTableData();
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        lblSearch1 = new javax.swing.JTextField();
        btnSearch1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();

        btnSearch1.setBackground(new java.awt.Color(102, 102, 255));
        btnSearch1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnSearch1.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch1.setText("Tìm kiếm");
        btnSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearch1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(296, 296, 296)
                .addComponent(lblSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(308, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnSearch1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addComponent(lblSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnAdd.setBackground(new java.awt.Color(0, 204, 204));
        btnAdd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnDelete.setBackground(new java.awt.Color(255, 102, 102));
        btnDelete.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Xóa");

        btnEdit.setBackground(new java.awt.Color(51, 153, 0));
        btnEdit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Cập nhật");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbl);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearch1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSearch1ActionPerformed

        public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new SanPhamPanel().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lblSearch1;
    private javax.swing.JTable tbl;
    // End of variables declaration//GEN-END:variables
}
