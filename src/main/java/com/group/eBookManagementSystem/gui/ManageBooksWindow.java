package com.group.eBookManagementSystem.gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.model.Book;
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

public class ManageBooksWindow {

    private static final Logger LOG = LoggerFactory.getLogger(ManageBooksWindow.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final SingletonWindow singletonWindow;
    private final JTable userTable;
    private final JTextField bookIDField;
    private final JButton deleteBookButton;
    private final JButton addNewBookButton;
    private final JTextField alarmField;

    public ManageBooksWindow() throws JsonProcessingException {
        String allBooks = findAllBooks();
        LOG.info("AllBooks:" + allBooks);
        List<Book> bookList = new ObjectMapper().readValue(allBooks, new TypeReference<List<Book>>() {
        });
        LOG.info("Size:" + bookList.size());

        singletonWindow = SingletonWindow.getInstance();
        singletonWindow.getContentPane().removeAll();
        singletonWindow.repaint();

        JPanel panel = new JPanel();
        String[] columnNames = {"Book ID", "Book Name", "Author", "Subject", "Main Content", "Rating"};
        Object[][] booksData = new Object[bookList.size()][6];
        for (int i = 0; i < bookList.size(); i++) {
            booksData[i][0] = bookList.get(i).getId();
            booksData[i][1] = bookList.get(i).getBookName();
            booksData[i][2] = bookList.get(i).getAuthor();
            booksData[i][3] = bookList.get(i).getSubject();
            booksData[i][4] = bookList.get(i).getContent();
            booksData[i][5] = bookList.get(i).getRate();
        }
        userTable = new JTable(booksData, columnNames);
        JScrollPane tablePane = new JScrollPane(userTable);
        tablePane.setPreferredSize(new Dimension(400, 300));

        JLabel bookIDLabel = new JLabel("Book ID:");
        bookIDField = new JTextField(20);

        deleteBookButton = new JButton("Delete");
        deleteBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String response = findBookByIDRequest(Integer.parseInt(bookIDField.getText()));
                LOG.info("res:" + response);
                if (response.equals("")) {
                    alarmField.setText("BookID does not exist！");
                } else {
                    deleteBookByID();
                }
            }
        });

        addNewBookButton = new JButton(("Add a New Book"));
        addNewBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                javax.swing.SwingUtilities.invokeLater(AddNewBookWindow::new);
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

        panel.add(bookIDLabel);
        panel.add(bookIDField);
        panel.add(deleteBookButton);
        panel.add(addNewBookButton);
        panel.add(alarmField);
        panel.add(tablePane);
        panel.add(goBackButton);

        singletonWindow.setSize(500, 450);
        singletonWindow.add(panel);
        singletonWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        singletonWindow.setLocationRelativeTo(null);
        singletonWindow.setVisible(true);
    }

    public String deleteBookByID() {
        try {
            URL url = new URL("http://localhost:8080/deleteBook?id=" + Integer.parseInt(bookIDField.getText()));
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
                    ManageBooksWindow manageBooksWindow = new ManageBooksWindow();
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

    public static String findAllBooks() {
        try {
            URL url = new URL("http://localhost:8080/listBooks");
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
