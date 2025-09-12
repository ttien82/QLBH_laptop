/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.dao.KhachHangDAO;
import com.qlbh.qlbhlaptop.model.KhachHang;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.qlbh.qlbhlaptop.config.CheckExpression;

/*

 */
public class KhachHang_Dialog_Sua extends JDialog{
    private JTextField txtMaKH, txtTenKH, txtSdt, txtEmail, txtDiaChi;
    private JButton btnLuu;
    private KhachHangDAO khdao = new KhachHangDAO();  
    private CheckExpression check = new CheckExpression();

    private void Load_SuaKHDialog()
    {
        //clear data field
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtDiaChi.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
    }
    
    public KhachHang_Dialog_Sua(JFrame parent, String ma) {
        super(parent, "Update Khach Hang", true);
        //1.Lay du lieu de fill vao cboNCC va cboTenLoaiSP
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));
        
        KhachHang kh = khdao.getById(ma);
        txtMaKH = new JTextField(ma);
        txtMaKH.setVisible(false);

        panelForm.add(new JLabel("Tên Khách Hàng:"));
        txtTenKH = new JTextField(kh.getTenKH());
        panelForm.add(txtTenKH);
   
        panelForm.add(new JLabel("Sdt:"));
        txtSdt = new JTextField(kh.getDienThoai());
        panelForm.add(txtSdt);
        
        panelForm.add(new JLabel("Email:"));
        txtEmail = new JTextField(kh.getEmail());
        panelForm.add(txtEmail);
        
        panelForm.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField(kh.getDiaChi());
        panelForm.add(txtDiaChi);


        add(panelForm, BorderLayout.CENTER);

        // Panel nút Lưu
        JPanel panelButtons = new JPanel();
        btnLuu = new JButton("Lưu");
        panelButtons.add(btnLuu);
        add(panelButtons, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            LuuThayDoi();
        });
        setSize(450, 500);
        setLocationRelativeTo(parent);   
    }

    //Luu
    private void LuuThayDoi() {
        try {
            if (!check.checkPhone(txtSdt.getText()) || txtTenKH.getText().isEmpty() || !check.isValidGmail(txtEmail.getText())) {
                JOptionPane.showMessageDialog(
                    null,
                    "Update Failed\nTên Khách hàng, Sđt, Email không được để trống hoặc Sđt, email không đúng định dạng!",
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
                if (khdao.update(khMoi))// Lưu xuống DB 
                {           
                    JOptionPane.showMessageDialog(this, "Update KH thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update thất bại!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra định dạng đầu vào");
        }    
    }
}
