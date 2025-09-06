package com.qlbh.qlbhlaptop.model;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String dienThoai;
    private String email;
    private String diaChi;
    
    public KhachHang(){
    
    }
    
    public KhachHang(String maKH, String tenKH, String dienThoai, String email, String diaChi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.dienThoai = dienThoai;
        this.email = email;
        this.diaChi = diaChi;
    }
    
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    
    @Override
    public String toString() {
        return "KhachHang{" +
                "maKH='" + maKH + '\'' +
                ", tenKH='" + tenKH + '\'' +
                ", dienThoai='" + dienThoai + '\'' +
                ", email='" + email + '\'' +
                ", diaChi='" + diaChi + '\'' +
                '}';
    }
}
