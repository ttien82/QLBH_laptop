/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.config;

/**
 *
 * @author PC
 */
public class CheckExpression {
    //Check Email
    public boolean isValidGmail(String email) {
        // Regex kiểm tra Gmail (chỉ cho phép kết thúc bằng @gmail.com)
        String gmailRegex = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
        return email.matches(gmailRegex);
    }
    
    //Check SDT
    public Boolean checkPhone(String str) {
        // Bieu thuc chinh quy mo ta dinh dang so dien thoai
        String reg = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";

        // Kiem tra dinh dang
        boolean kt = str.matches(reg);

        if (kt == false) {
            System.out.println("Loi: Khong dung dinh dang!");
        } else {
            System.out.println("Dung dinh dang so dien thoai!");
        }
        return kt;
    }
}
