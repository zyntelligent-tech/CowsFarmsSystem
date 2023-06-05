package com.jozzz.util;

import com.jozzz.Main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Objects;

public class ProgressBar {
    private final JFrame progressBarFrame;
    private final JProgressBar progressBar;
    private final JLabel progressLabel;
    public ProgressBar(int progress) {
        progressBarFrame = new JFrame();
        progressBarFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        progressBarFrame.setTitle("กำลังส่งออกไฟล์...");
        progressBarFrame.setIconImage(new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("img/MomCow60px.png"))).getImage());
        progressBarFrame.setSize(300, 150);
        progressBarFrame.setLayout(new GridLayout(2, 1));

        progressBar = new JProgressBar(0, progress);
        progressBar.setFont(Element.getFont(20));
        progressBar.setStringPainted(false);
        progressBar.setForeground(Color.GREEN);

        progressLabel = new JLabel("กำลังส่งออกไฟล์ : 0 %");
        progressLabel.setFont(Element.getFont(20));
        progressLabel.setHorizontalAlignment(JLabel.CENTER);
        progressBarFrame.add(progressLabel);

        progressBarFrame.add(progressBar);
        progressBarFrame.setResizable(false);
        progressBarFrame.setLocationRelativeTo(null);
        progressBarFrame.setVisible(true);
    }

    public JFrame getProgressBarFrame() {
        return progressBarFrame;
    }

    public JLabel getProgressLabel() {
        return progressLabel;
    }

    public void setProgress(int progress){
        progressBar.setValue(progress);
        int percent = (int) ((double) progress / progressBar.getMaximum() * 100);
        String labelText = "กำลังส่งออกไฟล์ : " + percent + " %";
        progressLabel.setText(labelText);
    }
}
