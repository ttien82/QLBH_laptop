
package com.qlbh.qlbhlaptop.dto;
import java.time.LocalDate;

public class OrderOfCustomerDTO {
    private final LocalDate ngayLap;
    private final String maDH;
    private final String maNV;
    private final String tenNV;
    private final double tongTien;

    public OrderOfCustomerDTO(LocalDate ngayLap, String maDH, String maNV, String tenNV, double tongTien) {
        this.ngayLap = ngayLap; this.maDH = maDH; this.maNV = maNV; this.tenNV = tenNV; this.tongTien = tongTien;
    }
    public LocalDate getNgayLap(){ return ngayLap; }
    public String getMaDH(){ return maDH; }
    public String getMaNV(){ return maNV; }
    public String getTenNV(){ return tenNV; }
    public double getTongTien(){ return tongTien; }
}
