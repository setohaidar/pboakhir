package org.example.controllers;

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
import org.example.data.User;
import org.example.models.ReservationsModel;
import org.example.models.RoomsModel;
import org.example.models.UsersModel;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

public class LoginController {

    private final UsersModel usersModel;
    private final RoomsModel roomsModel;
    private final ReservationsModel reservationsModel;

    public LoginController(UsersModel usersModel, RoomsModel roomsModel, ReservationsModel reservationsModel, Stage stage) {
        this.usersModel = usersModel;
        this.roomsModel = roomsModel;
        this.reservationsModel = reservationsModel;

        // Membuat layout utama
        BorderPane root = new BorderPane();

        // Background gradient yang modern
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(1, Color.web("#764ba2"))
        );

        BackgroundFill backgroundFill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        // Container untuk form login
        VBox loginContainer = new VBox(25);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setPadding(new Insets(40));
        loginContainer.setMaxWidth(400);
        loginContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;"
        );

        // Efek bayangan untuk container
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setOffsetX(0);
        shadow.setOffsetY(10);
        shadow.setRadius(20);
        loginContainer.setEffect(shadow);

        // Logo dan judul
        FontIcon logoIcon = new FontIcon(FontAwesomeSolid.BUILDING);
        logoIcon.setIconSize(60);
        logoIcon.setIconColor(Color.web("#667eea"));

        Label titleLabel = new Label("SIPERU");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label subtitleLabel = new Label("Sistem Peminjaman Ruangan");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        VBox headerBox = new VBox(10, logoIcon, titleLabel, subtitleLabel);
        headerBox.setAlignment(Pos.CENTER);

        // Form input
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);

        // Email field dengan icon
        HBox emailBox = new HBox(10);
        emailBox.setAlignment(Pos.CENTER_LEFT);
        emailBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-padding: 12;");

        FontIcon emailIcon = new FontIcon(FontAwesomeSolid.ENVELOPE);
        emailIcon.setIconSize(16);
        emailIcon.setIconColor(Color.web("#6c757d"));

        TextField emailField = new TextField();
        emailField.setPromptText("Masukkan email Anda");
        emailField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 14px;");
        emailField.setPrefWidth(300);

        emailBox.getChildren().addAll(emailIcon, emailField);

        // Password field dengan icon
        HBox passwordBox = new HBox(10);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        passwordBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-padding: 12;");

        FontIcon passwordIcon = new FontIcon(FontAwesomeSolid.LOCK);
        passwordIcon.setIconSize(16);
        passwordIcon.setIconColor(Color.web("#6c757d"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password Anda");
        passwordField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-font-size: 14px;");
        passwordField.setPrefWidth(300);

        passwordBox.getChildren().addAll(passwordIcon, passwordField);

        // Tombol login yang modern
        Button loginButton = new Button("MASUK");
        loginButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 40;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
        loginButton.setPrefWidth(320);

        // Efek hover untuk tombol
        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #5a6fd8, #6a4190);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 15 40;" +
                            "-fx-background-radius: 25;" +
                            "-fx-cursor: hand;"
            );
        });

        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 15 40;" +
                            "-fx-background-radius: 25;" +
                            "-fx-cursor: hand;"
            );
        });

        // Label untuk akun demo
        Label demoLabel = new Label("Akun Demo:");
        demoLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #495057;");

        Label demoInfo = new Label(
                "Peminjam: john.doe@example.com / password123\n" +
                        "Admin: admin@example.com / admin123\n" +
                        "Cleaning: cleaning@example.com / clean123"
        );
        demoInfo.setStyle("-fx-font-size: 11px; -fx-text-fill: #6c757d; -fx-text-alignment: center;");

        formBox.getChildren().addAll(emailBox, passwordBox, loginButton, demoLabel, demoInfo);

        loginContainer.getChildren().addAll(headerBox, formBox);

        // Menempatkan container di tengah
        StackPane centerPane = new StackPane(loginContainer);
        centerPane.setAlignment(Pos.CENTER);
        root.setCenter(centerPane);

        // Animasi fade in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), loginContainer);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Scene dan stage
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("SIPERU - Login");
        stage.setResizable(false);
        stage.show();

        fadeIn.play();

        // Event handling untuk login
        Runnable loginAction = () -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showModernAlert(Alert.AlertType.WARNING, "Peringatan", "Email dan password tidak boleh kosong!");
                return;
            }

            // Animasi loading
            loginButton.setText("MEMPROSES...");
            loginButton.setDisable(true);

            // Simulasi delay untuk efek loading
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                javafx.application.Platform.runLater(() -> {
                    User user = usersModel.getUserByCredentials(email, password);

                    if (user == null) {
                        showModernAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau password salah!");
                        loginButton.setText("MASUK");
                        loginButton.setDisable(false);
                    } else {
                        // Pindah ke Dashboard
                        new DashboardController(user, stage, roomsModel, reservationsModel);
                    }
                });
            }).start();
        };

        loginButton.setOnAction(e -> loginAction.run());

        // Enter key untuk login
        scene.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                loginAction.run();
            }
        });
    }

    private void showModernAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Styling untuk alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");

        alert.showAndWait();
    }
}
