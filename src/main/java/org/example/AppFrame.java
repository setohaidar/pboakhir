package org.example;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    private static AppFrame instance;

    private AppFrame() {
        setTitle("Sistem Peminjaman Ruangan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Returns a singleton instance of a JFrame.
     *
     * @return a JFrame instance
     */
    public static AppFrame getInstance() {
        if (instance == null) {
            instance = new AppFrame();
        }

        return instance;
    }

    /**
     * Sets the content pane of the frame, repacks the frame, refreshes the frame, and centers the frame.
     */
    @Override
    public void setContentPane(Container contentPane) {
        super.setContentPane(contentPane);
        pack();
        refresh();
        setLocationRelativeTo(null);
    }

    private void refresh() {
        revalidate();
        repaint();
    }
}
