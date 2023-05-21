package util;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Dimension;

public class Dialog {

    private JDialog dialog;
    public Dialog(){
        JLabel label = new JLabel("รอข้อมูลจากเซิร์ฟเวอร์...");
        label.setPreferredSize(new Dimension(300, 100));
        label.setFont(Element.getFont(25));
        label.setHorizontalAlignment(JLabel.CENTER);
        dialog = new JDialog();
        dialog.setTitle("Loading...");
        dialog.setContentPane(label);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
    }

    public JDialog getDialog(){
        return dialog;
    }
}
