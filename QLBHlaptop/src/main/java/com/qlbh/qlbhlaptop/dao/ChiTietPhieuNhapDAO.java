package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.ChiTietPhieuNhap;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete)
 * để quản lý thông tin chi tiết phiếu nhập.
 * Sử dụng PreparedStatement để tránh SQL Injection và tối ưu hiệu suất.
 */
public class ChiTietPhieuNhapDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng ChiTietPhieuNhap.
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng ChiTietPhieuNhap đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
     */
    private ChiTietPhieuNhap mapResultSetToCTPN(ResultSet rs) throws SQLException {
        ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
        ctpn.setMaPN(rs.getString("MaPN"));
        ctpn.setMaSP(rs.getString("MaSP"));
        ctpn.setSoLuong(rs.getInt("SoLuong"));
        ctpn.setGiaNhap(rs.getBigDecimal("GiaNhap"));
        return ctpn;
    }

    /**
     * Lấy danh sách chi tiết của một phiếu nhập dựa trên mã phiếu nhập.
     * @param maPN Mã phiếu nhập cần lấy chi tiết.
     * @return Một danh sách (List) các đối tượng ChiTietPhieuNhap.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<ChiTietPhieuNhap> getByPhieuNhap(String maPN) {
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPN=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPN);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCTPN(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy chi tiết phiếu nhập: " + maPN, e);
        }
        return list;
    }
    
    /**
     * Lấy một chi tiết phiếu nhập cụ thể dựa trên mã phiếu nhập và mã sản phẩm.
     * @param maPN Mã phiếu nhập.
     * @param maSP Mã sản phẩm.
     * @return Đối tượng ChiTietPhieuNhap nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public ChiTietPhieuNhap getById(String maPN, String maSP) {
        String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPN=? AND MaSP=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPN);
            ps.setString(2, maSP);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCTPN(rs);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy chi tiết phiếu nhập theo ID: " + maPN + ", " + maSP, e);
        }
        return null;
    }

    /**
     * Lấy tất cả chi tiết phiếu nhập có trong cơ sở dữ liệu.
     * @return Một danh sách (List) các đối tượng ChiTietPhieuNhap. Trả về danh sách rỗng nếu không có dữ liệu nào.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<ChiTietPhieuNhap> getAll() {
        List<ChiTietPhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM ChiTietPhieuNhap";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToCTPN(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy tất cả chi tiết phiếu nhập", e);
        }
        return list;
    }

    /**
     * Thêm một chi tiết phiếu nhập mới vào cơ sở dữ liệu.
     * @param ctpn Đối tượng ChiTietPhieuNhap cần thêm.
     * @return true nếu chi tiết phiếu nhập được thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(ChiTietPhieuNhap ctpn) {
        String sql = "INSERT INTO ChiTietPhieuNhap(MaPN, MaSP, SoLuong, GiaNhap) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ctpn.getMaPN());
            ps.setString(2, ctpn.getMaSP());
            ps.setInt(3, ctpn.getSoLuong());
            ps.setBigDecimal(4, ctpn.getGiaNhap());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm chi tiết phiếu nhập", e);
        }
    }

    /**
     * Cập nhật thông tin của một chi tiết phiếu nhập đã tồn tại.
     * @param ctpn Đối tượng ChiTietPhieuNhap chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(ChiTietPhieuNhap ctpn) {
        String sql = "UPDATE ChiTietPhieuNhap SET SoLuong=?, GiaNhap=? WHERE MaPN=? AND MaSP=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ctpn.getSoLuong());
            ps.setBigDecimal(2, ctpn.getGiaNhap());
            ps.setString(3, ctpn.getMaPN());
            ps.setString(4, ctpn.getMaSP());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật chi tiết phiếu nhập", e);
        }
    }

    /**
     * Xóa một chi tiết phiếu nhập khỏi cơ sở dữ liệu dựa trên mã phiếu nhập và mã sản phẩm.
     * @param maPN Mã phiếu nhập.
     * @param maSP Mã sản phẩm.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maPN, String maSP) {
        String sql = "DELETE FROM ChiTietPhieuNhap WHERE MaPN=? AND MaSP=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPN);
            ps.setString(2, maSP);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa chi tiết phiếu nhập", e);
        }
    }

    /**
     * Phương thức main để kiểm tra chức năng của lớp ChiTietPhieuNhapDAO.
     */
    public static void main(String[] args) {
        ChiTietPhieuNhapDAO dao = new ChiTietPhieuNhapDAO();
        
        System.out.println("--- IN T?T C? CHI TI?T PHI?U NH?P ---");
        List<ChiTietPhieuNhap> allDetails = dao.getAll();
        for (ChiTietPhieuNhap ctpn : allDetails) {
            System.out.println(ctpn);
        }
        
        System.out.println("\n--- TÌM CHI TI?T THEO MÃ PHI?U NH?P C? TH? ---");
        List<String> maPhieuNhaps = List.of("PN001", "PN002", "PN003");
        for (String maPN : maPhieuNhaps) {
            System.out.println("\n- Chi ti?t phi?u nh?p: " + maPN + " -");
            List<ChiTietPhieuNhap> ds = dao.getByPhieuNhap(maPN);
            if (ds.isEmpty()) {
                System.out.println("Không tìm th?y chi ti?t cho phi?u nh?p " + maPN);
            } else {
                for (ChiTietPhieuNhap ctpn : ds) {
                    System.out.println(ctpn);
                }
            }
        }

        System.out.println("\n--- TÌM CHI TI?T THEO ID (maPN và maSP) ---");
        ChiTietPhieuNhap ctpnById = dao.getById("PN001", "SP001");
        if (ctpnById != null) {
            System.out.println("Tìm th?y: " + ctpnById);
        } else {
            System.out.println("Không tìm th?y chi ti?t phi?u nh?p v?i ID đã cho.");
        }

        // Dữ liệu dùng để kiểm tra
        ChiTietPhieuNhap ctpnMoi = new ChiTietPhieuNhap("PN002", "SP005", 2, new BigDecimal("12000000"));

        System.out.println("\n--- THÊM CHI TI?T PHI?U NH?P ---");
        boolean themOK = dao.insert(ctpnMoi);
        System.out.println("Thêm thành công? " + themOK);

        System.out.println("\n--- CẬP NHẬT CHI TI?T PHI?U NH?P ---");
        ctpnMoi.setSoLuong(3);
        ctpnMoi.setGiaNhap(new BigDecimal("11500000"));
        boolean capNhatOK = dao.update(ctpnMoi);
        System.out.println("Cập nhật thành công? " + capNhatOK);

        System.out.println("\n--- XÓA CHI TI?T PHI?U NH?P ---");
        boolean xoaOK = dao.delete("PN002", "SP005");
        System.out.println("Xóa thành công? " + xoaOK);
    }
}
