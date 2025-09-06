package com.qlbh.qlbhlaptop.dao;

/**
 * Lớp ngoại lệ tùy chỉnh (custom exception) để xử lý các lỗi liên quan đến DAO.
 * Đây là một RuntimeException để tránh việc phải khai báo throws trong mỗi phương thức DAO.
 */

public class DAOException extends RuntimeException{
    
    /**
     * Khởi tạo một DAOException mới với thông báo lỗi và nguyên nhân gốc.
     *
     * @param message Thông báo mô tả lỗi.
     * @param cause Nguyên nhân gốc của lỗi (thường là một SQLException).
     */
    
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
