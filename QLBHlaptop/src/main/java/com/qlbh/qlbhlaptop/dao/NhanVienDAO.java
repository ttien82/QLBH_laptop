package com.qlbh.qlbhlaptop.dao;

import com.qlbh.qlbhlaptop.config.DatabaseConnection;
import com.qlbh.qlbhlaptop.dto.RevenueByEmployeeDTO;
import com.qlbh.qlbhlaptop.dto.TopCustomerByEmpDTO;
import com.qlbh.qlbhlaptop.model.NhanVien;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete) để quản lý thông
 * tin nhân viên trong cơ sở dữ liệu. Sử dụng PreparedStatement để bảo mật và
 * tối ưu hiệu suất.
 */
public class NhanVienDAO {

    /**
     * Ánh xạ (map) dữ liệu từ ResultSet vào đối tượng NhanVien.
     *
     * @param rs Đối tượng ResultSet chứa dữ liệu từ cơ sở dữ liệu.
     * @return Một đối tượng NhanVien đã được điền đầy đủ dữ liệu.
     * @throws SQLException Nếu có lỗi xảy ra khi truy cập dữ liệu trong
     * ResultSet.
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
     *
     * @return Một danh sách (List) các đối tượng NhanVien.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public List<NhanVien> getAll() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

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
     *
     * @param maNV Mã nhân viên.
     * @return Đối tượng NhanVien nếu tìm thấy, ngược lại trả về null.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn dữ liệu.
     */
    public NhanVien getById(String maNV) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
     *
     * @param nv Đối tượng NhanVien cần thêm.
     * @return true nếu thêm thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình thêm dữ liệu.
     */
    public boolean insert(NhanVien nv) {
        String sql = "INSERT INTO NhanVien VALUES (?, ?, ?, ?)";//(MaNV, TenNV, DiaChi, DienThoai)
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
     *
     * @param nv Đối tượng NhanVien chứa thông tin mới.
     * @return true nếu cập nhật thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình cập nhật dữ liệu.
     */
    public boolean update(NhanVien nv) {
        String sql = "UPDATE NhanVien SET TenNV=?, DiaChi=?, DienThoai=? WHERE MaNV=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

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
     *
     * @param maNV Mã nhân viên cần xóa.
     * @return true nếu xóa thành công, ngược lại trả về false.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình xóa dữ liệu.
     */
    public boolean delete(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Lỗi khi xóa nhân viên", e);
        }
    }

    /**
     * Lấy danh sách nhân viên có doanh thu cao nhất trong khoảng thời gian chỉ
     * định.
     *
     * Hàm này thực hiện truy vấn cơ sở dữ liệu để tính toán tổng doanh thu và
     * số đơn hàng mà mỗi nhân viên đã thực hiện trong khoảng từ {@code from}
     * đến {@code to}. Kết quả được sắp xếp theo tổng doanh thu giảm dần và giới
     * hạn bởi tham số {@code limit}. Nếu {@code limit} là null, mặc định sẽ lấy
     * tất cả nhân viên (với ngưỡng rất lớn)
     *
     *
     * Tổng doanh thu: được tính bằng tổng số lượng * đơn giá của tất cả chi
     * tiết đơn hàng Số đơn hàng: số lượng đơn hàng khác nhau mà nhân viên đã
     * phụ trách.</li>
     * Giá trị đơn trung bình: doanh thu chia cho số đơn hàng (0 nếu không có
     * đơn)
     *
     *
     * @param from Ngày bắt đầu (bao gồm). Nếu null, không giới hạn ngày bắt
     * đầu.
     * @param to Ngày kết thúc (bao gồm). Nếu null, không giới hạn ngày kết
     * thúc.
     * @param limit Số lượng nhân viên tối đa cần lấy (Top N). Nếu null, mặc
     * định lấy tất cả.
     * @return Danh sách {@link RevenueByEmployeeDTO} chứa thông tin doanh thu
     * theo nhân viên. Danh sách có thể ít hơn {@code limit} nếu số nhân viên
     * hợp lệ nhỏ hơn.
     * @throws DAOException Nếu có lỗi xảy ra trong quá trình truy vấn hoặc xử
     * lý dữ liệu.
     */
    public List<RevenueByEmployeeDTO> getTopRevenueEmployees(LocalDate from, LocalDate to, Integer limit)
            throws DAOException {
        List<RevenueByEmployeeDTO> list = new ArrayList<>();
        String sql = """
        SELECT TOP (ISNULL(?, 1000000))
               nv.MaNV, nv.TenNV,
               SUM(ct.SoLuong * ct.DonGia) AS TongDoanhThu,
               COUNT(DISTINCT dh.MaDH)     AS SoDonHang,
               SUM(ct.SoLuong * ct.DonGia) / NULLIF(COUNT(DISTINCT dh.MaDH),0) AS AvgOrderValue
        FROM NhanVien nv
        JOIN DonHang dh       ON nv.MaNV = dh.MaNV
        JOIN ChiTietDonHang ct ON dh.MaDH = ct.MaDH
        WHERE (? IS NULL OR dh.NgayLap >= ?)
          AND (? IS NULL OR dh.NgayLap <  DATEADD(day, 1, ?))  -- inclusive to-date
          AND (dh.TrangThai IS NULL OR dh.TrangThai <> 'Huy')  -- tùy bạn: lọc đơn hủy
        GROUP BY nv.MaNV, nv.TenNV
        ORDER BY TongDoanhThu DESC;
        """;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // limit
            if (limit == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, limit);
            }
            // date params (4 chỗ giống nhau)
            if (from == null) {
                ps.setNull(2, Types.DATE);
                ps.setNull(3, Types.DATE);
            } else {
                ps.setDate(2, java.sql.Date.valueOf(from));
                ps.setDate(3, java.sql.Date.valueOf(from));
            }
            if (to == null) {
                ps.setNull(4, Types.DATE);
                ps.setNull(5, Types.DATE);
            } else {
                ps.setDate(4, java.sql.Date.valueOf(to));
                ps.setDate(5, java.sql.Date.valueOf(to));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double rev = rs.getDouble("TongDoanhThu");
                    int orders = rs.getInt("SoDonHang");
                    list.add(new RevenueByEmployeeDTO(
                            rs.getString("MaNV"),
                            rs.getString("TenNV"),
                            rev,
                            orders,
                            orders == 0 ? 0.0 : rs.getDouble("AvgOrderValue")
                    ));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Lỗi KPI doanh thu nhân viên", ex);
        }
        return list;
    }

    // com.qlbh.qlbhlaptop.dao.NhanVienDAO
    public List<TopCustomerByEmpDTO> getTopCustomersByEmployee(
            String maNV, java.time.LocalDate fromInclusive, java.time.LocalDate toInclusive,
            Integer limit, boolean excludeCanceled
    ) throws DAOException {
        List<TopCustomerByEmpDTO> out = new ArrayList<>();
        String sql = """
        SELECT TOP (ISNULL(?, 1000000))
               kh.MaKH, kh.TenKH,
               COUNT(DISTINCT dh.MaDH)     AS SoDon,
               SUM(ct.SoLuong * ct.DonGia) AS DoanhThu
        FROM DonHang dh
        JOIN ChiTietDonHang ct ON dh.MaDH = ct.MaDH
        JOIN KhachHang kh       ON dh.MaKH = kh.MaKH
        WHERE dh.MaNV = ?
          AND dh.NgayLap >= ?
          AND dh.NgayLap <  DATEADD(day, 1, ?)
          """ + (excludeCanceled ? " AND (dh.TrangThai IS NULL OR dh.TrangThai <> 'Huy')" : "") + """
        GROUP BY kh.MaKH, kh.TenKH
        ORDER BY DoanhThu DESC, kh.MaKH ASC;
    """;
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = 1;
            if (limit == null) {
                ps.setNull(i++, java.sql.Types.INTEGER);
            } else {
                ps.setInt(i++, limit);
            }
            ps.setString(i++, maNV);
            ps.setDate(i++, java.sql.Date.valueOf(fromInclusive));
            ps.setDate(i++, java.sql.Date.valueOf(toInclusive));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new TopCustomerByEmpDTO(
                            rs.getString("MaKH"),
                            rs.getString("TenKH"),
                            rs.getInt("SoDon"),
                            rs.getDouble("DoanhThu")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Top khách hàng theo nhân viên", e);
        }
        return out;
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
