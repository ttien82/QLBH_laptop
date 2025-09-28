package com.qlbh.qlbhlaptop.Ho_Tro;

import com.qlbh.qlbhlaptop.model.TaiKhoan;
import com.qlbh.qlbhlaptop.Ho_Tro.VaiTro;

 /**
 * Quản lý phiên đăng nhập của người dùng hiện tại.
 */
public class PhienDangNhap {
    private static PhienDangNhap Phien;
    private TaiKhoan ngDung;
    
     private PhienDangNhap() {} 

    public static PhienDangNhap getPhien() {
        if (Phien == null) {
            Phien = new PhienDangNhap();
        }
        return Phien;
    }

    public void login(TaiKhoan tk) {
        this.ngDung = tk;
    }

    public void logout() {
        this.ngDung = null;
    }

    public TaiKhoan getngDung() {
        return ngDung;
    }

    public String getRole() {
        return ngDung != null ? ngDung.getMaQuyen() : "";
    }
    
    public boolean hasRole(String requiredRole) {
        if (ngDung == null) return false;
        String currentRole = getRole();

        if (currentRole.equals(requiredRole) || currentRole.equals(VaiTro.ADMIN)) {
            return true;
        }
        
        // MANAGER có quyền của STAFF
        if (currentRole.equals(VaiTro.MANAGER) && requiredRole.equals(VaiTro.STAFF)) {
            return true;
        }
        
        return false;
    }
}
