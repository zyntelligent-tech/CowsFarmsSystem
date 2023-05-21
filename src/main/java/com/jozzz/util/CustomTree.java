package com.jozzz.util;

import com.jozzz.Main;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.util.Objects;

public class CustomTree extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node.getUserObject().toString().startsWith("(CHILD)")){
            setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/Cow60px.png")))
                    .getImage().getScaledInstance(60,60, Image.SCALE_SMOOTH)));
        }
        else if (node.getUserObject().toString().startsWith("(MOM")){
            setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/MomCow60px.png")))
                    .getImage().getScaledInstance(60,60, Image.SCALE_SMOOTH)));
        }
        else if (node.getUserObject().toString().startsWith("(DAD")){
            setIcon(new ImageIcon(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/DadCow60px.png")))
                    .getImage().getScaledInstance(60,60, Image.SCALE_SMOOTH)));
        }

        if (node.getUserObject().toString().startsWith("(MOM 1)")
            || node.getUserObject().toString().startsWith("(DAD 1)")){
            component.setForeground(Color.RED);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 2)")
                || node.getUserObject().toString().startsWith("(DAD 2)")){
            component.setForeground(Color.BLUE);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 3)")
                || node.getUserObject().toString().startsWith("(DAD 3)")){
            component.setForeground(Color.MAGENTA);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 4)")
                || node.getUserObject().toString().startsWith("(DAD 4)")){
            component.setForeground(Color.ORANGE);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 5)")
                || node.getUserObject().toString().startsWith("(DAD 5)")){
            component.setForeground(Color.PINK);
        }
        else  if (node.getUserObject().toString().startsWith("(MOM 6)")
                || node.getUserObject().toString().startsWith("(DAD 6)")){
            component.setForeground(Color.RED);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 7)")
                || node.getUserObject().toString().startsWith("(DAD 7)")){
            component.setForeground(Color.BLUE);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 8)")
                || node.getUserObject().toString().startsWith("(DAD 8)")){
            component.setForeground(Color.MAGENTA);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 9)")
                || node.getUserObject().toString().startsWith("(DAD 9)")){
            component.setForeground(Color.ORANGE);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 10)")
                || node.getUserObject().toString().startsWith("(DAD 10)")){
            component.setForeground(Color.PINK);
        }
        else {
            component.setForeground(Color.BLACK);
        }

        return component;
    }
}
