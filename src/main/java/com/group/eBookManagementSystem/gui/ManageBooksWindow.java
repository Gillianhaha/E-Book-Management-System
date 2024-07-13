package com.group.eBookManagementSystem.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.gui.utlils.HttpRequestUtils;
import com.group.eBookManagementSystem.model.Book;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ManageBooksWindow {

    private static final Logger LOG = LogManager.getLogger(ManageBooksWindow.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final SingletonWindow singletonWindow;
    private final JTable userTable;
    private final JTextField bookIDField;
    private final JButton deleteBookButton;
    private final JButton addNewBookButton;
    private final JTextField alarmField;

    public ManageBooksWindow() {
        List<Book> bookList = handleFindAllBooksRequest();

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
        deleteBookButton.addActionListener(event -> handleDeleteBookRequest());

        addNewBookButton = new JButton(("Add a New Book"));
        addNewBookButton.addActionListener(event -> SwingUtilities.invokeLater(AddNewBookWindow::new));

        alarmField = new JTextField(20);
        alarmField.setForeground(Color.red);
        alarmField.setBorder(null);
        alarmField.setBackground(null);
        JButton goBackButton = new JButton("Go Back");
        goBackButton.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> {
                AccountWindow accountWindow = new AccountWindow(HttpRequestUtils.handleGetAdminName());
            });
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

    private void handleDeleteBookRequest() {
        try {
            URL url = new URL("http://localhost:8080/deleteBook?id=" + Integer.parseInt(bookIDField.getText()));
            String[] response = HttpRequestUtils.sendPostRequest(url);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleDeleteBookRequest: %s", responseMessage));
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                SwingUtilities.invokeLater(ManageBooksWindow::new);
            } else {
                alarmField.setText(responseMessage);
                LOG.error("handleDeleteBookRequest failed: " + responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleDeleteBookRequest failed: " + e.getMessage());
        }
    }

    private List<Book> handleFindAllBooksRequest() {
        try {
            URL url = new URL("http://localhost:8080/listBooks");
            String[] response = HttpRequestUtils.sendGetRequest(url);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleFindAllBooksRequest: %s", responseMessage));
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                return objectMapper.readValue(responseMessage, new TypeReference<List<Book>>() {});
            } else {
                alarmField.setText(responseMessage);
                LOG.error("handleFindAllBooksRequest failed: " + responseMessage);
                throw new RuntimeException(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleFindAllBooksRequest failed: " + e);
            throw new RuntimeException(e);
        }
    }

}
