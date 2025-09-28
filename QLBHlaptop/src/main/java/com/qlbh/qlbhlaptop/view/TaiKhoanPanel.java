package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dto.TaiKhoanDTO;
import com.qlbh.qlbhlaptop.model.NhanVien;
import com.qlbh.qlbhlaptop.model.Quyen;
import com.qlbh.qlbhlaptop.model.TaiKhoan;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaiKhoanPanel extends JPanel {
    // --- Components ---
    private JTable tblTaiKhoan;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnDelete, btnRefresh, btnSave, btnHuy, btnTim;

    private JTextField txtMaTK, txtUsername, txtTim ;
    private JPasswordField pwdPassword;
    private JComboBox<String> cboNhanVien, cboQuyen;

    // Map cần thiết cho việc ánh xạ (được Controller truyền vào)
    private Map<String, String> nhanVienMap; // TenNV -> MaNV
    private Map<String, String> quyenMap;     // TenQuyen -> MaQuyen

    // --- Constructor & Setup ---
    public TaiKhoanPanel() {
        setLayout(new BorderLayout());
        
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtTim = new JTextField(25);
        btnTim = new JButton("Tìm kiếm");
        //btnTim.setIcon(new ImageIcon(getClass().getResource("/images/search_icon.png"))); // Nếu có icon    
        
        pnlTimKiem.add(new JLabel("Tìm kiếm:"));
        pnlTimKiem.add(txtTim);
        pnlTimKiem.add(btnTim);
        
        add(pnlTimKiem, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.7); 
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(createToolbar(), BorderLayout.NORTH);
        tableContainer.add(createTablePanel(), BorderLayout.CENTER);
        
        splitPane.setLeftComponent(tableContainer);
        splitPane.setRightComponent(createDetailFormPanel());
        
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAdd = new JButton("Thêm Mới");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Làm mới");
        btnSave = new JButton("LƯU");
        btnHuy = new JButton("HỦY");

        btnSave.setEnabled(false); 

        toolbar.add(btnAdd);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);
        toolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.add(btnSave);
        toolbar.add(btnHuy);
        
        return toolbar;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"Mã TK", "Tài Khoản", "Tên NV", "Tên quyền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTaiKhoan = new JTable(tableModel);
        tblTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        return new JScrollPane(tblTaiKhoan);
    }
    
    private JPanel createDetailFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết tài khoản"));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaTK = new JTextField(20);
        txtUsername = new JTextField(20);
        pwdPassword = new JPasswordField(20);
        cboNhanVien = new JComboBox<>();
        cboQuyen = new JComboBox<>();
        
        txtMaTK.setEnabled(false);

        // Thêm các trường vào formPanel (Giữ nguyên layout trước đó)
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; formPanel.add(new JLabel("Mã TK:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1; formPanel.add(txtMaTK, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; formPanel.add(new JLabel("Tài khoản:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1; formPanel.add(txtUsername, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; formPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1; formPanel.add(pwdPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; formPanel.add(new JLabel("Nhân Viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1; formPanel.add(cboNhanVien, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; formPanel.add(new JLabel("Quyền:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1; formPanel.add(cboQuyen, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 1; gbc.weighty = 1;
        formPanel.add(new JPanel(), gbc);
        
        panel.add(formPanel, BorderLayout.NORTH);
        return panel;
    }

    // ==========================================================
    // --- PUBLIC GETTERS / SETTERS (Giao tiếp với Controller) ---
    // ==========================================================

    // --- 1. Getters cho Components (Controller dùng để gắn sự kiện) ---
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JButton getBtnLuu() { return btnSave; }
    public JButton getBtnHuy() { return btnHuy; }
    public JButton getBtnTim()  { return btnTim; }
    public JTable getTblTaiKhoan() { return tblTaiKhoan; }
    public JTextField getTxtMaTK() { return txtMaTK; }
    public JTextField getTxtTim() {return txtTim;}
    public JPasswordField getPwdPassword() { return pwdPassword; }
    
    /** Gắn ListSelectionListener do Controller cung cấp */
    public void addTableSelectionListener(ListSelectionListener listener) {
        tblTaiKhoan.getSelectionModel().addListSelectionListener(listener);
    }
    
    // --- 2. Setter để đổ dữ liệu từ Controller vào View ---
    
    /** Đổ dữ liệu danh sách DTO vào bảng. */
    public void fillTable(List<TaiKhoanDTO> dsTK) { 
        tableModel.setRowCount(0);
        for (TaiKhoanDTO dto : dsTK) {
            tableModel.addRow(new Object[]{
                dto.getMaTK(), dto.getTenDangNhap(), dto.getTenNV(), dto.getTenQuyen()
            });
        }
    }
    
    /** Đổ dữ liệu Nhân Viên vào ComboBox và lưu Map ánh xạ. */
    public void loadNhanVienComboBox(List<NhanVien> dsNV) { 
        cboNhanVien.removeAllItems();
        // Lưu Map để ánh xạ ngược trong getTaiKhoanFromForm()
        nhanVienMap = dsNV.stream().collect(
            Collectors.toMap(NhanVien::getTenNV, NhanVien::getMaNV));
        dsNV.forEach(nv -> cboNhanVien.addItem(nv.getTenNV()));
    }
    
    /** Đổ dữ liệu Quyền vào ComboBox và lưu Map ánh xạ. */
    public void loadQuyenComboBox(List<Quyen> dsQ) { 
        cboQuyen.removeAllItems();
        // Lưu Map để ánh xạ ngược trong getTaiKhoanFromForm()
        quyenMap = dsQ.stream().collect(
            Collectors.toMap(Quyen::getTenQuyen, Quyen::getMaQuyen));
        dsQ.forEach(q -> cboQuyen.addItem(q.getTenQuyen()));
    }

    /** Điền thông tin từ đối tượng TaiKhoan vào Form. */
    public void setDetailForm(TaiKhoan tk) { 
        txtMaTK.setText(tk.getMaTK());
        txtUsername.setText(tk.getTenDangNhap());
        pwdPassword.setText(""); 
        
        // Dùng Map để tìm tên tương ứng từ Mã
        String tenNV = nhanVienMap.entrySet().stream()
            .filter(entry -> entry.getValue().equals(tk.getMaNV()))
            .map(Map.Entry::getKey)
            .findFirst().orElse(null);
        if (tenNV != null) cboNhanVien.setSelectedItem(tenNV);
        
        String tenQuyen = quyenMap.entrySet().stream()
            .filter(entry -> entry.getValue().equals(tk.getMaQuyen()))
            .map(Map.Entry::getKey)
            .findFirst().orElse(null);
        if (tenQuyen != null) cboQuyen.setSelectedItem(tenQuyen);
        
        btnSave.setEnabled(true);
    }

    /** Xóa trắng Form Chi Tiết. */
    public void clearDetailForm() { 
        tblTaiKhoan.clearSelection();
        txtMaTK.setText("");
        txtUsername.setText("");
        pwdPassword.setText("");
        if (cboNhanVien.getItemCount() > 0) cboNhanVien.setSelectedIndex(0);
        if (cboQuyen.getItemCount() > 0) cboQuyen.setSelectedIndex(0);
        btnSave.setEnabled(false);
    }

    // --- 3. Getter để Controller lấy dữ liệu từ View ---

    /** Lấy dữ liệu từ Form để tạo đối tượng TaiKhoan. */
    public TaiKhoan getTaiKhoanFromForm() { 
        String maTK = txtMaTK.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(pwdPassword.getPassword());
        String tenNV = (String) cboNhanVien.getSelectedItem();
        String tenQuyen = (String) cboQuyen.getSelectedItem();
        
        // Ánh xạ ngược từ Tên -> Mã để tạo đối tượng TaiKhoan
        String maNV = nhanVienMap != null && tenNV != null ? nhanVienMap.get(tenNV) : null;
        String maQuyen = quyenMap != null && tenQuyen != null ? quyenMap.get(tenQuyen) : null;
        
        return new TaiKhoan(maTK, maNV, username, password, maQuyen);
    }
}