package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.model.TaiKhoan;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete)
 * để quản lý thông tin tài khoản trong cơ sở dữ liệu.
 * Sử dụng PreparedStatement để bảo mật và tối ưu hiệu suất.
 */
public class TaiKhoanDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng TaiKhoan.
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng TaiKhoan đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong ResultSet.
     */
    private TaiKhoan mapResultSet(ResultSet rs) throws SQLException {
        return new TaiKhoan(
                rs.getString("MaTK"),
                rs.getString("MaNV"),
                rs.getString("TenDangNhap"),
                rs.getString("MatKhau"),
                rs.getString("MaQuyen")
        );
    }

    /**
     * Lấy tất cả tài khoản có trong cơ sở dữ liệu.
     * @return Một danh sách (List) các đối tượng TaiKhoan.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM TaiKhoan";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy danh sách tài khoản", e);
        }
        return list;
    }

    /**
     * Lấy một tài khoản cụ thể dựa trên mã tài khoản.
     * @param maTK Mã tài khoản.
     * @return Đối tượng TaiKhoan nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public TaiKhoan getById(String maTK) {
        String sql = "SELECT * FROM TaiKhoan WHERE MaTK=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy tài khoản theo mã: " + maTK, e);
        }
        return null;
    }

    /**
     * Lấy một tài khoản cụ thể dựa trên tên đăng nhập.
     * @param username Tên đăng nhập.
     * @return Đối tượng TaiKhoan nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public TaiKhoan getByUsername(String username) {
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi lấy tài khoản theo username: " + username, e);
        }
        return null;
    }

    /**
     * Thêm một tài khoản mới vào cơ sở dữ liệu. Mật khẩu sẽ được mã hóa trước khi lưu.
     * @param tk Đối tượng TaiKhoan cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu, đặc biệt là lỗi trùng lặp tên đăng nhập.
     */
    public boolean insert(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan(MaTK, MaNV, TenDangNhap, MatKhau, MaQuyen) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getMaTK());
            ps.setString(2, tk.getMaNV());
            ps.setString(3, tk.getTenDangNhap());
            // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
            String hashedPassword = BCrypt.hashpw(tk.getMatKhau(), BCrypt.gensalt());
            ps.setString(4, hashedPassword);
            ps.setString(5, tk.getMaQuyen());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // Xử lý lỗi trùng lặp tên đăng nhập (Integrity Constraint Violation)
            if (e.getSQLState().equals("23000")) {
                throw new DAOException("Tên đăng nhập hoặc mã nhân viên đã tồn tại!", e);
            }
            throw new DAOException("Lỗi khi thêm tài khoản", e);
        }
    }

    /**
     * Cập nhật thông tin của một tài khoản đã tồn tại. Mật khẩu sẽ được mã hóa nếu có thay đổi.
     * @param tk Đối tượng TaiKhoan chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE TaiKhoan SET MaNV=?, TenDangNhap=?, MatKhau=?, MaQuyen=? WHERE MaTK=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getMaNV());
            ps.setString(2, tk.getTenDangNhap());
            
            // Kiểm tra và xử lý mật khẩu.
            // Nếu mật khẩu đã được mã hóa (bắt đầu bằng "$2a$"), không mã hóa lại.
            // Nếu là mật khẩu thô (raw password), thì mã hóa.
            String rawPassword = tk.getMatKhau();
            String hashedPassword = rawPassword.startsWith("$2a$") 
                ? rawPassword 
                : BCrypt.hashpw(rawPassword, BCrypt.gensalt());
            ps.setString(3, hashedPassword);
            
            ps.setString(4, tk.getMaQuyen());
            ps.setString(5, tk.getMaTK());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi cập nhật tài khoản", e);
        }
    }

    /**
     * Xóa một tài khoản khỏi cơ sở dữ liệu dựa trên mã tài khoản.
     * @param maTK Mã tài khoản cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maTK) {
        String sql = "DELETE FROM TaiKhoan WHERE MaTK=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maTK);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa tài khoản", e);
        }
    }

    /**
     * Xác thực mật khẩu của người dùng.
     * @param username Tên đăng nhập.
     * @param password Mật khẩu người dùng nhập vào.
     * @return true nếu mật khẩu chính xác, ngược lại trả về false.
     */
    public boolean checkPassword(String username, String password) {
        TaiKhoan tk = getByUsername(username);
        if (tk != null) {
            // So sánh mật khẩu người dùng nhập với mật khẩu đã được mã hóa trong DB
            return BCrypt.checkpw(password, tk.getMatKhau());
        }
        return false;
    }

    /**
     * Phương thức main để kiểm tra chức năng của lớp TaiKhoanDAO.
     */
    public static void main(String[] args) {
        TaiKhoanDAO dao = new TaiKhoanDAO();
        // Sử dụng dữ liệu thử nghiệm mới để tránh xung đột với dữ liệu mẫu trong DB
        String testMaTK = "TK002";
        String testMaNV = "NV002";
        String testUser = "test";
        String testPass = "password1234";

        try {
            // Kiểm tra xem tài khoản đã tồn tại chưa để tránh lỗi trùng lặp
            TaiKhoan existingAccount = dao.getByUsername(testUser);
            if (existingAccount == null) {
                System.out.println("--- THÊM TÀI KHOẢN MỚI ---");
                TaiKhoan tkMoi = new TaiKhoan(testMaTK, testMaNV, testUser, testPass, "MANAGER");
                boolean themOK = dao.insert(tkMoi);
                System.out.println("Thêm thành công? " + themOK);
            } else {
                System.out.println("Tài khoản '" + testUser + "' đã tồn tại, bỏ qua bước thêm.");
            }
        } catch (DAOException e) {
            System.out.println("Lỗi khi thêm tài khoản: " + e.getMessage());
        }

        TaiKhoan tkTimThay = dao.getByUsername(testUser);
        if (tkTimThay != null) {
            System.out.println("\n--- TÌM TÀI KHOẢN THEO USERNAME ---");
            System.out.println("Tìm thấy: " + tkTimThay);

            System.out.println("\n--- XÁC THỰC MẬT KHẨU ---");
            System.out.println("Mật khẩu chính xác? " + dao.checkPassword(testUser, testPass));
            System.out.println("Mật khẩu sai? " + dao.checkPassword(testUser, "sai_mat_khau"));

            System.out.println("\n--- CẬP NHẬT TÀI KHOẢN ---");
            tkTimThay.setMatKhau("newpass456");
            boolean capNhatOK = dao.update(tkTimThay);
            System.out.println("Cập nhật thành công? " + capNhatOK);
        } else {
            System.out.println("Không tìm thấy tài khoản để thực hiện các thao tác tiếp theo.");
        }

        System.out.println("\n--- XÓA TÀI KHOẢN ---");
        System.out.println("Đang xóa tài khoản có mã '" + testMaTK + "'");
        System.out.println("Xóa thành công? " + dao.delete(testMaTK));
    }
}
