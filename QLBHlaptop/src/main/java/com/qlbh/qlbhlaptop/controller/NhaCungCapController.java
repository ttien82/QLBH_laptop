/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qlbh.qlbhlaptop.controller;

import com.qlbh.qlbhlaptop.model.NhaCungCap;
import com.qlbh.qlbhlaptop.model.SanPham;
import com.qlbh.qlbhlaptop.view.NhaCungCapPanel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ttien
 */
public class NhaCungCapController {

    public NhaCungCapController(NhaCungCapPanel nhaCungCapPanel) {
    }
    
    public List<NhaCungCap> importFromExcel(File inFile) throws Exception {
    List<NhaCungCap> NhaCC = new ArrayList<>();
    try (FileInputStream fis = new FileInputStream(inFile);
         Workbook wb = new XSSFWorkbook(fis)) {

        Sheet sheet = wb.getSheetAt(0);
        int rows = sheet.getPhysicalNumberOfRows();

        // Bỏ qua header (row 0)
        for (int r = 1; r < rows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) continue;

            NhaCungCap ncc = new NhaCungCap();
            ncc.setMaNCC(row.getCell(0).getStringCellValue());
            ncc.setTenNCC(row.getCell(1).getStringCellValue());
            ncc.setDienThoai(row.getCell(3).getStringCellValue());
            ncc.setDiaChi(row.getCell(2).getStringCellValue());
            NhaCC.add(ncc);
        }
    }
    return NhaCC;
}
    public void exportsToExcel(List<NhaCungCap> NhaCC, File outFile) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Nhà cung cấp");
        // header
        Row header = sheet.createRow(0);
        String[] cols = {"MaNCC","TenNCC","DienThoai","DiaChi"};
        for (int i=0;i<cols.length;i++) header.createCell(i).setCellValue(cols[i]);
        // data
        int r = 1;
        for (NhaCungCap n : NhaCC) {
            Row row = sheet.createRow(r++);
            row.createCell(0).setCellValue(n.getMaNCC());
            row.createCell(1).setCellValue(n.getTenNCC());
            row.createCell(3).setCellValue(n.getDienThoai());
            row.createCell(2).setCellValue(n.getDiaChi());

        }
        // autosize
        for (int i=0;i<cols.length;i++) sheet.autoSizeColumn(i);
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            wb.write(fos);
        }
        wb.close();
}
    
}

