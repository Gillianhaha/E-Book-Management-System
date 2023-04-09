//package com.group.eBookManagementSystem.GUI;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.group.eBookManagementSystem.model.Customer;
//
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import java.awt.Dimension;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class HelloWorldSwing {
//    private static ObjectMapper objectMapper = new ObjectMapper();
//    public static void createAndShowGUI() throws JsonProcessingException {
//        JFrame frame = new JFrame("Hello World Swing");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel panel = new JPanel();
//        JLabel label = new JLabel("Response:");
//        panel.add(label);
//
//        Customer customer = new Customer();
//        customer.setId(1);
//
//        String response = sendRequest(objectMapper.writeValueAsString(customer));
//        JLabel responseLabel = new JLabel(response);
//        panel.add(responseLabel);
//
//        frame.getContentPane().add(panel);
//        frame.setPreferredSize(new Dimension(300, 100));
//        frame.pack();
//        frame.setVisible(true);
//    }
//
//    private static String sendRequest(String payload) {
//        try {
//            URL url = new URL("http://localhost:8080/hello");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST");
//            con.setRequestProperty("Content-Type", "application/json");
//            con.setDoOutput(true);
//
//            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
//            out.write(payload);
//            out.close();
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuilder response = new StringBuilder();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//            return response.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "Error: " + e.getMessage();
//        }
//    }
//
//
//    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(() -> {
//            try {
//                createAndShowGUI();
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//}
