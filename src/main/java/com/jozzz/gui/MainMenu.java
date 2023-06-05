package com.jozzz.gui;

import com.jozzz.Main;
import com.jozzz.util.Element;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

public class MainMenu extends JPanel {

    private DPODisplay dpoDisplay = null;
    private DairyDisplay dairyDisplay = null;
    public MainMenu(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new GridBagLayout());



        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 200));
        panel.setLayout(new GridLayout(2, 1));

        JLabel textTitle = new JLabel("DPO Cleanser");
        textTitle.setFont(Element.getFont(35));
        textTitle.setHorizontalAlignment(JLabel.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10));

        JButton dpoButton = new JButton("Data DPO");
        dpoButton.setFont(Element.getFont(20));
        dpoButton.addActionListener(event -> {
            if(dpoDisplay == null){
                dpoDisplay = new DPODisplay();
            }
            Main.display.add(dpoDisplay, "DPO_DISPLAY");
            Element.getCardLayout().show(Main.display, "DPO_DISPLAY");
        });

        JButton dairyButton = new JButton("Data Zyan Dairy");
        dairyButton.setFont(Element.getFont(20));
        dairyButton.addActionListener(event -> {
            if(dairyDisplay == null){
                dairyDisplay = new DairyDisplay();
            }
            Main.display.add(dairyDisplay, "DAIRY_DISPLAY");
            Element.getCardLayout().show(Main.display, "DAIRY_DISPLAY");
        });

        buttonPanel.add(dpoButton);
        buttonPanel.add(dairyButton);

        panel.add(textTitle);
        panel.add(buttonPanel);

        this.add(panel);
    }
}
