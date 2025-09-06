package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.DonHang;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD để quản lý thông tin đơn hàng.
 * Sử dụng PreparedStatement để tránh SQL Injection và tối ưu hiệu suất.
 */
public class DonHangDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng DonHang.
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng DonHang đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
     */
    private DonHang mapResultSetToDonHang(ResultSet rs) throws SQLException {
        DonHang dh = new DonHang();
        dh.setMaDH(rs.getString("MaDH"));
        dh.setMaKH(rs.getString("MaKH"));
        dh.setMaNV(rs.getString("MaNV"));
        dh.setNgayLap(rs.getDate("NgayLap"));
        dh.setTongTien(rs.getBigDecimal("TongTien"));
        dh.setTrangThai(rs.getString("TrangThai"));
        return dh;
    }

    /**
     * Lấy tất cả đơn hàng có trong cơ sở dữ liệu.
     * @return Một danh sách (List) các đối tượng DonHang.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<DonHang> getAll() {
        List<DonHang> list = new ArrayList<>();
        String sql = "SELECT * FROM DonHang";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToDonHang(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách đơn hàng", e);
        }
        return list;
    }

    /**
     * Lấy một đơn hàng cụ thể dựa trên mã đơn hàng.
     * @param maDH Mã đơn hàng.
     * @return Đối tượng DonHang nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public DonHang getById(String maDH) {
        String sql = "SELECT * FROM DonHang WHERE MaDH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDonHang(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy đơn hàng: " + maDH, e);
        }
        return null;
    }

    /**
     * Thêm một đơn hàng mới vào cơ sở dữ liệu.
     * @param dh Đối tượng DonHang cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(DonHang dh) {
        String sql = "INSERT INTO DonHang(MaDH, MaKH, MaNV, NgayLap, TongTien, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dh.getMaDH());
            ps.setString(2, dh.getMaKH());
            ps.setString(3, dh.getMaNV());
            ps.setDate(4, new java.sql.Date(dh.getNgayLap().getTime())); // Chuyển đổi từ java.util.Date sang java.sql.Date
            ps.setBigDecimal(5, dh.getTongTien());
            ps.setString(6, dh.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm đơn hàng", e);
        }
    }

    /**
     * Cập nhật thông tin của một đơn hàng đã tồn tại.
     * @param dh Đối tượng DonHang chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(DonHang dh) {
        String sql = "UPDATE DonHang SET MaKH=?, MaNV=?, NgayLap=?, TongTien=?, TrangThai=? WHERE MaDH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dh.getMaKH());
            ps.setString(2, dh.getMaNV());
            ps.setDate(3, new java.sql.Date(dh.getNgayLap().getTime())); // Chuyển đổi từ java.util.Date sang java.sql.Date
            ps.setBigDecimal(4, dh.getTongTien());
            ps.setString(5, dh.getTrangThai());
            ps.setString(6, dh.getMaDH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật đơn hàng", e);
        }
    }

    /**
     * Xóa một đơn hàng khỏi cơ sở dữ liệu dựa trên mã đơn hàng.
     * @param maDH Mã đơn hàng cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maDH) {
        String sql = "DELETE FROM DonHang WHERE MaDH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa đơn hàng: " + maDH, e);
        }
    }

    /**
     * Phương thức main để kiểm tra chức năng của lớp DonHangDAO.
     */
    public static void main(String[] args) {
        DonHangDAO dao = new DonHangDAO();

        System.out.println("--- DANH SÁCH ĐƠN HÀNG ---");
        List<DonHang> ds = dao.getAll();
        for (DonHang dh : ds) {
            System.out.println(dh);
        }

        System.out.println("\n--- THÊM ĐƠN HÀNG ---");
        DonHang dhMoi = new DonHang("DH010", "KH001", "NV001",
                new java.util.Date(), // Sử dụng java.util.Date
                new BigDecimal("50000000"),
                "Đang xử lý");
        boolean themOK = dao.insert(dhMoi);
        System.out.println("Thêm thành công? " + themOK);

        System.out.println("\n--- CẬP NHẬT ĐƠN HÀNG ---");
        dhMoi.setTongTien(new BigDecimal("45000000"));
        dhMoi.setTrangThai("Đã giao");
        boolean capNhatOK = dao.update(dhMoi);
        System.out.println("Cập nhật thành công? " + capNhatOK);

        System.out.println("\n--- TÌM ĐƠN HÀNG THEO ID ---");
        DonHang dhById = dao.getById("DH010");
        if (dhById != null) {
            System.out.println("Tìm thấy: " + dhById);
        } else {
            System.out.println("Không tìm thấy đơn hàng.");
        }

        System.out.println("\n--- XÓA ĐƠN HÀNG ---");
        boolean xoaOK = dao.delete("DH010");
        System.out.println("Xóa thành công? " + xoaOK);
    }
}
