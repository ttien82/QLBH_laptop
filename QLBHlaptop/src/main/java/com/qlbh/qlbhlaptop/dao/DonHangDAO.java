package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.dto.DonHangViewDTO;
import com.qlbh.qlbhlaptop.model.DonHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonHangDAO {

    // Lấy tất cả đơn hàng kèm thông tin khách hàng
    public List<DonHangViewDTO> getAllDonHangWithKhachHang() {
        List<DonHangViewDTO> list = new ArrayList<>();
        String sql = "SELECT dh.MaDH, kh.TenKH, kh.DienThoai, kh.DiaChi " +
                     "FROM DonHang dh " +
                     "JOIN KhachHang kh ON dh.MaKH = kh.MaKH";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new DonHangViewDTO(
                        rs.getString("MaDH"),
                        rs.getString("TenKH"),
                        rs.getString("DienThoai"),
                        rs.getString("DiaChi")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách đơn hàng", e);
        }
        return list;
    }

    // Tìm kiếm đơn hàng theo mã hoặc tên khách hàng
    public List<DonHangViewDTO> searchDonHang(String keyword) {
        List<DonHangViewDTO> list = new ArrayList<>();
        String sql = "SELECT dh.MaDH, kh.TenKH, kh.DienThoai, kh.DiaChi " +
                     "FROM DonHang dh " +
                     "JOIN KhachHang kh ON dh.MaKH = kh.MaKH " +
                     "WHERE dh.MaDH LIKE ? OR kh.TenKH LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new DonHangViewDTO(
                            rs.getString("MaDH"),
                            rs.getString("TenKH"),
                            rs.getString("DienThoai"),
                            rs.getString("DiaChi")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi tìm kiếm đơn hàng", e);
        }
        return list;
    }

    // Thêm đơn hàng
    public boolean insert(DonHang dh) {
        String sql = "INSERT INTO DonHang(MaDH, MaKH, MaNV, NgayLap, TongTien, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dh.getMaDH());
            ps.setString(2, dh.getMaKH());
            ps.setString(3, dh.getMaNV());
            ps.setDate(4, new java.sql.Date(dh.getNgayLap().getTime()));
            ps.setBigDecimal(5, dh.getTongTien());
            ps.setString(6, dh.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm đơn hàng", e);
        }
    }

    // Cập nhật đơn hàng
    public boolean update(DonHang dh) {
        String sql = "UPDATE DonHang SET MaKH=?, MaNV=?, NgayLap=?, TongTien=?, TrangThai=? WHERE MaDH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dh.getMaKH());
            ps.setString(2, dh.getMaNV());
            ps.setDate(3, new java.sql.Date(dh.getNgayLap().getTime()));
            ps.setBigDecimal(4, dh.getTongTien());
            ps.setString(5, dh.getTrangThai());
            ps.setString(6, dh.getMaDH());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật đơn hàng", e);
        }
    }

    // Xóa đơn hàng (và chi tiết đơn hàng liên quan)
    public boolean delete(String maDH) {
        String sqlDeleteCT = "DELETE FROM ChiTietDonHang WHERE MaDH=?";
        String sqlDeleteDH = "DELETE FROM DonHang WHERE MaDH=?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // bắt đầu transaction

            try (PreparedStatement ps1 = conn.prepareStatement(sqlDeleteCT)) {
                ps1.setString(1, maDH);
                ps1.executeUpdate();
            }

            int rows;
            try (PreparedStatement ps2 = conn.prepareStatement(sqlDeleteDH)) {
                ps2.setString(1, maDH);
                rows = ps2.executeUpdate();
            }

            conn.commit(); // xác nhận transaction
            return rows > 0;
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa đơn hàng: " + maDH, e);
        }
    }
    public DonHang findById(String maDH) {
        String sql = "SELECT * FROM DonHang WHERE MaDH = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DonHang(
                            rs.getString("MaDH"),
                            rs.getString("MaKH"),
                            rs.getString("MaNV"),
                            rs.getDate("NgayLap"),
                            rs.getBigDecimal("TongTien"),
                            rs.getString("TrangThai")
                    );
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi tìm đơn hàng theo mã: " + maDH, e);
        }
        return null;
    }

    // Kiểm tra đơn hàng tồn tại
    public boolean exists(String maDH) {
        String sql = "SELECT 1 FROM DonHang WHERE MaDH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maDH);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi kiểm tra tồn tại đơn hàng", e);
        }
    }
}
