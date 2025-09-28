package com.qlbh.qlbhlaptop.view;

import com.qlbh.qlbhlaptop.dao.TaiKhoanDAO;
import com.qlbh.qlbhlaptop.model.TaiKhoan;
import com.qlbh.qlbhlaptop.Ho_Tro.PhienDangNhap;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;


public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField pwdPassword;
    private JLabel lblStatus;
    private JButton btnLogin, btnCancel;
    private JCheckBox chkGhinho;

    
    public LoginFrame() {
        setTitle("Đăng nhập hệ thống");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        loadLogin();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(20);
        panel.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        pwdPassword = new JPasswordField(20);
        panel.add(pwdPassword, gbc);
        
        // Ghi nhớ đăng nhập
        gbc.gridx = 1; gbc.gridy = 2;
        chkGhinho = new JCheckBox("Ghi nhớ đăng nhập");
        panel.add(chkGhinho, gbc);
        

        // Status
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        lblStatus = new JLabel(" ");
        lblStatus.setForeground(Color.RED);
        panel.add(lblStatus, gbc);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnLogin = new JButton("Đăng nhập");
        btnCancel = new JButton("Thoát");
        btnPanel.add(btnLogin);
        btnPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        add(panel);

        // Actions
        btnLogin.addActionListener(e -> doLogin());
        btnCancel.addActionListener(e -> System.exit(0));
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(pwdPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        lblStatus.setText("Đang kiểm tra...");

        // chạy ngầm bằng SwingWorker
        SwingWorker<TaiKhoan, Void> worker = new SwingWorker<>() {
            @Override
            protected TaiKhoan doInBackground() {
                TaiKhoanDAO dao = new TaiKhoanDAO();
                return dao.login(username, password); // login trả về TaiKhoan nếu đúng
            }

            @Override
            protected void done() {
                try {
                    TaiKhoan user = get();
                    if (user != null) {
                        PhienDangNhap.getPhien().login(user);
                        
                        if (chkGhinho.isSelected()) {
                            saveLogin(username, password);
                        } else {
                            clearLogin();
                        }

                        
                        lblStatus.setText("Đăng nhập thành công!");
                        SwingUtilities.invokeLater(() ->{
                            dispose();
                        new MainFrame().setVisible(true); 
                        });
                    } else {
                        
                        lblStatus.setText("Sai tên đăng nhập hoặc mật khẩu!");
                    }
                } catch (Exception e) {
                    lblStatus.setText("Lỗi hệ thống!");
                    e.printStackTrace();
                }
            }       
        };
        worker.execute();
    }
    
            private static final String LOGIN_FILE = "login.properties";
        
        private void saveLogin(String user, String pass) {
            try {
                Properties  props = new Properties();
                props.setProperty("username", user);
                props.setProperty("password", pass);
                FileOutputStream out = new FileOutputStream(LOGIN_FILE);
                props.store(out, "Login config");
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void loadLogin() {
            try {
                File file = new File(LOGIN_FILE);
                if (!file.exists()) return;

                Properties props = new Properties();
                FileInputStream in = new FileInputStream(file);
                props.load(in);
                in.close();

                txtUsername.setText(props.getProperty("username", ""));
                pwdPassword.setText(props.getProperty("password", ""));
                chkGhinho.setSelected(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void clearLogin() {
            File file = new File(LOGIN_FILE);
            if (file.exists()) file.delete();
        }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
