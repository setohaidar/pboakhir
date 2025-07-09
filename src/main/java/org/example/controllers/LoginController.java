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

        // Membuat layout utama yang responsif
        BorderPane root = new BorderPane();

        // Background gradient yang lebih modern dengan animasi
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(0.3, Color.web("#764ba2")),
                new Stop(0.7, Color.web("#f093fb")),
                new Stop(1, Color.web("#f5576c"))
        );

        BackgroundFill backgroundFill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        // Container untuk form login dengan responsivitas
        VBox loginContainer = new VBox(30);
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setPadding(new Insets(50));
        loginContainer.setMaxWidth(450);
        loginContainer.setMinWidth(400);
        loginContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.98);" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 25, 0, 0, 15);"
        );

        // Logo dan judul yang lebih menarik
        FontIcon logoIcon = new FontIcon(FontAwesomeSolid.BUILDING);
        logoIcon.setIconSize(80);
        logoIcon.setIconColor(Color.web("#667eea"));

        Label titleLabel = new Label("SIPERU");
        titleLabel.setStyle("-fx-font-size: 38px; -fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-font-family: 'Segoe UI', Arial, sans-serif;");

        Label subtitleLabel = new Label("Sistem Peminjaman Ruangan");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-font-family: 'Segoe UI', Arial, sans-serif;");

        Label versionLabel = new Label("v1.0");
        versionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #bdc3c7; -fx-font-style: italic;");

        VBox headerBox = new VBox(15, logoIcon, titleLabel, subtitleLabel, versionLabel);
        headerBox.setAlignment(Pos.CENTER);

        // Form input dengan styling yang lebih modern
        VBox formBox = new VBox(20);
        formBox.setAlignment(Pos.CENTER);

        // Email field dengan efek modern
        VBox emailContainer = new VBox(8);
        Label emailLabel = new Label("Email Address");
        emailLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        HBox emailBox = new HBox(15);
        emailBox.setAlignment(Pos.CENTER_LEFT);
        emailBox.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 15;" +
                        "-fx-border-color: #e9ecef;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 2;"
        );

        FontIcon emailIcon = new FontIcon(FontAwesomeSolid.ENVELOPE);
        emailIcon.setIconSize(18);
        emailIcon.setIconColor(Color.web("#6c757d"));

        TextField emailField = new TextField();
        emailField.setPromptText("Masukkan email Anda");
        emailField.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );
        emailField.setPrefWidth(320);

        // Focus effect untuk email field
        emailField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                emailBox.setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-background-radius: 12;" +
                                "-fx-padding: 15;" +
                                "-fx-border-color: #667eea;" +
                                "-fx-border-radius: 12;" +
                                "-fx-border-width: 2;" +
                                "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.25), 10, 0, 0, 0);"
                );
            } else {
                emailBox.setStyle(
                        "-fx-background-color: #f8f9fa;" +
                                "-fx-background-radius: 12;" +
                                "-fx-padding: 15;" +
                                "-fx-border-color: #e9ecef;" +
                                "-fx-border-radius: 12;" +
                                "-fx-border-width: 2;"
                );
            }
        });

        emailBox.getChildren().addAll(emailIcon, emailField);
        emailContainer.getChildren().addAll(emailLabel, emailBox);

        // Password field dengan styling serupa
        VBox passwordContainer = new VBox(8);
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        HBox passwordBox = new HBox(15);
        passwordBox.setAlignment(Pos.CENTER_LEFT);
        passwordBox.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 15;" +
                        "-fx-border-color: #e9ecef;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 2;"
        );

        FontIcon passwordIcon = new FontIcon(FontAwesomeSolid.LOCK);
        passwordIcon.setIconSize(18);
        passwordIcon.setIconColor(Color.web("#6c757d"));

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password Anda");
        passwordField.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );
        passwordField.setPrefWidth(320);

        // Focus effect untuk password field
        passwordField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                passwordBox.setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-background-radius: 12;" +
                                "-fx-padding: 15;" +
                                "-fx-border-color: #667eea;" +
                                "-fx-border-radius: 12;" +
                                "-fx-border-width: 2;" +
                                "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.25), 10, 0, 0, 0);"
                );
            } else {
                passwordBox.setStyle(
                        "-fx-background-color: #f8f9fa;" +
                                "-fx-background-radius: 12;" +
                                "-fx-padding: 15;" +
                                "-fx-border-color: #e9ecef;" +
                                "-fx-border-radius: 12;" +
                                "-fx-border-width: 2;"
                );
            }
        });

        passwordBox.getChildren().addAll(passwordIcon, passwordField);
        passwordContainer.getChildren().addAll(passwordLabel, passwordBox);

        // Tombol login yang lebih modern dengan gradient
        Button loginButton = new Button("MASUK");
        FontIcon loginIcon = new FontIcon(FontAwesomeSolid.SIGN_IN_ALT);
        loginIcon.setIconSize(16);
        loginIcon.setIconColor(Color.WHITE);
        loginButton.setGraphic(loginIcon);

        loginButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-padding: 18 50;" +
                        "-fx-background-radius: 30;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 15, 0, 0, 8);"
        );
        loginButton.setPrefWidth(350);

        // Efek hover yang smooth untuk tombol
        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #5a6fd8, #6a4190);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                            "-fx-padding: 18 50;" +
                            "-fx-background-radius: 30;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.6), 20, 0, 0, 12);" +
                            "-fx-scale-x: 1.02;" +
                            "-fx-scale-y: 1.02;"
            );
        });

        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                            "-fx-padding: 18 50;" +
                            "-fx-background-radius: 30;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.4), 15, 0, 0, 8);"
            );
        });

        // Separator
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #ecf0f1;");
        separator.setPrefWidth(350);

        // Informasi akun demo dengan styling yang lebih baik
        Label demoLabel = new Label("📋 Akun Demo untuk Testing");
        demoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox demoContainer = new VBox(8);
        demoContainer.setAlignment(Pos.CENTER);
        demoContainer.setPadding(new Insets(15));
        demoContainer.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #dee2e6;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 1;"
        );

        Label[] demoAccounts = {
                new Label("👤 Peminjam: john.doe@example.com / password123"),
                new Label("🔧 Admin: admin@example.com / admin123"),
                new Label("🧹 Cleaning: cleaning@example.com / clean123")
        };

        for (Label account : demoAccounts) {
            account.setStyle("-fx-font-size: 12px; -fx-text-fill: #495057; -fx-font-family: 'Consolas', monospace;");
        }

        demoContainer.getChildren().addAll(demoAccounts);

        formBox.getChildren().addAll(
                emailContainer, 
                passwordContainer, 
                loginButton, 
                separator, 
                demoLabel, 
                demoContainer
        );

        loginContainer.getChildren().addAll(headerBox, formBox);

        // Menempatkan container di tengah dengan responsivitas
        StackPane centerPane = new StackPane(loginContainer);
        centerPane.setAlignment(Pos.CENTER);
        centerPane.setPadding(new Insets(30));
        root.setCenter(centerPane);

        // Animasi fade in yang smooth
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), loginContainer);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Scene dan stage dengan konfigurasi window yang lebih baik
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("SIPERU - Sistem Peminjaman Ruangan");
        
        // ENABLE WINDOW CONTROLS - maximize, minimize, resize
        stage.setResizable(true);
        stage.setMaximized(false);
        
        // Set minimum window size
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        
        stage.show();
        fadeIn.play();

        // Event handling untuk login dengan loading state
        Runnable loginAction = () -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showModernAlert(Alert.AlertType.WARNING, "Peringatan", "Email dan password tidak boleh kosong!");
                return;
            }

            // Animasi loading yang lebih smooth
            loginButton.setText("MEMPROSES...");
            loginButton.setDisable(true);
            loginIcon.setIconLiteral("fas-spinner");
            
            // Simulasi delay untuk efek loading
            new Thread(() -> {
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                javafx.application.Platform.runLater(() -> {
                    User user = usersModel.getUserByCredentials(email, password);

                    if (user == null) {
                        showModernAlert(Alert.AlertType.ERROR, "Login Gagal", 
                            "Email atau password salah! Silakan coba lagi atau gunakan akun demo.");
                        loginButton.setText("MASUK");
                        loginButton.setDisable(false);
                        loginIcon.setIconLiteral("fas-sign-in-alt");
                    } else {
                        // Success animation
                        loginButton.setText("BERHASIL!");
                        loginIcon.setIconLiteral("fas-check");
                        
                        // Delay sebelum pindah ke dashboard
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), loginContainer);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.setOnFinished(ev -> {
                            new DashboardController(user, stage, roomsModel, reservationsModel);
                        });
                        fadeOut.play();
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

        // Styling untuk alert yang lebih modern
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 10);" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        // Styling untuk button di alert
        dialogPane.getButtonTypes().forEach(buttonType -> {
            Button button = (Button) dialogPane.lookupButton(buttonType);
            if (button != null) {
                button.setStyle(
                        "-fx-background-color: #667eea;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 12px;" +
                                "-fx-padding: 8 20;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;"
                );
            }
        });

        alert.showAndWait();
    }
}
