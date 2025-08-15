package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.SanPham;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import java.sql.SQLException;

// Bắt lỗi khi SanPHamDAO gặp lỗi 
class DAOException extends RuntimeException {
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class SanPhamDAO {
    
    //SELECT đến model SanPham
    private SanPham mapResultSetToSanPham(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham();
        sp.setMaSP(rs.getString("MaSP"));
        sp.setTenSP(rs.getString("TenSP"));
        sp.setMaNCC(rs.getString("MaNCC"));
        sp.setMaLoaiSP(rs.getString("MaLoaiSP"));
        sp.setCpu(rs.getString("CPU"));
        sp.setRam(rs.getString("Ram"));
        sp.setOCung(rs.getString("OCung"));
        sp.setCardManHinh(rs.getString("CardManHinh"));
        sp.setGiaBan(rs.getBigDecimal("GiaBan"));
        sp.setSoLuongTon(rs.getInt("SoLuongTon"));
        sp.setHinhAnh(rs.getString("HinhAnh"));
        return sp;
    }
    // lấy dữ liệu sản phẩm
    public List<SanPham> getAll() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";

        // try-with-resources: Tự đóng kết nối khi dùng xong
        try (Connection conn = DatabaseConnection.getConnection(); // Kết nối CSDL
             PreparedStatement ps = conn.prepareStatement(sql);     // Chuẩn bị câu lệnh SQL
             ResultSet rs = ps.executeQuery()) {                     // Thực thi SELECT

            while (rs.next()) { // Duyệt từng dòng kết quả
                list.add(mapResultSetToSanPham(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách sản phẩm", e);
        }
        return list;
    }
    // Tìm sản phẩm theo mã
    public SanPham getById(String maSP) {
        String sql = "SELECT * FROM SanPham WHERE MaSP = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSP); // Gán giá trị cho dấu ? (tránh SQL Injection)

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Nếu tìm thấy
                    return mapResultSetToSanPham(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy sản phẩm theo mã: " + maSP, e);
        }
        return null;
    }
    // Tìm sản phẩm theo tên 
    public List<SanPham> search(String keyword) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE TenSP LIKE ?"; // Tìm gần đúng theo tên

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%"); // Thêm % để tìm kiếm chứa từ khóa

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSanPham(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi tìm kiếm sản phẩm với từ khóa: " + keyword, e);
        }
        return list;
    }
    // Thêm sản phẩm 
    public boolean insert(SanPham sp) {
        String sql = "INSERT INTO SanPham(MaSP, TenSP, MaNCC, MaLoaiSP, CPU, Ram, OCung, CardManHinh, GiaBan, SoLuongTon, HinhAnh)"
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Gán giá trị cho từng dấu ?
            ps.setString(1, sp.getMaSP());
            ps.setString(2, sp.getTenSP());
            ps.setString(3, sp.getMaNCC());
            ps.setString(4, sp.getMaLoaiSP());
            ps.setString(5, sp.getCpu());
            ps.setString(6, sp.getRam());
            ps.setString(7, sp.getOCung());
            ps.setString(8, sp.getCardManHinh());
            ps.setBigDecimal(9, sp.getGiaBan()); 
            ps.setInt(10, sp.getSoLuongTon());
            ps.setString(11, sp.getHinhAnh());

            return ps.executeUpdate() > 0; // Trả về true nếu thêm thành công

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm sản phẩm", e);
        }
    }
    //Cập nhật sản phẩm 
    public boolean update(SanPham sp) {
        String sql = "UPDATE SanPham SET TenSP=?, MaNCC=?, MaLoaiSP=?, CPU=?, Ram=?, OCung=?, CardManHinh=?, GiaBan=?, SoLuongTon=?, HinhAnh=? "
                   + "WHERE MaSP=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSP());
            ps.setString(2, sp.getMaNCC());
            ps.setString(3, sp.getMaLoaiSP());
            ps.setString(4, sp.getCpu());
            ps.setString(5, sp.getRam());
            ps.setString(6, sp.getOCung());
            ps.setString(7, sp.getCardManHinh());
            ps.setBigDecimal(8, sp.getGiaBan());
            ps.setInt(9, sp.getSoLuongTon());
            ps.setString(10, sp.getHinhAnh());
            ps.setString(11, sp.getMaSP());

            return ps.executeUpdate() > 0; // Trả về true nếu cập nhật thành công

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật sản phẩm", e);
        }
    }
    //Xóa sản phẩm 
    public boolean delete(String maSP) {
        String sql = "DELETE FROM SanPham WHERE MaSP=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSP);
            return ps.executeUpdate() > 0; // Trả về true nếu xóa thành công

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa sản phẩm", e);
        }
    }
    //Kiểm tra 
    public static void main(String[] args) {
        SanPhamDAO dao = new SanPhamDAO();
        
        // /* và // */
        
        /*
        System.out.println("danh sách sản phẩm");
        List<SanPham> ds = dao.getAll();
        for (SanPham sp : ds) {
            System.out.println(sp);
        }
        */

        
        //System.out.println("\n THÊM SẢN PHẨM");
        SanPham spMoi = new SanPham(
                "SP006", "Laptop Test", "NC001", "LTGAMING",
                "Intel i5", "8GB", "256GB SSD", "GTX 1650",
                new BigDecimal("15000000"), 10, "laptop.jpg"
        );
        
        // kiểm tra 2 lệnh 1 lúc sẽ gặp lỗi SQL đóng
        
        /*
        boolean themOK = dao.insert(spMoi);
        System.out.println("Thêm thành công? " + themOK);
        
        */
        
        /*
        System.out.println("\n Tìm theo mã");
        SanPham spTim = dao.getById("SP006");
        System.out.println(spTim);
        */
        
        /*
        System.out.println("\n Tìm theo tên ");
        List<SanPham> ketQuaTim = dao.search("Laptop");
        for (SanPham sp : ketQuaTim) {
        System.out.println(sp);
        }
        */
        
        /*
        System.out.println("\n===== CẬP NHẬT =====");
        spMoi.setTenSP("Laptop Test Updated");
        spMoi.setGiaBan(new BigDecimal("15500000"));
        boolean capNhatOK = dao.update(spMoi);
        System.out.println("Cập nhật thành công? " + capNhatOK);
        */
        // /*
        System.out.println("\n Xóa");
        boolean xoaOK = dao.delete("SP006");
        System.out.println("Xóa thành công? " + xoaOK);
        // */
    }
}
