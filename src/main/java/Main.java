import gui.Display;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Frame;

public class Main {

    public static void main(String[] args){

        JFrame frame = new JFrame();
        frame.setTitle("DPO Cleanser");
        frame.setIconImage(new ImageIcon("src//main//resources//img//MomCow60px.png").getImage());

        frame.add(new Display());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1366, 768));
        frame.setMinimumSize(frame.getPreferredSize());
        frame.pack();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
