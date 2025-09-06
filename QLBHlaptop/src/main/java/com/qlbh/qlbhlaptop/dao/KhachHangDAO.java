package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.KhachHang;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD để quản lý thông tin khách hàng.
 * Sử dụng PreparedStatement để tránh SQL Injection và tối ưu hiệu suất.
 */
public class KhachHangDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng KhachHang.
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng KhachHang đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
     */
    private KhachHang mapResultSet(ResultSet rs) throws SQLException {
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString("MaKH"));
        kh.setTenKH(rs.getString("TenKH"));
        kh.setDienThoai(rs.getString("DienThoai"));
        kh.setEmail(rs.getString("Email"));
        kh.setDiaChi(rs.getString("DiaChi"));
        return kh;
    }

    /**
     * Lấy tất cả khách hàng có trong cơ sở dữ liệu.
     * @return Một danh sách (List) các đối tượng KhachHang.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách khách hàng", e);
        }
        return list;
    }

    /**
     * Lấy một khách hàng cụ thể dựa trên mã khách hàng.
     * @param maKH Mã khách hàng.
     * @return Đối tượng KhachHang nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public KhachHang getById(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE MaKH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy khách hàng theo mã: " + maKH, e);
        }
        return null;
    }

    /**
     * Thêm một khách hàng mới vào cơ sở dữ liệu.
     * @param kh Đối tượng KhachHang cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang(MaKH, TenKH, DienThoai, Email, DiaChi) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getDienThoai());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getDiaChi());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm khách hàng", e);
        }
    }

    /**
     * Cập nhật thông tin của một khách hàng đã tồn tại.
     * @param kh Đối tượng KhachHang chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET TenKH=?, DienThoai=?, Email=?, DiaChi=? WHERE MaKH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getDienThoai());
            ps.setString(3, kh.getEmail());
            ps.setString(4, kh.getDiaChi());
            ps.setString(5, kh.getMaKH());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật khách hàng", e);
        }
    }

    /**
     * Xóa một khách hàng khỏi cơ sở dữ liệu dựa trên mã khách hàng.
     * @param maKH Mã khách hàng cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE MaKH=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa khách hàng", e);
        }
    }

    /**
     * Phương thức main để kiểm tra chức năng của lớp KhachHangDAO.
     */
    public static void main(String[] args) {
        KhachHangDAO dao = new KhachHangDAO();

        System.out.println("--- DANH SÁCH KHÁCH HÀNG ---");
        dao.getAll().forEach(System.out::println);

        System.out.println("\n--- THÊM KHÁCH HÀNG ---");
        KhachHang khMoi = new KhachHang("KH010", "Nguyễn Văn Test", "0909999999", "test@gmail.com", "Hà Nội");
        System.out.println("Thêm thành công? " + dao.insert(khMoi));

        System.out.println("\n--- TÌM THEO MÃ ---");
        System.out.println(dao.getById("KH010"));

        System.out.println("\n--- CẬP NHẬT KHÁCH HÀNG ---");
        khMoi.setTenKH("Nguyễn Văn Updated");
        System.out.println("Cập nhật thành công? " + dao.update(khMoi));

        System.out.println("\n--- XÓA KHÁCH HÀNG ---");
        System.out.println("Xóa thành công? " + dao.delete("KH010"));
    }
}
