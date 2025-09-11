/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.dao.NhanVienDAO;
import com.qlbh.qlbhlaptop.model.NhanVien;
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

public class NhanVien_Dialog_Sua extends JDialog{
    private JTextField txtMaNV, txtTenNV, txtSdt, txtDiaChi;
    private JButton btnLuu;
    private NhanVienDAO nvdao = new NhanVienDAO();
    private CheckExpression check = new CheckExpression();
    
    public NhanVien_Dialog_Sua(JFrame parent, String maNV) {
        super(parent, "Update NV", true);
        //1.Lay du lieu de fill vao cboNCC va cboTenLoaiSP
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));
        
        NhanVien nv = nvdao.getById(maNV);
        txtMaNV = new JTextField(maNV);
        txtMaNV.setVisible(false);

        panelForm.add(new JLabel("Tên NV:"));
        txtTenNV = new JTextField(nv.getTenNV());
        panelForm.add(txtTenNV);
   
        panelForm.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField(nv.getDiaChi());
        panelForm.add(txtDiaChi);

        panelForm.add(new JLabel("Sdt:"));
        txtSdt = new JTextField(nv.getDienThoai());
        panelForm.add(txtSdt);
        add(panelForm, BorderLayout.CENTER);

        // Panel nút Lưu
        JPanel panelButtons = new JPanel();
        btnLuu = new JButton("Lưu");
        panelButtons.add(btnLuu);
        add(panelButtons, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> {
            LuuThayDoi();
            //Đóng hẳn dialog và giải phóng tài nguyên, nếu muốn mở lại phải tạo mới.
            //dispose();
            //chi an di
            //setVisible(false);
        });
        setSize(450, 500);
        setLocationRelativeTo(parent);   
    }

    //Luu
    private void LuuThayDoi() {
        // Kiểm tra giá bán có phải là số VA Kiểm tra tên SP rỗng
        try {
            if (!check.checkPhone(txtSdt.getText()) || txtTenNV.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên Nhân viên, Sđt không được để trống hoặc Sđt không đúng định dạng!");
                txtSdt.requestFocus();
            }
            else
            {
                NhanVien nvMoi = new NhanVien(
                                txtMaNV.getText(),
                                txtTenNV.getText(),
                                txtDiaChi.getText(),
                                txtSdt.getText()
                            );          
                if (nvdao.update(nvMoi))// Lưu xuống DB 
                {           
                    JOptionPane.showMessageDialog(this, "Update NV thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update thất bại!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra định dạng đầu vào");
        }    

    }
}
