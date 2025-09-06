package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.Quyen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete)
 * để quản lý thông tin quyền (Quyen) trong cơ sở dữ liệu.
 * Sử dụng PreparedStatement để bảo mật và tối ưu hiệu suất.
 */
public class QuyenDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng Quyen.
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng Quyen đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
     */
    private Quyen mapResultSet(ResultSet rs) throws SQLException {
        return new Quyen(
                rs.getString("MaQuyen"),
                rs.getString("TenQuyen")
        );
    }

    /**
     * Lấy tất cả quyền có trong cơ sở dữ liệu.
     * @return Một danh sách (List) các đối tượng Quyen.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<Quyen> getAll() {
        List<Quyen> list = new ArrayList<>();
        String sql = "SELECT * FROM Quyen";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách quyền", e);
        }
        return list;
    }

    /**
     * Lấy một quyền cụ thể dựa trên mã quyền.
     * @param maQuyen Mã quyền.
     * @return Đối tượng Quyen nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public Quyen getById(String maQuyen) {
        String sql = "SELECT * FROM Quyen WHERE MaQuyen=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maQuyen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy quyền theo mã: " + maQuyen, e);
        }
        return null;
    }

    /**
     * Thêm một quyền mới vào cơ sở dữ liệu.
     * @param q Đối tượng Quyen cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(Quyen q) {
        String sql = "INSERT INTO Quyen(MaQuyen, TenQuyen) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, q.getMaQuyen());
            ps.setString(2, q.getTenQuyen());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi thêm quyền", e);
        }
    }

    /**
     * Cập nhật thông tin của một quyền đã tồn tại.
     * @param q Đối tượng Quyen chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(Quyen q) {
        String sql = "UPDATE Quyen SET TenQuyen=? WHERE MaQuyen=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, q.getTenQuyen());
            ps.setString(2, q.getMaQuyen());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật quyền", e);
        }
    }

    /**
     * Xóa một quyền khỏi cơ sở dữ liệu dựa trên mã quyền.
     * @param maQuyen Mã quyền cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maQuyen) {
        String sql = "DELETE FROM Quyen WHERE MaQuyen=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maQuyen);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa quyền", e);
        }
    }

    /**
     * Phương thức main để kiểm tra chức năng của lớp QuyenDAO.
     */
    public static void main(String[] args) {
        QuyenDAO dao = new QuyenDAO();
        Quyen qMoi = new Quyen("TEST", "Quyền test");

        System.out.println("--- DANH SÁCH QUYỀN HIỆN CÓ ---");
        dao.getAll().forEach(System.out::println);

        System.out.println("\n--- THÊM QUYỀN MỚI ---");
        System.out.println("Thêm: " + qMoi);
        boolean themOK = dao.insert(qMoi);
        System.out.println("Thêm thành công? " + themOK);

        if (themOK) {
            System.out.println("\n--- TÌM QUYỀN THEO MÃ ---");
            Quyen qTimThay = dao.getById("TEST");
            System.out.println("Tìm thấy: " + qTimThay);

            System.out.println("\n--- CẬP NHẬT QUYỀN ---");
            qMoi.setTenQuyen("Quyền test cập nhật");
            System.out.println("Cập nhật: " + qMoi);
            System.out.println("Cập nhật thành công? " + dao.update(qMoi));
        }

        System.out.println("\n--- XÓA QUYỀN ---");
        System.out.println("Đang xóa quyền có mã 'TEST'");
        System.out.println("Xóa thành công? " + dao.delete("TEST"));
    }
}
