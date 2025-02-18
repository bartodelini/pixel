package com.bartodelini.pixel.core;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class UIUtils {

    public static void setSystemLookAndFeel() {
        // Set the default LookAndFeel of the Swing application to the default look of the used platform
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void showExceptionDialog(String title, String message, Exception e) {
        // Write the stack trace of the passed in exception
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));

        // Create the panel for the error message and cause as well as the exception stack trace
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create the panel for the error message and cause
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());
        panel.add(messagePanel, BorderLayout.NORTH);

        // Create the labels for the error message and cause
        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(new Color(0x0033bc));
//        messageLabel.setForeground(UIManager.getColor("textHighlight"));
        messageLabel.setFont(messageLabel.getFont().deriveFont(14.5f));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 10, 0));
        messagePanel.add(messageLabel, BorderLayout.NORTH);

        JLabel causeLabel = new JLabel("Cause: " + e.getMessage());
        causeLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 4, 0));
        messagePanel.add(causeLabel, BorderLayout.SOUTH);

        // Create the text area for the exception stack trace.
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setForeground(Color.RED);
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setText(stringWriter.toString());
        textArea.setCaretPosition(0); // Scroll to the top of the text area.

        // Create the scroll pane for the text area.
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        panel.add(scrollPane, BorderLayout.SOUTH);

        // Create the holding frame.
        JFrame frame = new JFrame(title);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        // Play the default "beep" sound of the platform.
        Toolkit.getDefaultToolkit().beep();

        // Show the MessageDialog
        JOptionPane.showMessageDialog(frame, panel, title, JOptionPane.ERROR_MESSAGE);
        frame.dispose();
    }
}