package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.controller.SanPhamController;
import com.qlbh.qlbhlaptop.dao.SanPhamDAO;
import com.qlbh.qlbhlaptop.dialog.*;
import com.qlbh.qlbhlaptop.model.SanPham;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.io.File;
import java.util.List;
import javax.swing.table.TableRowSorter;



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
        btnSearch1.addActionListener(e -> onSearch());
        btnAdd.addActionListener(e -> onAdd());
        btnEdit.addActionListener(e -> onEdit());
        btnDelete.addActionListener(e -> onDelete());
        btnExport.addActionListener(e -> onExport());
        btnThongKe.addActionListener(e -> onThongKe());
        btnImport.addActionListener(e -> onImport());

    }

    // Khởi tạo bảng
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
            boolean selected = tbl.getSelectedRow() != -1;
            btnEdit.setEnabled(selected);
            btnDelete.setEnabled(selected);
        });

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        
        // Bật chức năng sort theo cột
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        tbl.setRowSorter(sorter);
    }

    // Load dữ liệu
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

    // Sự kiện nút
    private void onSearch() {
    String keyword = lblSearch1.getText().trim(); 
    List<SanPham> list = dao.search(keyword);

    DefaultTableModel model = (DefaultTableModel) tbl.getModel();
    model.setRowCount(0); 

    for (SanPham sp : list) {
        model.addRow(new Object[]{
            sp.getMaSP(), sp.getTenSP(),sp.getMaNCC(),sp.getMaLoaiSP(), sp.getCpu(),
            sp.getRam(), sp.getOCung(), sp.getCardManHinh(),
            sp.getGiaBan(), sp.getSoLuongTon(),sp.getHinhAnh()
        });
    }
}

    private void onAdd() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        SanPham_Dialog_Them dialog = new SanPham_Dialog_Them(parent);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) { fillTableData(); }
            public void windowClosing(WindowEvent e) { fillTableData(); }
        });
        dialog.setVisible(true);
    }

    private void onEdit() {
        int row = tbl.getSelectedRow();
        if (row == -1) return;
        String ma = tbl.getValueAt(row, 0).toString();

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        SanPham_Dialog_Sua dialog = new SanPham_Dialog_Sua(parent, ma);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) { fillTableData(); }
            public void windowClosing(WindowEvent e) { fillTableData(); }
        });
        dialog.setVisible(true);
    }

    private void onDelete() {
        int row = tbl.getSelectedRow();
        if (row == -1) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa Sản Phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String ma = tbl.getValueAt(row, 0).toString();
            dao.delete(ma);
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
            SanPhamController controller = new SanPhamController(this);
            List<SanPham> products = controller.importFromExcel(file);

            // TODO: lưu vào DB (gọi dao.insert/update)
            for (SanPham sp : products) {
               try {
                dao.insert(sp);
            } catch (Exception e) {
                e.printStackTrace(); // log chi tiết SQL
                throw new Exception("Lỗi khi thêm sản phẩm: " + sp.getMaSP() + " - " + e.getMessage());
            }
            }

            JOptionPane.showMessageDialog(this, 
                "Nhập Excel thành công: " + products.size() + " sản phẩm",
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
                List<SanPham> products = dao.getAll();  
                // Gọi controller để export
                SanPhamController controller = new SanPhamController(this);
                System.out.println("File path: " + file);
                System.out.println("Số sản phẩm: " + products.size());

                controller.exportsToExcel(products, file);

                JOptionPane.showMessageDialog(this, "Xuất Excel thành công: " + file.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    private void onThongKe() {
    try {
        // gọi DAO để lấy dữ liệu thống kê
        List<Object[]> data = dao.getSanPhamBanChay();

        // tạo TableModel để hiển thị kết quả
        String[] columns = {"Mã SP", "Tên sản phẩm", "Tổng SL bán"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Object[] row : data) {
            model.addRow(row);
        }

        // hiển thị trong 1 JTable mới
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                     "Thống kê sản phẩm bán chạy", true);
        dialog.add(scrollPane);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Lỗi khi thống kê: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnSearch1 = new javax.swing.JButton();
        lblSearch1 = new javax.swing.JTextField();
        btnExport = new javax.swing.JButton();
        btnImport = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        btnThongKe = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1160, 692));

        btnAdd.setBackground(new java.awt.Color(0, 204, 204));
        btnAdd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm");

        btnSearch1.setBackground(new java.awt.Color(102, 102, 255));
        btnSearch1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSearch1.setForeground(new java.awt.Color(255, 255, 255));
        btnSearch1.setText("Tìm kiếm");
        btnSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearch1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(lblSearch1, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSearch1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSearch1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap())
        );

        btnExport.setBackground(new java.awt.Color(102, 204, 255));
        btnExport.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setText("Xuất Excel");
        btnExport.setActionCommand("btnExport");

        btnImport.setBackground(new java.awt.Color(255, 204, 102));
        btnImport.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnImport.setForeground(new java.awt.Color(255, 255, 255));
        btnImport.setText("Nhập Excel");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DANH SÁCH SẢN PHẨM");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnImport, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnImport, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnExport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
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

        btnThongKe.setBackground(new java.awt.Color(153, 153, 255));
        btnThongKe.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnThongKe.setForeground(new java.awt.Color(255, 255, 255));
        btnThongKe.setText("Thống kê bán chạy");
        btnThongKe.setActionCommand("btnThongKe");

        btnEdit.setBackground(new java.awt.Color(51, 153, 0));
        btnEdit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("Cập nhật");
        btnEdit.setBorder(null);

        btnDelete.setBackground(new java.awt.Color(255, 102, 102));
        btnDelete.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Xóa");
        btnDelete.setBorder(null);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnThongKe)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThongKe, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnThongKe.getAccessibleContext().setAccessibleName("btnThongKe");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(151, Short.MAX_VALUE))
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
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnImport;
    private javax.swing.JButton btnSearch1;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lblSearch1;
    private javax.swing.JTable tbl;
    // End of variables declaration//GEN-END:variables
}
