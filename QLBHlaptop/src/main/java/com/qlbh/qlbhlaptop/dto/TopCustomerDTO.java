
package com.qlbh.qlbhlaptop.dto;


public class TopCustomerDTO {
    private final String maKH;
    private final String tenKH;
    private final int soDon;
    private final double tongDoanhThu;

    public TopCustomerDTO(String maKH, String tenKH, int soDon, double tongDoanhThu) {
        this.maKH = maKH; this.tenKH = tenKH;
        this.soDon = soDon; this.tongDoanhThu = tongDoanhThu;
    }
    public String getMaKH(){ return maKH; }
    public String getTenKH(){ return tenKH; }
    public int getSoDon(){ return soDon; }
    public double getTongDoanhThu(){ return tongDoanhThu; }
}