package org.example.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import org.example.data.*;
import org.example.models.ReservationsModel;
import org.example.models.RoomsModel;
import org.example.models.UsersModel;
import org.example.views.RoomList;
import org.example.views.TimePickerView;
import org.example.views.dialogs.NotificationDialog;
import org.example.views.dialogs.PurposeModal;
import org.example.views.dialogs.ReservationList;
import org.example.views.dialogs.StatisticsDialog;
import org.example.AppDataSource;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DashboardController {

    private final User user;
    private final Stage stage;
    private final BorderPane root;
    private final RoomsModel roomsModel;
    private final ReservationsModel reservationsModel;

    public DashboardController(User user, Stage stage, RoomsModel roomsModel, ReservationsModel reservationsModel) {
        this.user = user;
        this.stage = stage;
        this.roomsModel = roomsModel;
        this.reservationsModel = reservationsModel;

        root = new BorderPane();

        // Background gradient yang lebih modern dan konsisten dengan login
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
        setupMainContent();

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("SIPERU - Dashboard (" + user.role().getRoleName() + ")");
        
        // ENABLE WINDOW CONTROLS - maximize, minimize, resize
        stage.setResizable(true);
        stage.setMaximized(false);
        
        // Set minimum window size
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        
        stage.show();

        // Animasi fade in yang smooth
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // Show welcome notification
        NotificationDialog.show(
                "Selamat Datang!",
                "Halo " + user.name() + ", selamat datang di SIPERU!",
                NotificationDialog.NotificationType.SUCCESS
        );
    }

    private void setupHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(25, 40, 25, 40));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Logo dan title dengan styling yang konsisten
        FontIcon logoIcon = new FontIcon(FontAwesomeSolid.BUILDING);
        logoIcon.setIconSize(28);
        logoIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("SIPERU");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        Label subtitleLabel = new Label("Dashboard");
        subtitleLabel.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.8);" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        VBox logoBox = new VBox(5);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.getChildren().addAll(
                new HBox(10, logoIcon, titleLabel),
                subtitleLabel
        );

        // User info dengan styling yang lebih baik
        FontIcon userIcon = new FontIcon(FontAwesomeSolid.USER_CIRCLE);
        userIcon.setIconSize(20);
        userIcon.setIconColor(Color.WHITE);

        Label userLabel = new Label("Halo, " + user.name());
        userLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        Label roleLabel = new Label(user.role().getRoleName());
        roleLabel.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.8);" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        // Badge untuk role
        Label roleBadge = new Label(user.role().getRoleName().toUpperCase());
        roleBadge.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 10px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 4 8;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        Button logoutButton = createModernButton("Keluar", FontAwesomeSolid.SIGN_OUT_ALT, "#e74c3c");
        logoutButton.setOnAction(e -> handleLogout());

        VBox userInfo = new VBox(5);
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        userInfo.getChildren().addAll(
                new HBox(8, userIcon, userLabel),
                roleBadge
        );

        HBox rightBox = new HBox(20, userInfo, logoutButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(logoBox, spacer, rightBox);
        root.setTop(header);
    }

    private void handleLogout() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Logout");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Apakah Anda yakin ingin keluar dari sistem?");

        // Modern alert styling
        DialogPane dialogPane = confirmAlert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 10);" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Fade out animation sebelum kembali ke login
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), root);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                new LoginController(
                        new UsersModel(AppDataSource.getInstance()),
                        roomsModel,
                        reservationsModel,
                        stage
                );
            });
            fadeOut.play();
        }
    }

    private void setupMainContent() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox mainContent = new VBox(40);
        mainContent.setPadding(new Insets(40, 60, 40, 60));
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Welcome section dengan styling yang lebih baik
        VBox welcomeSection = createWelcomeSection();
        
        // Quick stats dengan cards yang lebih modern
        HBox statsSection = createQuickStats();

        // Menu cards dengan layout yang responsif
        VBox menuSection = createMenuSection();

        mainContent.getChildren().addAll(welcomeSection, statsSection, menuSection);
        scrollPane.setContent(mainContent);
        root.setCenter(scrollPane);
    }

    private VBox createWelcomeSection() {
        VBox welcomeSection = new VBox(15);
        welcomeSection.setAlignment(Pos.CENTER);

        // Icon untuk role
        FontIcon roleIcon = getRoleIcon();
        roleIcon.setIconSize(50);
        roleIcon.setIconColor(Color.WHITE);

        Label welcomeLabel = new Label("Selamat Datang, " + user.name() + "!");
        welcomeLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );

        Label descLabel = new Label(getRoleDescription());
        descLabel.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.9);" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        // Current time
        Label timeLabel = new Label("📅 " + java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm")
        ));
        timeLabel.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.8);" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        welcomeSection.getChildren().addAll(roleIcon, welcomeLabel, descLabel, timeLabel);
        return welcomeSection;
    }

    private FontIcon getRoleIcon() {
        return switch (user.role()) {
            case LOANER -> new FontIcon(FontAwesomeSolid.USER);
            case ADMIN_STAFF -> new FontIcon(FontAwesomeSolid.USER_TIE);
            case CLEANING_STAFF -> new FontIcon(FontAwesomeSolid.BROOM);
        };
    }

    private String getRoleDescription() {
        return switch (user.role()) {
            case LOANER -> "Kelola reservasi ruangan dengan mudah dan efisien";
            case ADMIN_STAFF -> "Kelola dan setujui semua pengajuan reservasi";
            case CLEANING_STAFF -> "Pantau jadwal pembersihan ruangan";
        };
    }

    private HBox createQuickStats() {
        HBox statsBox = new HBox(30);
        statsBox.setAlignment(Pos.CENTER);

        // Total reservasi hari ini
        int todayReservations = getTodayReservationsCount();
        VBox todayStats = createStatCard("Reservasi Hari Ini", String.valueOf(todayReservations), "#3498db", FontAwesomeSolid.CALENDAR_DAY);

        if (user.role() == Roles.ADMIN_STAFF) {
            // Pending reservasi untuk admin
            int pendingCount = reservationsModel.getAllPendingReservations().size();
            VBox pendingStats = createStatCard("Menunggu Persetujuan", String.valueOf(pendingCount), "#f39c12", FontAwesomeSolid.CLOCK);
            
            // Total ruangan
            int totalRooms = roomsModel.getAllRooms().size();
            VBox roomStats = createStatCard("Total Ruangan", String.valueOf(totalRooms), "#2ecc71", FontAwesomeSolid.DOOR_OPEN);
            
            statsBox.getChildren().addAll(todayStats, pendingStats, roomStats);
        } else {
            // User reservasi untuk loaner
            int userReservations = reservationsModel.getReservationsByUserId(user.id()).size();
            VBox userStats = createStatCard("Total Reservasi Saya", String.valueOf(userReservations), "#2ecc71", FontAwesomeSolid.LIST_ALT);
            
            // Reservasi aktif
            int activeReservations = (int) reservationsModel.getReservationsByUserId(user.id()).stream()
                    .filter(r -> r.startTime().after(new java.util.Date()))
                    .count();
            VBox activeStats = createStatCard("Reservasi Aktif", String.valueOf(activeReservations), "#9b59b6", FontAwesomeSolid.PLAY_CIRCLE);
            
            statsBox.getChildren().addAll(todayStats, userStats, activeStats);
        }

        return statsBox;
    }

    private VBox createStatCard(String title, String value, String color, FontAwesomeSolid icon) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(220, 120);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 8);"
        );

        FontIcon cardIcon = new FontIcon(icon);
        cardIcon.setIconSize(30);
        cardIcon.setIconColor(Color.web(color));

        Label valueLabel = new Label(value);
        valueLabel.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: " + color + ";" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #7f8c8d;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-text-alignment: center;"
        );
        titleLabel.setWrapText(true);

        card.getChildren().addAll(cardIcon, valueLabel, titleLabel);

        // Hover animation
        card.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        card.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return card;
    }

    private int getTodayReservationsCount() {
        // Implementasi sederhana - bisa diperbaiki dengan query database yang lebih spesifik
        LocalDate today = LocalDate.now();
        return (int) reservationsModel.getReservationsByUserId(user.id()).stream()
                .filter(r -> {
                    LocalDate reservationDate = r.startTime().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return reservationDate.equals(today);
                })
                .count();
    }

    private VBox createMenuSection() {
        VBox menuSection = new VBox(25);
        menuSection.setAlignment(Pos.CENTER);

        Label menuTitle = new Label("Menu Utama");
        menuTitle.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        // Grid layout yang responsif untuk menu cards
        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(25);
        menuGrid.setVgap(25);
        menuGrid.setAlignment(Pos.CENTER);

        // Add menu cards berdasarkan role
        int col = 0;
        int row = 0;
        final int maxCols = 3;

        switch (user.role()) {
            case LOANER:
                menuGrid.add(createMenuCard("Buat Reservasi", "Ajukan peminjaman ruangan baru",
                        FontAwesomeSolid.PLUS_CIRCLE, "#3498db", this::openAddReservation), col++, row);
                menuGrid.add(createMenuCard("Riwayat Reservasi", "Lihat semua reservasi Anda",
                        FontAwesomeSolid.HISTORY, "#2ecc71", this::openReservationHistory), col++, row);
                menuGrid.add(createMenuCard("Panduan Sistem", "Cara menggunakan SIPERU",
                        FontAwesomeSolid.QUESTION_CIRCLE, "#f39c12", this::showGuide), col++, row);
                break;
            case ADMIN_STAFF:
                menuGrid.add(createMenuCard("Kelola Reservasi", "Setujui atau tolak reservasi",
                        FontAwesomeSolid.TASKS, "#e74c3c", this::openReservationManagement), col++, row);
                menuGrid.add(createMenuCard("Semua Reservasi", "Lihat semua reservasi sistem",
                        FontAwesomeSolid.LIST, "#9b59b6", this::showAllReservations), col++, row);
                menuGrid.add(createMenuCard("Statistik Sistem", "Lihat statistik penggunaan",
                        FontAwesomeSolid.CHART_BAR, "#1abc9c", this::showStatistics), col++, row);
                break;
            case CLEANING_STAFF:
                menuGrid.add(createMenuCard("Jadwal Pembersihan", "Lihat ruangan yang perlu dibersihkan",
                        FontAwesomeSolid.CALENDAR_ALT, "#1abc9c", this::openRoomUseSchedule), col++, row);
                menuGrid.add(createMenuCard("Riwayat Pembersihan", "Lihat riwayat pembersihan",
                        FontAwesomeSolid.CHECK_CIRCLE, "#27ae60", this::showCleaningHistory), col++, row);
                break;
        }

        menuSection.getChildren().addAll(menuTitle, menuGrid);
        return menuSection;
    }

    private VBox createMenuCard(String title, String description, FontAwesomeSolid icon, String color, Runnable action) {
        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(280, 200);
        card.setMaxWidth(280);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );

        // Icon dengan background circle
        StackPane iconContainer = new StackPane();
        iconContainer.setPrefSize(80, 80);
        iconContainer.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 40;"
        );

        FontIcon cardIcon = new FontIcon(icon);
        cardIcon.setIconSize(35);
        cardIcon.setIconColor(Color.WHITE);
        iconContainer.getChildren().add(cardIcon);

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-text-alignment: center;"
        );
        titleLabel.setWrapText(true);

        // Description
        Label descLabel = new Label(description);
        descLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #7f8c8d;" +
                        "-fx-text-alignment: center;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );
        descLabel.setWrapText(true);

        card.getChildren().addAll(iconContainer, titleLabel, descLabel);

        // Enhanced hover animation
        card.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.08);
            scale.setToY(1.08);
            scale.play();

            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 20;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 30, 0, 0, 15);"
            );
        });

        card.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            card.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                            "-fx-background-radius: 20;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
            );
        });

        card.setOnMouseClicked(e -> action.run());

        return card;
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

    private void openAddReservation() {
        TimePickerView timePickerView = new TimePickerView(stage);

        timePickerView.getBackButton().setOnAction(e -> {
            new DashboardController(user, stage, roomsModel, reservationsModel);
        });

        timePickerView.getContinueButton().setOnAction(e -> {
            LocalDate startDate = timePickerView.getStartDatePicker().getValue();
            String startTimeStr = timePickerView.getStartTimeComboBox().getValue();
            LocalDate endDate = timePickerView.getEndDatePicker().getValue();
            String endTimeStr = timePickerView.getEndTimeComboBox().getValue();

            if (startDate == null || startTimeStr == null || endDate == null || endTimeStr == null) {
                NotificationDialog.show("Peringatan", "Semua kolom harus diisi!",
                        NotificationDialog.NotificationType.WARNING);
                return;
            }

            LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.parse(startTimeStr));
            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.parse(endTimeStr));

            if (startDateTime.isAfter(endDateTime)) {
                NotificationDialog.show("Error", "Waktu mulai tidak boleh setelah waktu selesai!",
                        NotificationDialog.NotificationType.ERROR);
                return;
            }

            if (startDateTime.isBefore(LocalDateTime.now())) {
                NotificationDialog.show("Error", "Tidak dapat membuat reservasi untuk waktu yang sudah lewat!",
                        NotificationDialog.NotificationType.ERROR);
                return;
            }

            Date startTime = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date endTime = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

            showAvailableRooms(startTime, endTime);
        });

        timePickerView.show();
    }

    private void showAvailableRooms(Date startTime, Date endTime) {
        List<Room> allRooms = roomsModel.getAllRooms();
        List<Reservation> overlappingReservations = reservationsModel.getAllReservationsByStartAndEndTime(startTime, endTime);
        List<Integer> reservedRoomIds = overlappingReservations.stream()
                .map(r -> r.room().id())
                .distinct()
                .toList();
        List<Room> availableRooms = allRooms.stream()
                .filter(room -> !reservedRoomIds.contains(room.id()))
                .collect(Collectors.toList());

        RoomList roomListView = new RoomList(stage);
        roomListView.getReturnButton().setOnAction(e -> {
            new DashboardController(user, stage, roomsModel, reservationsModel);
        });

        List<ReservationsByRoom> availableRoomsForDisplay = availableRooms.stream()
                .map(room -> new ReservationsByRoom(room, new ArrayList<>()))
                .collect(Collectors.toList());

        roomListView.displayRoomItemsWithCreateReservationButton(availableRoomsForDisplay, (room, event) -> {
            openPurposeDialog(room, startTime, endTime);
        });

        roomListView.setTime(startTime, endTime);
        roomListView.show();
    }

    private void openPurposeDialog(Room selectedRoom, Date startTime, Date endTime) {
        Optional<String> result = PurposeModal.showAndGetPurpose(selectedRoom.name());
        result.ifPresent(purpose -> {
            if (purpose.trim().isEmpty()) {
                NotificationDialog.show("Peringatan", "Tujuan reservasi tidak boleh kosong!",
                        NotificationDialog.NotificationType.WARNING);
            } else {
                Reservation newReservation = new Reservation(
                        null,
                        selectedRoom,
                        user,
                        purpose,
                        startTime,
                        endTime,
                        new Date(),
                        null
                );
                boolean success = reservationsModel.addReservation(newReservation);
                if (success) {
                    NotificationDialog.show("Berhasil",
                            "Reservasi untuk ruangan " + selectedRoom.name() + " berhasil diajukan!",
                            NotificationDialog.NotificationType.SUCCESS);
                    new DashboardController(user, stage, roomsModel, reservationsModel);
                } else {
                    NotificationDialog.show("Error", "Gagal mengajukan reservasi!",
                            NotificationDialog.NotificationType.ERROR);
                }
            }
        });
    }

    private void openReservationManagement() {
        List<Reservation> pendingReservations = reservationsModel.getAllPendingReservations();
        if (pendingReservations.isEmpty()) {
            NotificationDialog.show("Informasi",
                    "Tidak ada pengajuan reservasi yang perlu dikonfirmasi.",
                    NotificationDialog.NotificationType.INFO);
            return;
        }

        ReservationList reservationListView = new ReservationList(pendingReservations);
        reservationListView.setTitle("Kelola Reservasi");
        reservationListView.setAction("Kelola Reservasi", () -> {
            Reservation selected = reservationListView.getSelectedReservation();
            if (selected != null) {
                reservationListView.close();
                showConfirmationDialog(selected);
            } else {
                NotificationDialog.show("Peringatan",
                        "Silakan pilih salah satu reservasi dari tabel!",
                        NotificationDialog.NotificationType.WARNING);
            }
        });

        Button backButton = createModernButton("Kembali ke Dashboard", FontAwesomeSolid.ARROW_LEFT, "#6c757d");
        backButton.setOnAction(e -> {
            reservationListView.close();
            new DashboardController(user, stage, roomsModel, reservationsModel);
        });

        reservationListView.addBackButton(backButton);
        reservationListView.show();
    }

    private void showConfirmationDialog(Reservation reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Reservasi");
        alert.setHeaderText("Kelola reservasi untuk: " + reservation.user().name() + " - Ruang " + reservation.room().name());
        alert.setContentText("Pilih aksi untuk reservasi ini:");

        ButtonType buttonTypeApprove = new ButtonType("Setujui");
        ButtonType buttonTypeReject = new ButtonType("Tolak");
        ButtonType buttonTypeCancel = new ButtonType("Batal", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeApprove, buttonTypeReject, buttonTypeCancel);

        alert.showAndWait().ifPresent(type -> {
            boolean success = false;
            String action = "";
            if (type == buttonTypeApprove) {
                success = reservationsModel.confirmReservation(reservation.id(), ConfirmationStatus.ACCEPTED);
                action = "disetujui";
            } else if (type == buttonTypeReject) {
                success = reservationsModel.confirmReservation(reservation.id(), ConfirmationStatus.REJECTED);
                action = "ditolak";
            } else {
                openReservationManagement();
                return;
            }

            if (success) {
                NotificationDialog.show("Berhasil",
                        "Reservasi berhasil " + action + "!",
                        NotificationDialog.NotificationType.SUCCESS);
            } else {
                NotificationDialog.show("Error", "Gagal memperbarui status reservasi!",
                        NotificationDialog.NotificationType.ERROR);
            }
            openReservationManagement();
        });
    }

    // Ganti method openReservationHistory() dengan:
    private void openReservationHistory() {
        List<Reservation> reservations = reservationsModel.getReservationsByUserId(user.id());
        if (reservations.isEmpty()) {
            NotificationDialog.show("Informasi",
                    "Anda belum memiliki riwayat reservasi.",
                    NotificationDialog.NotificationType.INFO);
            return;
        }

        ReservationList reservationListView = new ReservationList(reservations);
        reservationListView.setTitle("Riwayat Reservasi Saya");
        reservationListView.getActionButton().setVisible(false);

        Button backButton = createModernButton("Kembali ke Dashboard", FontAwesomeSolid.ARROW_LEFT, "#6c757d");
        backButton.setOnAction(e -> {
            reservationListView.close();
            new DashboardController(user, stage, roomsModel, reservationsModel);
        });

        reservationListView.addBackButton(backButton);
        reservationListView.show();
    }

    // Ganti method showAllReservations() dengan:
    private void showAllReservations() {
        List<Reservation> allReservations = reservationsModel.getAllReservations();
        if (allReservations.isEmpty()) {
            NotificationDialog.show("Informasi",
                    "Belum ada reservasi dalam sistem.",
                    NotificationDialog.NotificationType.INFO);
            return;
        }

        ReservationList reservationListView = new ReservationList(allReservations);
        reservationListView.setTitle("Semua Reservasi Sistem");
        reservationListView.getActionButton().setVisible(false);

        Button backButton = createModernButton("Kembali ke Dashboard", FontAwesomeSolid.ARROW_LEFT, "#6c757d");
        backButton.setOnAction(e -> {
            reservationListView.close();
            new DashboardController(user, stage, roomsModel, reservationsModel);
        });

        reservationListView.addBackButton(backButton);
        reservationListView.show();
    }

    // Ganti method openRoomUseSchedule() dengan:
    private void openRoomUseSchedule() {
        List<Reservation> reservationsToClean = reservationsModel.getAllCompletedReservationsForCleaning();
        if (reservationsToClean.isEmpty()) {
            NotificationDialog.show("Informasi",
                    "Tidak ada ruangan yang perlu dibersihkan saat ini.",
                    NotificationDialog.NotificationType.INFO);
            return;
        }

        ReservationList scheduleView = new ReservationList(reservationsToClean);
        scheduleView.setTitle("Jadwal Pembersihan Ruangan");
        scheduleView.setAction("Tandai Selesai Dibersihkan", () -> {
            Reservation selected = scheduleView.getSelectedReservation();
            if (selected != null) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Tandai reservasi untuk ruang " + selected.room().name() + " sebagai selesai dibersihkan?",
                        ButtonType.YES, ButtonType.NO);
                confirmAlert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        boolean success = reservationsModel.confirmReservation(selected.id(), ConfirmationStatus.NOT_CONFIRMED);
                        if (success) {
                            NotificationDialog.show("Berhasil",
                                    "Status ruangan berhasil diperbarui!",
                                    NotificationDialog.NotificationType.SUCCESS);
                        } else {
                            NotificationDialog.show("Error",
                                    "Gagal memperbarui status ruangan!",
                                    NotificationDialog.NotificationType.ERROR);
                        }
                        scheduleView.close();
                        openRoomUseSchedule();
                    }
                });
            } else {
                NotificationDialog.show("Peringatan",
                        "Silakan pilih salah satu jadwal dari tabel!",
                        NotificationDialog.NotificationType.WARNING);
            }
        });

        Button backButton = createModernButton("Kembali ke Dashboard", FontAwesomeSolid.ARROW_LEFT, "#6c757d");
        backButton.setOnAction(e -> {
            scheduleView.close();
            new DashboardController(user, stage, roomsModel, reservationsModel);
        });

        scheduleView.addBackButton(backButton);
        scheduleView.show();
    }

    // Fitur tambahan yang diperbaiki
    private void showGuide() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Panduan Penggunaan SIPERU");
        alert.setHeaderText("Cara Menggunakan Sistem");
        alert.setContentText(
                "📋 PANDUAN PEMINJAMAN RUANGAN:\n\n" +
                        "1️⃣ Klik 'Buat Reservasi' untuk mengajukan peminjaman\n" +
                        "2️⃣ Pilih tanggal dan waktu yang diinginkan\n" +
                        "3️⃣ Pilih ruangan yang tersedia\n" +
                        "4️⃣ Masukkan tujuan peminjaman dengan jelas\n" +
                        "5️⃣ Tunggu konfirmasi dari admin\n" +
                        "6️⃣ Lihat status di 'Riwayat Reservasi'\n\n" +
                        "⏰ Jam operasional: 07:00 - 18:00\n" +
                        "📅 Reservasi minimal H+1 dari hari ini\n" +
                        "✅ Pastikan tujuan peminjaman jelas dan spesifik"
        );

        // Styling untuk dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");

        alert.showAndWait();
    }

    // Ganti method showCleaningHistory() dengan:
    private void showCleaningHistory() {
        List<Reservation> cleanedReservations = reservationsModel.getCleanedReservations();
        if (cleanedReservations.isEmpty()) {
            NotificationDialog.show("Informasi",
                    "Belum ada riwayat pembersihan.",
                    NotificationDialog.NotificationType.INFO);
            return;
        }

        ReservationList historyView = new ReservationList(cleanedReservations);
        historyView.setTitle("Riwayat Pembersihan");
        historyView.getActionButton().setVisible(false);

        Button backButton = createModernButton("Kembali ke Dashboard", FontAwesomeSolid.ARROW_LEFT, "#6c757d");
        backButton.setOnAction(e -> {
            historyView.close();
            new DashboardController(user, stage, roomsModel, reservationsModel);
        });

        historyView.addBackButton(backButton);
        historyView.show();
    }

    private void showStatistics() {
        int[] reservationStats = reservationsModel.getReservationStatistics();
        List<String[]> roomUsageStats = reservationsModel.getRoomUsageStatistics();

        StatisticsDialog.show(stage, reservationStats, roomUsageStats);
    }
}
