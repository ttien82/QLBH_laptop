package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.ChiTietDonHang;
import com.qlbh.qlbhlaptop.dto.ChiTietDonHangViewDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDonHangDAO {

    // Ánh xạ dữ liệu cơ bản
    private ChiTietDonHang mapResultSetToCTDH(ResultSet rs) throws SQLException {
        ChiTietDonHang ctdh = new ChiTietDonHang();
        ctdh.setMaDH(rs.getString("MaDH"));
        ctdh.setMaSP(rs.getString("MaSP"));
        ctdh.setSoLuong(rs.getInt("SoLuong"));
        ctdh.setDonGia(rs.getBigDecimal("DonGia"));
        return ctdh;
    }

    // Lấy chi tiết theo mã đơn hàng
    public List<ChiTietDonHang> getByDonHang(String maDH) {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietDonHang WHERE MaDH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCTDH(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy chi tiết đơn hàng: " + maDH, e);
        }
        return list;
    }

    // Lấy tất cả chi tiết đơn hàng (chỉ đơn hàng hợp lệ)
    public List<ChiTietDonHang> getAll() {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = """
            SELECT c.MaDH, c.MaSP, c.SoLuong, c.DonGia
            FROM ChiTietDonHang c
            JOIN DonHang d ON c.MaDH = d.MaDH
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToCTDH(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy tất cả chi tiết đơn hàng", e);
        }
        return list;
    }

    // Thêm chi tiết đơn hàng
    public boolean insert(ChiTietDonHang ctdh) {
        String sql = "INSERT INTO ChiTietDonHang(MaDH, MaSP, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ctdh.getMaDH());
            ps.setString(2, ctdh.getMaSP());
            ps.setInt(3, ctdh.getSoLuong());
            ps.setBigDecimal(4, ctdh.getDonGia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm chi tiết đơn hàng", e);
        }
    }

    // Cập nhật chi tiết đơn hàng
    public boolean update(ChiTietDonHang ctdh) {
        String sql = "UPDATE ChiTietDonHang SET SoLuong=?, DonGia=? WHERE MaDH=? AND MaSP=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ctdh.getSoLuong());
            ps.setBigDecimal(2, ctdh.getDonGia());
            ps.setString(3, ctdh.getMaDH());
            ps.setString(4, ctdh.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật chi tiết đơn hàng", e);
        }
    }

    // Xóa chi tiết đơn hàng
    public boolean delete(String maDH, String maSP) {
        String sql = "DELETE FROM ChiTietDonHang WHERE MaDH=? AND MaSP=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
            ps.setString(2, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa chi tiết đơn hàng", e);
        }
    }

    // Kiểm tra tồn tại
    public boolean exists(String maDH, String maSP) {
        String sql = "SELECT 1 FROM ChiTietDonHang WHERE MaDH=? AND MaSP=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDH);
            ps.setString(2, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi kiểm tra tồn tại chi tiết đơn hàng", e);
        }
    }

    // ✅ Lấy tất cả chi tiết JOIN với DonHang (cho Panel hiển thị)
    public List<ChiTietDonHangViewDTO> getAllWithDonHang() {
        List<ChiTietDonHangViewDTO> list = new ArrayList<>();
        String sql = """
            SELECT d.MaDH, c.MaSP, c.SoLuong, c.DonGia
            FROM DonHang d
            JOIN ChiTietDonHang c ON d.MaDH = c.MaDH
            ORDER BY d.MaDH
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ChiTietDonHangViewDTO(
                        rs.getString("MaDH"),
                        rs.getString("MaSP"),
                        rs.getInt("SoLuong"),
                        rs.getBigDecimal("DonGia")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy chi tiết đơn hàng kèm đơn hàng", e);
        }
        return list;
    }

    // ✅ Tìm kiếm chi tiết theo Mã ĐH
    public List<ChiTietDonHangViewDTO> searchByMaDH(String maDH) {
        List<ChiTietDonHangViewDTO> list = new ArrayList<>();
        String sql = """
            SELECT d.MaDH, c.MaSP, c.SoLuong, c.DonGia
            FROM DonHang d
            JOIN ChiTietDonHang c ON d.MaDH = c.MaDH
            WHERE d.MaDH LIKE ?
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + maDH + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ChiTietDonHangViewDTO(
                            rs.getString("MaDH"),
                            rs.getString("MaSP"),
                            rs.getInt("SoLuong"),
                            rs.getBigDecimal("DonGia")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi tìm chi tiết đơn hàng theo Mã ĐH", e);
        }
        return list;
    }
}
