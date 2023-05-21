package com.jozzz.gui;

import com.jozzz.util.CustomTree;
import com.jozzz.util.Dialog;
import com.jozzz.util.Element;
import com.jozzz.util.RunDB;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class CowDetail extends JPanel{

    private final Display display;
    private final JPanel treePanel;
    private final String cowCode;
    private JCheckBox cowDetailCB;
    private JCheckBox farmerDetailCB;
    private JCheckBox centerDetailCB;
    private JCheckBox breedDetailCB;
    private ArrayList<String[]> cowParent;
    private String[] cowDetail;
    private String[] farmerDetail;
    private String[] centerDetail;
    private String[] sectorDetail;
    private String[] breedDetail;
    public CowDetail(Display display, String cowCode){
        this.display = display;
        this.cowCode = cowCode;
        this.setLayout(new BorderLayout());

        treePanel = new JPanel();
        treePanel.setPreferredSize(new Dimension(500,768));
        treePanel.setBorder(new EmptyBorder(0,10,0,10));
        treePanel.setLayout(new BorderLayout());
        this.add(treePanel);

        setUpCheckBox();

        Dialog dialog = new Dialog();
        new Thread(() -> {
            InitCowData();
            SwingUtilities.invokeLater(() -> {
                setUpCowTreePanel(cowCode);
                dialog.getDialog().setVisible(false);
            });
        }).start();
        dialog.getDialog().setVisible(true);

        display.add(this, "COW_DETAIL");
    }

    private void InitCowData(){
        cowParent = RunDB.getCowParent(cowCode);
        cowDetail = RunDB.getCow("cow",cowCode);
        if (cowDetail.length > 0) {
            breedDetail = RunDB.getBreeds(cowCode);
            farmerDetail = RunDB.getFarmer(cowDetail[0]);
            if (farmerDetail.length > 0){
                centerDetail = RunDB.getCenter(farmerDetail[0]);
                sectorDetail = RunDB.getSector(centerDetail[0]);
            }
        } else {
            cowDetail = RunDB.getCow("breeder",cowCode);
        }
    }

    private void setUpCowTreePanel(String cowCode){
        treePanel.removeAll();
        JTree tree = new JTree(getCowsTree(cowParent, cowCode));
        tree.setCellRenderer(new CustomTree());
        tree.setFont(Element.getFont(25));
        expandAllNodes(tree);

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(500,768));
        scrollPane.setBorder(new EmptyBorder(20,20,20,20));

        treePanel.add(scrollPane);
        treePanel.validate();
    }

    private void setUpCheckBox(){
        JPanel menuBarPanel = new JPanel();
        menuBarPanel.setPreferredSize(new Dimension(0,50));
        menuBarPanel.setLayout(new FlowLayout());

        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(Element.getFont(20));

        backButton.addActionListener(event -> Display.getCardLayout().show(display, "COWS_TABLE"));

        menuBarPanel.add(backButton);

        cowDetailCB = new JCheckBox();
        cowDetailCB.setFont(Element.getFont(20));
        cowDetailCB.setText("ข้อมูลวัว");
        cowDetailCB.setSelected(true);
        breedDetailCB = new JCheckBox();
        breedDetailCB.setFont(Element.getFont(20));
        breedDetailCB.setText("สายพันธุ์");
        breedDetailCB.setSelected(true);
        farmerDetailCB = new JCheckBox();
        farmerDetailCB.setFont(Element.getFont(20));
        farmerDetailCB.setText("ข้อมูลเกษตรกร");
        farmerDetailCB.setSelected(true);
        centerDetailCB = new JCheckBox();
        centerDetailCB.setFont(Element.getFont(20));
        centerDetailCB.setText("ข้อมูลศูนย์/สหกรณ์");
        centerDetailCB.setSelected(true);


        JButton filterBut = new JButton("แสดงข้อมูลที่เลือก");
        filterBut.setFont(Element.getFont(20));
        filterBut.addActionListener(event -> setUpCowTreePanel(cowCode));

        menuBarPanel.add(cowDetailCB);
        menuBarPanel.add(farmerDetailCB);
        menuBarPanel.add(centerDetailCB);
        menuBarPanel.add(breedDetailCB);
        menuBarPanel.add(filterBut);
        this.add(menuBarPanel, BorderLayout.NORTH);
    }

    private String getFilterCowsDetail(){
        String filterStr = "";
        if (cowDetail.length > 0){
            if(cowDetailCB.isSelected()){
                filterStr += " [ (ข้อมูลโค) หมายเลขโค : "+cowDetail[1]+" , ชื่อโค : "+cowDetail[4]
                        +" , วันเกิด : "+cowDetail[6]+" , สถานะโค : "+cowDetail[2]+" , รหัสสายพันธุ์ยุโรป : "+cowDetail[12]
                        +" , %สายพันธุ์ยุโรป : "+cowDetail[13]+" ] ";
            }

            if (breedDetailCB.isSelected()){
                filterStr += " [ (สายพันธุ์) "+breedDetail[1]+ " ] ";
            }

            if (farmerDetail.length > 0){
                if (farmerDetailCB.isSelected()){
                    filterStr += " [ (ข้อมูลเกษตรกร) หมายเลขสมาชิก : "+farmerDetail[1]+" , ชื่อ : "+farmerDetail[3]
                            +" , นามสกุล : "+farmerDetail[4]+" , วันเกิด : "+farmerDetail[5]+" , จำนวนโคที่มี : "
                            +farmerDetail[12]+" ] ";
                }
                if (centerDetailCB.isSelected()){
                    filterStr += " [ (ศูนย์/สหกรณ์) รหัสศูนย์/สหกรณ์ : "+centerDetail[1]+" , ชื่อศูนย์/สหกรณ์ : "+centerDetail[2]
                            +" , ชื่อย่อของศูนย์ : "+centerDetail[3]+" , ภาค : "+sectorDetail[1]+" ] ";
                }
            }
        }
        else {
            if (cowDetailCB.isSelected()){
                filterStr += " [ (ข้อมูลโค) หมายเลขโค : "+cowDetail[0]+" , ชื่อโค : "+cowDetail[1]
                        + " , หมายเลขพ่อพันธุ์ : "+cowDetail[2]+" , หมายเลขแม่พันธุ์ : "+cowDetail[3]+" ] ";
            }
        }
        return filterStr;
    }

    private DefaultMutableTreeNode getCowsTree(ArrayList<String[]> cowParent, String cowCode){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("(CHILD) "+cowCode + getFilterCowsDetail());
        DefaultMutableTreeNode prevNode = root;
        int index = 1;
        for (String[] rowParent : cowParent) {
            if (!rowParent[1].isEmpty() && !rowParent[2].isEmpty()) {
                DefaultMutableTreeNode mom = new DefaultMutableTreeNode("(MOM "+index+") "+rowParent[1] + getFilterCowsDetail());
                DefaultMutableTreeNode dad = new DefaultMutableTreeNode("(DAD "+index+") "+rowParent[2] + getFilterCowsDetail());
                prevNode.add(mom);
                prevNode.add(dad);
                prevNode = mom;
            }
            else{
                if(rowParent[1].isEmpty() && !rowParent[2].isEmpty()){
                    DefaultMutableTreeNode mom = new DefaultMutableTreeNode("(MOM "+index+") ไม่พบแม่พันธุ์");
                    DefaultMutableTreeNode dad = new DefaultMutableTreeNode("(DAD "+index+") "+rowParent[2] + getFilterCowsDetail());
                    prevNode.add(mom);
                    prevNode.add(dad);
                    prevNode = mom;
                }
                if (!rowParent[1].isEmpty() && rowParent[2].isEmpty()){
                    DefaultMutableTreeNode mom = new DefaultMutableTreeNode("(MOM "+index+") "+rowParent[1] + getFilterCowsDetail());
                    DefaultMutableTreeNode dad = new DefaultMutableTreeNode("(DAD "+index+") ไม่พบพ่อพันธุ์");
                    prevNode.add(mom);
                    prevNode.add(dad);
                    prevNode = mom;
                }
            }
            index++;
        }
        return root;
    }

    private void expandAllNodes(JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }
}
