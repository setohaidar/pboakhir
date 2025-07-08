package org.example.views;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.data.Roles;
import org.example.data.User;

public class Dashboard extends Application {

    private User user;

    public Dashboard(User user) {
        this.user = user;
    }

    public Dashboard() {
        // default constructor for launch
    }

    @Override
    public void start(Stage primaryStage) {
        // Simulasi user login
        user = new User(1, "John Doe", "john.doe@wemail.you", Roles.LOANER);

        // Membuat root pane dengan gradasi
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Background gradasi
        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(
                        0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#43cea2")),
                        new Stop(1, Color.web("#185a9d"))
                ),
                CornerRadii.EMPTY, Insets.EMPTY
        );
        root.setBackground(new Background(backgroundFill));

        // Welcome message
        Label welcomeMessage = new Label("Selamat datang, " + user.name() + " (" + user.role().getRoleName() + ")!");
        welcomeMessage.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Buttons
        Button btnAddReservation = new Button("Tambah Reservasi");
        Button btnReservationHistory = new Button("Riwayat Reservasi");
        Button btnReservationManagement = new Button("Manajemen Reservasi");
        Button btnRoomUseSchedule = new Button("Jadwal Penggunaan Ruangan");

        // Styling button
        String buttonStyle = "-fx-background-color: #ffffff; -fx-text-fill: #333333; -fx-font-size: 14px; -fx-padding: 10 20 10 20; -fx-background-radius: 8;";
        btnAddReservation.setStyle(buttonStyle);
        btnReservationHistory.setStyle(buttonStyle);
        btnReservationManagement.setStyle(buttonStyle);
        btnRoomUseSchedule.setStyle(buttonStyle);

        // Set button visibility based on role
        switch (user.role()) {
            case LOANER -> {
                root.getChildren().addAll(btnAddReservation, btnReservationHistory);
            }
            case ADMIN_STAFF -> {
                root.getChildren().add(btnReservationManagement);
            }
            case CLEANING_STAFF -> {
                root.getChildren().add(btnRoomUseSchedule);
            }
        }

        root.getChildren().add(0, welcomeMessage);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("Dashboard JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
