package com.group.eBookManagementSystem;

import com.group.eBookManagementSystem.GUI.LoginWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EBookManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBookManagementApplication.class, args);
//        javax.swing.SwingUtilities.invokeLater(LoginWindow::new);
//        System.setProperty("java.awt.headless", "false");
//        SwingUtilities.invokeLater(() -> {
//            try {
//                HelloWorldSwing.createAndShowGUI();
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

}
