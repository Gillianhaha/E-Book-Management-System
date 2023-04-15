package com.group.eBookManagementSystem.gui;

import javax.swing.JFrame;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingletonWindow extends JFrame {

    private static SingletonWindow singletonWindow = null;

    private SingletonWindow() {

    }

    public static synchronized SingletonWindow getInstance(){
        if(singletonWindow == null){
            singletonWindow = new SingletonWindow();
        }
        return singletonWindow;
    }

}
