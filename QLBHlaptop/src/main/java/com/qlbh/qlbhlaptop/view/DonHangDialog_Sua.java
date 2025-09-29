package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.DonHangDAO;
import com.qlbh.qlbhlaptop.model.DonHang;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;

public class DonHangDialog_Sua extends JDialog {
    private JTextField txtMaKH, txtMaNV, txtTongTien, txtTrangThai;
    private JFormattedTextField txtNgayLap;
    private DonHang donHang;
    private DonHangDAO dao = new DonHangDAO();

    public DonHangDialog_Sua(Frame parent, DonHang dh) {
        super(parent, "Cập nhật đơn hàng", true);
        this.donHang = dh;
        initComponents();
        fillForm();
        setSize(400, 300);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        panel.add(new JLabel("Mã KH:"));
        txtMaKH = new JTextField();
        panel.add(txtMaKH);

        panel.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField();
        panel.add(txtMaNV);

        panel.add(new JLabel("Ngày lập (yyyy-mm-dd):"));
        txtNgayLap = new JFormattedTextField();
        panel.add(txtNgayLap);

        panel.add(new JLabel("Tổng tiền:"));
        txtTongTien = new JTextField();
        panel.add(txtTongTien);

        panel.add(new JLabel("Trạng thái:"));
        txtTrangThai = new JTextField();
        panel.add(txtTrangThai);

        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> save());
        panel.add(btnSave);

        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dispose());
        panel.add(btnCancel);

        add(panel);
    }

    private void fillForm() {
        txtMaKH.setText(donHang.getMaKH());
        txtMaNV.setText(donHang.getMaNV());
        txtNgayLap.setText(donHang.getNgayLap().toString());
        txtTongTien.setText(donHang.getTongTien().toString());
        txtTrangThai.setText(donHang.getTrangThai());
    }

    private void save() {
        try {
            donHang.setMaKH(txtMaKH.getText().trim());
            donHang.setMaNV(txtMaNV.getText().trim());
            donHang.setNgayLap(Date.valueOf(txtNgayLap.getText().trim()));
            donHang.setTongTien(new BigDecimal(txtTongTien.getText().trim()));
            donHang.setTrangThai(txtTrangThai.getText().trim());

            if (dao.update(donHang)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }
}
