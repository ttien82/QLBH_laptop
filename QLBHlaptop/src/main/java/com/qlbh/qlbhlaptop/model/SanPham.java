package com.qlbh.qlbhlaptop.model;

public class SanPham {
    private String maSP;
    private String tenSP;
    private String maNCC;
    private String maLoaiSP;
    private String cpu;
    private String ram;
    private String oCung;
    private String cardManHinh;
    private double giaBan;
    private int soLuongTon;
    private String hinhAnh;
    
    public SanPham(String maSP, String tenSP, String maNCC, String maLoaiSP,
                   String cpu, String ram, String oCung, String cardManHinh,
                   double giaBan, int soLuongTon, String hinhAnh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.maNCC = maNCC;
        this.maLoaiSP = maLoaiSP;
        this.cpu = cpu;
        this.ram = ram;
        this.oCung = oCung;
        this.cardManHinh = cardManHinh;
        this.giaBan = giaBan;
        this.soLuongTon = soLuongTon;
        this.hinhAnh = hinhAnh;
    }
    
    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getMaLoaiSP() {
        return maLoaiSP;
    }

    public void setMaLoaiSP(String maLoaiSP) {
        this.maLoaiSP = maLoaiSP;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getOCung() {
        return oCung;
    }

    public void setOCung(String oCung) {
        this.oCung = oCung;
    }

    public String getCardManHinh() {
        return cardManHinh;
    }

    public void setCardManHinh(String cardManHinh) {
        this.cardManHinh = cardManHinh;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    
    @Override
    public String toString() {
        return "SanPham{" +
                "maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", maNCC='" + maNCC + '\'' +
                ", maLoaiSP='" + maLoaiSP + '\'' +
                ", cpu='" + cpu + '\'' +
                ", ram='" + ram + '\'' +
                ", oCung='" + oCung + '\'' +
                ", cardManHinh='" + cardManHinh + '\'' +
                ", giaBan=" + giaBan +
                ", soLuongTon=" + soLuongTon +
                ", hinhAnh='" + hinhAnh + '\'' +
                '}';
    }
}
