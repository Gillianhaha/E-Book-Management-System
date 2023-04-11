package com.group.eBookManagementSystem.GUI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.eBookManagementSystem.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddNewBookWindow {
    private static final Logger LOG = LoggerFactory.getLogger(AddNewBookWindow.class);
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
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                submit();
                SwingUtilities.invokeLater(() -> {
                    try {
                        ManageBooksWindow manageBooksWindow = new ManageBooksWindow();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
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


    public void submit() {
        String bookName = bookNameField.getText();
        String author = authorField.getText();
        String subject = subjectField.getText();
        String content = contentField.getText();
        int statusCode = -1;
        StringBuilder response = new StringBuilder();

        try {
            Book book = new Book();
            book.setBookName(bookName);
            book.setAuthor(author);
            book.setSubject(subject);
            book.setContent(content);

            URL url = new URL("http://localhost:8080/addBook");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            String payload = objectMapper.writeValueAsString(book);
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
//        if (statusCode == 200) {
//            LOG.info("Added!");
//            javax.swing.SwingUtilities.invokeLater(LoginWindow::new);
//        } else {
//            alarmField.setText("Failed to add!" + response);
//        }
    }

}


