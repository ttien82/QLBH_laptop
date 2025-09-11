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


public class LoaiSP_Dialog_Them extends JDialog{
    private JTextField txtMaLoaiSP, txtTenLoaiSP;
    private JButton btnLuu;
    private LoaiSPDAO khdao = new LoaiSPDAO();

    private void Load_ThemLSPDialog()
    {
        //clear data field
        txtMaLoaiSP.setText("");
        txtMaLoaiSP.requestFocus(); 
        txtTenLoaiSP.setText("");
    }
    
    public LoaiSP_Dialog_Them(JFrame parent) {
        super(parent, "Thêm LoaiSP", true);
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));

        panelForm.add(new JLabel("Mã Loại SP:"));
        txtMaLoaiSP = new JTextField();
        panelForm.add(txtMaLoaiSP);

        panelForm.add(new JLabel("Tên Loại SP:"));
        txtTenLoaiSP = new JTextField();
        panelForm.add(txtTenLoaiSP);

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
        String ten = txtTenLoaiSP.getText().trim();

        // Kiểm tra giá bán có phải là số VA Kiểm tra tên SP rỗng
        try {
            if (ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên Loại SP không được để trống!");
                txtTenLoaiSP.requestFocus();
            }
            else
            {
                LoaiSP nvMoi = new LoaiSP(
                    txtMaLoaiSP.getText(),
                    txtTenLoaiSP.getText()
                );          
                if (khdao.insert(nvMoi))// Lưu xuống DB 
                {            
                    JOptionPane.showMessageDialog(this, "Thêm Loại SP thành công!");
                    Load_ThemLSPDialog();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra định dạng đầu vào");
        }     
    }
}