package com.group.eBookManagementSystem.GUI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.group.eBookManagementSystem.service.CustomerService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Getter
public class LoginWindow extends JFrame implements ActionListener {
    private static final Logger LOG = LoggerFactory.getLogger(LoginWindow.class);
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JTextField alarmField;
    public LoginWindow() {
        JPanel panel = new JPanel();
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        alarmField = new JTextField(20);
        alarmField.setBackground(null);
        alarmField.setBorder(null);
        alarmField.setForeground(Color.red);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(alarmField);

        add(panel);
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer id = Integer.valueOf(usernameField.getText());
        String password = passwordField.getText();
        String ans = sendRequest(id,password);
        LOG.info(String.format("Response %s",ans));
        if(ans.equals("true") ) {
            alarmField.setText("The ID and Password are good!");
        }
        else {
            alarmField.setText("The ID and Password do not match!");
        }
    }

    private static String sendRequest(Integer id, String password) {
        try {
            URL url = new URL(String.format("http://localhost:8080/login?id=%s&password=%s",id,password));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");


//            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
//            out.write(payload);
//            out.close();

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

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(LoginWindow::new);
    }
}
