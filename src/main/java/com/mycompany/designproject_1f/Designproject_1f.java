package com.mycompany.designproject_1f;

import javax.swing.SwingUtilities;

public class Designproject_1f {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = new MainFrame();
            mf.setVisible(true);
        });
    }
}

