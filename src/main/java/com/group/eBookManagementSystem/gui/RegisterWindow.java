package com.group.eBookManagementSystem.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.gui.utlils.HttpRequestUtils;
import com.group.eBookManagementSystem.model.Customer;
import java.awt.BorderLayout;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterWindow {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterWindow.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final SingletonWindow singletonWindow;
    private final JTextField usernameField;
    private final JTextField firstnameField;
    private final JTextField lastnameField;
    private final JPasswordField passwordField;
    private final JTextField emailField;
    private final JButton submitButton;
    private final JTextField alarmField;

    public RegisterWindow() {
        singletonWindow = SingletonWindow.getInstance();
        singletonWindow.getContentPane().removeAll();
        singletonWindow.repaint();

        JPanel panel = new JPanel();
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel firstnameLabel = new JLabel("Firstname:");
        firstnameField = new JTextField(20);
        JLabel lastnameLabel = new JLabel("Lastname:");
        lastnameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        JLabel emailLabel = new JLabel("E-mail:");
        emailField = new JTextField(20);
        submitButton = new JButton("Submit");
        alarmField = new JTextField(20);
        alarmField.setBorder(null);
        alarmField.setBackground(null);
        alarmField.setForeground(Color.red);
        submitButton.addActionListener(e -> handleRegisterRequest());

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(firstnameLabel);
        panel.add(firstnameField);
        panel.add(lastnameLabel);
        panel.add(lastnameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(submitButton);
        panel.add(alarmField);

        singletonWindow.add(panel, BorderLayout.CENTER);
        singletonWindow.setSize(300, 450);
        singletonWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        singletonWindow.setLocationRelativeTo(null);
        singletonWindow.setVisible(true);
    }


    public void handleRegisterRequest() {
        try {
            Customer customer = new Customer();
            customer.setUserName(usernameField.getText());
            customer.setPassword(passwordField.getText());
            customer.setEmail(emailField.getText());
            customer.setLastName(lastnameField.getText());
            customer.setFirstName(firstnameField.getText());

            URL url = new URL("http://localhost:8080/addCustomer");
            String[] response = HttpRequestUtils.sendPostRequest(url, customer);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleRegisterRequest: %s", responseMessage));
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                SwingUtilities.invokeLater(LoginWindow::new);
            } else {
                alarmField.setText(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleRegisterRequest failed: " + e.getMessage());
        }
    }

}

