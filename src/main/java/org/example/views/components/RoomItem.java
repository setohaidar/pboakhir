package org.example.views.components;

import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.data.ReservationsByRoom;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class RoomItem {
    private final VBox contentPane;
    private final Label roomName;
    private final Label reservationAmount;
    private final Button actionButton;

    public RoomItem() {
        contentPane = new VBox(15);
        contentPane.setPadding(new Insets(20));
        contentPane.setAlignment(Pos.CENTER_LEFT);
        contentPane.setPrefWidth(600);
        contentPane.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;"
        );

        // Efek bayangan
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setOffsetX(0);
        shadow.setOffsetY(5);
        shadow.setRadius(15);
        contentPane.setEffect(shadow);

        // Room name dengan icon
        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        FontIcon roomIcon = new FontIcon(FontAwesomeSolid.DOOR_OPEN);
        roomIcon.setIconSize(20);
        roomIcon.setIconColor(Color.web("#3498db"));

        roomName = new Label();
        roomName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        nameBox.getChildren().addAll(roomIcon, roomName);

        // Reservation amount dengan icon
        HBox infoBox = new HBox(8);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        FontIcon infoIcon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
        infoIcon.setIconSize(14);
        infoIcon.setIconColor(Color.web("#7f8c8d"));

        reservationAmount = new Label();
        reservationAmount.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

        infoBox.getChildren().addAll(infoIcon, reservationAmount);

        // Action button
        actionButton = new Button();
        actionButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );
        actionButton.setVisible(false);

        // Layout untuk button
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        buttonBox.getChildren().addAll(spacer, actionButton);

        contentPane.getChildren().addAll(nameBox, infoBox, buttonBox);

        // Animasi hover
        contentPane.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), contentPane);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();

            contentPane.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-radius: 15;"
            );
        });

        contentPane.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), contentPane);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            contentPane.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-radius: 15;"
            );
        });
    }

    public void setData(ReservationsByRoom reservationsByRoom) {
        roomName.setText(reservationsByRoom.room().name());

        if (reservationsByRoom.reservations() != null) {
            int count = reservationsByRoom.reservations().size();
            reservationAmount.setText(count + " reservasi aktif");
        } else {
            reservationAmount.setText("Tidak ada reservasi");
        }
    }

    public void setAction(String actionName, Runnable action) {
        actionButton.setText(actionName);
        actionButton.setVisible(true);
        actionButton.setOnAction(e -> action.run());

        // Icon untuk button
        FontIcon buttonIcon = new FontIcon(FontAwesomeSolid.CHECK_CIRCLE);
        buttonIcon.setIconSize(14);
        buttonIcon.setIconColor(Color.WHITE);
        actionButton.setGraphic(buttonIcon);
    }

    public VBox getContentPane() {
        return contentPane;
    }

    public Button getActionButton() {
        return actionButton;
    }
}
