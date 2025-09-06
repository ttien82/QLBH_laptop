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
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng ChiTietDonHang đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
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
     * @param maDH Mã đơn hàng cần lấy chi tiết.
     * @return Một danh sách (List) các đối tượng ChiTietDonHang.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
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
     * @return Một danh sách (List) các đối tượng ChiTietDonHang. Trả về danh sách rỗng nếu không có dữ liệu nào.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
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
     * Thêm một chi tiết đơn hàng mới vào cơ sở dữ liệu.
     * @param ctdh Đối tượng ChiTietDonHang cần thêm.
     * @return true nếu chi tiết đơn hàng được thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
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
     * Cập nhật thông tin của một chi tiết đơn hàng đã tồn tại.
     * @param ctdh Đối tượng ChiTietDonHang chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
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
     * Xóa một chi tiết đơn hàng khỏi cơ sở dữ liệu dựa trên mã đơn hàng và mã sản phẩm.
     * @param maDH Mã đơn hàng.
     * @param maSP Mã sản phẩm.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
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
     * Phương thức main để kiểm tra chức năng của lớp ChiTietDonHangDAO.
     */
    public static void main(String[] args) {
        ChiTietDonHangDAO dao = new ChiTietDonHangDAO();
        
        System.out.println("--- IN TẤT CẢ CHI TIẾT ĐƠN HÀNG ---");
        List<ChiTietDonHang> allDetails = dao.getAll();
        for (ChiTietDonHang ctdh : allDetails) {
            System.out.println(ctdh);
        }

        System.out.println("\n----------------------------------------------------");
        System.out.println("--- IN CHI TIẾT CỦA MỘT SỐ ĐƠN HÀNG CỤ THỂ ---");
        System.out.println("----------------------------------------------------");
        // Tạo một danh sách các mã đơn hàng để in
        List<String> maDonHangs = List.of("DH001", "DH002", "DH003");

        for (String maDH : maDonHangs) {
            System.out.println("\n- Chi tiết đơn hàng: " + maDH + " -");
            List<ChiTietDonHang> ds = dao.getByDonHang(maDH);
            if (ds.isEmpty()) {
                System.out.println("Không tìm thấy chi tiết cho đơn hàng " + maDH);
            } else {
                for (ChiTietDonHang ctdh : ds) {
                    System.out.println(ctdh);
                }
            }
        }

        System.out.println("\n--- THÊM CHI TIẾT ĐƠN HÀNG ---");
        ChiTietDonHang ctdhMoi = new ChiTietDonHang("DH002", "SP004", 2, new BigDecimal("15000000"));
        boolean themOK = dao.insert(ctdhMoi);
        System.out.println("Thêm thành công? " + themOK);

        System.out.println("\n--- CẬP NHẬT CHI TIẾT ĐƠN HÀNG ---");
        ctdhMoi.setSoLuong(3);
        ctdhMoi.setDonGia(new BigDecimal("14000000"));
        boolean capNhatOK = dao.update(ctdhMoi);
        System.out.println("Cập nhật thành công? " + capNhatOK);

        System.out.println("\n--- XÓA CHI TIẾT ĐƠN HÀNG ---");
        boolean xoaOK = dao.delete("DH002", "SP004");
        System.out.println("Xóa thành công? " + xoaOK);
    }
}
