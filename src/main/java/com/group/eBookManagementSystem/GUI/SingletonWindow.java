package com.group.eBookManagementSystem.GUI;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
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
