package com.group.eBookManagementSystem.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.model.Book;
import com.group.eBookManagementSystem.model.Customer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
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

public class AccountWindow {
    private static final Logger LOG = LoggerFactory.getLogger(AccountWindow.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final SingletonWindow singletonWindow;
    private final JTable userTable;
    private final JTextField bookNameField;
    private final JButton addButton;
    private final JButton deleteButton;
    private final JTextField rateBookField;
    private final JButton rateButton;
    private final JButton booksButton;
    private final JButton usersButton;
    private final String userName;
    private final Customer customer;
    private final JTextField alarmField;

    public AccountWindow(String userName) throws JsonProcessingException {
        this.userName = userName;
        String customerInfo = findByUserNameRequest(userName);
        customer = objectMapper.readValue(customerInfo, Customer.class);
        boolean isAdmin = customer.getRole() == Customer.Role.ADMIN;

        singletonWindow = SingletonWindow.getInstance();
        singletonWindow.getContentPane().removeAll();
        singletonWindow.repaint();

        JPanel userPanel = new JPanel();
        List<Integer> bookIDList = customer.getMyLibrary();
        String[][] booksData = buildBookList(bookIDList);
        String[] columnNames = {"BookID", "BookName", "Author", "Subject", "Content", "Rate"};
        userTable = new JTable(booksData, columnNames);
        JScrollPane tablePane = new JScrollPane(userTable);
        tablePane.setPreferredSize(new Dimension(400, 300));

        JLabel bookNameLabel = new JLabel("Book ID:");
        bookNameField = new JTextField(20);

        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                addBookToCustomer(Integer.valueOf(bookNameField.getText()));
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                deleteBookFromCustomer(Integer.valueOf(bookNameField.getText()));
                SwingUtilities.invokeLater(() -> {
                    try {
                        AccountWindow accountWindow = new AccountWindow(userName);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        JLabel bookRateLabel = new JLabel("Enter rating:");
        rateBookField = new JTextField(20);
        rateButton = new JButton("Rate It");
        rateButton.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                rateBook(Integer.valueOf(bookNameField.getText()), Integer.valueOf(rateBookField.getText()));
                SwingUtilities.invokeLater(() -> {
                    try {
                        AccountWindow accountWindow = new AccountWindow(userName);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }));

        alarmField = new JTextField(20);
        alarmField.setBorder(null);
        alarmField.setBackground(null);
        alarmField.setForeground(Color.red);

        userPanel.add(bookNameLabel);
        userPanel.add(bookNameField);
        userPanel.add(addButton);
        userPanel.add(deleteButton);
        userPanel.add(bookRateLabel);
        userPanel.add(rateBookField);
        userPanel.add(rateButton);
        userPanel.add(alarmField);
        userPanel.add(tablePane);


        JPanel adminPanel = new JPanel();
        booksButton = new JButton("Manage Books");
        booksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        ManageBooksWindow manageBooksWindow = new ManageBooksWindow();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
        usersButton = new JButton("Manage Users");
        usersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        ManageCustomersWindow manageCustomersWindow = new ManageCustomersWindow();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        adminPanel.add(booksButton);
        adminPanel.add(usersButton);

        if (isAdmin) {
            singletonWindow.add(adminPanel, BorderLayout.CENTER);
        } else {
            singletonWindow.add(userPanel, BorderLayout.CENTER);
        }

        singletonWindow.setSize(500, 450);

        singletonWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        singletonWindow.setLocationRelativeTo(null);
        singletonWindow.setVisible(true);
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

    public String[][] buildBookList(List<Integer> bookIDList) throws JsonProcessingException {
        String[][] booksData = new String[bookIDList.size()][6];
        LOG.info(String.valueOf(bookIDList.size()));
        for (int i = 0; i < bookIDList.size(); i++) {
            String response = findBookByIDRequest(bookIDList.get(i));
            LOG.info(response);

            Book book = objectMapper.readValue(response, Book.class);
            booksData[i][0] = book.getId().toString();
            booksData[i][1] = book.getBookName();
            booksData[i][2] = book.getAuthor();
            booksData[i][3] = book.getSubject();
            booksData[i][4] = book.getContent();
            booksData[i][5] = book.getRate().toString();
        }
        return booksData;
    }

    private static String findBookByIDRequest(Integer bookID) {
        try {
            URL url = new URL(String.format("http://localhost:8080/findBookByID/%s", bookID));
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

    public void manageCustomerBooks() {
        StringBuilder response = new StringBuilder();
        int statusCode = 0;

        try {
            URL url = new URL(String.format("http://localhost:8080/updateCustomerByUserName/%s", userName));
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
    }

    public void deleteBookFromCustomer(Integer bookID) {
        List<Integer> bookList = customer.getMyLibrary();
        bookList.removeAll(Collections.singletonList(bookID));
        customer.setMyLibrary(bookList);

        manageCustomerBooks();
    }

    public void addBookToCustomer(Integer bookID) {
        List<Integer> bookList = customer.getMyLibrary();
        if (!bookList.contains(bookID)) {
            bookList.add(bookID);
            customer.setMyLibrary(bookList);
            manageCustomerBooks();

            SwingUtilities.invokeLater(() -> {
                try {
                    AccountWindow updateAccountWindow = new AccountWindow(customer.getUserName());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            alarmField.setText("This book is already in your Library!");
        }


    }

    public void rateBook(Integer bookID, Integer rate) {
        try {
            URL url = new URL(String.format("http://localhost:8080/rateBook/%s?rate=%s", bookID, rate));
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
