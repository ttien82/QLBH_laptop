package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.PhieuNhap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete)
 * để quản lý thông tin phiếu nhập trong cơ sở dữ liệu.
 * Sử dụng PreparedStatement để bảo mật và tối ưu hiệu suất.
 */
public class PhieuNhapDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng PhieuNhap.
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng PhieuNhap đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
     */
    private PhieuNhap mapResultSet(ResultSet rs) throws SQLException {
        return new PhieuNhap(
                rs.getString("MaPN"),
                rs.getString("MaNCC"),
                rs.getString("MaNV"),
                rs.getTimestamp("NgayNhap"),
                rs.getBigDecimal("TongTien")
        );
    }

    /**
     * Lấy tất cả phiếu nhập có trong cơ sở dữ liệu.
     * @return Một danh sách (List) các đối tượng PhieuNhap.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<PhieuNhap> getAll() {
        List<PhieuNhap> list = new ArrayList<>();
        String sql = "SELECT * FROM PhieuNhap";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách phiếu nhập", e);
        }
        return list;
    }

    /**
     * Lấy một phiếu nhập cụ thể dựa trên mã phiếu nhập.
     * @param maPN Mã phiếu nhập.
     * @return Đối tượng PhieuNhap nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public PhieuNhap getById(String maPN) {
        String sql = "SELECT * FROM PhieuNhap WHERE MaPN=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy phiếu nhập theo mã: " + maPN, e);
        }
        return null;
    }

    /**
     * Thêm một phiếu nhập mới vào cơ sở dữ liệu.
     * @param pn Đối tượng PhieuNhap cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(PhieuNhap pn) {
        String sql = "INSERT INTO PhieuNhap(MaPN, MaNCC, MaNV, NgayNhap, TongTien) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pn.getMaPN());
            ps.setString(2, pn.getMaNCC());
            ps.setString(3, pn.getMaNV());
            // Sửa lỗi: Chuyển đổi java.util.Date sang java.sql.Timestamp
            ps.setTimestamp(4, new Timestamp(pn.getNgayNhap().getTime()));
            ps.setBigDecimal(5, pn.getTongTien());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm phiếu nhập", e);
        }
    }

    /**
     * Cập nhật thông tin của một phiếu nhập đã tồn tại.
     * @param pn Đối tượng PhieuNhap chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(PhieuNhap pn) {
        String sql = "UPDATE PhieuNhap SET MaNCC=?, MaNV=?, NgayNhap=?, TongTien=? WHERE MaPN=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pn.getMaNCC());
            ps.setString(2, pn.getMaNV());
            // Sửa lỗi: Chuyển đổi java.util.Date sang java.sql.Timestamp
            ps.setTimestamp(3, new Timestamp(pn.getNgayNhap().getTime()));
            ps.setBigDecimal(4, pn.getTongTien());
            ps.setString(5, pn.getMaPN());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật phiếu nhập", e);
        }
    }

    /**
     * Xóa một phiếu nhập khỏi cơ sở dữ liệu dựa trên mã phiếu nhập.
     * @param maPN Mã phiếu nhập cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maPN) {
        String sql = "DELETE FROM PhieuNhap WHERE MaPN=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maPN);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa phiếu nhập", e);
        }
    }

    /**
     * Phương thức main để kiểm tra chức năng của lớp PhieuNhapDAO.
     */
    public static void main(String[] args) {
        PhieuNhapDAO dao = new PhieuNhapDAO();
        PhieuNhap pnMoi = new PhieuNhap("PN999", "NC001", "NV001", new Timestamp(System.currentTimeMillis()), new BigDecimal("12345678"));

        System.out.println("--- DANH SÁCH PHIẾU NHẬP HIỆN CÓ ---");
        dao.getAll().forEach(System.out::println);

        System.out.println("\n--- THÊM PHIẾU NHẬP MỚI ---");
        System.out.println("Thêm: " + pnMoi);
        boolean themOK = dao.insert(pnMoi);
        System.out.println("Thêm thành công? " + themOK);

        if (themOK) {
            System.out.println("\n--- TÌM PHIẾU NHẬP THEO MÃ ---");
            PhieuNhap pnTimThay = dao.getById("PN999");
            System.out.println("Tìm thấy: " + pnTimThay);

            System.out.println("\n--- CẬP NHẬT PHIẾU NHẬP ---");
            pnMoi.setTongTien(new BigDecimal("87654321"));
            System.out.println("Cập nhật: " + pnMoi);
            System.out.println("Cập nhật thành công? " + dao.update(pnMoi));
        }

        System.out.println("\n--- XÓA PHIẾU NHẬP ---");
        System.out.println("Đang xóa phiếu nhập có mã 'PN999'");
        System.out.println("Xóa thành công? " + dao.delete("PN999"));
    }
}
