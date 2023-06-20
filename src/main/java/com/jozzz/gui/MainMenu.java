package com.jozzz.gui;

import com.jozzz.Main;
import com.jozzz.constant.CompName;
import com.jozzz.constant.DisplayState;
import com.jozzz.util.Element;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Objects;

public class MainMenu extends JPanel {

    private DIPDisplay dipDisplay;
    private DairyDisplay dairyDisplay;
    private TableListSelect tableListSelect;
    public MainMenu(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 300));
        panel.setLayout(new GridLayout(2, 1));

        JLabel dipTitle = new JLabel(CompName.DIP_TITLE);
        dipTitle.setFont(Element.getFont(35));
        dipTitle.setIcon(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/MomCow.png"))));
        dipTitle.setHorizontalAlignment(JLabel.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 10));

        JButton dipDataButton = new JButton(CompName.DIP_DATA_BUTTON);
        dipDataButton.setFont(Element.getFont(20));
        dipDataButton.addActionListener(event -> {
            if(dipDisplay == null){
                dipDisplay = new DIPDisplay();
            }
            Main.display.add(dipDisplay, DisplayState.DIP);
            Element.getCardLayout().show(Main.display, DisplayState.DIP);
        });

        JButton dairyPatternButton = new JButton(CompName.DAIRY_PATTERN_BUTTON);
        dairyPatternButton.setFont(Element.getFont(20));
        dairyPatternButton.addActionListener(event -> {
            if(dairyDisplay == null){
                dairyDisplay = new DairyDisplay();
            }
            Main.display.add(dairyDisplay, DisplayState.DAIRY);
            Element.getCardLayout().show(Main.display, DisplayState.DAIRY);
        });

        JButton tableListButton = new JButton(CompName.TABLE_LIST_BUTTON);
        tableListButton.setFont(Element.getFont(20));
        tableListButton.addActionListener(event -> {
            if(tableListSelect == null){
                tableListSelect = new TableListSelect();
            }
            Main.display.add(tableListSelect, DisplayState.TABLE_LIST);
            Element.getCardLayout().show(Main.display, DisplayState.TABLE_LIST);
        });

        buttonPanel.add(dipDataButton);
        buttonPanel.add(dairyPatternButton);
        buttonPanel.add(tableListButton);

        panel.add(dipTitle);
        panel.add(buttonPanel);

        this.add(panel);
    }
}
