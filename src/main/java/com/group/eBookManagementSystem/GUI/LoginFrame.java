package com.group.eBookManagementSystem.GUI;
import javax.swing.*;
public class LoginFrame extends JFrame{

        private JTextField usernameField;
        private JPasswordField passwordField;
        private JButton loginButton;

        public LoginFrame() {
            initComponents();
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private void initComponents() {
            JPanel panel = new JPanel();
            JLabel usernameLabel = new JLabel("Username:");
            usernameField = new JTextField(20);
            JLabel passwordLabel = new JLabel("Password:");
            passwordField = new JPasswordField(20);
            loginButton = new JButton("Login");

            panel.add(usernameLabel);
            panel.add(usernameField);
            panel.add(passwordLabel);
            panel.add(passwordField);
            panel.add(loginButton);
            add(panel);
        }

        public JTextField getUsernameField() {
            return usernameField;
        }

        public JPasswordField getPasswordField() {
            return passwordField;
        }

        public JButton getLoginButton() {
            return loginButton;
        }

}
