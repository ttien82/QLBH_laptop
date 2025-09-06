package com.qlbh.qlbhlaptop.model;

import java.math.BigDecimal;

public class ChiTietPhieuNhap {
    private String maPN;
    private String maSP;
    private int soLuong;
    private BigDecimal giaNhap;
    
    public ChiTietPhieuNhap(){
        
    }
    
    public ChiTietPhieuNhap(String maPN, String maSP, int soLuong, BigDecimal giaNhap) {
        this.maPN = maPN;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
    }

    public String getMaPN() {
        return maPN;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
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

    public BigDecimal getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(BigDecimal giaNhap) {
        this.giaNhap = giaNhap;
    }
    
    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" +
                "maPN='" + maPN + '\'' +
                ", maSP='" + maSP + '\'' +
                ", soLuong=" + soLuong +
                ", giaNhap=" + giaNhap +
                '}';
    }
}
