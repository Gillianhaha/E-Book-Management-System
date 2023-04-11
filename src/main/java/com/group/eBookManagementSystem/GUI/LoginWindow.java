package com.group.eBookManagementSystem.GUI;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        registerButton = new JButton("Sign up");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

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


    public void login() {
        String userName = usernameField.getText();
        String password = passwordField.getText();
        String ans = sendLoginRequest(userName, password);
        LOG.info(String.format("Response %s", ans));
        if (ans.equals("true")) {
            SwingUtilities.invokeLater(() -> {
                try {
                    AccountWindow accountWindow = new AccountWindow(usernameField.getText());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            alarmField.setText("The UserName and Password do not match!");
        }
    }

    private static String sendLoginRequest(String userName, String password) {
        try {
            URL url = new URL(String.format("http://localhost:8080/login?userName=%s&password=%s", userName, password));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public void register() {
        javax.swing.SwingUtilities.invokeLater(RegisterWindow::new);

    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(LoginWindow::new);
    }
}
