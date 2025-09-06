package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.NhanVien;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete)
 * để quản lý thông tin nhân viên trong cơ sở dữ liệu.
 * Sử dụng PreparedStatement để bảo mật và tối ưu hiệu suất.
 */
public class NhanVienDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng NhanVien.
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng NhanVien đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
     */
    private NhanVien mapResultSet(ResultSet rs) throws SQLException {
        return new NhanVien(
                rs.getString("MaNV"),
                rs.getString("TenNV"),
                rs.getString("DiaChi"),
                rs.getString("DienThoai")
        );
    }

    /**
     * Lấy tất cả nhân viên có trong cơ sở dữ liệu.
     * @return Một danh sách (List) các đối tượng NhanVien.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách nhân viên", e);
        }
        return list;
    }

    /**
     * Lấy một nhân viên cụ thể dựa trên mã nhân viên.
     * @param maNV Mã nhân viên.
     * @return Đối tượng NhanVien nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public NhanVien getById(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy nhân viên theo mã: " + maNV, e);
        }
        return null;
    }

    /**
     * Thêm một nhân viên mới vào cơ sở dữ liệu.
     * @param nv Đối tượng NhanVien cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien(MaNV, TenNV, DiaChi, DienThoai) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getDiaChi());
            ps.setString(4, nv.getDienThoai());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm nhân viên", e);
        }
    }

    /**
     * Cập nhật thông tin của một nhân viên đã tồn tại.
     * @param nv Đối tượng NhanVien chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET TenNV=?, DiaChi=?, DienThoai=? WHERE MaNV=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nv.getTenNV());
            ps.setString(2, nv.getDiaChi());
            ps.setString(3, nv.getDienThoai());
            ps.setString(4, nv.getMaNV());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật nhân viên", e);
        }
    }

    /**
     * Xóa một nhân viên khỏi cơ sở dữ liệu dựa trên mã nhân viên.
     * @param maNV Mã nhân viên cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa nhân viên", e);
        }
    }

    /**
     * Phương thức main để kiểm tra chức năng của lớp NhanVienDAO.
     */
    public static void main(String[] args) {
        NhanVienDAO dao = new NhanVienDAO();
        NhanVien nvMoi = new NhanVien("NV999", "Nguyễn Test", "HCM", "0909000111");

        System.out.println("--- DANH SÁCH NHÂN VIÊN HIỆN CÓ ---");
        dao.getAll().forEach(System.out::println);

        System.out.println("\n--- THÊM NHÂN VIÊN MỚI ---");
        System.out.println("Thêm: " + nvMoi);
        boolean themOK = dao.insert(nvMoi);
        System.out.println("Thêm thành công? " + themOK);

        if (themOK) {
            System.out.println("\n--- TÌM NHÂN VIÊN THEO MÃ ---");
            NhanVien nvTimThay = dao.getById("NV999");
            System.out.println("Tìm thấy: " + nvTimThay);

            System.out.println("\n--- CẬP NHẬT NHÂN VIÊN ---");
            nvMoi.setTenNV("Nguyễn Test Updated");
            System.out.println("Cập nhật: " + nvMoi);
            System.out.println("Cập nhật thành công? " + dao.update(nvMoi));
        }

        System.out.println("\n--- XÓA NHÂN VIÊN ---");
        System.out.println("Đang xóa nhân viên có mã 'NV999'");
        System.out.println("Xóa thành công? " + dao.delete("NV999"));
    }
}
