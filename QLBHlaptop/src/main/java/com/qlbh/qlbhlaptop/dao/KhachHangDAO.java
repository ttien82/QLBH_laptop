package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.dto.OrderOfCustomerDTO;
import com.qlbh.qlbhlaptop.dto.TopCustomerDTO;
import com.qlbh.qlbhlaptop.model.KhachHang;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD để quản lý thông tin khách hàng. Sử dụng
 * PreparedStatement để tránh SQL Injection và tối ưu hiệu suất.
 */
public class KhachHangDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng KhachHang.
     *
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng KhachHang đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong
     * ResultSet.
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
     *
     * @return Một danh sách (List) các đối tượng KhachHang.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

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
     *
     * @param maKH Mã khách hàng.
     * @return Đối tượng KhachHang nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public KhachHang getById(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE MaKH=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
     *
     * @param kh Đối tượng KhachHang cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO KhachHang VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
     *
     * @param kh Đối tượng KhachHang chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET TenKH=?, DienThoai=?, Email=?, DiaChi=? WHERE MaKH=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
     *
     * @param maKH Mã khách hàng cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE MaKH=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa khách hàng", e);
        }
    }

    /* Search by MaKH, TenKH, DienThoai, Email, DiaChi (LIKE %kw%)*/
    /**
     * Lấy danh sách các đơn hàng mà một khách hàng đã đặt.
     *
     * @param maKH Mã khách hàng cần truy vấn đơn hàng.
     * @return Danh sách {@link OrderOfCustomerDTO} chứa thông tin: - Ngày lập
     * đơn - Mã đơn hàng - Nhân viên lập (Mã + Tên) - Tổng tiền của đơn
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn cơ sở dữ
     * liệu.
     */
    public List<OrderOfCustomerDTO> getOrdersByCustomer(String maKH) throws DAOException {
        List<OrderOfCustomerDTO> out = new ArrayList<>();
        String sql = """
        SELECT dh.NgayLap,
               dh.MaDH,
               nv.MaNV,
               nv.TenNV,
               SUM(ct.SoLuong * ct.DonGia) AS TongTien
        FROM DonHang dh
        JOIN ChiTietDonHang ct ON dh.MaDH = ct.MaDH
        JOIN NhanVien nv       ON dh.MaNV = nv.MaNV
        WHERE dh.MaKH = ?
          AND (dh.TrangThai IS NULL OR dh.TrangThai <> 'Huy')  -- tùy: bỏ nếu muốn thấy cả đơn hủy
        GROUP BY dh.NgayLap, dh.MaDH, nv.MaNV, nv.TenNV
        ORDER BY dh.NgayLap DESC, dh.MaDH DESC
    """;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new OrderOfCustomerDTO(
                            rs.getDate("NgayLap").toLocalDate(),
                            rs.getString("MaDH"),
                            rs.getString("MaNV"),
                            rs.getString("TenNV"),
                            rs.getDouble("TongTien")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lấy danh sách đơn theo khách hàng " + maKH, e);
        }
        return out;
    }

    /**
     * Lấy danh sách Top khách hàng có doanh thu cao nhất (toàn hệ thống).
     *
     * @param fromInclusive Ngày bắt đầu (bao gồm).
     * @param toInclusive Ngày kết thúc (bao gồm).
     * @param limit Số lượng Top N KH cần lấy (nếu null thì lấy tất cả).
     * @param excludeCanceled Có loại bỏ đơn hủy hay không.
     * @return Danh sách {@link TopCustomerDTO}.
     * @throws DAOException Nếu có lỗi xảy ra khi truy vấn DB.
     */
    public List<TopCustomerDTO> getTopCustomers(
            LocalDate fromInclusive, LocalDate toInclusive,
            Integer limit, boolean excludeCanceled
    ) throws DAOException {
        List<TopCustomerDTO> out = new ArrayList<>();
        String sql = """
        SELECT TOP (ISNULL(?,1000000))
               kh.MaKH, kh.TenKH,
               COUNT(DISTINCT dh.MaDH)     AS SoDon,
               SUM(ct.SoLuong * ct.DonGia) AS TongDoanhThu
        FROM KhachHang kh
        JOIN DonHang dh       ON kh.MaKH = dh.MaKH
        JOIN ChiTietDonHang ct ON dh.MaDH = ct.MaDH
        WHERE dh.NgayLap >= ?
          AND dh.NgayLap <  DATEADD(day,1,?)
    """ + (excludeCanceled ? " AND (dh.TrangThai IS NULL OR dh.TrangThai <> 'Huy')" : "") + """ 
        GROUP BY kh.MaKH, kh.TenKH
        ORDER BY TongDoanhThu DESC, kh.MaKH ASC
    """;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = 1;
            if (limit == null) {
                ps.setNull(i++, Types.INTEGER);
            } else {
                ps.setInt(i++, limit);
            }
            ps.setDate(i++, java.sql.Date.valueOf(fromInclusive));
            ps.setDate(i++, java.sql.Date.valueOf(toInclusive));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new TopCustomerDTO(
                            rs.getString("MaKH"),
                            rs.getString("TenKH"),
                            rs.getInt("SoDon"),
                            rs.getDouble("TongDoanhThu")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Lấy Top khách hàng", e);
        }
        return out;
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
