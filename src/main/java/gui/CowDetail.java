package gui;

import util.CustomTree;
import util.Element;
import util.RunDB;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CowDetail extends JPanel{

    private final JPanel detailPanel;
    private final JPanel treePanel;
    private final JPanel menuBarPanel;

    private final Display display;
    private String cowCode;
    private JCheckBox cowDetailCB;
    private JCheckBox farmerDetailCB;
    private JCheckBox centerDetailCB;
    private JCheckBox breedDetailCB;
    JTree tree;
    JScrollPane scrollPane;
    public CowDetail(Display display, String cowCode){
        this.display = display;
        this.cowCode = cowCode;
        treePanel = new JPanel();
        treePanel.setPreferredSize(new Dimension(500,768));
        treePanel.setBorder(new EmptyBorder(0,10,0,10));
        treePanel.setLayout(new BorderLayout());

        menuBarPanel = new JPanel();
        menuBarPanel.setPreferredSize(new Dimension(0,50));
        menuBarPanel.setLayout(new FlowLayout());

        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(Element.getFont(20));
//        backButton.setPreferredSize(new Dimension(0,50));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Display.getCardLayout().show(display, "COWS_TABLE");
            }
        });

        menuBarPanel.add(backButton);

        detailPanel = new JPanel();
        detailPanel.setLayout(new BorderLayout());

        this.setLayout(new BorderLayout());

        this.add(menuBarPanel, BorderLayout.NORTH);
        setUpCheckBox();
        setUpCowTreePanel(cowCode);

        this.add(treePanel);
        display.add(this, "COW_DETAIL");
    }

    public void setUpCowTreePanel(String cowCode){
        treePanel.removeAll();
        ArrayList<String[]> cowParent = RunDB.getCowParent(cowCode);

//        setUpCowsDetail(cowCode);

        tree = new JTree(getCowsTree(cowParent, cowCode));
        tree.setCellRenderer(new CustomTree());
        tree.setFont(Element.getFont(25));
        expandAllNodes(tree);

//        tree.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
//
//                if (path != null) {
//                    DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
//                    setUpCowsDetail(clickedNode.getUserObject().toString().split("\\)")[1].trim());
//                }
//            }
//        });
        scrollPane = new JScrollPane(tree);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(500,768));
        scrollPane.setBorder(new EmptyBorder(20,20,20,20));

        treePanel.add(scrollPane);
//        this.add(treePanel);
////        this.add(detailPanel);
//        display.add(this, "COW_DETAIL");
        treePanel.validate();
    }

    private void setUpCheckBox(){
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
        filterBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpCowTreePanel(cowCode);
            }
        });

        menuBarPanel.add(cowDetailCB);
        menuBarPanel.add(farmerDetailCB);
        menuBarPanel.add(centerDetailCB);
        menuBarPanel.add(breedDetailCB);
        menuBarPanel.add(filterBut);
    }

    private String getFilterCowsDetail(String cowCode){

        String filterStr = "";
        String[] cowDetail;
        if (RunDB.getCow("cow",cowCode).length > 0){
            cowDetail = RunDB.getCow("cow",cowCode);
            if(cowDetailCB.isSelected()){
                filterStr += " [ (ข้อมูลโค) หมายเลขโค : "+cowDetail[1]+" , ชื่อโค : "+cowDetail[4]
                        +" , วันเกิด : "+cowDetail[6]+" , สถานะโค : "+cowDetail[2]+" , รหัสสายพันธุ์ยุโรป : "+cowDetail[12]
                        +" , %สายพันธุ์ยุโรป : "+cowDetail[13]+" ] ";
            }

            if (breedDetailCB.isSelected()){
                filterStr += " [ (สายพันธุ์) "+RunDB.getBreeds(cowCode)[1]+ " ] ";
            }

            if (RunDB.getFarmer(cowDetail[0]).length > 0){
                String[] farmerDetail = RunDB.getFarmer(cowDetail[0]);
                String[] centerDetail = RunDB.getCenter(farmerDetail[0]);
                String[] sectorDetail = RunDB.getSector(centerDetail[0]);

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
        else if (RunDB.getCow("breeder",cowCode).length > 0){
            if (cowDetailCB.isSelected()){
                cowDetail = RunDB.getCow("breeder",cowCode);
                filterStr += " [ (ข้อมูลโค) หมายเลขโค : "+cowDetail[0]+" , ชื่อโค : "+cowDetail[1]
                        + " , หมายเลขพ่อพันธุ์ : "+cowDetail[2]+" , หมายเลขแม่พันธุ์ : "+cowDetail[3]+" ] ";
            }
        }
        return filterStr;
    }

    private void setUpCowsDetail(String cowCode){
        detailPanel.removeAll();
        JTextArea textArea = new JTextArea();
        textArea.setBorder(new EmptyBorder(20,50,20,20));
        textArea.setFont(Element.getFont(30));
        textArea.setEditable(false);

        String[] cowDetail;
        if (RunDB.getCow("cow",cowCode).length > 0){
            cowDetail = RunDB.getCow("cow",cowCode);
            textArea.append("ข้อมูลวัว\n");
            textArea.append("\tหมายเลขโค : "+cowDetail[1]+"\n\tชื่อโค : "+cowDetail[4]+"\n\tวันเกิด : "+cowDetail[6]+"\n" +
                    "\tสถานะโค : "+cowDetail[2]+"\n\tรหัสสายพันธุ์ยุโรป : "+cowDetail[12]+"\n\t%สายพันธุ์ยุโรป : "+cowDetail[13]+"\n\n");

            if (RunDB.getCow("cow",cowDetail[7]).length > 0){
                String[] cowMomDetail = RunDB.getCow("cow",cowDetail[7]);
                textArea.append("ข้อมูลแม่\n");
                textArea.append("\tหมายเลขโค : "+cowMomDetail[1]+"\n\tชื่อโค : "+cowMomDetail[4]+"\n\tวันเกิด : "+cowMomDetail[6]+"\n" +
                        "\tสถานะโค : "+cowMomDetail[2]+"\n\tรหัสสายพันธุ์ยุโรป : "+cowMomDetail[12]+"\n\t%สายพันธุ์ยุโรป : "+cowMomDetail[13]+"\n\n");
            }

            if (RunDB.getCow("breeder", cowDetail[8]).length > 0){
                String[] cowDadDetail = RunDB.getCow("breeder", cowDetail[8]);
                textArea.append("ข้อมูลพ่อ\n");
                textArea.append("\tหมายเลขโค : "+cowDadDetail[0]+"\n\tชื่อโค : "+cowDadDetail[1]+"\n"+
                        "\tหมายเลขพ่อพันธุ์ : "+cowDadDetail[2]+"\n\tหมายเลขแม่พันธุ์ : "+cowDadDetail[3]+"\n\n");
            }

            if (RunDB.getFarmer(cowDetail[0]).length > 0){
                String[] farmerDetail = RunDB.getFarmer(cowDetail[0]);
                String[] centerDetail = RunDB.getCenter(farmerDetail[0]);
                String[] sectorDetail = RunDB.getSector(centerDetail[0]);

                textArea.append("ข้อมูลเกษตรกร\n");
                textArea.append("\tหมายเลขสมาชิก : "+farmerDetail[1]+"\n\tชื่อ : "+farmerDetail[3]+"\n\tนามสกุล : "+farmerDetail[4]+"\n" +
                        "\tวันเกิด : "+farmerDetail[5]+"\n\tจำนวนโคที่มี : "+farmerDetail[12]+"\n\n");
                textArea.append("ศูนย์/สหกรณ์\n");
                textArea.append("\tรหัสศูนย์/สหกรณ์ : "+centerDetail[1]+"\n\tชื่อศูนย์/สหกรณ์ : "+centerDetail[2]+"\n" +
                        "\tชื่อย่อของศูนย์ : "+centerDetail[3]+"\n\tภาค : "+sectorDetail[1]+"\n\n");
            }
        }
        else if (RunDB.getCow("breeder",cowCode).length > 0){
            cowDetail = RunDB.getCow("breeder",cowCode);
            textArea.append("ข้อมูลพ่อพันธุ์\n");
            textArea.append("\tหมายเลขโค : "+cowDetail[0]+"\n\tชื่อโค : "+cowDetail[1]+"\n"+
                    "\tหมายเลขพ่อพันธุ์ : "+cowDetail[2]+"\n\tหมายเลขแม่พันธุ์ : "+cowDetail[3]+"\n\n");
        }

        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        detailPanel.add(scrollPane);
        detailPanel.validate();
    }

    public DefaultMutableTreeNode getCowsTree(ArrayList<String[]> cowParent, String cowCode){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("(CHILD) "+cowCode + getFilterCowsDetail(cowCode));
        DefaultMutableTreeNode prevNode = root;
        int index = 1;
        for (String[] rowParent : cowParent) {
            if (!rowParent[1].isEmpty() && !rowParent[2].isEmpty()) {
                DefaultMutableTreeNode mom = new DefaultMutableTreeNode("(MOM "+index+") "+rowParent[1] + getFilterCowsDetail(rowParent[1]));
                DefaultMutableTreeNode dad = new DefaultMutableTreeNode("(DAD "+index+") "+rowParent[2] + getFilterCowsDetail(rowParent[2]));
                prevNode.add(mom);
                prevNode.add(dad);
                prevNode = mom;
            }
            else{
                if(rowParent[1].isEmpty() && !rowParent[2].isEmpty()){
                    DefaultMutableTreeNode mom = new DefaultMutableTreeNode("(MOM "+index+") ไม่พบแม่พันธุ์");
                    DefaultMutableTreeNode dad = new DefaultMutableTreeNode("(DAD "+index+") "+rowParent[2] + getFilterCowsDetail(rowParent[2]));
                    prevNode.add(mom);
                    prevNode.add(dad);
                    prevNode = mom;
                }
                if (!rowParent[1].isEmpty() && rowParent[2].isEmpty()){
                    DefaultMutableTreeNode mom = new DefaultMutableTreeNode("(MOM "+index+") "+rowParent[1] + getFilterCowsDetail(rowParent[1]));
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
