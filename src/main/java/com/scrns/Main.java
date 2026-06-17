package com.scrns; // <--- Yeh line add karein

import com.scrns.gui.MainFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(MainFrame::new);
    }
}