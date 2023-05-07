package gui;

import util.CustomTree;
import util.Element;
import util.RunDB;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CowDetail extends JPanel{

    private final JPanel detailPanel;
    private final JPanel treePanel;

    private final Display display;
    public CowDetail(Display display){
        this.display = display;
        treePanel = new JPanel();
        treePanel.setPreferredSize(new Dimension(500,768));
        treePanel.setBorder(new EmptyBorder(0,10,0,10));
        treePanel.setLayout(new BorderLayout());

        JButton backButton = new JButton("ย้อนกลับ");
        backButton.setFont(Element.getFont(20));
        backButton.setPreferredSize(new Dimension(0,50));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Display.getCardLayout().show(display, "COWS_TABLE");
            }
        });

        treePanel.add(backButton, BorderLayout.NORTH);

        detailPanel = new JPanel();
        detailPanel.setLayout(new BorderLayout());


        this.setLayout(new BorderLayout());

    }

    public void setUpCowTreePanel(String cowCode){
        ArrayList<String[]> cowParent = RunDB.getCowParent(cowCode);

        setUpCowsDetail(cowCode);

        JTree tree = new JTree(getCowsTree(cowParent, cowCode));
        tree.setCellRenderer(new CustomTree());
        tree.setFont(Element.getFont(25));
        expandAllNodes(tree);

        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());

                if (path != null) {
                    DefaultMutableTreeNode clickedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    setUpCowsDetail(clickedNode.getUserObject().toString().split("\\)")[1].trim());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(500,768));
        scrollPane.setBorder(new EmptyBorder(20,20,20,20));

        treePanel.add(scrollPane);
        this.add(treePanel, BorderLayout.WEST);
        this.add(detailPanel);
        display.add(this, "COW_DETAIL");
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
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("(CHILD) "+cowCode);
        DefaultMutableTreeNode prevNode = root;
        int index = 1;
        for (String[] rowParent : cowParent) {
            if (!rowParent[1].isEmpty() && !rowParent[2].isEmpty()) {
                DefaultMutableTreeNode mom = new DefaultMutableTreeNode("(MOM "+index+") "+rowParent[1]);
                DefaultMutableTreeNode dad = new DefaultMutableTreeNode("(DAD "+index+") "+rowParent[2]);
                prevNode.add(mom);
                prevNode.add(dad);
                prevNode = mom;
            }
            else{
                if(rowParent[1].isEmpty() && !rowParent[2].isEmpty()){
                    DefaultMutableTreeNode mom = new DefaultMutableTreeNode("(MOM "+index+") ไม่พบแม่พันธุ์");
                    DefaultMutableTreeNode dad = new DefaultMutableTreeNode("(DAD "+index+") "+rowParent[2]);
                    prevNode.add(mom);
                    prevNode.add(dad);
                    prevNode = mom;
                }
                if (!rowParent[1].isEmpty() && rowParent[2].isEmpty()){
                    DefaultMutableTreeNode mom = new DefaultMutableTreeNode("(MOM "+index+") "+rowParent[1]);
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
