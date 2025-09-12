package com.qlbh.qlbhlaptop.dialog;

import com.qlbh.qlbhlaptop.model.SanPham;
import com.qlbh.qlbhlaptop.dao.NhaCungCapDAO;
import com.qlbh.qlbhlaptop.model.NhaCungCap;
import com.qlbh.qlbhlaptop.model.LoaiSP;
import com.qlbh.qlbhlaptop.dao.LoaiSPDAO;
import com.qlbh.qlbhlaptop.dao.SanPhamDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;


public class SanPham_Dialog_Them extends JDialog{
    private JTextField txtMaSP, txtTenSP, txtCPU, txtRAM, txtOCung, txtCard, txtGiaBan, txtSoLuong;
    private JComboBox<NhaCungCap> cboNCC;
    private JComboBox<LoaiSP> cboLoaiSP;
    private JLabel lblHinhAnh;
    private JButton btnChonAnh, btnLuu;
    private SanPhamDAO spdao = new SanPhamDAO();
    private File file;
    private DefaultComboBoxModel<NhaCungCap> modelNCC = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<LoaiSP> modelLoaiSP = new DefaultComboBoxModel<>();

    private void Load_ThemSPDialog()
    {
        //clear data field
        txtMaSP.setText("");
        txtMaSP.requestFocus();
        txtCPU.setText("");
        txtCard.setText("");
        txtGiaBan.setText("");
        txtOCung.setText("");
        txtRAM.setText("");
        txtSoLuong.setText("");
        txtTenSP.setText("");
        cboNCC.setSelectedIndex(0);
        cboLoaiSP.setSelectedIndex(0);
        lblHinhAnh.setText("Chưa chọn ảnh");
        file = null;
    }
    
    public SanPham_Dialog_Them(JFrame parent) {
        super(parent, "Thêm Sản Phẩm", true);
        //1.Lay du lieu de fill vao cboNCC va cboTenLoaiSP
        NhaCungCapDAO nccdao = new NhaCungCapDAO();
        LoaiSPDAO loaispdao = new LoaiSPDAO();
                
        var LstNcc = nccdao.getAll();
        var lstLSP =  loaispdao.getAll();
        for(NhaCungCap nhacc: LstNcc)
        {
            modelNCC.addElement(nhacc);
        }      
        for (LoaiSP loai : lstLSP) {
            modelLoaiSP.addElement(loai);
        }
        
        setLayout(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 5, 5));

        panelForm.add(new JLabel("Mã SP:"));
        txtMaSP = new JTextField();
        panelForm.add(txtMaSP);

        panelForm.add(new JLabel("Tên SP:"));
        txtTenSP = new JTextField();
        panelForm.add(txtTenSP);

        
        panelForm.add(new JLabel("Nhà Cung Cấp:"));
        cboNCC = new JComboBox(modelNCC);
        cboNCC.setModel(modelNCC);
       
        cboNCC.setSelectedIndex(0);
        panelForm.add(cboNCC);
        
        panelForm.add(new JLabel("Loại SP:"));
        cboLoaiSP = new JComboBox<>();   
        cboLoaiSP.setModel(modelLoaiSP);
        cboLoaiSP.setSelectedIndex(0);
        panelForm.add(cboLoaiSP);
                
        panelForm.add(new JLabel("CPU:"));
        txtCPU = new JTextField();
        panelForm.add(txtCPU);

        panelForm.add(new JLabel("RAM:"));
        txtRAM = new JTextField();
        panelForm.add(txtRAM);

        panelForm.add(new JLabel("Ổ Cứng:"));
        txtOCung = new JTextField();
        panelForm.add(txtOCung);

        panelForm.add(new JLabel("Card MH:"));
        txtCard = new JTextField();
        panelForm.add(txtCard);

        panelForm.add(new JLabel("Giá Bán:"));
        txtGiaBan = new JTextField();
        panelForm.add(txtGiaBan);

        panelForm.add(new JLabel("Số Lượng:"));
        txtSoLuong = new JTextField();
        panelForm.add(txtSoLuong);

        add(panelForm, BorderLayout.CENTER);

        // Panel ảnh
        JPanel panelImage = new JPanel(new FlowLayout());
        lblHinhAnh = new JLabel("Chưa chọn ảnh");
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
            LuuThem();
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
        //chooser.setCurrentDirectory(new File("images")); //set thư mục khi mo len
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();//file Goc
            lblHinhAnh.setText(file.getName());
        }
    }

    //Luu
    private void LuuThem() {
        String tenSP = txtTenSP.getText().trim();
        String giaBanStr = txtGiaBan.getText().trim();

        // Kiểm tra giá bán có phải là số VA Kiểm tra tên SP rỗng
            double giaBan = Double.parseDouble(giaBanStr);
            if (giaBan <= 0 || tenSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống va Giá bán phải lớn hơn 0!");
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
                if (spdao.insert(spMoi))// Lưu xuống DB 
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

                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                    Load_ThemSPDialog();
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                }
            }
    }
}