package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.DonHangDAO;
import com.qlbh.qlbhlaptop.dto.DonHangViewDTO;
import com.qlbh.qlbhlaptop.model.DonHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.Frame;

public class DonHangPanel extends javax.swing.JPanel {

    public DonHangPanel() {
        initComponents();
        loadData();
    }

    private void loadData() {
        String[] columnNames = {"Mã Đơn Hàng", "Tên Khách Hàng", "Số Điện Thoại", "Địa Chỉ"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        DonHangDAO dao = new DonHangDAO();
        List<DonHangViewDTO> list = dao.getAllDonHangWithKhachHang();
        for (DonHangViewDTO dh : list) {
            model.addRow(new Object[]{
                    dh.getMaDH(), dh.getTenKH(), dh.getDienThoai(), dh.getDiaChi()
            });
        }
        tbl.setModel(model);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
        btnEdit1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();

        btnSearch1.setBackground(new java.awt.Color(102, 102, 255));
        btnSearch1.setFont(new java.awt.Font("Arial", 0, 12)); 
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
        btnAdd.setFont(new java.awt.Font("Arial", 1, 14)); 
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

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
        btnDelete.setFont(new java.awt.Font("Arial", 1, 14)); 
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(51, 153, 0));
        btnEdit.setFont(new java.awt.Font("Arial", 1, 14)); 
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Cập nhật");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnEdit1.setBackground(new java.awt.Color(51, 153, 0));
        btnEdit1.setFont(new java.awt.Font("Arial", 1, 14)); 
        btnEdit1.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit1.setText("Hiển Thị Chi Tiết");
        btnEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdit1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEdit1)
                .addGap(18, 18, 18)
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
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
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
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Mã Đơn Hàng", "Tên Khách Hàng", "Số Điện Thoại", "Địa Chỉ"}
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
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
    }// </editor-fold>                        

    private void btnSearch1ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        String keyword = lblSearch1.getText().trim();
        DonHangDAO dao = new DonHangDAO();
        List<DonHangViewDTO> list = dao.searchDonHang(keyword);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã Đơn Hàng","Tên Khách Hàng","Số Điện Thoại","Địa Chỉ"}, 0);
        for (DonHangViewDTO dh : list) {
            model.addRow(new Object[]{dh.getMaDH(), dh.getTenKH(), dh.getDienThoai(), dh.getDiaChi()});
        }
        tbl.setModel(model);
    }                                          

    private void btnEdit1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        int row = tbl.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn hàng!");
            return;
        }
        String maDH = (String) tbl.getValueAt(row, 0);
        ChiTietDonHangDialog dialog = new ChiTietDonHangDialog(maDH);
        dialog.setVisible(true);
    }                                        

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {                                       
        DonHangDialog_Them dialog = new DonHangDialog_Them((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        loadData(); // refresh bảng sau khi thêm
    }                                     

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {                                        
        int row = tbl.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn hàng để cập nhật!");
            return;
        }

        String maDH = (String) tbl.getValueAt(row, 0);
        DonHangDAO dao = new DonHangDAO();
        DonHang dh = dao.findById(maDH);

        if (dh != null) {
            DonHangDialog_Sua dialog = new DonHangDialog_Sua((Frame) SwingUtilities.getWindowAncestor(this), dh);
            dialog.setVisible(true);
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy đơn hàng cần cập nhật!");
        }
    }                                       

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {                                          
        int row = tbl.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng cần xóa!");
            return;
        }
        String maDH = (String) tbl.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa đơn hàng " + maDH + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DonHangDAO dao = new DonHangDAO();
            if (dao.delete(maDH)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }                                         


    // Variables declaration - do not modify                     
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEdit1;
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
    // End of variables declaration                   
}
