package util;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

public class CustomTree extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component component = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        if (node.getUserObject().toString().startsWith("(CHILD)")){
            setIcon(new ImageIcon(new ImageIcon("src//main//resources//img//Cow60px.png")
                    .getImage().getScaledInstance(60,60, Image.SCALE_SMOOTH)));
        }
        else if (node.getUserObject().toString().startsWith("(MOM")){
            setIcon(new ImageIcon(new ImageIcon("src//main//resources//img//MomCow60px.png")
                    .getImage().getScaledInstance(60,60, Image.SCALE_SMOOTH)));
        }
        else if (node.getUserObject().toString().startsWith("(DAD")){
            setIcon(new ImageIcon(new ImageIcon("src//main//resources//img//DadCow60px.png")
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
            component.setForeground(Color.PINK);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 5)")
                || node.getUserObject().toString().startsWith("(DAD 5)")){
            component.setForeground(Color.ORANGE);
        }
        else if (node.getUserObject().toString().startsWith("(MOM 6)")
                || node.getUserObject().toString().startsWith("(DAD 6)")){
            component.setForeground(Color.DARK_GRAY);
        }
        else {
            component.setForeground(Color.BLACK);
        }

        return component;
    }
}
