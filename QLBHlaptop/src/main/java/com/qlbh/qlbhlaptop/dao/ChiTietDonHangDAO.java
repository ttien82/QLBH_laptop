package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.ChiTietDonHang;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete)
 * để quản lý thông tin chi tiết đơn hàng.
 * Sử dụng PreparedStatement để tránh SQL Injection và tối ưu hiệu suất.
 */
public class ChiTietDonHangDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng ChiTietDonHang.
     */
    private ChiTietDonHang mapResultSetToCTDH(ResultSet rs) throws SQLException {
        ChiTietDonHang ctdh = new ChiTietDonHang();
        ctdh.setMaDH(rs.getString("MaDH"));
        ctdh.setMaSP(rs.getString("MaSP"));
        ctdh.setSoLuong(rs.getInt("SoLuong"));
        ctdh.setDonGia(rs.getBigDecimal("DonGia"));
        return ctdh;
    }

    /**
     * Lấy danh sách chi tiết của một đơn hàng dựa trên mã đơn hàng.
     */
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

    /**
     * Lấy tất cả chi tiết đơn hàng có trong cơ sở dữ liệu.
     */
    public List<ChiTietDonHang> getAll() {
        List<ChiTietDonHang> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietDonHang";
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

    /**
     * Thêm một chi tiết đơn hàng mới.
     */
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

    /**
     * Cập nhật chi tiết đơn hàng.
     */
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

    /**
     * Xóa chi tiết đơn hàng.
     */
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

    /**
     * Kiểm tra chi tiết đơn hàng có tồn tại hay không.
     */
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

    /**
     * Main test.
     */
    public static void main(String[] args) {
        ChiTietDonHangDAO dao = new ChiTietDonHangDAO();

        System.out.println("--- IN TẤT CẢ CHI TIẾT ĐƠN HÀNG ---");
        for (ChiTietDonHang ctdh : dao.getAll()) {
            System.out.println(ctdh);
        }

        System.out.println("\n--- IN CHI TIẾT ĐƠN HÀNG DH001 ---");
        for (ChiTietDonHang ctdh : dao.getByDonHang("DH001")) {
            System.out.println(ctdh);
        }

        System.out.println("\n--- THÊM CHI TIẾT ---");
        ChiTietDonHang ct = new ChiTietDonHang("DH002", "SP004", 2, new BigDecimal("15000000"));
        System.out.println("Thêm thành công? " + dao.insert(ct));

        System.out.println("\n--- CẬP NHẬT CHI TIẾT ---");
        ct.setSoLuong(3);
        ct.setDonGia(new BigDecimal("14000000"));
        System.out.println("Cập nhật thành công? " + dao.update(ct));

        System.out.println("\n--- KIỂM TRA TỒN TẠI ---");
        System.out.println("Có tồn tại DH002-SP004? " + dao.exists("DH002", "SP004"));

        System.out.println("\n--- XÓA CHI TIẾT ---");
        System.out.println("Xóa thành công? " + dao.delete("DH002", "SP004"));
    }
}
