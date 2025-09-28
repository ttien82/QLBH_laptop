package com.qlbh.qlbhlaptop.controller;

import com.qlbh.qlbhlaptop.dao.DAOException;
import com.qlbh.qlbhlaptop.dao.NhanVienDAO;
import com.qlbh.qlbhlaptop.model.NhanVien;
import com.qlbh.qlbhlaptop.view.NhanVienPanel;
import com.qlbh.qlbhlaptop.dto.RevenueByEmployeeDTO;
import com.qlbh.qlbhlaptop.dialog.NhanVien_Dialog_Them;
import com.qlbh.qlbhlaptop.dialog.NhanVien_Dialog_Sua;
import com.qlbh.qlbhlaptop.dialog.NhanVien_TopCustomer_Dialog;
import com.qlbh.qlbhlaptop.dialog.NhanVien_TopSale_Dialog;
import com.qlbh.qlbhlaptop.dto.TopCustomerByEmpDTO;

//
import java.time.LocalDate;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.awt.Window;

public class NhanVienController {

    private final NhanVienPanel view;
    private final NhanVienDAO dao;
    private final DefaultTableModel model;
    private List<NhanVien> cache = new ArrayList<>();

    public NhanVienController(NhanVienPanel view) {
        this.view = view;
        this.dao = new NhanVienDAO();

        // 1) Model & cấu hình bảng
        String[] cols = {"Mã NV", "Tên NV", "Điện thoại", "Địa chỉ"};
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

        // 2) Selection → enable/disable nút
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

        // 3) Gắn sự kiện (không đụng action trong View)
        view.getBtnSearch().addActionListener(ev -> onSearch());
        view.getBtnAdd().addActionListener(ev -> onAdd());
        view.getBtnEdit().addActionListener(ev -> onEdit());
        view.getBtnDelete().addActionListener(ev -> onDelete());
        view.getBtnTopSaleEmp().addActionListener(ev -> onTopSale());
        view.getBtnTopCustomerByEmp().addActionListener(e -> onTopCustomerByEmp());
        view.getBtnReload().addActionListener(e -> onReload());

        refreshData();
    }

    private void refreshData() {
        try {
            cache = dao.getAll();
            fillTable(cache);
        } catch (DAOException ex) {
            showError("Không thể tải danh sách nhân viên", ex);
        }
    }

    private void onSearch() {
        String kw = view.getTxtSearch().getText().trim().toLowerCase(Locale.ROOT);
        if (kw.isEmpty()) {
            fillTable(cache);
            return;
        }
        List<NhanVien> filtered = cache.stream().filter(nv
                -> contains(nv.getMaNV(), kw)
                || contains(nv.getTenNV(), kw)
                || contains(nv.getDienThoai(), kw)
                || contains(nv.getDiaChi(), kw)
        ).collect(Collectors.toList());
        fillTable(filtered);
    }

//    Top 10 nhân viên trong 90 ngày gần nhất
    private void onTopSale() {
        try {
            LocalDate to = LocalDate.now();
            LocalDate from = to.minusDays(90); // lấy trong 90 ngày
            List<RevenueByEmployeeDTO> top = dao.getTopRevenueEmployees(from, to, 10); // set top 10 or 100 tùy

            Window w = SwingUtilities.getWindowAncestor(view);
            JFrame parent = (w instanceof JFrame) ? (JFrame) w : null;
            new NhanVien_TopSale_Dialog(parent, top, from, to).setVisible(true);
        } catch (DAOException ex) {
            showError("Không thể tính KPI doanh thu nhân viên", ex);
        }
    }

    private void onTopCustomerByEmp() {
        JTable tbl = view.getTbl();
        int viewRow = tbl.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(view, "Hãy chọn 1 nhân viên trước.");
            return;
        }
        int row = tbl.convertRowIndexToModel(viewRow);
        String maNV = (String) model.getValueAt(row, 0);
        String tenNV = (String) model.getValueAt(row, 1);

        try {
            // gọi DAO, ví dụ lấy Top 10 toàn thời gian
            List<TopCustomerByEmpDTO> rows = dao.getTopCustomersByEmployee(
                    maNV,
                    java.time.LocalDate.of(2025, 9, 1),
                    java.time.LocalDate.now(),
                    10, // Top 10
                    true // loại đơn hủy
            );

            java.awt.Window w = SwingUtilities.getWindowAncestor(view);
            JFrame parent = (w instanceof JFrame) ? (JFrame) w : null;
            new NhanVien_TopCustomer_Dialog(parent, tenNV, rows, null, null).setVisible(true);

        } catch (DAOException ex) {
            showError("Không thể lấy Top khách hàng của nhân viên " + maNV, ex);
        }
    }

    private void onAdd() {
        // Lấy "cửa sổ main" (MainFrame) từ View
        Window window = SwingUtilities.getWindowAncestor(view);
        JFrame parent = (window instanceof JFrame) ? (JFrame) window : null;

        // Mở dialog "Thêm nhân viên" ở chế độ modal
        NhanVien_Dialog_Them dlg = new NhanVien_Dialog_Them(parent);
        dlg.setVisible(true); // Chặn ở đây cho đến khi dialog đóng

        // Khi dialog đóng, refresh lại dữ liệu bảng
        refreshData();
    }

    private void onEdit() {
        int viewRow = view.getTbl().getSelectedRow();
        if (viewRow == -1) {
            return;
        }

        int row = view.getTbl().convertRowIndexToModel(viewRow);
        String ma = (String) model.getValueAt(row, 0);

        // Lấy parent JFrame từ view để truyền cho JDialog
        Window window = SwingUtilities.getWindowAncestor(view);
        JFrame parent = (window instanceof JFrame) ? (JFrame) window : null;

        // Mở dialog sửa (modal)
        NhanVien_Dialog_Sua dlg = new NhanVien_Dialog_Sua(parent, ma);
        dlg.setVisible(true); // chờ đến khi dialog đóng

        // Sau khi dialog đóng, tải lại dữ liệu
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
                "Xoá nhân viên mã " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (!dao.delete(ma)) {
                JOptionPane.showMessageDialog(view, "Không xoá được nhân viên " + ma);
            }
            refreshData();
        } catch (DAOException ex) {
            showError("Lỗi khi xoá nhân viên " + ma, ex);
        }
    }

    private void fillTable(List<NhanVien> list) {
        model.setRowCount(0);
        for (NhanVien nv : list) {
            model.addRow(new Object[]{
                nv.getMaNV(), nv.getTenNV(), nv.getDienThoai(), nv.getDiaChi()
            });
        }
        view.getBtnEdit().setEnabled(false);
        view.getBtnDelete().setEnabled(false);
    }
    
    // tải lại dữ liệu
    private void onReload() {
    refreshData();
    JOptionPane.showMessageDialog(view, "Dữ liệu đã được tải lại.");
}

    private static boolean contains(String s, String kw) {
        return s != null && s.toLowerCase(Locale.ROOT).contains(kw);
    }
    
    

    private void showError(String msg, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(view, msg + "\n" + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}
