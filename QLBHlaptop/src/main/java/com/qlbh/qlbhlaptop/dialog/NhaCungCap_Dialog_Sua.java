/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.dao.NhaCungCapDAO;
import com.qlbh.qlbhlaptop.model.NhaCungCap;
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

public class NhaCungCap_Dialog_Sua extends JDialog{
    private JTextField txtMaNCC, txtTenNCC, txtSdt, txtDiaChi;
    private JButton btnLuu;
    private NhaCungCapDAO nccdao = new NhaCungCapDAO();
    private CheckExpression check = new CheckExpression();
    
    public NhaCungCap_Dialog_Sua(JFrame parent, String maNcc) {
        super(parent, "Update NV", true);
        //1.Lay du lieu de fill vao cboNCC va cboTenLoaiSP
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));
        
        NhaCungCap ncc = nccdao.getById(maNcc);
        txtMaNCC = new JTextField(maNcc);
        txtMaNCC.setVisible(false);

        panelForm.add(new JLabel("Tên Nhà Cung Cấp:"));
        txtTenNCC = new JTextField(ncc.getTenNCC());
        panelForm.add(txtTenNCC);
   
        panelForm.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField(ncc.getDiaChi());
        panelForm.add(txtDiaChi);

        panelForm.add(new JLabel("Sdt:"));
        txtSdt = new JTextField(ncc.getDienThoai());
        panelForm.add(txtSdt);
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
        // Kiểm tra giá bán có phải là số VA Kiểm tra tên SP rỗng
        try {
            if (!check.checkPhone(txtSdt.getText()) || txtTenNCC.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên Nhà Cung Cấp, Sđt không được để trống hoặc Sđt không đúng định dạng!");
                txtSdt.requestFocus();
            }
            else
            {
                NhaCungCap nccMoi = new NhaCungCap(
                    txtMaNCC.getText(),
                    txtTenNCC.getText(),
                    txtDiaChi.getText(),
                    txtSdt.getText()

                );          
                if (nccdao.update(nccMoi))// Lưu xuống DB 
                {           
                    JOptionPane.showMessageDialog(this, "Update Nhà Cung Cấp thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update thất bại!");
                }
            }       
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra định dạng đầu vào");
        }    

    }
}
