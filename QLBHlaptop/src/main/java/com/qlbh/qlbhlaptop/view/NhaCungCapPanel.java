package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.controller.NhaCungCapController;
import com.qlbh.qlbhlaptop.dao.NhaCungCapDAO;
import com.qlbh.qlbhlaptop.model.NhaCungCap;
import com.qlbh.qlbhlaptop.dialog.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class NhaCungCapPanel extends javax.swing.JPanel {

    private NhaCungCapDAO dao = new NhaCungCapDAO();
    private DefaultTableModel model;
    private List<NhaCungCap> ListNCC;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(NhaCungCapPanel.class.getName());
    public NhaCungCapPanel() {
        initComponents();

        initTable();
        fillTableData();

        // Sự kiện nút
        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnSearch1.addActionListener(e -> onSearch());
        btnExport.addActionListener(e -> onExport());
        btnImport.addActionListener(e -> onImport());
    }
    private void initTable() {
        String[] columns = {"Mã NCC", "Tên NCC", "Địa chỉ", "Điện thoại"};
        model = new DefaultTableModel(columns, 0);
        tbl.setModel(model);
        tbl.setAutoCreateRowSorter(true);
        tbl.setDefaultEditor(Object.class, null);

        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean rowSelected = tbl.getSelectedRow() != -1;
                btnEdit.setEnabled(rowSelected);
                btnDelete.setEnabled(rowSelected);
            }
        });

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    private void fillTableData() {
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
    }

    private void onSearch() {
    String keyword = lblSearch1.getText().trim(); 
    List<NhaCungCap> list = dao.search(keyword);

    DefaultTableModel model = (DefaultTableModel) tbl.getModel();
    model.setRowCount(0); 

    for (NhaCungCap ncc : list) {
        model.addRow(new Object[]{
            ncc.getMaNCC(),ncc.getTenNCC(), ncc.getDiaChi(), ncc.getDienThoai()
        });
    }
}
     
    private void onAdd() {
        NhaCungCap_Dialog_Them dialog = new NhaCungCap_Dialog_Them((JFrame) SwingUtilities.getWindowAncestor(this));
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
        NhaCungCap_Dialog_Sua dialog = new NhaCungCap_Dialog_Sua((JFrame) SwingUtilities.getWindowAncestor(this), ma);
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
                "Bạn có chắc chắn muốn xóa Nhà Cung Cấp này?",
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
    
     private void onImport() {
    JFileChooser fc = new JFileChooser();
    fc.setDialogTitle("Chọn file Excel để nhập");
    if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        try {
            NhaCungCapController controller = new NhaCungCapController(this);
            List<NhaCungCap> NhaCC = controller.importFromExcel(file);

            // TODO: lưu vào DB (gọi dao.insert/update)
            for (NhaCungCap ncc : NhaCC) {
               try {
                dao.insert(ncc);
            } catch (Exception e) {
                e.printStackTrace(); // log chi tiết SQL
                throw new Exception("Lỗi khi thêm nhà cung cấp: " + ncc.getMaNCC() + " - " + e.getMessage());
            }
            }

            JOptionPane.showMessageDialog(this, 
                "Nhập Excel thành công: " + NhaCC.size() + " nhà cung cấp",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            
            fillTableData();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi nhập Excel: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}

    
    private void onExport() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Chọn nơi lưu file Excel");
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }
            try {
                // Lấy dữ liệu từ DAO
                List<NhaCungCap> NhaCC = dao.getAll();  
                // Gọi controller để export
                NhaCungCapController controller = new NhaCungCapController(this);
                System.out.println("File path: " + file);
                System.out.println("Số nhà cung cấp: " + NhaCC.size());

                controller.exportsToExcel(NhaCC, file);

                JOptionPane.showMessageDialog(this, "Xuất Excel thành công: " + file.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnImport = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        btnSearch1 = new javax.swing.JButton();
        lblSearch1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();

        btnImport.setBackground(new java.awt.Color(255, 204, 102));
        btnImport.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnImport.setForeground(new java.awt.Color(255, 255, 255));
        btnImport.setText("Nhập Excel");

        btnExport.setBackground(new java.awt.Color(102, 204, 255));
        btnExport.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setText("Xuất Excel");
        btnExport.setActionCommand("btnExport");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImport))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnSearch1.setBackground(new java.awt.Color(102, 102, 255));
        btnSearch1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSearch1.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch1.setText("Tìm kiếm");
        btnSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearch1ActionPerformed(evt);
            }
        });

        lblSearch1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        lblSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblSearch1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DANH SÁCH NHÀ CUNG CẤP");

        btnAdd.setBackground(new java.awt.Color(0, 204, 204));
        btnAdd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSearch1, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSearch1)
                    .addComponent(btnSearch1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        tbl.setMinimumSize(new java.awt.Dimension(60, 120));
        tbl.setRowHeight(30);
        jScrollPane1.setViewportView(tbl);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void lblSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblSearch1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblSearch1ActionPerformed

        public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new NhaCungCapPanel().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnSearch1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lblSearch1;
    private javax.swing.JTable tbl;
    // End of variables declaration//GEN-END:variables
}
