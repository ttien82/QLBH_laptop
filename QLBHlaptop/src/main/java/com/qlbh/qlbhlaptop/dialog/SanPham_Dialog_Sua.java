/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.dao.LoaiSPDAO;
import com.qlbh.qlbhlaptop.dao.NhaCungCapDAO;
import com.qlbh.qlbhlaptop.dao.SanPhamDAO;
import com.qlbh.qlbhlaptop.model.LoaiSP;
import com.qlbh.qlbhlaptop.model.NhaCungCap;
import com.qlbh.qlbhlaptop.model.SanPham;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author PC
 */
public class SanPham_Dialog_Sua extends JDialog{
    private JTextField txtMaSP, txtTenSP, txtCPU, txtRAM, txtOCung, txtCard, txtGiaBan, txtSoLuong;
    private JComboBox<NhaCungCap> cboNCC;
    private JComboBox<LoaiSP> cboLoaiSP;
    private JLabel lblHinhAnh;
    private JButton btnChonAnh, btnLuu;
    private File file;
    private SanPhamDAO spdao = new SanPhamDAO();
    private DefaultComboBoxModel<NhaCungCap> modelNCC = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<LoaiSP> modelLoaiSP = new DefaultComboBoxModel<>();

    public SanPham_Dialog_Sua(JFrame parent, String maSP) {
        super(parent, "Update Sản Phẩm", true);
        //1.Lay du lieu de fill vao cboNCC va cboTenLoaiSP
        NhaCungCapDAO nccdao = new NhaCungCapDAO();
        LoaiSPDAO loaispdao = new LoaiSPDAO();
        SanPham sp = new SanPham();
        sp = spdao.getById(maSP);
        txtMaSP = new JTextField(maSP);

        List <NhaCungCap> LstNcc = nccdao.getAll();
        List <LoaiSP> lstLSP =  loaispdao.getAll();
        for(NhaCungCap nhacc: LstNcc)
        {
            modelNCC.addElement(nhacc);
        }      
        for (LoaiSP loai : lstLSP) {
            modelLoaiSP.addElement(loai);
        }
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));

        panelForm.add(new JLabel("Tên SP:"));
        txtTenSP = new JTextField();
        txtTenSP.setText(sp.getTenSP());
        panelForm.add(txtTenSP);

        panelForm.add(new JLabel("Nhà Cung Cấp:"));
        cboNCC = new JComboBox<NhaCungCap>(modelNCC);
        cboNCC.setModel(modelNCC);
        //HienThi NhaCC
        for (int i = 0; i < cboNCC.getItemCount(); i++) {
            NhaCungCap item = cboNCC.getItemAt(i);
            if (item.getMaNCC().equals(sp.getMaNCC())) {
                cboNCC.setSelectedIndex(i);
                break;
            }
        }
        panelForm.add(cboNCC);
        
        panelForm.add(new JLabel("Loại SP:"));
        cboLoaiSP = new JComboBox<LoaiSP>(modelLoaiSP);   
        //HienThi LoaiSPTuongUng
        for (int i = 0; i < cboLoaiSP.getItemCount(); i++) {
            LoaiSP item = cboLoaiSP.getItemAt(i);
            if (item.getMaLoaiSP().equals(sp.getMaLoaiSP())) {
                cboLoaiSP.setSelectedIndex(i);
                break;
            }
        }
        panelForm.add(cboLoaiSP);
                
        panelForm.add(new JLabel("CPU:"));
        txtCPU = new JTextField();
        txtCPU.setText(sp.getCpu());
        panelForm.add(txtCPU);

        panelForm.add(new JLabel("RAM:"));
        txtRAM = new JTextField();
        txtRAM.setText(sp.getRam());
        panelForm.add(txtRAM);

        panelForm.add(new JLabel("Ổ Cứng:"));
        txtOCung = new JTextField();
        txtOCung.setText(sp.getOCung());
        panelForm.add(txtOCung);

        panelForm.add(new JLabel("Card MH:"));
        txtCard = new JTextField();
        txtCard.setText(sp.getCardManHinh());
        panelForm.add(txtCard);

        panelForm.add(new JLabel("Giá Bán:"));
        txtGiaBan = new JTextField();
        txtGiaBan.setText(sp.getGiaBan().toString());
        panelForm.add(txtGiaBan);

        panelForm.add(new JLabel("Số Lượng:"));
        txtSoLuong = new JTextField();
        txtSoLuong.setText(String.valueOf(sp.getSoLuongTon()));
        panelForm.add(txtSoLuong);

        add(panelForm, BorderLayout.CENTER);

        // Panel ảnh
        JPanel panelImage = new JPanel(new FlowLayout());
        lblHinhAnh = new JLabel(sp.getHinhAnh());
        btnChonAnh = new JButton("Chọn Ảnh");
        btnChonAnh.addActionListener(this::chonAnh);
        panelImage.add(lblHinhAnh);
        panelImage.add(btnChonAnh);
        add(panelImage, BorderLayout.NORTH);

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

    //Lay mancc va maloaisp tuong ung
    private String getSelectedMaLoaiSP(JComboBox<LoaiSP> comboBox) 
    {
        LoaiSP selected = (LoaiSP) comboBox.getSelectedItem();
        return selected.getMaLoaiSP();
    }
    
    private String getSelectedMaNCC(JComboBox<NhaCungCap> comboBox) 
    {
        NhaCungCap selected = (NhaCungCap) comboBox.getSelectedItem();
        return selected.getMaNCC();
    }
    
    private void chonAnh(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("images")); // thư mục ảnh
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            lblHinhAnh.setText(file.getName());
        }
    }

    //Luu
    private void LuuThayDoi() {
        String tenSP = txtTenSP.getText().trim();
        String giaBanStr = txtGiaBan.getText().trim();

        // Kiểm tra giá bán có phải là số VA Kiểm tra tên SP rỗng
        try {
            double giaBan = Double.parseDouble(giaBanStr);
            if (giaBan <= 0 || tenSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống va Giá bán phải lớn hơn 0!");
                txtGiaBan.requestFocus();
            }
            else
            {
                SanPham spMoi = new SanPham(
                        txtMaSP.getText(),
                        txtTenSP.getText(),
                        getSelectedMaNCC(cboNCC),
                        getSelectedMaLoaiSP(cboLoaiSP),
                        txtCPU.getText(),
                        txtRAM.getText(),
                        txtOCung.getText(),
                        txtCard.getText(),
                        new BigDecimal(txtGiaBan.getText()),
                        Integer.parseInt(txtSoLuong.getText()),
                        lblHinhAnh.getText()
                    );          
                if (spdao.update(spMoi))// Lưu xuống DB 
                {
                    if (file != null) {
                        // Thư mục đích (folder images trong dự án)
                        File destDir = new File("images");
                        // File đích trong folder images
                        File destFile = new File(destDir, lblHinhAnh.getText());
                        try 
                        {
                            // Copy file vào folder images, nếu trùng thì ghi đè
                            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);   

                        } 
                        catch (IOException ex) 
                        {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(this, "Lỗi khi copy ảnh!");
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Update sản phẩm thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Update thất bại!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi kiểm tra dữ liệu");
        }      
    }
}
