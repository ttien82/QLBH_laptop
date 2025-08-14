package com.qlbh.qlbhlaptop.model;

import java.math.BigDecimal;

public class ChiTietDonHang {
    private String maDH;
    private String maSP;
    private int soLuong;
    private BigDecimal donGia;
    
    public ChiTietDonHang(String maDH, String maSP, int soLuong, BigDecimal donGia) {
        this.maDH = maDH;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public String getMaDH() {
        return maDH;
    }

    public void setMaDH(String maDH) {
        this.maDH = maDH;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
}
