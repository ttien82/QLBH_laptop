
package com.qlbh.qlbhlaptop.util;


import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public final class CsvExportUtil {
    private CsvExportUtil(){}

    public static void exportTableToCSV(JTable table, Component parent, String defaultFileName) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(defaultFileName));
        if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        File file = ensureCsvExtension(fc.getSelectedFile());
        if (file.exists()) {
            int ok = JOptionPane.showConfirmDialog(parent,
                    "File đã tồn tại. Ghi đè?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION);
            if (ok != JOptionPane.YES_OPTION) return;
        }

        try (OutputStream os = new FileOutputStream(file);
             // Ghi BOM để Excel mở UTF-8 không lỗi dấu
             BufferedOutputStream bos = new BufferedOutputStream(os)) {
            bos.write(new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF});
            try (Writer w = new OutputStreamWriter(bos, StandardCharsets.UTF_8);
                 PrintWriter pw = new PrintWriter(w)) {
                writeModelAsCsv(table.getModel(), pw);
            }
            JOptionPane.showMessageDialog(parent, "Xuất CSV thành công:\n" + file.getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Lỗi khi xuất CSV:\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static File ensureCsvExtension(File f) {
        String name = f.getName();
        if (!name.toLowerCase().endsWith(".csv")) {
            return new File(f.getParentFile(), name + ".csv");
        }
        return f;
    }

    private static void writeModelAsCsv(TableModel model, PrintWriter pw) {
        // header
        int cols = model.getColumnCount();
        for (int c = 0; c < cols; c++) {
            if (c > 0) pw.print(",");
            pw.print(escapeCsv(model.getColumnName(c)));
        }
        pw.println();

        // rows
        int rows = model.getRowCount();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (c > 0) pw.print(",");
                Object v = model.getValueAt(r, c);
                pw.print(escapeCsv(v == null ? "" : v.toString()));
            }
            pw.println();
        }
    }

    private static String escapeCsv(String s) {
        boolean needQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String t = s.replace("\"", "\"\"");
        return needQuotes ? "\"" + t + "\"" : t;
    }
}