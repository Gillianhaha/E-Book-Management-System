package com.group.eBookManagementSystem.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.gui.utlils.HttpRequestUtils;
import com.group.eBookManagementSystem.model.Customer;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
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

    public ManageCustomersWindow() {
        List<Customer> allCustomers = handleFindAllCustomersRequest();

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
        deleteButton.addActionListener(event -> handleDeleteCustomerByUserNameRequest());
        alarmField = new JTextField(20);
        alarmField.setForeground(Color.red);
        alarmField.setBorder(null);
        alarmField.setBackground(null);

        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(event -> SwingUtilities.invokeLater(() -> {
            AccountWindow accountWindow = new AccountWindow(handleGetAdminName());
        }));

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

    public void handleDeleteCustomerByUserNameRequest() {
        try {
            URL url = new URL("http://localhost:8080/deleteCustomer?userName=" + userNameField.getText());
            String[] response = HttpRequestUtils.sendPostRequest(url);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleDeleteCustomerByUserNameRequest: %s", responseMessage));
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                SwingUtilities.invokeLater(ManageCustomersWindow::new);
            } else {
                alarmField.setText(responseMessage);
                LOG.error("handleDeleteCustomerByUserNameRequest failed: " + responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleDeleteCustomerByUserNameRequest failed: " + e);
        }
    }

    public List<Customer> handleFindAllCustomersRequest() {
        try {
            URL url = new URL("http://localhost:8080/listCustomers");
            String[] response = HttpRequestUtils.sendGetRequest(url);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleFindAllCustomersRequest: %s", responseMessage));
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                return new ObjectMapper().readValue(responseMessage, new TypeReference<List<Customer>>() {});
            } else {
                LOG.error("handleFindAllCustomersRequest failed: " + responseMessage);
                throw new RuntimeException(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleFindAllCustomersRequest failed: " + e);
            throw new RuntimeException(e);
        }
    }

    private String handleGetAdminName() {
        try {
            URL url = new URL("http://localhost:8080/getAdminName");
            String[] response = HttpRequestUtils.sendGetRequest(url);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleGetAdminName: %s", responseMessage));
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                return responseMessage;
            } else {
                LOG.error("handleGetAdminName failed: " + responseMessage);
                throw new RuntimeException(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleGetAdminName failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
