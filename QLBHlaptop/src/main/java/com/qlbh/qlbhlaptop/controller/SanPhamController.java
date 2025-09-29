/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import com.qlbh.qlbhlaptop.view.SanPhamPanel;
import com.qlbh.qlbhlaptop.model.SanPham;
import com.qlbh.qlbhlaptop.dao.SanPhamDAO;
import java.util.*;
import java.io.*;
import java.math.*;

/**
 *
 * @author ttien
 */
public class SanPhamController {

    public SanPhamController(SanPhamPanel sanPhamPanel) {
        
    }
    private BigDecimal getBigDecimalFromCell(Cell cell) {
    if (cell == null) return BigDecimal.ZERO;

    switch (cell.getCellType()) {
        case NUMERIC:
            return BigDecimal.valueOf(cell.getNumericCellValue());
        case STRING:
            try {
                return new BigDecimal(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO; // hoặc throw exception tùy ý
            }
        default:
            return BigDecimal.ZERO;
    }
}

    private int getIntFromCell(Cell cell) {
        if (cell == null) return 0;

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    public List<SanPham> importFromExcel(File inFile) throws Exception {
    List<SanPham> products = new ArrayList<>();
    try (FileInputStream fis = new FileInputStream(inFile);
         Workbook wb = new XSSFWorkbook(fis)) {

        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();

        // Bỏ qua header (row 0)
        for (int r = 1; r < rows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            SanPham sp = new SanPham();
            sp.setMaSP(row.getCell(0).getStringCellValue());
            sp.setTenSP(row.getCell(1).getStringCellValue());
            sp.setMaNCC(row.getCell(2).getStringCellValue());
            sp.setMaLoaiSP(row.getCell(3).getStringCellValue());
            sp.setCpu(row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "");
            sp.setRam(row.getCell(5) != null ? row.getCell(5).getStringCellValue() : "");
            sp.setOCung(row.getCell(6) != null ? row.getCell(6).getStringCellValue() : "");
            sp.setCardManHinh(row.getCell(7) != null ? row.getCell(7).getStringCellValue() : "");
            sp.setGiaBan(getBigDecimalFromCell(row.getCell(8)));
            sp.setSoLuongTon(getIntFromCell(row.getCell(9)));
            products.add(sp);
        }
    }
    return products;
}

    
    public void exportsToExcel(List<SanPham> products, File outFile) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Products");
        // header
        Row header = sheet.createRow(0);
        String[] cols = {"MaSP","TenSP","NCC","LoaiSP","CPU","RAM","OCung","CardMH","GiaBan","SoLuong"};
        for (int i=0;i<cols.length;i++) header.createCell(i).setCellValue(cols[i]);
        // data
        int r = 1;
        for (SanPham s : products) {
            Row row = sheet.createRow(r++);
            row.createCell(0).setCellValue(s.getMaSP());
            row.createCell(1).setCellValue(s.getTenSP());
            row.createCell(2).setCellValue(s.getMaNCC() != null ? s.getMaNCC() : "");
            row.createCell(3).setCellValue(s.getMaLoaiSP() != null ? s.getMaLoaiSP() : "");
            row.createCell(4).setCellValue(s.getCpu() != null ? s.getCpu() : "");
            row.createCell(5).setCellValue(s.getRam() != null ? s.getRam() : "");
            row.createCell(6).setCellValue(s.getOCung() != null ? s.getOCung() : "");
            row.createCell(7).setCellValue(s.getCardManHinh() != null ? s.getCardManHinh() : "");
            row.createCell(8).setCellValue(s.getGiaBan() != null ? s.getGiaBan().doubleValue() : 0);
            row.createCell(9).setCellValue(s.getSoLuongTon());

        }
        // autosize
        for (int i=0;i<cols.length;i++) sheet.autoSizeColumn(i);
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            wb.write(fos);
        }
        wb.close();
}
    
}
