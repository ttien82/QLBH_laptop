package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.NhaCungCap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete)
 * để quản lý thông tin nhà cung cấp trong cơ sở dữ liệu.
 * Sử dụng PreparedStatement để bảo mật và tối ưu hiệu suất.
 */
public class NhaCungCapDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng NhaCungCap.
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng NhaCungCap đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
     */
    private NhaCungCap mapResultSet(ResultSet rs) throws SQLException {
        return new NhaCungCap(
                rs.getString("MaNCC"),
                rs.getString("TenNCC"),
                rs.getString("DiaChi"),
                rs.getString("DienThoai")
        );
    }

    /**
     * Lấy tất cả nhà cung cấp có trong cơ sở dữ liệu.
     * @return Một danh sách (List) các đối tượng NhaCungCap.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<NhaCungCap> getAll() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách nhà cung cấp", e);
        }
        return list;
    }

    /**
     * Lấy một nhà cung cấp cụ thể dựa trên mã nhà cung cấp.
     * @param maNCC Mã nhà cung cấp.
     * @return Đối tượng NhaCungCap nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public NhaCungCap getById(String maNCC) {
        String sql = "SELECT * FROM NhaCungCap WHERE MaNCC=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy nhà cung cấp theo mã: " + maNCC, e);
        }
        return null;
    }

    /**
     * Thêm một nhà cung cấp mới vào cơ sở dữ liệu.
     * @param ncc Đối tượng NhaCungCap cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap(MaNCC, TenNCC, DiaChi, DienThoai) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getDienThoai());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm nhà cung cấp", e);
        }
    }

    /**
     * Cập nhật thông tin của một nhà cung cấp đã tồn tại.
     * @param ncc Đối tượng NhaCungCap chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET TenNCC=?, DiaChi=?, DienThoai=? WHERE MaNCC=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getDiaChi());
            ps.setString(3, ncc.getDienThoai());
            ps.setString(4, ncc.getMaNCC());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật nhà cung cấp", e);
        }
    }

    /**
     * Xóa một nhà cung cấp khỏi cơ sở dữ liệu dựa trên mã nhà cung cấp.
     * @param maNCC Mã nhà cung cấp cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maNCC) {
        String sql = "DELETE FROM NhaCungCap WHERE MaNCC=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa nhà cung cấp", e);
        }
    }

    /**
     * Phương thức main để kiểm tra chức năng của lớp NhaCungCapDAO.
     */
    public static void main(String[] args) {
        NhaCungCapDAO dao = new NhaCungCapDAO();
        NhaCungCap nccMoi = new NhaCungCap("NCC999", "Công ty Test", "HCM", "0911222333");

        System.out.println("--- DANH SÁCH NHÀ CUNG CẤP HIỆN CÓ ---");
        dao.getAll().forEach(System.out::println);

        System.out.println("\n--- THÊM NHÀ CUNG CẤP MỚI ---");
        System.out.println("Thêm: " + nccMoi);
        boolean themOK = dao.insert(nccMoi);
        System.out.println("Thêm thành công? " + themOK);

        if (themOK) {
            System.out.println("\n--- TÌM NHÀ CUNG CẤP THEO MÃ ---");
            NhaCungCap nccTimThay = dao.getById("NCC999");
            System.out.println("Tìm thấy: " + nccTimThay);

            System.out.println("\n--- CẬP NHẬT NHÀ CUNG CẤP ---");
            nccMoi.setTenNCC("Công ty Test Updated");
            System.out.println("Cập nhật: " + nccMoi);
            System.out.println("Cập nhật thành công? " + dao.update(nccMoi));
        }

        System.out.println("\n--- XÓA NHÀ CUNG CẤP ---");
        System.out.println("Đang xóa nhà cung cấp có mã 'NCC999'");
        System.out.println("Xóa thành công? " + dao.delete("NCC999"));
    }
}
