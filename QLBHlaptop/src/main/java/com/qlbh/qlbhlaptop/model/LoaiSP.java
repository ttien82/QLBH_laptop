package com.qlbh.qlbhlaptop.model;

public class LoaiSP {
    private String maLoaiSP;
    private String tenLoaiSP;
    
    public LoaiSP(){
    
    }
    
    public LoaiSP(String maLoaiSP, String tenLoaiSP){
        this.maLoaiSP = maLoaiSP;
        this.tenLoaiSP = tenLoaiSP;
    }
    
    public String getMaLoaiSP() {
        return maLoaiSP;
    }

    public void setMaLoaiSP(String maLoaiSP) {
        this.maLoaiSP = maLoaiSP;
    }

    public String getTenLoaiSP() {
        return tenLoaiSP;
    }

    public void setTenLoaiSP(String tenLoaiSP) {
        this.tenLoaiSP = tenLoaiSP;
    }
    
    @Override
    public String toString() {
        return "LoaiSP{" +
                "maLoaiSP='" + maLoaiSP + '\'' +
                ", tenLoai='" +tenLoaiSP + '\'' +
                '}';
    }
}
