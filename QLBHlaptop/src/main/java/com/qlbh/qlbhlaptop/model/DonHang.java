package com.qlbh.qlbhlaptop.model;

import java.math.BigDecimal;
import java.util.Date;

public class DonHang {
    private String maDH;
    private String maKH;
    private String maNV;
    private Date ngayLap;
    private BigDecimal tongTien;
    private String trangThai;
    
    public DonHang(){
        
    }
    
    public DonHang(String maDH, String maKH, String maNV, Date ngayLap, BigDecimal tongTien, String trangThai) {
        this.maDH = maDH;
        this.maKH = maKH;
        this.maNV = maNV;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    @Override
    public String toString() {
        return "DonHang{" +
                "maDH='" + maDH + '\'' +
                ", maKH='" + maKH + '\'' +
                ", maNV='" + maNV + '\'' +
                ", ngayLap=" + ngayLap +
                ", tongTien=" + tongTien +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
