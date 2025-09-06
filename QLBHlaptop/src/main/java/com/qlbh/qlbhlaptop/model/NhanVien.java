package com.qlbh.qlbhlaptop.model;

public class NhanVien {
    private String maNV;
    private String tenNV;
    private String diaChi;
    private String dienThoai;
    
    public NhanVien(){
    
    }
    
    public NhanVien(String maNV, String tenNV, String diaChi, String dienThoai) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.diaChi = diaChi;
        this.dienThoai = dienThoai;
    }
    
    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }
    
    @Override
    public String toString() {
        return "NhanVien{" +
                "maNV='" + maNV + '\'' +
                ", tenNV='" + tenNV + '\'' +
                ", diaChi='" + diaChi + '\'' +
                ", dienThoai='" + dienThoai + '\'' +
                '}';
    }
}
