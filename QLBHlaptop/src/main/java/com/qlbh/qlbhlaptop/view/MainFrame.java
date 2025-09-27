package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.controller.DonHangController;
import com.qlbh.qlbhlaptop.controller.KhachHangController;
import com.qlbh.qlbhlaptop.controller.LoaiSPController;
import com.qlbh.qlbhlaptop.controller.NhaCungCapController;
import com.qlbh.qlbhlaptop.controller.NhanVienController;
import com.qlbh.qlbhlaptop.controller.PhieuNhapController;
import com.qlbh.qlbhlaptop.controller.QuyenController;
import com.qlbh.qlbhlaptop.controller.SanPhamController;
import com.qlbh.qlbhlaptop.controller.TaiKhoanController;
import com.qlbh.qlbhlaptop.controller.ChiTietDonHangController;   // ✅ thêm
import com.qlbh.qlbhlaptop.view.ChiTietDonHangPanel;            // ✅ thêm
import com.qlbh.qlbhlaptop.model.TaiKhoan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private JLabel lblUser, lblRole;
    private TaiKhoan loggedInUser;

    private static final Map<String, PanelInfo> managedPanels = new LinkedHashMap<>();

    private static class PanelInfo {
        String panelClassName;
        Consumer<JPanel> controllerInitializer;

        PanelInfo(String panelClassName, Consumer<JPanel> controllerInitializer) {
            this.panelClassName = panelClassName;
            this.controllerInitializer = controllerInitializer;
        }
    }

    static {
        managedPanels.put("Chi tiết đơn hàng", new PanelInfo("com.qlbh.qlbhlaptop.view.ChiTietDonHangPanel",
        panel -> new ChiTietDonHangController((ChiTietDonHangPanel) panel)));

        managedPanels.put("Nhân viên", new PanelInfo("com.qlbh.qlbhlaptop.view.NhanVienPanel",
                panel -> new NhanVienController((NhanVienPanel) panel)));
        managedPanels.put("Khách hàng", new PanelInfo("com.qlbh.qlbhlaptop.view.KhachHangPanel",
                panel -> new KhachHangController((KhachHangPanel) panel)));
        managedPanels.put("Sản phẩm", new PanelInfo("com.qlbh.qlbhlaptop.view.SanPhamPanel",
                panel -> new SanPhamController((SanPhamPanel) panel)));
        managedPanels.put("Loại sản phẩm", new PanelInfo("com.qlbh.qlbhlaptop.view.LoaiSPPanel",
                panel -> new LoaiSPController((LoaiSPPanel) panel)));
        managedPanels.put("Nhà cung cấp", new PanelInfo("com.qlbh.qlbhlaptop.view.NhaCungCapPanel",
                panel -> new NhaCungCapController((NhaCungCapPanel) panel)));
        managedPanels.put("Đơn hàng", new PanelInfo("com.qlbh.qlbhlaptop.view.DonHangPanel",
                panel -> new DonHangController((DonHangPanel) panel)));

        // ✅ Thêm tab Chi tiết đơn hàng
        managedPanels.put("Chi tiết đơn hàng", new PanelInfo("com.qlbh.qlbhlaptop.view.ChiTietDonHangPanel",
                panel -> new ChiTietDonHangController((ChiTietDonHangPanel) panel)));

        managedPanels.put("Phiếu nhập", new PanelInfo("com.qlbh.qlbhlaptop.view.PhieuNhapPanel",
                panel -> new PhieuNhapController((PhieuNhapPanel) panel)));
        managedPanels.put("Tài khoản", new PanelInfo("com.qlbh.qlbhlaptop.view.TaiKhoanPanel",
                panel -> new TaiKhoanController((TaiKhoanPanel) panel)));
        managedPanels.put("Quyền", new PanelInfo("com.qlbh.qlbhlaptop.view.QuyenPanel",
                panel -> new QuyenController((QuyenPanel) panel)));
    }

    public MainFrame(TaiKhoan user) {
        this.loggedInUser = user;

        setTitle("Quản lý bán hàng Laptop");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuSystem = new JMenu("Hệ thống");
        menuSystem.setMnemonic(KeyEvent.VK_H);
        JMenuItem menuItemLogout = new JMenuItem("Đăng xuất");
        JMenuItem menuItemExit = new JMenuItem("Thoát");

        menuSystem.add(menuItemLogout);
        menuSystem.addSeparator();
        menuSystem.add(menuItemExit);

        JMenu menuManage = new JMenu("Quản lý");
        menuManage.setMnemonic(KeyEvent.VK_Q);

        for (String title : managedPanels.keySet()) {
            JMenuItem item = new JMenuItem(title);
            item.addActionListener(e -> openManagedPanel(title));
            menuManage.add(item);
        }

        JMenu menuHelp = new JMenu("Trợ giúp");
        menuHelp.setMnemonic(KeyEvent.VK_T);
        JMenuItem menuItemAbout = new JMenuItem("Giới thiệu");
        menuHelp.add(menuItemAbout);

        menuBar.add(menuSystem);
        menuBar.add(menuManage);
        menuBar.add(menuHelp);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(tabbedPane, BorderLayout.CENTER);

        JPanel pnlStatus = new JPanel(new BorderLayout());
        pnlStatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        lblUser = new JLabel("Người dùng: " + loggedInUser.getTenDangNhap());
        lblRole = new JLabel("Quyền: " + loggedInUser.getMaQuyen());

        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel rightStatus = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightStatus.add(lblUser);
        rightStatus.add(new JLabel(" | "));
        rightStatus.add(lblRole);

        pnlStatus.add(rightStatus, BorderLayout.EAST);
        add(pnlStatus, BorderLayout.SOUTH);

        menuItemExit.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(MainFrame.this,
                    "Bạn có chắc chắn muốn thoát khỏi ứng dụng?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        menuItemLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(MainFrame.this,
                    "Bạn có muốn đăng xuất không?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        menuItemAbout.addActionListener(e -> JOptionPane.showMessageDialog(MainFrame.this,
                "<html><p><b>Phần mềm Quản lý Bán hàng</b></p><p>Phiên bản 1.0</p><p>Nhóm: XYZ</p></html>",
                "Giới thiệu", JOptionPane.INFORMATION_MESSAGE));
    }

    private void openManagedPanel(String title) {
        int index = tabbedPane.indexOfTab(title);
        if (index != -1) {
            tabbedPane.setSelectedIndex(index);
            return;
        }

        try {
            PanelInfo info = managedPanels.get(title);
            JPanel panel;

            try {
                panel = (JPanel) Class.forName(info.panelClassName)
                        .getDeclaredConstructor(TaiKhoan.class)
                        .newInstance(loggedInUser);
            } catch (NoSuchMethodException e) {
                panel = (JPanel) Class.forName(info.panelClassName)
                        .getDeclaredConstructor()
                        .newInstance();
            }

            if (info.controllerInitializer != null) {
                info.controllerInitializer.accept(panel);
            }

            tabbedPane.addTab(title, panel);
            tabbedPane.setSelectedComponent(panel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể mở panel: " + title + "\nLỗi: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TaiKhoan testUser = new TaiKhoan();
        testUser.setTenDangNhap("admin");
        testUser.setMaQuyen("QL");

        SwingUtilities.invokeLater(() -> new MainFrame(testUser).setVisible(true));
    }
}
