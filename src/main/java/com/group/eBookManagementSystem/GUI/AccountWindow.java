package com.group.eBookManagementSystem.GUI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.model.Customer;
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

public class AccountWindow {
    private static final Logger LOG = LoggerFactory.getLogger(LoginWindow.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final SingletonWindow singletonWindow;
    private final JTextField usernameField;
    private final JTextField firstnameField;
    private final JTextField lastnameField;
    private final JPasswordField passwordField;
    private final JTextField emailField;
    private final JButton submitButton;
    private final JTextField alarmField;

    public AccountWindow(String userName) {
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
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });

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
        singletonWindow.setSize(300, 300);
        singletonWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        singletonWindow.setLocationRelativeTo(null);
        singletonWindow.setVisible(true);
    }


    public void submit() {
        String userName = usernameField.getText();
        String firstName = firstnameField.getText();
        String lastName = lastnameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        int statusCode = -1;
        StringBuilder response = new StringBuilder();

        try {
            Customer customer = new Customer();
            customer.setUserName(userName);
            customer.setPassword(password);
            customer.setEmail(email);
            customer.setLastName(lastName);
            customer.setFirstName(firstName);

            URL url = new URL("http://localhost:8080/addCustomer");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String payload = objectMapper.writeValueAsString(customer);
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(payload);
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            statusCode = con.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info(String.format("Response %s", response));
        if (statusCode == 200) {
            javax.swing.SwingUtilities.invokeLater(AccountWindow::new);
        } else {
            alarmField.setText("Failed!" + response);
        }
    }
}
