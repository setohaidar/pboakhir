package org.example.views.dialogs;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class NotificationDialog {

    public enum NotificationType {
        SUCCESS("#27ae60", FontAwesomeSolid.CHECK_CIRCLE),
        ERROR("#e74c3c", FontAwesomeSolid.TIMES_CIRCLE),
        WARNING("#f39c12", FontAwesomeSolid.EXCLAMATION_TRIANGLE),
        INFO("#3498db", FontAwesomeSolid.INFO_CIRCLE);

        private final String color;
        private final FontAwesomeSolid icon;

        NotificationType(String color, FontAwesomeSolid icon) {
            this.color = color;
            this.icon = icon;
        }

        public String getColor() { return color; }
        public FontAwesomeSolid getIcon() { return icon; }
    }

    public static void show(String title, String message, NotificationType type) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.NONE);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER_LEFT);
        root.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: " + type.getColor() + ";" +
                        "-fx-border-width: 0 0 0 5;" +
                        "-fx-border-radius: 10;"
        );

        // Efek bayangan
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setOffsetX(0);
        shadow.setOffsetY(5);
        shadow.setRadius(15);
        root.setEffect(shadow);

        // Header dengan icon dan title
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        FontIcon icon = new FontIcon(type.getIcon());
        icon.setIconSize(20);
        icon.setIconColor(Color.web(type.getColor()));

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Button closeButton = new Button("×");
        closeButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #7f8c8d;" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 0 5;"
        );
        closeButton.setOnAction(e -> stage.close());

        header.getChildren().addAll(icon, titleLabel);

        // Message
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        // Top container dengan close button
        HBox topContainer = new HBox();
        topContainer.setAlignment(Pos.CENTER_LEFT);
        topContainer.getChildren().addAll(header);

        HBox.setHgrow(header, javafx.scene.layout.Priority.ALWAYS);
        topContainer.getChildren().add(closeButton);

        root.getChildren().addAll(topContainer, messageLabel);

        Scene scene = new Scene(root, 350, 100);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);

        // Posisi di pojok kanan atas
        stage.setX(javafx.stage.Screen.getPrimary().getVisualBounds().getMaxX() - 370);
        stage.setY(20);

        stage.show();

        // Animasi masuk
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), root);
        slideIn.setFromX(350);
        slideIn.setToX(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        slideIn.play();
        fadeIn.play();

        // Auto close setelah 4 detik
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(Duration.seconds(4), e -> {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(event -> stage.close());
                    fadeOut.play();
                })
        );
        timeline.play();
    }
}
