package com.scrns;

import com.formdev.flatlaf.FlatDarkLaf;
import com.scrns.gui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        // Apply FlatLaf Dark Theme
        FlatDarkLaf.setup();

        // Launch GUI
        SwingUtilities.invokeLater( MainFrame::new);
    }
}