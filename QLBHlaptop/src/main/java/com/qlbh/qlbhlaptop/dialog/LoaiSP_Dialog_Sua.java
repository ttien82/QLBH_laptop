/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.dao.LoaiSPDAO;
import com.qlbh.qlbhlaptop.model.LoaiSP;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*

 */
public class LoaiSP_Dialog_Sua extends JDialog{
    private JTextField txtMaLoaiSP, txtTenLoaiSP;
    private JButton btnLuu;
    private LoaiSPDAO lspdao = new LoaiSPDAO();

    public LoaiSP_Dialog_Sua(JFrame parent, String ma) {
        super(parent, "Update NV", true);
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));
        
        LoaiSP lsp = lspdao.getById(ma);
        txtMaLoaiSP = new JTextField(ma);
        txtMaLoaiSP.setVisible(false);

        panelForm.add(new JLabel("Tên Loại Sản Phẩm:"));
        txtTenLoaiSP = new JTextField(lsp.getTenLoaiSP());
        panelForm.add(txtTenLoaiSP);
  
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
            if (txtTenLoaiSP.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Update Failed\nTen Loai SP không được để trống!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            else
            {
                LoaiSP lspMoi = new LoaiSP(
                        txtMaLoaiSP.getText(),
                        txtTenLoaiSP.getText()
                    ); 
                if (lspdao.update(lspMoi))// Lưu xuống DB 
                {           
                    JOptionPane.showMessageDialog(this, "Update Loại SP thành công");
                } else {
                    JOptionPane.showMessageDialog(this, "Update thất bại!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra định dạng đầu vào");
        }    
    }
}
