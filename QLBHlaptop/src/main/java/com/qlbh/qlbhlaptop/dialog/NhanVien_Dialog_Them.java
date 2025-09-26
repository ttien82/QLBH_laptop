package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.model.NhanVien;
import com.qlbh.qlbhlaptop.dao.NhanVienDAO;
import javax.swing.*;
import java.awt.*;
import com.qlbh.qlbhlaptop.config.CheckExpression;

public class NhanVien_Dialog_Them extends JDialog{
    private JTextField txtMaNV, txtTenNV, txtSdt, txtDiaChi;
    private JButton btnLuu;
    private NhanVienDAO nvdao = new NhanVienDAO();
    private CheckExpression check = new CheckExpression();
    
    private void Load_ThemNVDialog()
    {
        //clear data field
        txtMaNV.setText("");
        txtMaNV.requestFocus();
        txtTenNV.setText("");
        txtDiaChi.setText("");
        txtSdt.setText("");}
    
    public NhanVien_Dialog_Them(JFrame parent) {
        super(parent, "Thêm NV", true);
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));

        panelForm.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        panelForm.add(txtMaNV);

        panelForm.add(new JLabel("Tên NV:"));
        txtTenNV = new JTextField();
        panelForm.add(txtTenNV);
   
        panelForm.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        panelForm.add(txtDiaChi);

        panelForm.add(new JLabel("Sđt:"));
        txtSdt = new JTextField();
        panelForm.add(txtSdt);

        add(panelForm, BorderLayout.CENTER);

        // Panel nút Lưu
        JPanel panelButtons = new JPanel();
        btnLuu = new JButton("Lưu");
        panelButtons.add(btnLuu);
        add(panelButtons, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            LuuThem();
        });
        setSize(450, 500);
        setLocationRelativeTo(parent);
        
    } 
    
    //Luu
    private void LuuThem() {
        // Kiểm tra giá bán có phải là số VA Kiểm tra tên SP rỗng
        try {
            if (!check.checkPhone(txtSdt.getText()) || txtTenNV.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên Nhân viên, Sđt không được để trống hoặc Sđt không đúng định dạng!");
                txtTenNV.requestFocus();
            }
            else    
            {
                NhanVien nvMoi = new NhanVien(
                        txtMaNV.getText(),
                        txtTenNV.getText(),
                        txtDiaChi.getText(),
                        txtSdt.getText()
                    );          
                if (nvdao.insert(nvMoi))// Lưu xuống DB 
                {            
                    JOptionPane.showMessageDialog(this, "Thêm Nhân viên thành công!");
//                    Load_ThemNVDialog();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm NV");
        }    

    }
}