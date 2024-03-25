package com.jozzz.gui;

import com.jozzz.Main;
import com.jozzz.constant.CompName;
import com.jozzz.constant.DisplayState;
import com.jozzz.gui.dip.DIPDisplay;
import com.jozzz.gui.dip.TableListSelect;
import com.jozzz.gui.dip_dairy.DipDairyMergeDisplay;
import com.jozzz.gui.dip_dairy.DairyNewFormat.DairyNewFormatDisplay;
import com.jozzz.util.CardPage;
import com.jozzz.util.Element;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class MainDisplay extends JPanel {

    private DIPDisplay dipDisplay;
    private DairyNewFormatDisplay dairyDisplay;
    private DipDairyMergeDisplay dipDairyMergeDisplay;
    private TableListSelect tableListSelect;
    public MainDisplay(){
        this.setPreferredSize(new Dimension(1366, 768));
        this.setBorder(new EmptyBorder(10,10,10,10));
        this.setLayout(new GridLayout(1, 2));

        JPanel dipMenuPanel = new JPanel();
        dipMenuPanel.setPreferredSize(new Dimension(500, 300));
        dipMenuPanel.setLayout(new GridLayout(2, 1));

        JPanel dairyMenuPanel = new JPanel();
        dairyMenuPanel.setPreferredSize(new Dimension(500, 300));
        dairyMenuPanel.setLayout(new GridLayout(2, 1));

        JLabel dipTitle = new JLabel(CompName.DIP_TITLE);
        dipTitle.setFont(Element.getFont(35));
        dipTitle.setIcon(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/DadCow.png"))));
        dipTitle.setHorizontalAlignment(JLabel.CENTER);

        JPanel dipButtonPanel = new JPanel();
        dipButtonPanel.setLayout(new GridLayout(2, 1, 0, 10));

        JLabel dairyTitle = new JLabel(CompName.DIP_DAIRY_TITLE);
        dairyTitle.setFont(Element.getFont(25));
        dairyTitle.setIcon(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/MomCow.png"))));
        dairyTitle.setHorizontalAlignment(JLabel.CENTER);

        JPanel dairyButtonPanel = new JPanel();
        dairyButtonPanel.setLayout(new GridLayout(2, 1, 0, 10));

        JButton dipDataButton = new JButton(CompName.DIP_DATA_BUTTON);
        dipDataButton.setFont(Element.getFont(20));
        dipDataButton.addActionListener(event -> {
            if(dipDisplay == null){
                dipDisplay = new DIPDisplay();
            }
            else{
                if(dipDisplay.isPageLoading()){
                    dipDisplay = new DIPDisplay();
                }
            }
            CardPage.addPage(dipDisplay, DisplayState.DIP);
            CardPage.showPage(DisplayState.DIP);
        });

        JButton dairyPatternButton = new JButton(CompName.DAIRY_PATTERN_BUTTON);
        dairyPatternButton.setFont(Element.getFont(20));
        dairyPatternButton.addActionListener(event -> {
            if(dairyDisplay == null){
                dairyDisplay = new DairyNewFormatDisplay();
            }
            else{
                if(dairyDisplay.isPageLoading()){
                    dairyDisplay = new DairyNewFormatDisplay();
                }
            }
            CardPage.addPage(dairyDisplay, DisplayState.DAIRY);
            CardPage.showPage(DisplayState.DAIRY);
        });

        JButton tableListButton = new JButton(CompName.TABLE_LIST_BUTTON);
        tableListButton.setFont(Element.getFont(20));
        tableListButton.addActionListener(event -> {
          
                tableListSelect = new TableListSelect();
            
            
            CardPage.addPage(tableListSelect, DisplayState.TABLE_LIST);
            CardPage.showPage(DisplayState.TABLE_LIST);
        });

        JButton dairyMergeButton = new JButton(CompName.DAIRY_MERGE_BUTTON);
        dairyMergeButton.setFont(Element.getFont(20));
        dairyMergeButton.addActionListener(event -> {
            if(dipDairyMergeDisplay == null){
                dipDairyMergeDisplay = new DipDairyMergeDisplay();
            }
            else{
                if(dipDairyMergeDisplay.isPageLoading()){
                    dipDairyMergeDisplay = new DipDairyMergeDisplay();
                }
            }
            CardPage.addPage(dipDairyMergeDisplay, DisplayState.DIP_DAIRY_MERGE);
            CardPage.showPage(DisplayState.DIP_DAIRY_MERGE);
        });

        dipButtonPanel.add(dipDataButton);
        dipButtonPanel.add(tableListButton);

        dipMenuPanel.add(dipTitle);
        dipMenuPanel.add(dipButtonPanel);

        dairyButtonPanel.add(dairyMergeButton);
        dairyButtonPanel.add(dairyPatternButton);

        dairyMenuPanel.add(dairyTitle);
        dairyMenuPanel.add(dairyButtonPanel);

        this.add(getMenuPanel(dipMenuPanel));
        this.add(getMenuPanel(dairyMenuPanel));
    }

    private JPanel getMenuPanel(JPanel addPanel){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.add(addPanel);
        return panel;
    }
}
