package org.example.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Optional;

/**
 * Modern JavaFX Alert Utility - Replaces JOptionPane with beautiful styled alerts
 * Provides consistent styling across the application
 */
public class AlertUtils {

    /**
     * Show modern success alert
     */
    public static void showSuccess(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message, "#27ae60", FontAwesomeSolid.CHECK_CIRCLE);
    }

    /**
     * Show modern error alert
     */
    public static void showError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message, "#e74c3c", FontAwesomeSolid.EXCLAMATION_TRIANGLE);
    }

    /**
     * Show modern warning alert
     */
    public static void showWarning(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message, "#f39c12", FontAwesomeSolid.EXCLAMATION_CIRCLE);
    }

    /**
     * Show modern info alert
     */
    public static void showInfo(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message, "#3498db", FontAwesomeSolid.INFO_CIRCLE);
    }

    /**
     * Show modern confirmation dialog
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style the alert
        styleAlert(alert, "#667eea", FontAwesomeSolid.QUESTION_CIRCLE);

        // Custom buttons
        ButtonType yesButton = new ButtonType("Ya");
        ButtonType noButton = new ButtonType("Tidak");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    /**
     * Show custom confirmation with custom button texts
     */
    public static boolean showConfirmation(String title, String message, String yesText, String noText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style the alert
        styleAlert(alert, "#667eea", FontAwesomeSolid.QUESTION_CIRCLE);

        // Custom buttons
        ButtonType yesButton = new ButtonType(yesText);
        ButtonType noButton = new ButtonType(noText);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    /**
     * Show alert with owner window
     */
    public static void showSuccess(Stage owner, String title, String message) {
        Alert alert = createAlert(Alert.AlertType.INFORMATION, title, message, "#27ae60", FontAwesomeSolid.CHECK_CIRCLE);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    public static void showError(Stage owner, String title, String message) {
        Alert alert = createAlert(Alert.AlertType.ERROR, title, message, "#e74c3c", FontAwesomeSolid.EXCLAMATION_TRIANGLE);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    public static void showWarning(Stage owner, String title, String message) {
        Alert alert = createAlert(Alert.AlertType.WARNING, title, message, "#f39c12", FontAwesomeSolid.EXCLAMATION_CIRCLE);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    public static void showInfo(Stage owner, String title, String message) {
        Alert alert = createAlert(Alert.AlertType.INFORMATION, title, message, "#3498db", FontAwesomeSolid.INFO_CIRCLE);
        alert.initOwner(owner);
        alert.showAndWait();
    }

    /**
     * Private method to show alert with styling
     */
    private static void showAlert(Alert.AlertType type, String title, String message, String color, FontAwesomeSolid icon) {
        Alert alert = createAlert(type, title, message, color, icon);
        alert.showAndWait();
    }

    /**
     * Create styled alert
     */
    private static Alert createAlert(Alert.AlertType type, String title, String message, String color, FontAwesomeSolid icon) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        styleAlert(alert, color, icon);
        return alert;
    }

    /**
     * Apply modern styling to alert
     */
    private static void styleAlert(Alert alert, String color, FontAwesomeSolid icon) {
        DialogPane dialogPane = alert.getDialogPane();
        
        // Style dialog pane
        dialogPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 10);" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-padding: 20;"
        );

        // Add icon to header
        try {
            FontIcon alertIcon = new FontIcon(icon);
            alertIcon.setIconSize(30);
            alertIcon.setIconColor(Color.web(color));
            dialogPane.setGraphic(alertIcon);
        } catch (Exception e) {
            // Continue without icon if there's an issue
        }

        // Style buttons
        dialogPane.getButtonTypes().forEach(buttonType -> {
            Button button = (Button) dialogPane.lookupButton(buttonType);
            if (button != null) {
                button.setStyle(
                        "-fx-background-color: " + color + ";" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 13px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                                "-fx-padding: 10 20;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);"
                );

                // Hover effect
                button.setOnMouseEntered(e -> {
                    button.setStyle(
                            "-fx-background-color: derive(" + color + ", -10%);" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-size: 13px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                                    "-fx-padding: 10 20;" +
                                    "-fx-background-radius: 20;" +
                                    "-fx-cursor: hand;" +
                                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);"
                    );
                });

                button.setOnMouseExited(e -> {
                    button.setStyle(
                            "-fx-background-color: " + color + ";" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-size: 13px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                                    "-fx-padding: 10 20;" +
                                    "-fx-background-radius: 20;" +
                                    "-fx-cursor: hand;" +
                                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);"
                    );
                });
            }
        });

        // Style content text
        dialogPane.lookup(".content").setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );
    }
}