package com.qlbh.qlbhlaptop.controller;

import com.qlbh.qlbhlaptop.dao.TaiKhoanDAO;
import com.qlbh.qlbhlaptop.model.Quyen;
import com.qlbh.qlbhlaptop.model.NhanVien;
import com.qlbh.qlbhlaptop.model.TaiKhoan;
import com.qlbh.qlbhlaptop.view.TaiKhoanPanel;
import com.qlbh.qlbhlaptop.dao.QuyenDAO;
import com.qlbh.qlbhlaptop.dao.NhanVienDAO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.SwingWorker;

/**
 * Lớp điều khiển cho TaiKhoanPanel, xử lý các sự kiện và tương tác với DAO.
 */
public class TaiKhoanController {

    private final TaiKhoanPanel view;
    private final TaiKhoanDAO taiKhoanDAO;
    private final NhanVienDAO nhanVienDAO;
    private final QuyenDAO quyenDAO;

    public TaiKhoanController(TaiKhoanPanel view) {
        this.view = view;
        this.taiKhoanDAO = new TaiKhoanDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.quyenDAO = new QuyenDAO();
        
        // Thêm các ActionListener cho các nút
        view.getBtnAdd().addActionListener(e -> onAdd());
        view.getBtnEdit().addActionListener(e -> onEdit());
        view.getBtnDelete().addActionListener(e -> onDelete());
        view.getBtnRefresh().addActionListener(e -> onRefresh());
        view.getBtnLuu().addActionListener(e -> onLuu());
        view.getBtnHuy().addActionListener(e -> onHuy());
        
        // Tải dữ liệu ban đầu khi khởi tạo
        loadAllData();
    }
    
    // Tải tất cả dữ liệu (tài khoản, nhân viên, quyền)
    public void loadAllData() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                final List<TaiKhoan> danhSachTaiKhoan = taiKhoanDAO.getAll();
                final List<NhanVien> danhSachNhanVien = nhanVienDAO.getAll();
                final List<Quyen> danhSachQuyen = quyenDAO.getAll();
                SwingUtilities.invokeLater(() -> {
                    view.fillTable(danhSachTaiKhoan);
                    view.loadNhanVienComboBox(danhSachNhanVien);
                    view.loadQuyenComboBox(danhSachQuyen);
                });
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
    
    // Xử lý sự kiện khi bấm nút Thêm
    private void onAdd() {
        view.clearDetailForm();
        String newMaTK = taiKhoanDAO.getLatestMaTK();
        TaiKhoan newAccount = new TaiKhoan();
        newAccount.setMaTK(newMaTK);
        view.setDetailForm(newAccount);
        view.showDetailDialog(true);
    }
    
    // Xử lý sự kiện khi bấm nút Sửa
    private void onEdit() {
        int selectedRow = view.getTblTaiKhoan().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một tài khoản để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TaiKhoan tk = view.getTaiKhoanFromSelectedRow(selectedRow);
        view.setDetailForm(tk);
        view.showDetailDialog(false);
    }
    
    // Xử lý sự kiện khi bấm nút Xóa
    private void onDelete() {
        int selectedRow = view.getTblTaiKhoan().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một tài khoản để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String maTK = (String) view.getTblTaiKhoan().getValueAt(selectedRow, 0);
            
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return taiKhoanDAO.delete(maTK);
                }
                
                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(view, "Xóa tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            loadAllData();
                        } else {
                            JOptionPane.showMessageDialog(view, "Xóa tài khoản thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(view, "Lỗi khi xóa tài khoản: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }
    }
    
    // Xử lý sự kiện khi bấm nút Làm mới
    private void onRefresh() {
        loadAllData();
    }
    
    // Xử lý sự kiện khi bấm nút Lưu (Thêm/Sửa)
    private void onLuu() {
        TaiKhoan tk = view.getTaiKhoanFromDetailForm();
        if (view.isAdding()) {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    taiKhoanDAO.insert(tk);
                    return null;
                }
                
                @Override
                protected void done() {
                    try {
                        get(); // Lấy kết quả để bắt ngoại lệ
                        JOptionPane.showMessageDialog(view, "Thêm tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        view.hideDetailDialog();
                        loadAllData();
                    } catch (Exception e) {
                        String errorMessage = "Lỗi khi thêm tài khoản: ";
                        Throwable cause = e.getCause();
                        if (cause != null) {
                            errorMessage += cause.getMessage();
                        } else {
                            errorMessage += e.getMessage();
                        }
                        JOptionPane.showMessageDialog(view, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        } else {
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return taiKhoanDAO.update(tk);
                }
                
                @Override
                protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(view, "Sửa tài khoản thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            view.hideDetailDialog();
                            loadAllData();
                        } else {
                            JOptionPane.showMessageDialog(view, "Sửa tài khoản thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(view, "Lỗi khi sửa tài khoản: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        }
    }
    
    // Xử lý sự kiện khi bấm nút Hủy
    private void onHuy() {
        view.hideDetailDialog();
    }
}
