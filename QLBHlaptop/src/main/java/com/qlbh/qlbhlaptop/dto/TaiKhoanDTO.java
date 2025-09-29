package com.qlbh.qlbhlaptop.dto;

public class TaiKhoanDTO {
    private String maTK;
    private String tenDangNhap;
    private String tenNV;     // từ bảng NhanVien
    private String tenQuyen;  // từ bảng Quyen
    
    public TaiKhoanDTO() {}

    public TaiKhoanDTO(String maTK, String tenDangNhap, String tenNV, String tenQuyen) {
        this.maTK = maTK;
        this.tenDangNhap = tenDangNhap;
        this.tenNV = tenNV;
        this.tenQuyen = tenQuyen;
    }

    public String getMaTK() { return maTK; }
    public void setMaTK(String maTK) { this.maTK = maTK; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getTenNV() { return tenNV; }
    public void setTenNV(String tenNV) { this.tenNV = tenNV; }

    public String getTenQuyen() { return tenQuyen; }
    public void setTenQuyen(String tenQuyen) { this.tenQuyen = tenQuyen; }

    @Override
    public String toString() {
        return "TaiKhoanDTO{" +
                "maTK='" + maTK + '\'' +
                ", tenDangNhap='" + tenDangNhap + '\'' +
                ", tenNV='" + tenNV + '\'' +
                ", tenQuyen='" + tenQuyen + '\'' +
                '}';
    }
}
