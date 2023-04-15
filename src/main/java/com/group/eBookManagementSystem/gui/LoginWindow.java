package com.group.eBookManagementSystem.gui;

import com.group.eBookManagementSystem.gui.utlils.HttpRequestUtils;
import java.awt.Color;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class LoginWindow {

    private static final Logger LOG = LoggerFactory.getLogger(LoginWindow.class);

    private final SingletonWindow singletonWindow;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JTextField alarmField;
    private final JButton registerButton;

    public LoginWindow() {
        singletonWindow = SingletonWindow.getInstance();
        singletonWindow.getContentPane().removeAll();
        singletonWindow.repaint();
        JPanel panel = new JPanel();
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Log in");
        loginButton.addActionListener(e -> handleLoginRequest());

        registerButton = new JButton("Sign up");
        registerButton.addActionListener(e -> SwingUtilities.invokeLater(RegisterWindow::new));

        alarmField = new JTextField(20);
        alarmField.setBackground(null);
        alarmField.setBorder(null);
        alarmField.setForeground(Color.red);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(alarmField);

        singletonWindow.add(panel);
        singletonWindow.setSize(300, 300);
        singletonWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        singletonWindow.setLocationRelativeTo(null);
        singletonWindow.setVisible(true);
    }

    private void handleLoginRequest() {
        try {
            String userName = usernameField.getText();
            String password = passwordField.getText();
            URL url = new URL(String.format("http://localhost:8080/login?userName=%s&password=%s", userName, password));
            String[] response = HttpRequestUtils.sendGetRequest(url);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleLoginRequest: %s", responseMessage));
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                SwingUtilities.invokeLater(() -> {
                    AccountWindow accountWindow = new AccountWindow(usernameField.getText());
                });
            } else {
                alarmField.setText(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleLoginRequest failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }

}
