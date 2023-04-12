package com.group.eBookManagementSystem.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.model.Customer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManageCustomersWindow {
    private static final Logger LOG = LoggerFactory.getLogger(ManageCustomersWindow.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final SingletonWindow singletonWindow;
    private final JTable userTable;
    private final JTextField userNameField;
    private final JButton deleteButton;
    private final JTextField alarmField;

    public ManageCustomersWindow() throws JsonProcessingException {
        String allUsers = findAllCustomer();
        LOG.info("AllUsers:" + allUsers);
        List<Customer> allCustomers = new ObjectMapper().readValue(allUsers, new TypeReference<List<Customer>>() {
        });
        LOG.info("Size:" + allCustomers.size());

        singletonWindow = SingletonWindow.getInstance();
        singletonWindow.getContentPane().removeAll();
        singletonWindow.repaint();

        JPanel panel = new JPanel();
        String[] columnNames = {"User Name", "Firstname", "Lastname", "Email", "Role"};
        Object[][] usersData = new Object[allCustomers.size()][5];
        for (int i = 0; i < allCustomers.size(); i++) {
            usersData[i][0] = allCustomers.get(i).getUserName();
            usersData[i][1] = allCustomers.get(i).getFirstName();
            usersData[i][2] = allCustomers.get(i).getLastName();
            usersData[i][3] = allCustomers.get(i).getEmail();
            usersData[i][4] = allCustomers.get(i).getRole();
        }
        userTable = new JTable(usersData, columnNames);
        JScrollPane tablePane = new JScrollPane(userTable);
        tablePane.setPreferredSize(new Dimension(400, 300));

        JLabel userNameLabel = new JLabel("User Name:");
        userNameField = new JTextField(20);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String response = findByUserNameRequest(userNameField.getText());
                LOG.info("res:" + response);
                if (response.equals("")) {
                    alarmField.setText("Username does not exist！");
                } else {
                    deleteCustomerByUserName();
                }
            }
        });
        alarmField = new JTextField(20);
        alarmField.setForeground(Color.red);
        alarmField.setBorder(null);
        alarmField.setBackground(null);

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String userName = getUserName();
                SwingUtilities.invokeLater(() -> {
                    try {
                        AccountWindow accountWindow = new AccountWindow(userName);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        panel.add(userNameLabel);
        panel.add(userNameField);
        panel.add(deleteButton);
        panel.add(alarmField);
        panel.add(tablePane);
        panel.add(goBackButton);

        singletonWindow.setSize(500, 450);
        singletonWindow.add(panel);
        singletonWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        singletonWindow.setLocationRelativeTo(null);
        singletonWindow.setVisible(true);
    }

    public String deleteCustomerByUserName() {
        try {
            URL url = new URL("http://localhost:8080/deleteCustomer?userName=" + userNameField.getText());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            SwingUtilities.invokeLater(() -> {
                try {
                    ManageCustomersWindow manageCustomersWindow = new ManageCustomersWindow();
                } catch (JsonProcessingException e) {

                    throw new RuntimeException(e);
                }
            });
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();

            return "Error: " + e.getMessage();
        }
    }

    private static String findByUserNameRequest(String userName) {
        try {
            URL url = new URL(String.format("http://localhost:8080/findCustomerByUserName/%s", userName));
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

    public static String findAllCustomer() {
        try {
            URL url = new URL("http://localhost:8080/listCustomers");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getUserName() {
        try {
            URL url = new URL("http://localhost:8080/getAdminName");
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

}
