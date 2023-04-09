package com.group.eBookManagementSystem.GUI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {

    private LoginFrame view;
    private RestTemplate restTemplate = new RestTemplate();

    public LoginController(LoginFrame view) {
        this.view = view;
        this.view.getLoginButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = view.getUsernameField().getText();
        String password = new String(view.getPasswordField().getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/home", request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JOptionPane.showMessageDialog(view, "Login successful!");
        } else {
            JOptionPane.showMessageDialog(view, "Invalid username or password.");
        }
    }
}
