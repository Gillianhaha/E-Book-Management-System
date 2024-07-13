package com.group.eBookManagementSystem.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.gui.utlils.HttpRequestUtils;
import com.group.eBookManagementSystem.model.Book;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddNewBookWindow {

    private static final Logger LOG = LogManager.getLogger(AddNewBookWindow.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final SingletonWindow singletonWindow;
    private final JTextField bookNameField;
    private final JTextField authorField;
    private final JTextField subjectField;
    private final JTextField contentField;
    private final JButton submitButton;
    private final JTextField alarmField;

    public AddNewBookWindow() {
        singletonWindow = SingletonWindow.getInstance();
        singletonWindow.getContentPane().removeAll();
        singletonWindow.repaint();

        JPanel panel = new JPanel();
        JLabel bookNameLabel = new JLabel("Book Name:");
        bookNameField = new JTextField(20);
        JLabel authorLabel = new JLabel("Author:");
        authorField = new JTextField(20);
        JLabel subjectLabel = new JLabel("Subject:");
        subjectField = new JTextField(20);
        JLabel contentLabel = new JLabel("Main Content:");
        contentField = new JTextField(20);
        submitButton = new JButton("Add It");
        alarmField = new JTextField(20);
        alarmField.setBorder(null);
        alarmField.setBackground(null);
        alarmField.setForeground(Color.red);
        submitButton.addActionListener(event -> {
            handleAddBookRequest();
            SwingUtilities.invokeLater(ManageBooksWindow::new);
        });

        panel.add(bookNameLabel);
        panel.add(bookNameField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(subjectLabel);
        panel.add(subjectField);
        panel.add(contentLabel);
        panel.add(contentField);
        panel.add(submitButton);
        panel.add(alarmField);

        singletonWindow.add(panel, BorderLayout.CENTER);
        singletonWindow.setSize(300, 450);
        singletonWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        singletonWindow.setLocationRelativeTo(null);
        singletonWindow.setVisible(true);
    }

    private void handleAddBookRequest() {
        try{
            Book book = new Book();
            book.setBookName(bookNameField.getText());
            book.setAuthor(authorField.getText());
            book.setSubject(subjectField.getText());
            book.setContent(contentField.getText());

            URL url = new URL("http://localhost:8080/addBook");
            String[] response = HttpRequestUtils.sendPostRequest(url, book);
            int responseStatusCode = Integer.parseInt(response[0]);
            String responseMessage = response[1];
            LOG.info(String.format("Response of handleAddBookRequest: %s", responseMessage));
            if (responseStatusCode != HttpURLConnection.HTTP_OK) {
                alarmField.setText(responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("handleAddBookRequest failed: " + e.getMessage());
        }
    }

}


