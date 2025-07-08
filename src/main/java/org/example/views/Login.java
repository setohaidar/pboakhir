package org.example.views;

import javax.swing.*;
import java.awt.*;

public class Login {
    private JPanel contentPane;
    private JTextField emailField;
    private JTextField passwordField;
    private JButton submitButton;

    public Login() {
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // for demo purposes
    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        Login login = new Login();

        login.getSubmitButton().addActionListener(_ -> {
            String email = login.getEmailField().getText();
            String password = login.getPasswordField().getText();

            if (!email.equals("john.doe@wemail.you") || !password.equals("123456")) {
                JOptionPane.showMessageDialog(
                    login.getContentPane(),
                    "Email atau password salah.",
                    "Eror",
                    JOptionPane.ERROR_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    login.getContentPane(),
                    "Login berhasil!",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        frame.setContentPane(login.getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JTextField getEmailField() {
        return emailField;
    }

    public JTextField getPasswordField() {
        return passwordField;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
