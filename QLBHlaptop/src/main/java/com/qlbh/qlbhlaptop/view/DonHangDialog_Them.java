package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.DonHangDAO;
import com.qlbh.qlbhlaptop.dao.ChiTietDonHangDAO;
import com.qlbh.qlbhlaptop.model.DonHang;
import com.qlbh.qlbhlaptop.model.ChiTietDonHang;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;

public class DonHangDialog_Them extends JDialog {
    private JTextField txtMaDH, txtMaKH, txtMaNV, txtNgayLap, txtTongTien, txtTrangThai;
    private JTextField txtMaSP, txtSoLuong, txtDonGia;

    public DonHangDialog_Them(Frame parent) {
        super(parent, "Thêm Đơn Hàng", true);
        initComponents();
    }

    private void initComponents() {
        setSize(400, 450);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(10, 2, 5, 5));

        add(new JLabel("Mã Đơn Hàng:"));
        txtMaDH = new JTextField();
        add(txtMaDH);

        add(new JLabel("Mã Khách Hàng:"));
        txtMaKH = new JTextField();
        add(txtMaKH);

        add(new JLabel("Mã Nhân Viên:"));
        txtMaNV = new JTextField();
        add(txtMaNV);

        add(new JLabel("Ngày Lập (yyyy-MM-dd):"));
        txtNgayLap = new JTextField(java.time.LocalDate.now().toString());
        add(txtNgayLap);

        add(new JLabel("Tổng Tiền:"));
        txtTongTien = new JTextField();
        add(txtTongTien);

        add(new JLabel("Trạng Thái:"));
        txtTrangThai = new JTextField("Đang xử lý");
        add(txtTrangThai);

        // 🔹 Thêm 3 trường nhập cho chi tiết đơn hàng
        add(new JLabel("Mã Sản Phẩm:"));
        txtMaSP = new JTextField();
        add(txtMaSP);

        add(new JLabel("Số Lượng:"));
        txtSoLuong = new JTextField();
        add(txtSoLuong);

        add(new JLabel("Đơn Giá:"));
        txtDonGia = new JTextField();
        add(txtDonGia);

        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");

        add(btnSave);
        add(btnCancel);

        btnSave.addActionListener(e -> saveDonHang());
        btnCancel.addActionListener(e -> dispose());
    }

    private void saveDonHang() {
        try {
            DonHangDAO dhDAO = new DonHangDAO();
            ChiTietDonHangDAO ctDAO = new ChiTietDonHangDAO();

            DonHang dh = new DonHang(
                    txtMaDH.getText().trim(),
                    txtMaKH.getText().trim(),
                    txtMaNV.getText().trim(),
                    Date.valueOf(txtNgayLap.getText().trim()),
                    new BigDecimal(txtTongTien.getText().trim()),
                    txtTrangThai.getText().trim()
            );

            ChiTietDonHang ctdh = new ChiTietDonHang(
                    txtMaDH.getText().trim(),
                    txtMaSP.getText().trim(),
                    Integer.parseInt(txtSoLuong.getText().trim()),
                    new BigDecimal(txtDonGia.getText().trim())
            );

            if (dhDAO.insert(dh) && ctDAO.insert(ctdh)) {
                JOptionPane.showMessageDialog(this, "Thêm đơn hàng thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm đơn hàng!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
}
