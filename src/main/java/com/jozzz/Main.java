package com.jozzz;

import com.jozzz.constant.DisplayState;
import com.jozzz.gui.MainMenu;
import com.jozzz.gui.TableListSelect;
import com.jozzz.util.Element;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Objects;

public class Main {

    public static JPanel display;
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setTitle("DPO Cleanser");
                frame.setIconImage(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/MomCow60px.png"))).getImage());

                display = new JPanel();
                display.setLayout(Element.getCardLayout());
                display.add(new MainMenu(), DisplayState.MAIN_MENU);

                frame.add(display);

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setPreferredSize(new Dimension(1366, 768));
                frame.setMinimumSize(frame.getPreferredSize());
                frame.pack();
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
