package org.example.views;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        // Background gradient yang konsisten dengan design baru
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(0.3, Color.web("#764ba2")),
                new Stop(0.7, Color.web("#f093fb")),
                new Stop(1, Color.web("#f5576c"))
        );

        BackgroundFill backgroundFill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        setupHeader();
        setupContent();
    }

    private void setupHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(25, 40, 25, 40));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Title dengan icon yang lebih modern
        FontIcon roomIcon = new FontIcon(FontAwesomeSolid.DOOR_OPEN);
        roomIcon.setIconSize(28);
        roomIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("Pilih Ruangan");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        Label subtitleLabel = new Label("Ruangan Tersedia");
        subtitleLabel.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.8);" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.getChildren().addAll(
                new HBox(10, roomIcon, titleLabel),
                subtitleLabel
        );

        // Return button dengan styling modern
        returnButton = createModernButton("Kembali", FontAwesomeSolid.ARROW_LEFT, "#6c757d");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(titleBox, spacer, returnButton);
        root.setTop(header);
    }

    private Button createModernButton(String text, FontAwesomeSolid icon, String color) {
        Button button = new Button(text);

        FontIcon buttonIcon = new FontIcon(icon);
        buttonIcon.setIconSize(14);
        buttonIcon.setIconColor(Color.WHITE);
        button.setGraphic(buttonIcon);

        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 25;" +
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
                            "-fx-background-radius: 25;" +
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
                            "-fx-background-radius: 25;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);"
            );
        });

        return button;
    }

    private void setupContent() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40, 60, 40, 60));
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Time panel dengan design yang lebih modern
        timePanel = createTimePanel();
        
        // Room list container dengan modern styling
        VBox roomSection = createRoomSection();

        mainContent.getChildren().addAll(timePanel, roomSection);
        scrollPane.setContent(mainContent);
        root.setCenter(scrollPane);
    }

    private VBox createTimePanel() {
        VBox panel = new VBox(15);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(25));
        panel.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );
        panel.setVisible(false);

        // Header untuk time panel
        FontIcon timeIcon = new FontIcon(FontAwesomeSolid.CLOCK);
        timeIcon.setIconSize(30);
        timeIcon.setIconColor(Color.web("#3498db"));

        Label headerLabel = new Label("Waktu Reservasi");
        headerLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        VBox timeContainer = new VBox(10);
        timeContainer.setAlignment(Pos.CENTER);

        startTime = new Label();
        startTime.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #27ae60;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        endTime = new Label();
        endTime.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #e74c3c;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        timeContainer.getChildren().addAll(startTime, endTime);
        panel.getChildren().addAll(timeIcon, headerLabel, timeContainer);
        
        return panel;
    }

    private VBox createRoomSection() {
        VBox section = new VBox(20);
        section.setAlignment(Pos.CENTER);

        // Section header
        Label sectionTitle = new Label("Ruangan Tersedia");
        sectionTitle.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );

        // Room list container
        roomItemList = new VBox(20);
        roomItemList.setAlignment(Pos.CENTER);
        roomItemList.setPadding(new Insets(20));

        section.getChildren().addAll(sectionTitle, roomItemList);
        return section;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public BorderPane getContentPane() {
        return root;
    }

    public void setTime(Date start, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm");
        startTime.setText("📅 Mulai: " + sdf.format(start));
        endTime.setText("🕐 Selesai: " + sdf.format(end));
        
        timePanel.setVisible(true);
        
        // Smooth fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), timePanel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void displayRoomItemsWithCreateReservationButton(List<ReservationsByRoom> reservationGroups, ActionListenerWithRoom actionListenerWithRoom) {
        reservationGroups.sort((a, b) -> a.room().name().compareToIgnoreCase(b.room().name()));
        roomItemList.getChildren().clear();

        if (reservationGroups.isEmpty()) {
            showNoRoomsMessage();
            return;
        }

        for (ReservationsByRoom reservationGroup : reservationGroups) {
            VBox roomCard = createRoomCard(reservationGroup, actionListenerWithRoom);
            roomItemList.getChildren().add(roomCard);
        }

        // Animate room cards
        for (int i = 0; i < roomItemList.getChildren().size(); i++) {
            javafx.scene.Node card = roomItemList.getChildren().get(i);
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(400 + (i * 100)), card);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
    }

    private void showNoRoomsMessage() {
        VBox messageContainer = new VBox(15);
        messageContainer.setAlignment(Pos.CENTER);
        messageContainer.setPadding(new Insets(40));
        messageContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );

        FontIcon sadIcon = new FontIcon(FontAwesomeSolid.FROWN);
        sadIcon.setIconSize(50);
        sadIcon.setIconColor(Color.web("#95a5a6"));

        Label messageLabel = new Label("Tidak Ada Ruangan Tersedia");
        messageLabel.setStyle(
                "-fx-text-fill: #2c3e50;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        Label infoLabel = new Label("Silakan pilih waktu yang berbeda atau coba lagi nanti");
        infoLabel.setStyle(
                "-fx-text-fill: #7f8c8d;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-text-alignment: center;"
        );

        messageContainer.getChildren().addAll(sadIcon, messageLabel, infoLabel);
        roomItemList.getChildren().add(messageContainer);
    }

    private VBox createRoomCard(ReservationsByRoom reservationGroup, ActionListenerWithRoom actionListenerWithRoom) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 8);" +
                        "-fx-cursor: hand;"
        );
        card.setMaxWidth(600);

        // Room info
        HBox roomInfo = new HBox(15);
        roomInfo.setAlignment(Pos.CENTER_LEFT);

        FontIcon roomIcon = new FontIcon(FontAwesomeSolid.DOOR_OPEN);
        roomIcon.setIconSize(24);
        roomIcon.setIconColor(Color.web("#3498db"));

        VBox roomDetails = new VBox(5);
        
        Label roomName = new Label(reservationGroup.room().name());
        roomName.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        Label roomCapacity = new Label("Kapasitas: " + reservationGroup.room().capacity() + " orang");
        roomCapacity.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-text-fill: #7f8c8d;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        roomDetails.getChildren().addAll(roomName, roomCapacity);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Status dan action button
        VBox actionArea = new VBox(8);
        actionArea.setAlignment(Pos.CENTER_RIGHT);

        // Cek status ketersediaan
        boolean isAccepted = reservationGroup.reservations() != null &&
                !reservationGroup.reservations().isEmpty() &&
                reservationGroup.reservations().stream()
                        .anyMatch(r -> r.confirmationStatus() == ConfirmationStatus.ACCEPTED);

        Button actionButton;
        if (isAccepted) {
            actionButton = createModernButton("Tidak Tersedia", FontAwesomeSolid.BAN, "#e74c3c");
            actionButton.setDisable(true);
            
            Label statusLabel = new Label("❌ Sudah Dipesan");
            statusLabel.setStyle(
                    "-fx-font-size: 11px;" +
                            "-fx-text-fill: #e74c3c;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
            );
            actionArea.getChildren().add(statusLabel);
        } else {
            actionButton = createModernButton("Pilih Ruangan", FontAwesomeSolid.CHECK_CIRCLE, "#27ae60");
            actionButton.setOnAction(e -> actionListenerWithRoom.onAction(reservationGroup.room(), null));
            
            Label statusLabel = new Label("✅ Tersedia");
            statusLabel.setStyle(
                    "-fx-font-size: 11px;" +
                            "-fx-text-fill: #27ae60;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
            );
            actionArea.getChildren().add(statusLabel);
        }

        actionArea.getChildren().add(actionButton);

        roomInfo.getChildren().addAll(roomIcon, roomDetails, spacer, actionArea);
        card.getChildren().add(roomInfo);

        // Hover animation
        card.setOnMouseEntered(e -> {
            if (!isAccepted) {
                ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
                scale.setToX(1.03);
                scale.setToY(1.03);
                scale.play();

                card.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 25, 0, 0, 15);" +
                                "-fx-cursor: hand;"
                );
            }
        });

        card.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            card.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                            "-fx-background-radius: 15;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 8);" +
                            "-fx-cursor: hand;"
            );
        });

        return card;
    }

    public void show() {
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("SIPERU - Pilih Ruangan");
        
        // ENABLE WINDOW CONTROLS - maximize, minimize, resize
        stage.setResizable(true);
        stage.setMaximized(false);
        
        // Set minimum window size
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        stage.show();

        // Animasi fade in yang smooth
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}
