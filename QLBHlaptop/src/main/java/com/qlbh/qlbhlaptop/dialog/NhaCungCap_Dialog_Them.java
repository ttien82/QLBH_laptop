package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.model.NhaCungCap;
import com.qlbh.qlbhlaptop.dao.NhaCungCapDAO;
import javax.swing.*;
import java.awt.*;
import com.qlbh.qlbhlaptop.config.CheckExpression;

public class NhaCungCap_Dialog_Them extends JDialog{
    private JTextField txtMaNCC, txtTenNCC, txtSdt, txtDiaChi;
    private JButton btnLuu;
    private NhaCungCapDAO nccdao = new NhaCungCapDAO();
    private CheckExpression check = new CheckExpression();
    
    private void Load_ThemNCCDialog()
    {
        //clear data field
        txtMaNCC.setText("");
        txtMaNCC.requestFocus();
        txtTenNCC.setText("");
        txtDiaChi.setText("");
        txtSdt.setText("");}
    
    public NhaCungCap_Dialog_Them(JFrame parent) {
        super(parent, "Thêm NhaCungCap", true);
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));

        panelForm.add(new JLabel("Mã Nhà Cung Cấp:"));
        txtMaNCC = new JTextField();
        panelForm.add(txtMaNCC);

        panelForm.add(new JLabel("Tên Nhà Cung Cấp:"));
        txtTenNCC = new JTextField();
        panelForm.add(txtTenNCC);
   
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
        String ten = txtTenNCC.getText().trim();
        String SDT = txtSdt.getText().trim();

        // Kiểm tra giá bán có phải là số VA Kiểm tra tên SP rỗng
        try {
            if (check.checkPhone(SDT) || ten.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên Nhà cung cấp, Sđt không được để trống hoặc Sđt không đúng định dạng!");
                txtTenNCC.requestFocus();
            }
            else
            {
                NhaCungCap nccMoi = new NhaCungCap(
                    txtMaNCC.getText(),
                    txtTenNCC.getText(),
                    txtDiaChi.getText(),
                    txtSdt.getText()
                );          
                if (nccdao.insert(nccMoi))// Lưu xuống DB 
                {            
                    JOptionPane.showMessageDialog(this, "Thêm Nhà cung cấp thành công!");
                    Load_ThemNCCDialog();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm Nhà cung cấp");
        }    

    }
}