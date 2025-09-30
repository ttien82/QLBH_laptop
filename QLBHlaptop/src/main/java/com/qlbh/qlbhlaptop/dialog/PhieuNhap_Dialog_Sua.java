/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.dao.PhieuNhapDAO;
import com.qlbh.qlbhlaptop.model.PhieuNhap;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import java.math.BigDecimal;

import java.sql.Date;

public class PhieuNhap_Dialog_Sua extends JDialog{
    private JTextField txtMaPN, txtMaNCC, txtMaNV, txtTongTien;
    private JDateChooser dateChooser;
    private JButton btnLuu;
    private PhieuNhapDAO pndao = new PhieuNhapDAO();

    public PhieuNhap_Dialog_Sua(JFrame parent, String ma) {
        super(parent, "Update Phieu Nhap", true);
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));
        
        PhieuNhap pn = pndao.getById(ma);
        txtMaPN = new JTextField(ma);
        txtMaPN.setVisible(false);

        panelForm.add(new JLabel("Mã NCC:"));
        txtMaNCC = new JTextField(pn.getMaNCC());
        panelForm.add(txtMaNCC);
  
        panelForm.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField(pn.getMaNV());
        panelForm.add(txtMaNV);
          
        dateChooser  = new JDateChooser(pn.getNgayNhap());
        dateChooser.setDateFormatString("yyyy-MM-dd"); // Định dạng ngày
        
        panelForm.add(new JLabel("Ngày nhập: "));
        panelForm.add(dateChooser);
        
        panelForm.add(new JLabel("Tổng tiền: "));
        txtTongTien = new JTextField(pn.getTongTien().toString());
        txtTongTien.setEditable(false);
        panelForm.add(txtTongTien);
        
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
            BigDecimal tongtien = new BigDecimal(txtTongTien.getText().trim());
            PhieuNhap pnmoi = new PhieuNhap(
                    txtMaPN.getText(),
                    txtMaNCC.getText(),
                    txtMaNV.getText(),
                    dateChooser.getDate(),
                    tongtien
                ); 
            if (pndao.update(pnmoi))// Lưu xuống DB 
            {
                JOptionPane.showMessageDialog(this, "Update Phiếu Nhập thành công");
            } else {
                JOptionPane.showMessageDialog(this, "Update thất bại!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra định dạng đầu vào");
        }    
    }
}
