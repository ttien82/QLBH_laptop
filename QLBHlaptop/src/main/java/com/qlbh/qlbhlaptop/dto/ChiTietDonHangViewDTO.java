/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.dto;

import java.math.BigDecimal;

public class ChiTietDonHangViewDTO {
    private String maDH;
    private String maSP;
    private int soLuong;
    private BigDecimal donGia;

    public ChiTietDonHangViewDTO(String maDH, String maSP, int soLuong, BigDecimal donGia) {
        this.maDH = maDH;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getter
    public String getMaDH() { return maDH; }
    public String getMaSP() { return maSP; }
    public int getSoLuong() { return soLuong; }
    public BigDecimal getDonGia() { return donGia; }
}

