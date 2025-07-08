package org.example.views;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.data.ConfirmationStatus;
import org.example.data.ReservationsByRoom;
import org.example.utils.ActionListenerWithRoom;
import org.example.views.components.RoomItem;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RoomList {
    private final BorderPane root;
    private Button returnButton;
    private Label startTime;
    private Label endTime;
    private VBox roomItemList;
    private VBox timePanel;
    private final Stage stage;

    public RoomList(Stage stage) {
        this.stage = stage;

        root = new BorderPane();

        // Background gradient
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(1, Color.web("#764ba2"))
        );

        BackgroundFill backgroundFill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        setupHeader();
        setupContent();
    }

    private void setupHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1);");

        // Title dengan icon
        FontIcon roomIcon = new FontIcon(FontAwesomeSolid.DOOR_OPEN);
        roomIcon.setIconSize(24);
        roomIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("Pilih Ruangan");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox titleBox = new HBox(10, roomIcon, titleLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        // Return button
        returnButton = new Button("Kembali");
        returnButton.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );

        FontIcon backIcon = new FontIcon(FontAwesomeSolid.ARROW_LEFT);
        backIcon.setIconSize(14);
        backIcon.setIconColor(Color.WHITE);
        returnButton.setGraphic(backIcon);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(titleBox, spacer, returnButton);
        root.setTop(header);
    }

    private void setupContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Time panel
        timePanel = new VBox(10);
        timePanel.setAlignment(Pos.CENTER);
        timePanel.setPadding(new Insets(20));
        timePanel.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;"
        );
        timePanel.setVisible(false);

        // Efek bayangan untuk time panel
        DropShadow timeShadow = new DropShadow();
        timeShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        timeShadow.setOffsetX(0);
        timeShadow.setOffsetY(5);
        timeShadow.setRadius(15);
        timePanel.setEffect(timeShadow);

        startTime = new Label();
        startTime.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        endTime = new Label();
        endTime.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        FontIcon timeIcon = new FontIcon(FontAwesomeSolid.CLOCK);
        timeIcon.setIconSize(20);
        timeIcon.setIconColor(Color.web("#3498db"));

        timePanel.getChildren().addAll(timeIcon, startTime, endTime);

        // Room list container
        roomItemList = new VBox(15);
        roomItemList.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(roomItemList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;" +
                        "-fx-border-color: transparent;"
        );
        scrollPane.setPrefHeight(400);

        // Info label
        Label infoLabel = new Label("Pilih ruangan yang tersedia untuk reservasi Anda");
        infoLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.9); -fx-font-size: 16px;");

        mainContent.getChildren().addAll(infoLabel, timePanel, scrollPane);
        root.setCenter(mainContent);
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public BorderPane getContentPane() {
        return root;
    }

    public void setTime(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        startTime.setText("Waktu Mulai: " + sdf.format(start));
        endTime.setText("Waktu Selesai: " + sdf.format(end));
        timePanel.setVisible(true);
    }

    public void displayRoomItemsWithCreateReservationButton(List<ReservationsByRoom> reservationGroups, ActionListenerWithRoom actionListenerWithRoom) {
        reservationGroups.sort((a, b) -> a.room().name().compareToIgnoreCase(b.room().name()));
        roomItemList.getChildren().clear();

        if (reservationGroups.isEmpty()) {
            Label noRoomsLabel = new Label("Tidak ada ruangan yang tersedia pada waktu yang dipilih");
            noRoomsLabel.setStyle(
                    "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-padding: 20;"
            );
            roomItemList.getChildren().add(noRoomsLabel);
            return;
        }

        for (ReservationsByRoom reservationGroup : reservationGroups) {
            RoomItem roomItem = new RoomItem();
            roomItem.setData(reservationGroup);

            roomItem.setAction(
                    "Pilih Ruangan",
                    () -> actionListenerWithRoom.onAction(reservationGroup.room(), null)
            );

            // Cek jika ada reservasi yang sudah diterima
            boolean isAccepted = reservationGroup.reservations() != null &&
                    !reservationGroup.reservations().isEmpty() &&
                    reservationGroup.reservations().stream()
                            .anyMatch(r -> r.confirmationStatus() == ConfirmationStatus.ACCEPTED);

            if (isAccepted) {
                roomItem.getActionButton().setDisable(true);
                roomItem.getActionButton().setText("Tidak Tersedia");
            }

            roomItemList.getChildren().add(roomItem.getContentPane());
        }
    }

    public void show() {
        Scene scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.setTitle("SIPERU - Pilih Ruangan");
        stage.show();

        // Animasi fade in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}
