/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.view;

/**
 *
 * @author Bui Anh Quoc
 */
import com.qlbh.qlbhlaptop.dao.ChiTietDonHangDAO;
import com.qlbh.qlbhlaptop.model.ChiTietDonHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChiTietDonHangDialog extends JDialog {
    private JTable tblChiTiet;

    public ChiTietDonHangDialog(String maDH) {
        setTitle("Chi tiết đơn hàng - " + maDH);
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] columns = {"Mã ĐH", "Mã SP", "Số lượng", "Đơn giá"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        tblChiTiet = new JTable(model);
        add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        ChiTietDonHangDAO dao = new ChiTietDonHangDAO();
        List<ChiTietDonHang> list = dao.getByDonHang(maDH);
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
