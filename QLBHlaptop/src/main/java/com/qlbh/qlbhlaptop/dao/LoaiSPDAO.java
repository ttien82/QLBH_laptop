package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.LoaiSP;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete)
 * để quản lý thông tin loại sản phẩm trong cơ sở dữ liệu.
 * Sử dụng PreparedStatement để bảo mật và tối ưu hiệu suất.
 */
public class LoaiSPDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng LoaiSP.
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng LoaiSP đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
     */
    private LoaiSP mapResultSet(ResultSet rs) throws SQLException {
        return new LoaiSP(
                rs.getString("MaLoaiSP"),
                rs.getString("TenLoaiSP")
        );
    }

    /**
     * Lấy tất cả loại sản phẩm có trong cơ sở dữ liệu.
     * @return Một danh sách (List) các đối tượng LoaiSP.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<LoaiSP> getAll() {
        List<LoaiSP> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiSP";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách loại sản phẩm", e);
        }
        return list;
    }

    /**
     * Lấy một loại sản phẩm cụ thể dựa trên mã loại sản phẩm.
     * @param maLoaiSP Mã loại sản phẩm.
     * @return Đối tượng LoaiSP nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public LoaiSP getById(String maLoaiSP) {
        String sql = "SELECT * FROM LoaiSP WHERE MaLoaiSP=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy loại sản phẩm theo mã: " + maLoaiSP, e);
        }
        return null;
    }

    /**
     * Thêm một loại sản phẩm mới vào cơ sở dữ liệu.
     * @param loai Đối tượng LoaiSP cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(LoaiSP loai) {
        String sql = "INSERT INTO LoaiSP(MaLoaiSP, TenLoaiSP) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, loai.getMaLoaiSP());
            ps.setString(2, loai.getTenLoaiSP());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm loại sản phẩm", e);
        }
    }

    /**
     * Cập nhật thông tin của một loại sản phẩm đã tồn tại.
     * @param loai Đối tượng LoaiSP chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(LoaiSP loai) {
        String sql = "UPDATE LoaiSP SET TenLoaiSP=? WHERE MaLoaiSP=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, loai.getTenLoaiSP());
            ps.setString(2, loai.getMaLoaiSP());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật loại sản phẩm", e);
        }
    }

    /**
     * Xóa một loại sản phẩm khỏi cơ sở dữ liệu dựa trên mã loại sản phẩm.
     * @param maLoaiSP Mã loại sản phẩm cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maLoaiSP) {
        String sql = "DELETE FROM LoaiSP WHERE MaLoaiSP=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maLoaiSP);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa loại sản phẩm", e);
        }
    }

    /**
     * Phương thức main để kiểm tra chức năng của lớp LoaiSPDAO.
     */
    public static void main(String[] args) {
        LoaiSPDAO dao = new LoaiSPDAO();
        LoaiSP loaiMoi = new LoaiSP("L999", "Phụ kiện test");

        System.out.println("--- DANH SÁCH LOẠI SẢN PHẨM HIỆN CÓ ---");
        dao.getAll().forEach(System.out::println);

        System.out.println("\n--- THÊM LOẠI SẢN PHẨM MỚI ---");
        System.out.println("Thêm: " + loaiMoi);
        boolean themOK = dao.insert(loaiMoi);
        System.out.println("Thêm thành công? " + themOK);

        if (themOK) {
            System.out.println("\n--- TÌM LOẠI SẢN PHẨM THEO MÃ ---");
            LoaiSP loaiTimThay = dao.getById("L999");
            System.out.println("Tìm thấy: " + loaiTimThay);

            System.out.println("\n--- CẬP NHẬT LOẠI SẢN PHẨM ---");
            loaiMoi.setTenLoaiSP("Phụ kiện updated");
            System.out.println("Cập nhật: " + loaiMoi);
            System.out.println("Cập nhật thành công? " + dao.update(loaiMoi));
        }

        System.out.println("\n--- XÓA LOẠI SẢN PHẨM ---");
        System.out.println("Đang xóa loại sản phẩm có mã 'L999'");
        System.out.println("Xóa thành công? " + dao.delete("L999"));
    }
}
