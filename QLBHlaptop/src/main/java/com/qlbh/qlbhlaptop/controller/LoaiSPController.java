package com.qlbh.qlbhlaptop.controller;

import com.qlbh.qlbhlaptop.model.LoaiSP;
import com.qlbh.qlbhlaptop.view.LoaiSPPanel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ttien
 */
public class LoaiSPController {

    public LoaiSPController(LoaiSPPanel loaiSPPanel) {}
    public List<LoaiSP> importFromExcel(File inFile) throws Exception {
        List<LoaiSP> LoaiSP = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(inFile);
             Workbook wb = new XSSFWorkbook(fis)) {

            Sheet sheet = wb.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();

            // Bỏ qua header (row 0)
            for (int r = 1; r < rows; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                LoaiSP loai = new LoaiSP();
                loai.setMaLoaiSP(row.getCell(0).getStringCellValue());
                loai.setTenLoaiSP(row.getCell(1).getStringCellValue());
                LoaiSP.add(loai);
            }
        }
        return LoaiSP;
    }
    public void exportsToExcel(List<LoaiSP> LoaiSP, File outFile) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Loại sản phẩm");
        // header
        Row header = sheet.createRow(0);
        String[] cols = {"MaLoaiSP","TenLoaiSP"};
        for (int i=0;i<cols.length;i++) header.createCell(i).setCellValue(cols[i]);
        // data
        int r = 1;
        for (LoaiSP l : LoaiSP) {
            Row row = sheet.createRow(r++);
            row.createCell(0).setCellValue(l.getMaLoaiSP());
            row.createCell(1).setCellValue(l.getTenLoaiSP());

        }
        // autosize
        for (int i=0;i<cols.length;i++) sheet.autoSizeColumn(i);
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            wb.write(fos);
        }
        wb.close();
    }
    
}
