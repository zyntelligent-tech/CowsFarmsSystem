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
        label.setPreferredSize(new Dimension(300, 100));
        label.setFont(Element.getFont(25));
        label.setHorizontalAlignment(JLabel.CENTER);
        dialog = new JDialog();
        dialog.setIconImage(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/MomCow60px.png"))).getImage());
        dialog.setTitle("Loading...");
        dialog.setContentPane(label);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
    }

    public JDialog getDialog(){
        return dialog;
    }
}
