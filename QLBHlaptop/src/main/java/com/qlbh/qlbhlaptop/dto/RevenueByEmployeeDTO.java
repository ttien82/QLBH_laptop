
package com.qlbh.qlbhlaptop.dto;

public class RevenueByEmployeeDTO {
    private final String maNV;
    private final String tenNV;
    private final double tongDoanhThu;   // SUM(SoLuong*DonGia)
    private final int soDonHang;         // COUNT DISTINCT đơn
    private final double avgOrderValue;  // tongDoanhThu / soDonHang

    public RevenueByEmployeeDTO(String maNV, String tenNV,
                                double tongDoanhThu, int soDonHang, double avgOrderValue) {
        this.maNV = maNV; this.tenNV = tenNV;
        this.tongDoanhThu = tongDoanhThu; this.soDonHang = soDonHang;
        this.avgOrderValue = avgOrderValue;
    }
    public String getMaNV(){return maNV;}
    public String getTenNV(){return tenNV;}
    public double getTongDoanhThu(){return tongDoanhThu;}
    public int getSoDonHang(){return soDonHang;}
    public double getAvgOrderValue(){return avgOrderValue;}
}