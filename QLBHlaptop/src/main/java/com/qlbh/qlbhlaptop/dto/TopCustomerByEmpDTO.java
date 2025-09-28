package com.qlbh.qlbhlaptop.dto;

public class TopCustomerByEmpDTO {
    private final String maKH;
    private final String tenKH;
    private final int soDon;
    private final double doanhThu;

    public TopCustomerByEmpDTO(String maKH, String tenKH, int soDon, double doanhThu) {
        this.maKH = maKH; this.tenKH = tenKH;
        this.soDon = soDon; this.doanhThu = doanhThu;
    }
    public String getMaKH(){ return maKH; }
    public String getTenKH(){ return tenKH; }
    public int getSoDon(){ return soDon; }
    public double getDoanhThu(){ return doanhThu; }
}