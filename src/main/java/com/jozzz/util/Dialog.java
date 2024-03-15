package com.jozzz.util;

import com.jozzz.Main;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.util.Objects;

public class Dialog {

    private final JDialog dialog;
    public Dialog(){
        JLabel label = new JLabel("รอข้อมูลจากเซิร์ฟเวอร์...");
        label.setPreferredSize(new Dimension(500, 320));
        label.setFont(Element.getFont(40));
        label.setHorizontalAlignment(JLabel.CENTER);
        dialog = new JDialog(Main.frame);
        dialog.setIconImage(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/MomCow60px.png"))).getImage());
        dialog.setTitle("Loading...");
        dialog.setContentPane(label);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setUndecorated(true);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(Main.frame);
    }

    public JDialog getDialog(){
        return dialog;
    }
}
