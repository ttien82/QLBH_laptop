package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.config.CheckExpression;
import com.qlbh.qlbhlaptop.model.KhachHang;
import com.qlbh.qlbhlaptop.dao.KhachHangDAO;
import javax.swing.*;
import java.awt.*;

public class KhachHang_Dialog_Them extends JDialog{
    private JTextField txtMaKH, txtTenKH, txtSdt, txtEmail, txtDiaChi;
    private JButton btnLuu;
    private KhachHangDAO khdao = new KhachHangDAO();  
    private CheckExpression check = new CheckExpression();

    private void Load_ThemKHDialog()
    {
        //clear data field
        txtMaKH.setText("");
        txtMaKH.requestFocus();
        txtTenKH.setText("");
        txtDiaChi.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
    }
    
    public KhachHang_Dialog_Them(JFrame parent) {
        super(parent, "Thêm KhachHang", true);
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));

        panelForm.add(new JLabel("Mã Khách hàng:"));
        txtMaKH = new JTextField();
        panelForm.add(txtMaKH);

        panelForm.add(new JLabel("Tên Khách hàng:"));
        txtTenKH = new JTextField();
        panelForm.add(txtTenKH);
       
        panelForm.add(new JLabel("Sđt:"));
        txtSdt = new JTextField();
        panelForm.add(txtSdt);
             
        panelForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelForm.add(txtEmail);
        
        panelForm.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        panelForm.add(txtDiaChi);


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
        String ten = txtTenKH.getText().trim();
        String SDT = txtSdt.getText().trim();
        String email = txtEmail.getText().trim();
        // Kiểm tra giá bán có phải là số VA Kiểm tra tên SP rỗng
        try {
            if (!check.checkPhone(SDT) || ten.isEmpty() || !check.isValidGmail(email)) {
                JOptionPane.showMessageDialog(
                    null,
                    "Insert Failed\nTên Khách hàng, Sđt, Email không được để trống hoặc Sđt, email không đúng định dạng!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            else
            {
                KhachHang khMoi = new KhachHang(
                    txtMaKH.getText(),
                    txtTenKH.getText(),
                    txtSdt.getText(),
                    txtEmail.getText(),
                    txtDiaChi.getText()
                );          
                if (khdao.insert(khMoi))// Lưu xuống DB 
                {            
                    JOptionPane.showMessageDialog(this, "Thêm Khách Hàng thành công!");
                    Load_ThemKHDialog();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }  
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm Khách Hàng");
        }    

    }
}