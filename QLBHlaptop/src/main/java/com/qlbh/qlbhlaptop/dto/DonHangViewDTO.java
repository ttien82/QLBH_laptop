/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.dto;

public class DonHangViewDTO {
    private String maDH;
    private String tenKH;
    private String dienThoai;
    private String diaChi;

    public DonHangViewDTO(String maDH, String tenKH, String dienThoai, String diaChi) {
        this.maDH = maDH;
        this.tenKH = tenKH;
        this.dienThoai = dienThoai;
        this.diaChi = diaChi;
    }

    public String getMaDH() { return maDH; }
    public String getTenKH() { return tenKH; }
    public String getDienThoai() { return dienThoai; }
    public String getDiaChi() { return diaChi; }
}

