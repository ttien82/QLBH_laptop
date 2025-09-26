// com.qlbh.qlbhlaptop.controller.KhachHangController
package com.qlbh.qlbhlaptop.controller;

import com.qlbh.qlbhlaptop.dao.DAOException;
import com.qlbh.qlbhlaptop.dao.KhachHangDAO;
import com.qlbh.qlbhlaptop.model.KhachHang;
import com.qlbh.qlbhlaptop.view.KhachHangPanel;
import com.qlbh.qlbhlaptop.dialog.KhachHang_Dialog_Them;
import com.qlbh.qlbhlaptop.dialog.KhachHang_Dialog_Sua;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Window;
import java.util.stream.Collectors;
import java.util.Locale;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

public class KhachHangController {

    private final KhachHangPanel view;
    private final KhachHangDAO dao;

    private final DefaultTableModel model;
    private List<KhachHang> cache = new ArrayList<>();

    public KhachHangController(KhachHangPanel view) {
        this.view = view;
        this.dao = new KhachHangDAO();

        // 1) Model & cấu hình bảng
        String[] cols = {"Mã KH", "Tên KH", "Điện thoại", "Email", "Địa chỉ"};
        this.model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                return String.class;
            }
        };
        view.setTableModel(model);
        JTable tbl = view.getTbl();
        tbl.setAutoCreateRowSorter(true);
        tbl.setRowHeight(24);
        tbl.setDefaultEditor(Object.class, null);

        // Selection → enable/disable nút
        view.getBtnEdit().setEnabled(false);
        view.getBtnDelete().setEnabled(false);
        ListSelectionListener lsl = e -> {
            if (!e.getValueIsAdjusting()) {
                boolean selected = tbl.getSelectedRow() != -1;
                view.getBtnEdit().setEnabled(selected);
                view.getBtnDelete().setEnabled(selected);
            }
        };
        tbl.getSelectionModel().addListSelectionListener(lsl);

        view.getBtnSearch().addActionListener(ev -> onSearch());
        view.getBtnAdd().addActionListener(ev -> onAdd());
        view.getBtnEdit().addActionListener(ev -> onEdit());
        view.getBtnDelete().addActionListener(ev -> onDelete());

        // 4) Tải dữ liệu ban đầu
        refreshData();
    }

    /* =================== Actions =================== */
    private void refreshData() {
        try {
            cache = dao.getAll();
            fillTable(cache);
        } catch (DAOException ex) {
            showError("Không thể tải danh sách khách hàng", ex);
        }
    }
    
    private void onSearch() {
        String kw = view.getTxtSearch().getText().trim().toLowerCase(Locale.ROOT);
        if (kw.isEmpty()) {
            fillTable(cache);
            return;
        }
        List<KhachHang> filtered = cache.stream().filter(kh
                -> contains(kh.getMaKH(), kw)
                || contains(kh.getTenKH(), kw)
                || contains(kh.getDienThoai(), kw)
                || contains(kh.getDiaChi(), kw)
        ).collect(Collectors.toList());
        fillTable(filtered);
    }
    

    private static boolean contains(String s, String kw) {
        return s != null && s.toLowerCase(Locale.ROOT).contains(kw);
    }
    private void onAdd() {
        Window window = SwingUtilities.getWindowAncestor(view);
        JFrame parent = (window instanceof JFrame) ? (JFrame) window : null;

        JDialog dlg = new KhachHang_Dialog_Them(parent); // dialog modal
        dlg.setVisible(true); // chờ đến khi đóng
        refreshData();        // load lại bảng
    }

    private void onEdit() {
        int viewRow = view.getTbl().getSelectedRow();
        if (viewRow == -1) {
            return;
        }

        int row = view.getTbl().convertRowIndexToModel(viewRow);
        String ma = (String) model.getValueAt(row, 0); // "Mã KH" ở cột 0

        Window window = SwingUtilities.getWindowAncestor(view);
        JFrame parent = (window instanceof JFrame) ? (JFrame) window : null;

        JDialog dlg = new KhachHang_Dialog_Sua(parent, ma);
        dlg.setVisible(true);
        refreshData();
    }

    private void onDelete() {
        int viewRow = view.getTbl().getSelectedRow();
        if (viewRow == -1) {
            return;
        }

        int row = view.getTbl().convertRowIndexToModel(viewRow);
        String ma = (String) model.getValueAt(row, 0);

        int ok = JOptionPane.showConfirmDialog(view,
                "Xoá khách hàng mã " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean done = dao.delete(ma);
            if (!done) {
                JOptionPane.showMessageDialog(view, "Không xoá được khách hàng " + ma);
            }
            refreshData();
        } catch (DAOException ex) {
            showError("Lỗi khi xoá khách hàng " + ma, ex);
        }
    }

    /* =================== Helpers =================== */
    private void fillTable(List<KhachHang> list) {
        model.setRowCount(0);
        for (KhachHang kh : list) {
            model.addRow(new Object[]{
                kh.getMaKH(), // cột 0
                kh.getTenKH(), // cột 1
                kh.getDienThoai(), // cột 2
                kh.getEmail(), // cột 3
                kh.getDiaChi() // cột 4
            });
        }
        view.getBtnEdit().setEnabled(false);
        view.getBtnDelete().setEnabled(false);
    }

    private void showError(String msg, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view, msg + "\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
