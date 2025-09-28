package com.qlbh.qlbhlaptop.controller;

import com.qlbh.qlbhlaptop.dao.TaiKhoanDAO;
import com.qlbh.qlbhlaptop.dao.NhanVienDAO;
import com.qlbh.qlbhlaptop.dao.QuyenDAO;
import com.qlbh.qlbhlaptop.dto.TaiKhoanDTO;
import com.qlbh.qlbhlaptop.model.TaiKhoan;
import com.qlbh.qlbhlaptop.model.NhanVien;
import com.qlbh.qlbhlaptop.model.Quyen;
import com.qlbh.qlbhlaptop.view.TaiKhoanPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

public class TaiKhoanController {
    private final TaiKhoanPanel view;
    private final TaiKhoanDAO taiKhoanDAO;
    private final NhanVienDAO nhanVienDAO;
    private final QuyenDAO quyenDAO;

    private boolean isAdding = false; // Trạng thái đang Thêm/Sửa

    public TaiKhoanController(TaiKhoanPanel view) {
        this.view = view;
        this.taiKhoanDAO = new TaiKhoanDAO();
        this.nhanVienDAO = new NhanVienDAO();
        this.quyenDAO = new QuyenDAO();

        // 1. Gắn sự kiện (Event binding)
        view.getBtnAdd().addActionListener(e -> onAdd());
        view.getBtnDelete().addActionListener(e -> onDelete());
        view.getBtnRefresh().addActionListener(e -> onRefresh());
        view.getBtnLuu().addActionListener(e -> onLuu());
        view.getBtnHuy().addActionListener(e -> onHuy());
        view.getBtnTim().addActionListener(e -> TimKiem());
        view.getTxtTim().addActionListener(e -> TimKiem());
        view.addTableSelectionListener(this::onRowSelected); 

        // 2. Tải dữ liệu ban đầu
        loadAllData();
    }

    // --- I. Tải Dữ Liệu (Load Operations) ---

    private void loadAllData() {
        new SwingWorker<Void, Void>() {
            List<TaiKhoanDTO> dsTK;
            List<NhanVien> dsNV;
            List<Quyen> dsQ;

            @Override
            protected Void doInBackground() throws Exception {
                // Thao tác DAO trong luồng nền
                dsTK = taiKhoanDAO.getAllDTO();
                dsNV = nhanVienDAO.getAll();
                dsQ = quyenDAO.getAll();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Lấy kết quả (hoặc Exception)
                    // Đổ dữ liệu vào View (trên EDT)
                    view.loadNhanVienComboBox(dsNV);
                    view.loadQuyenComboBox(dsQ);
                    view.fillTable(dsTK);
                    view.clearDetailForm();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi tải dữ liệu: " + e.getCause().getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
    
    private void onRowSelected(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || view.getTblTaiKhoan().getSelectedRow() == -1) return;

        isAdding = false;
        view.getBtnLuu().setEnabled(false); // Khóa nút Lưu tạm thời

        int selectedRow = view.getTblTaiKhoan().getSelectedRow();
        String maTK = (String) view.getTblTaiKhoan().getValueAt(selectedRow, 0);
             
        new SwingWorker<TaiKhoan, Void>() {
            @Override
            protected TaiKhoan doInBackground() throws Exception {
                return taiKhoanDAO.getById(maTK);
            }

            @Override
            protected void done() {
                try {
                    TaiKhoan tk = get();
                    if (tk != null) {
                        view.setDetailForm(tk);
                        view.getBtnLuu().setEnabled(true);
                    } else {
                        JOptionPane.showMessageDialog(view, "Không tìm thấy tài khoản chi tiết.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        view.clearDetailForm();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Lỗi tải chi tiết tài khoản: " + ex.getCause().getMessage());
                    view.clearDetailForm();
                }
            }
        }.execute();
    }

    // --- II. Thao Tác CRUD (Action Handlers) ---

    private void onAdd() {
        view.clearDetailForm();
        isAdding = true;
        view.getBtnLuu().setEnabled(true);
        
        // Tải mã TK mới trong nền
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return taiKhoanDAO.getLatestMaTK();
            }

            @Override
            protected void done() {
                try {
                    view.getTxtMaTK().setText(get());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi tạo mã TK: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void onDelete() {
        int row = view.getTblTaiKhoan().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn một tài khoản để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final String maTK = (String) view.getTblTaiKhoan().getValueAt(row, 0);
        
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa tài khoản này không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return taiKhoanDAO.delete(maTK);
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(view, "Xóa tài khoản thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        loadAllData();
                    } else {
                        JOptionPane.showMessageDialog(view, "Xóa tài khoản thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi xóa: " + e.getCause().getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void onRefresh() {
        loadAllData();
    }

    private void onLuu() {
        final TaiKhoan tk = view.getTaiKhoanFromForm();
        
        // Validation (Nghiệp vụ Controller)
        if (tk.getTenDangNhap().isEmpty() || new String(view.getPwdPassword().getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(view, "Tên đăng nhập và Mật khẩu không được trống khi Lưu/Thêm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isAdding && tk.getMaTK().isEmpty()) {
             JOptionPane.showMessageDialog(view, "Không thể cập nhật tài khoản không có Mã TK.", "Lỗi", JOptionPane.ERROR_MESSAGE);
             return;
        }

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (isAdding) {
                    return taiKhoanDAO.insert(tk);
                } else {
                    return taiKhoanDAO.update(tk);
                }
            }

            @Override
            protected void done() {
                try {
                    if (get()) {
                        JOptionPane.showMessageDialog(view, (isAdding ? "Thêm" : "Sửa") + " thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        loadAllData();
                    } else {
                        JOptionPane.showMessageDialog(view, (isAdding ? "Thêm" : "Sửa") + " thất bại! (Lỗi logic hoặc dữ liệu không thay đổi)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi lưu: " + e.getCause().getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void onHuy() {
        view.clearDetailForm();
        view.getTblTaiKhoan().clearSelection();
    }
    
    private void TimKiem() {
        final String keyword = view.getTxtTim().getText().trim();

        // Nếu ô tìm kiếm trống, tải lại toàn bộ dữ liệu
        if (keyword.isEmpty()) {
            loadAllData();
            return;
        }

        new SwingWorker<List<TaiKhoanDTO>, Void>() {
            @Override
            protected List<TaiKhoanDTO> doInBackground() throws Exception {
                // Sử dụng phương thức searchDTO mà bạn đã thêm vào TaiKhoanDAO
                return taiKhoanDAO.TimDTO(keyword);
            }

            @Override
            protected void done() {
                try {
                    List<TaiKhoanDTO> ketQua = get();

                    // Cập nhật bảng
                    view.fillTable(ketQua); // Giả định view có phương thức fillTable nhận List<TaiKhoanDTO>
                    view.clearDetailForm();

                    if (ketQua.isEmpty()) {
                        JOptionPane.showMessageDialog(view, "Không tìm thấy tài khoản nào cho từ khóa: '" + keyword + "'", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Lỗi tìm kiếm: " + e.getCause().getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}