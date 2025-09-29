package com.qlbh.qlbhlaptop.controller;

import com.qlbh.qlbhlaptop.dao.ChiTietDonHangDAO;
import com.qlbh.qlbhlaptop.model.ChiTietDonHang;
import com.qlbh.qlbhlaptop.view.ChiTietDonHangPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.util.List;

public class ChiTietDonHangController {

    private ChiTietDonHangPanel view;
    private ChiTietDonHangDAO dao;

    public ChiTietDonHangController(ChiTietDonHangPanel panel) {
        this.view = panel;
        this.dao = new ChiTietDonHangDAO();
        initEventHandlers();
    }

    /** Gắn sự kiện cho các nút */
    private void initEventHandlers() {
        // Tìm kiếm theo mã đơn hàng
        view.getBtnSearch().addActionListener(e -> {
            String maDH = view.getTxtSearch().getText().trim();
            if (maDH.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập mã đơn hàng cần tìm!");
                return;
            }
            loadChiTietByMaDH(maDH);
        });

        // Thêm chi tiết đơn hàng
        view.getBtnAdd().addActionListener(e -> {
            String maDH = JOptionPane.showInputDialog("Nhập mã đơn hàng:");
            String maSP = JOptionPane.showInputDialog("Nhập mã sản phẩm:");
            String soLuongStr = JOptionPane.showInputDialog("Nhập số lượng:");
            String donGiaStr = JOptionPane.showInputDialog("Nhập đơn giá:");

            try {
                int soLuong = Integer.parseInt(soLuongStr);
                BigDecimal donGia = new BigDecimal(donGiaStr);
                ChiTietDonHang ct = new ChiTietDonHang(maDH, maSP, soLuong, donGia);
                if (dao.insert(ct)) {
                    JOptionPane.showMessageDialog(view, "Thêm chi tiết đơn hàng thành công!");
                    loadChiTietByMaDH(maDH);
                } else {
                    JOptionPane.showMessageDialog(view, "Thêm thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Dữ liệu nhập không hợp lệ: " + ex.getMessage());
            }
        });

        // Cập nhật chi tiết đơn hàng
        view.getBtnUpdate().addActionListener(e -> {
            int row = view.getTblChiTiet().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn 1 dòng để cập nhật!");
                return;
            }

            String maDH = (String) view.getTblChiTiet().getValueAt(row, 0);
            String maSP = (String) view.getTblChiTiet().getValueAt(row, 1);
            String soLuongStr = JOptionPane.showInputDialog("Nhập số lượng mới:");
            String donGiaStr = JOptionPane.showInputDialog("Nhập đơn giá mới:");

            try {
                int soLuong = Integer.parseInt(soLuongStr);
                BigDecimal donGia = new BigDecimal(donGiaStr);
                ChiTietDonHang ct = new ChiTietDonHang(maDH, maSP, soLuong, donGia);
                if (dao.update(ct)) {
                    JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
                    loadChiTietByMaDH(maDH);
                } else {
                    JOptionPane.showMessageDialog(view, "Cập nhật thất bại!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Dữ liệu nhập không hợp lệ: " + ex.getMessage());
            }
        });

        // Xóa chi tiết đơn hàng
        view.getBtnDelete().addActionListener(e -> {
            int row = view.getTblChiTiet().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn 1 dòng để xóa!");
                return;
            }

            String maDH = (String) view.getTblChiTiet().getValueAt(row, 0);
            String maSP = (String) view.getTblChiTiet().getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(view,
                    "Bạn có chắc chắn muốn xóa chi tiết đơn hàng [" + maDH + " - " + maSP + "]?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.delete(maDH, maSP)) {
                    JOptionPane.showMessageDialog(view, "Xóa thành công!");
                    loadChiTietByMaDH(maDH);
                } else {
                    JOptionPane.showMessageDialog(view, "Xóa thất bại!");
                }
            }
        });
    }

    /** Load dữ liệu vào JTable */
    public void loadChiTietByMaDH(String maDH) {
        List<ChiTietDonHang> list = dao.getByDonHang(maDH);
        DefaultTableModel model = (DefaultTableModel) view.getTblChiTiet().getModel();
        model.setRowCount(0);
        for (ChiTietDonHang ct : list) {
            model.addRow(new Object[]{
                    ct.getMaDH(),
                    ct.getMaSP(),
                    ct.getSoLuong(),
                    ct.getDonGia()
            });
        }
    }
}
