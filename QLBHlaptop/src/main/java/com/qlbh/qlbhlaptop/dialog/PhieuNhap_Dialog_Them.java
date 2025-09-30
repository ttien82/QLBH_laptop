package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.view.ChiTietPhieuNhapFrame;
import com.qlbh.qlbhlaptop.dao.PhieuNhapDAO;
import com.qlbh.qlbhlaptop.model.ChiTietPhieuNhap;
import com.qlbh.qlbhlaptop.model.PhieuNhap;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.function.Consumer;

public class PhieuNhap_Dialog_Them extends JDialog{
    private JTextField txtMaPN, txtMaNCC, txtMaNV;
    private JDateChooser dateChooser;
    private JButton btnLuu;
    private PhieuNhapDAO pndao = new PhieuNhapDAO();

    public PhieuNhap_Dialog_Them(JFrame parent) {
        super(parent, "Thêm PhieuNhap", true);
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));
        
        panelForm.add(new JLabel("Mã Phiếu Nhập:"));
        txtMaPN = new JTextField();        
        panelForm.add(txtMaPN);
        
        panelForm.add(new JLabel("Mã NCC:"));
        txtMaNCC = new JTextField();
        panelForm.add(txtMaNCC);
  
        panelForm.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        panelForm.add(txtMaNV);
          
        dateChooser  = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd"); // Định dạng ngày
        
        panelForm.add(new JLabel("Ngày nhập: "));
        panelForm.add(dateChooser);
        
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
        try {
            BigDecimal tongtien = new BigDecimal(0);
            PhieuNhap pnmoi = new PhieuNhap(
                    txtMaPN.getText(),
                    txtMaNCC.getText(),
                    txtMaNV.getText(),
                    dateChooser.getDate(),
                    tongtien
                ); 
            if (pndao.insert(pnmoi))// Lưu xuống DB 
            {          
                dispose();  // Đóng dialog
                ChiTietPhieuNhapFrame frame = new ChiTietPhieuNhapFrame(txtMaPN.getText());
                frame.setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(this, "Thêm Phiếu nhập thất bại!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra định dạng đầu vào");
        }     
    }
}