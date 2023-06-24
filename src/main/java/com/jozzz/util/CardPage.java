package com.jozzz.util;

import com.jozzz.Main;

import java.awt.*;


public class CardPage {
    private static final CardLayout cardLayout = new CardLayout();
    public static CardLayout getCardLayout() {
        return cardLayout;
    }
    public static void addPage(Component component,String pageName){
        Main.display.add(component, pageName);
    }
    public static void showPage(String pageName){
        getCardLayout().show(Main.display, pageName);
    }
}
