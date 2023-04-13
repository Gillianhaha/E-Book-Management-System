package com.group.eBookManagementSystem.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.gui.utlils.HttpRequestUtils;
import com.group.eBookManagementSystem.model.Book;
import com.group.eBookManagementSystem.model.Customer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
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

    public AccountWindow(String userName) {
        this.userName = userName;
        customer = handleFindByUserNameRequest(userName);
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
        addButton.addActionListener(event -> addBookToCustomer(Integer.valueOf(bookNameField.getText())));

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(event -> deleteBookFromCustomer(Integer.valueOf(bookNameField.getText())));

        JLabel bookRateLabel = new JLabel("Enter rating:");
        rateBookField = new JTextField(20);
        rateButton = new JButton("Rate It");
        rateButton.addActionListener((event -> {
            handleRateBookRequest(Integer.valueOf(bookNameField.getText()), Integer.valueOf(rateBookField.getText()));
            SwingUtilities.invokeLater(() -> {
                AccountWindow accountWindow = new AccountWindow(userName);
            });
        }));

        alarmField = new JTextField(30);
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
        booksButton.addActionListener(event -> SwingUtilities.invokeLater(ManageBooksWindow::new));
        usersButton = new JButton("Manage Users");
        usersButton.addActionListener(event -> SwingUtilities.invokeLater(ManageCustomersWindow::new));

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

    private Customer handleFindByUserNameRequest(String userName) {
        try {
            URL url = new URL(String.format("http://localhost:8080/findCustomerByUserName/%s", userName));
            String[] response = HttpRequestUtils.sendGetRequest(url);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleFindByUserNameRequest: %s", responseMessage));
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                return objectMapper.readValue(responseMessage, Customer.class);
            } else {
                throw new RuntimeException(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleFindByUserNameRequest failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String[][] buildBookList(List<Integer> bookIdList) {
        String[][] booksData = new String[bookIdList.size()][6];
        LOG.info(String.valueOf(bookIdList.size()));
        for (int i = 0; i < bookIdList.size(); i++) {
            Book book = handleFindBookByIdRequest(bookIdList.get(i));
            booksData[i][0] = book.getId().toString();
            booksData[i][1] = book.getBookName();
            booksData[i][2] = book.getAuthor();
            booksData[i][3] = book.getSubject();
            booksData[i][4] = book.getContent();
            booksData[i][5] = book.getRate().toString();
        }
        return booksData;
    }

    private Book handleFindBookByIdRequest(Integer bookId) {
        try {
            URL url = new URL(String.format("http://localhost:8080/findBookById/%s", bookId));
            String[] response = HttpRequestUtils.sendGetRequest(url);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleFindBookByIdRequest: %s", responseMessage));
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                return objectMapper.readValue(responseMessage, Book.class);
            } else {
                alarmField.setText(responseMessage);
                LOG.error("handleFindBookByIdRequest failed: " + responseMessage);
                throw new RuntimeException(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleFindBookByIdRequest failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void handleUpdateCustomerRequest(Customer customer) {
        try {
            URL url = new URL("http://localhost:8080/updateCustomer");
            String[] response = HttpRequestUtils.sendPostRequest(url, customer);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleUpdateCustomerRequest: %s", responseMessage));
            if (responseStatusCode != HttpURLConnection.HTTP_OK) {
                alarmField.setText(responseMessage);
            } else {
                SwingUtilities.invokeLater(() -> {
                    AccountWindow updateAccountWindow = new AccountWindow(customer.getUserName());
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleUpdateCustomerRequest failed: " + e.getMessage());
        }
    }

    public void deleteBookFromCustomer(Integer bookId) {
        List<Integer> bookList = customer.getMyLibrary();
        if (bookList.contains(bookId)) {
            bookList.removeAll(Collections.singletonList(bookId));
            customer.setMyLibrary(bookList);
            handleUpdateCustomerRequest(customer);

            SwingUtilities.invokeLater(() -> {
                AccountWindow accountWindow = new AccountWindow(userName);
            });
        } else {
            alarmField.setText(String.format("User %s does not have book %s.", userName, bookId));
        }
    }

    public void addBookToCustomer(Integer bookId) {
        List<Integer> bookList = customer.getMyLibrary();
        LOG.info(String.format("addBookToCustomer started: adding book %s to user %s", bookId, userName));
        LOG.info(String.format("addBookToCustomer started: user current book list %s", bookList));
        if (!bookList.contains(bookId)) {
            bookList.add(bookId);
            customer.setMyLibrary(bookList);
            handleUpdateCustomerRequest(customer);
            bookList.remove(bookId);
            customer.setMyLibrary(bookList);
        } else {
            alarmField.setText("This book is already in your Library!");
        }
    }

    public void handleRateBookRequest(Integer bookId, Integer rate) {
        try {
            List<Integer> bookIdList = handleFindByUserNameRequest(userName).getMyLibrary();
            if (!bookIdList.contains(bookId)) {
                String errorMessage = String.format("User %s does not have book %d, so can not rate it!", userName, bookId);
                alarmField.setText(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
            URL url = new URL(String.format("http://localhost:8080/rateBook/%s?rate=%s", bookId, rate));
            String[] response = HttpRequestUtils.sendPostRequest(url);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleRateBookRequest: %s", responseMessage));
            if (responseStatusCode != HttpURLConnection.HTTP_OK) {
                alarmField.setText(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleRateBookRequest failed: " + e.getMessage());
        }
    }

}
