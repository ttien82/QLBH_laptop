package com.qlbh.qlbhlaptop.view;


import com.qlbh.qlbhlaptop.controller.DonHangController;
import com.qlbh.qlbhlaptop.controller.KhachHangController;
import com.qlbh.qlbhlaptop.controller.LoaiSPController;
import com.qlbh.qlbhlaptop.controller.NhaCungCapController;
import com.qlbh.qlbhlaptop.controller.NhanVienController;
import com.qlbh.qlbhlaptop.controller.PhieuNhapController;
import com.qlbh.qlbhlaptop.controller.SanPhamController;
import com.qlbh.qlbhlaptop.controller.TaiKhoanController;
import com.qlbh.qlbhlaptop.controller.ChiTietDonHangController;       
import com.qlbh.qlbhlaptop.model.TaiKhoan;          
import com.qlbh.qlbhlaptop.Ho_Tro.PhienDangNhap;
import com.qlbh.qlbhlaptop.Ho_Tro.VaiTro;
import com.qlbh.qlbhlaptop.model.TaiKhoan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MainFrame extends JFrame {

    private JTabbedPane tabbedPane;
    private JLabel lblUser, lblRole, lblTime;
    private TaiKhoan loggedInUser;
    private JMenu menuManager;
    
    // Ánh xạ tên menu với tên lớp và Consumer để khởi tạo Controller

    // Ánh xạ tên menu với tên lớp và Consumer để khởi tạo Controller
    private static final Map<String, PanelInfo> managedPanels = new LinkedHashMap<>();

    // Lớp nội bộ để chứa thông tin Panel và Controller
    private static class PanelInfo {
        String panelClassName;
        Consumer<JPanel> controllerInitializer;

        PanelInfo(String panelClassName, Consumer<JPanel> controllerInitializer) {
            this.panelClassName = panelClassName;
            this.controllerInitializer = controllerInitializer;
        }
    }

    static {

        // Ánh xạ các panel và controller tương ứng

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
    }

    public MainFrame() {
        this.loggedInUser = PhienDangNhap.getPhien().getngDung();
        if (this.loggedInUser == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng đăng nhập lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(0); 
            return;
        }
        setTitle("Quản lý bán hàng Laptop");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== MENU BAR =====
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menu Hệ thống
        JMenu menuSystem = new JMenu("Hệ thống");
        menuSystem.setMnemonic(KeyEvent.VK_H);
        JMenuItem menuItemLogout = new JMenuItem("Đăng xuất");
        JMenuItem menuItemExit = new JMenuItem("Thoát");

        menuSystem.add(menuItemLogout);
        menuSystem.addSeparator();
        menuSystem.add(menuItemExit);

        // Menu Quản lý
        menuManager = new JMenu("Quản lý");
        menuManager.setMnemonic(KeyEvent.VK_Q);
        
        // Tạo và thêm menu item một cách tự động từ HashMap
        for (String title : managedPanels.keySet()) {
            JMenuItem item = new JMenuItem(title);
            item.addActionListener(e -> openManagedPanel(title));
            menuManager.add(item);
        }

        // Menu Trợ giúp
        JMenu menuHelp = new JMenu("Trợ giúp");
        menuHelp.setMnemonic(KeyEvent.VK_T);
        JMenuItem menuItemAbout = new JMenuItem("Giới thiệu");

        menuHelp.add(menuItemAbout);

        menuBar.add(menuSystem);
        menuBar.add(menuManager);
        menuBar.add(menuHelp);

        applyAuthorization(); 
        // ===== MAIN CONTENT =====
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(tabbedPane, BorderLayout.CENTER);

        // ===== STATUS BAR =====

        JPanel pnlStatus = new JPanel(new BorderLayout());
        pnlStatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Thêm padding

        lblUser = new JLabel("Người dùng: " + loggedInUser.getTenDangNhap());
        lblRole = new JLabel("Quyền: " + loggedInUser.getMaQuyen());
        lblTime = new JLabel();
        
        Timer timer = new Timer(1000, e -> {
        lblTime.setText(new java.text.SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new java.util.Date()));
        });
        timer.start();

        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Tạo panel để chứa vai trò và căn phải

        JPanel rightStatus = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightStatus.add(lblUser);
        rightStatus.add(new JLabel(" | "));
        rightStatus.add(lblRole);
        rightStatus.add(new JLabel(" | "));
        rightStatus.add(lblTime);

        pnlStatus.add(rightStatus, BorderLayout.EAST);
        add(pnlStatus, BorderLayout.SOUTH);

        // ===== XỬ LÝ SỰ KIỆN =====
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
                PhienDangNhap.getPhien().logout();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true)); // mở lại login
            }
        });

        menuItemAbout.addActionListener(e -> JOptionPane.showMessageDialog(MainFrame.this,
                "<html><p><b>Phần mềm Quản lý Bán hàng Laptop</b></p><p>Nhóm: 13</p>"
                        + "<p><b>Trần Nguyễn Như Ngọc: 24410204 </b></p><p>Bùi Anh Quốc: 24410218</p>"
                        + "<p><b>Võ Văn Quý: 24410219 </b></p><p>Trần Tiến: 24410239</p>"
                        + "</b></p><p>Nguyễn Hoàng Tài: 24410221</p></html>",
                "Giới thiệu", JOptionPane.INFORMATION_MESSAGE));
    }
    
    public void applyAuthorization(){
        String vaitro = loggedInUser.getMaQuyen();
        
        // Vô hiệu hóa
        for (int i = 0; i < menuManager.getItemCount(); i++) {
            JMenuItem item = menuManager.getItem(i);
            if (item == null) continue; 
            
            String title = item.getText();
            boolean isVisible = true; // Mặc định là hiển thị
            
            // Dành cho Admin
            if (title.equals("Tài khoản")) {
                isVisible = vaitro.equals(VaiTro.ADMIN);
            }
            // Dành cho Admin và manager
            else if (title.equals("Nhân viên") || title.equals("Phiếu nhập") || title.equals("Nhà cung cấp")) {
                isVisible = vaitro.equals(VaiTro.ADMIN) || vaitro.equals(VaiTro.MANAGER);
            }
            // Chức năng chung
            item.setVisible(isVisible);
        }
        getJMenuBar().revalidate();
        getJMenuBar().repaint();
    }

    // Phương thức chung để mở panel
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

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));

    }
}
