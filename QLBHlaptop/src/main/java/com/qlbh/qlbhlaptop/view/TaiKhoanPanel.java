package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.model.NhanVien;
import com.qlbh.qlbhlaptop.model.Quyen;
import com.qlbh.qlbhlaptop.model.TaiKhoan;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Giao diện quản lý Tài Khoản.
 */
public class TaiKhoanPanel extends JPanel {

    // Components của thanh công cụ
    private final JButton btnAdd;
    private final JButton btnEdit;
    private final JButton btnDelete;
    private final JButton btnRefresh;

    // Components của bảng dữ liệu
    private final JTable tblTaiKhoan;
    private final DefaultTableModel tableModel;

    // Components của Form chi tiết (JDialog)
    private final JDialog detailDialog;
    private final JTextField txtMaTK;
    private final JTextField txtUsername;
    private final JPasswordField pwdPassword;
    private final JComboBox<String> cboNhanVien;
    private final JComboBox<String> cboQuyen;
    private final JButton btnLuu;
    private final JButton btnHuy;

    private boolean isAdding;
    private final JLabel lblLoading;

    public TaiKhoanPanel() {
        this.setLayout(new BorderLayout());

        // Tạo và thêm thanh công cụ
        JPanel toolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAdd = createButton("Thêm", "src/main/resources/icons/add.png");
        btnEdit = createButton("Sửa", "src/main/resources/icons/edit.png");
        btnDelete = createButton("Xóa", "src/main/resources/icons/delete.png");
        btnRefresh = createButton("Làm mới", "src/main/resources/icons/refresh.png");
        toolBarPanel.add(btnAdd);
        toolBarPanel.add(btnEdit);
        toolBarPanel.add(btnDelete);
        toolBarPanel.add(btnRefresh);
        this.add(toolBarPanel, BorderLayout.NORTH);

        // Tạo và thêm bảng dữ liệu
        String[] columnNames = {"Mã tài khoản", "Tên đăng nhập", "Mã nhân viên", "Quyền"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTaiKhoan = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblTaiKhoan);
        this.add(scrollPane, BorderLayout.CENTER);

        // Khởi tạo dialog chi tiết
        detailDialog = new JDialog();
        detailDialog.setTitle("Thông tin Tài khoản");
        detailDialog.setSize(450, 350);
        detailDialog.setModal(true);
        detailDialog.setLocationRelativeTo(this);

        JPanel detailPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã tài khoản
        gbc.gridx = 0; gbc.gridy = 0;
        detailPanel.add(new JLabel("Mã TK:"), gbc);
        gbc.gridx = 1;
        txtMaTK = new JTextField(20);
        txtMaTK.setEditable(false);
        detailPanel.add(txtMaTK, gbc);

        // Tên đăng nhập
        gbc.gridx = 0; gbc.gridy = 1;
        detailPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(20);
        detailPanel.add(txtUsername, gbc);

        // Mật khẩu
        gbc.gridx = 0; gbc.gridy = 2;
        detailPanel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        pwdPassword = new JPasswordField(20);
        detailPanel.add(pwdPassword, gbc);

        // Mã Nhân viên
        gbc.gridx = 0; gbc.gridy = 3;
        detailPanel.add(new JLabel("Mã nhân viên"), gbc);
        gbc.gridx = 1;
        cboNhanVien = new JComboBox<>();
        detailPanel.add(cboNhanVien, gbc);

        // Quyền
        gbc.gridx = 0; gbc.gridy = 4;
        detailPanel.add(new JLabel("Quyền:"), gbc);
        gbc.gridx = 1;
        cboQuyen = new JComboBox<>();
        detailPanel.add(cboQuyen, gbc);

        // Panel chứa nút Lưu và Hủy
        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnLuu = createButton("Lưu", "src/main/resources/icons/save.png");
        btnHuy = createButton("Hủy", "src/main/resources/icons/cancel.png");
        dialogButtonPanel.add(btnLuu);
        dialogButtonPanel.add(btnHuy);
        
        detailDialog.add(detailPanel, BorderLayout.CENTER);
        detailDialog.add(dialogButtonPanel, BorderLayout.SOUTH);

        // Loading label
        lblLoading = new JLabel("Đang tải dữ liệu...", SwingConstants.CENTER);
        lblLoading.setFont(new Font("Arial", Font.ITALIC, 16));
        lblLoading.setVisible(false);
        this.add(lblLoading, BorderLayout.SOUTH);
    }
    
    // Phương thức tạo nút với icon
    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image image = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            // Xử lý nếu không tìm thấy icon
        }
        return button;
    }

    // Các phương thức public để Controller sử dụng
    public JButton getBtnAdd() { return btnAdd; }
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnDelete() { return btnDelete; }
    public JButton getBtnRefresh() { return btnRefresh; }
    public JButton getBtnLuu() { return btnLuu; }
    public JButton getBtnHuy() { return btnHuy; }
    public JTable getTblTaiKhoan() { return tblTaiKhoan; }
    public boolean isAdding() { return isAdding; }
    public void showDetailDialog(boolean isAdding) {
        this.isAdding = isAdding;
        detailDialog.setTitle(isAdding ? "Thêm Tài khoản mới" : "Sửa Tài khoản");
        txtUsername.setEditable(isAdding);
        detailDialog.setVisible(true);
    }
    public void hideDetailDialog() { detailDialog.setVisible(false); }
    public void showLoading(boolean show) { lblLoading.setVisible(show); }

    // Phương thức điền dữ liệu vào bảng
    public void fillTable(List<TaiKhoan> danhSachTaiKhoan) {
        tableModel.setRowCount(0);
        for (TaiKhoan tk : danhSachTaiKhoan) {
            tableModel.addRow(new Object[]{
                tk.getMaTK(), 
                tk.getTenDangNhap(), 
                tk.getMaNV(), 
                tk.getMaQuyen()
            });
        }
    }
    
    // Phương thức điền dữ liệu vào form chi tiết
    public void setDetailForm(TaiKhoan tk) {
        txtMaTK.setText(tk.getMaTK());
        txtUsername.setText(tk.getTenDangNhap());
        pwdPassword.setText(""); 
        cboNhanVien.setSelectedItem(tk.getMaNV());
        cboQuyen.setSelectedItem(tk.getMaQuyen());
    }
    
    // Phương thức lấy dữ liệu từ form chi tiết
    public TaiKhoan getTaiKhoanFromDetailForm() {
        TaiKhoan tk = new TaiKhoan();
        if (!isAdding) {
            tk.setMaTK(txtMaTK.getText());
        }
        tk.setTenDangNhap(txtUsername.getText());
        tk.setMatKhau(new String(pwdPassword.getPassword()));
        tk.setMaNV((String) cboNhanVien.getSelectedItem());
        tk.setMaQuyen((String) cboQuyen.getSelectedItem());
        return tk;
    }
    
    // Phương thức lấy dữ liệu từ hàng đã chọn
    public TaiKhoan getTaiKhoanFromSelectedRow(int row) {
        TaiKhoan tk = new TaiKhoan();
        tk.setMaTK((String) tableModel.getValueAt(row, 0));
        tk.setTenDangNhap((String) tableModel.getValueAt(row, 1));
        tk.setMaNV((String) tableModel.getValueAt(row, 2));
        tk.setMaQuyen((String) tableModel.getValueAt(row, 3));
        return tk;
    }
    
     //Phương thức load dữ liệu cho JComboBox NhanVien
    public void loadNhanVienComboBox(List<NhanVien> danhSachNhanVien) {
        cboNhanVien.removeAllItems();
        for (NhanVien nv : danhSachNhanVien) {
            cboNhanVien.addItem(nv.getMaNV());
        }
    }
    
    // Phương thức load dữ liệu cho JComboBox Quyen
    public void loadQuyenComboBox(List<Quyen> danhSachQuyen) {
        cboQuyen.removeAllItems();
        for (Quyen q : danhSachQuyen) {
            cboQuyen.addItem(q.getMaQuyen());
        }
    }
    
    // Để dọn dẹp các trường khi thêm mới
    public void clearDetailForm() {
        txtMaTK.setText("");
        txtUsername.setText("");
        pwdPassword.setText("");
    }
}
