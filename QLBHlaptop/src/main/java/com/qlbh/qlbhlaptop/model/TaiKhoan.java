package com.qlbh.qlbhlaptop.model;

public class TaiKhoan {
    private String maTK;
    private String maNV;
    private String tenDangNhap;
    private String matKhau;
    private String maQuyen;
    
    public TaiKhoan(String maTK, String maNV, String tenDangNhap, String matKhau, String maQuyen) {
        this.maTK = maTK;
        this.maNV = maNV;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.maQuyen = maQuyen;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(String maQuyen) {
        this.maQuyen = maQuyen;
    }
    
}
