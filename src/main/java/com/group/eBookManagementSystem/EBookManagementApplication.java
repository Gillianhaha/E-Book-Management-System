package com.group.eBookManagementSystem;

import com.group.eBookManagementSystem.gui.LoginWindow;
import javax.swing.SwingUtilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EBookManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(EBookManagementApplication.class, args);

        // This starts GUI as along with server.
        System.setProperty("java.awt.headless", "false");
        SwingUtilities.invokeLater(LoginWindow::new);
    }

}
