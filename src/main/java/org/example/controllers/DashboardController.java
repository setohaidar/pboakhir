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

        // Background gradient yang modern
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(1, Color.web("#764ba2"))
        );

        BackgroundFill backgroundFill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        setupHeader();
        setupMainContent();

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("SIPERU - Dashboard");
        stage.show();

        // Animasi fade in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
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
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1);");

        // Logo dan title
        FontIcon logoIcon = new FontIcon(FontAwesomeSolid.BUILDING);
        logoIcon.setIconSize(24);
        logoIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("SIPERU");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox logoBox = new HBox(10, logoIcon, titleLabel);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        // User info dan logout
        Label userLabel = new Label("Halo, " + user.name());
        userLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label roleLabel = new Label("(" + user.role().getRoleName() + ")");
        roleLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.8); -fx-font-size: 12px;");

        Button logoutButton = createModernButton("Keluar", FontAwesomeSolid.SIGN_OUT_ALT, "#e74c3c");
        logoutButton.setOnAction(e -> {
            // Konfirmasi logout
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Konfirmasi Logout");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Apakah Anda yakin ingin keluar?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                new LoginController(
                        new UsersModel(AppDataSource.getInstance()),
                        roomsModel,
                        reservationsModel,
                        stage
                );
            }
        });

        VBox userInfo = new VBox(2, userLabel, roleLabel);
        userInfo.setAlignment(Pos.CENTER_RIGHT);

        HBox rightBox = new HBox(15, userInfo, logoutButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(logoBox, spacer, rightBox);
        root.setTop(header);
    }

    private void setupMainContent() {
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.CENTER);

        // Welcome message
        Label welcomeLabel = new Label("Selamat Datang di Dashboard");
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label descLabel = new Label("Kelola reservasi ruangan dengan mudah dan efisien");
        descLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.9); -fx-font-size: 16px;");

        VBox welcomeBox = new VBox(10, welcomeLabel, descLabel);
        welcomeBox.setAlignment(Pos.CENTER);

        // Quick stats
        HBox statsBox = createQuickStats();

        // Menu cards berdasarkan role
        FlowPane menuPane = new FlowPane();
        menuPane.setHgap(20);
        menuPane.setVgap(20);
        menuPane.setAlignment(Pos.CENTER);

        switch (user.role()) {
            case LOANER:
                menuPane.getChildren().addAll(
                        createMenuCard("Buat Reservasi", "Ajukan peminjaman ruangan baru",
                                FontAwesomeSolid.PLUS_CIRCLE, "#3498db", this::openAddReservation),
                        createMenuCard("Riwayat Reservasi", "Lihat semua reservasi Anda",
                                FontAwesomeSolid.HISTORY, "#2ecc71", this::openReservationHistory),
                        createMenuCard("Panduan", "Cara menggunakan sistem",
                                FontAwesomeSolid.QUESTION_CIRCLE, "#f39c12", this::showGuide)
                );
                break;
            case ADMIN_STAFF:
                menuPane.getChildren().addAll(
                        createMenuCard("Kelola Reservasi", "Setujui atau tolak reservasi",
                                FontAwesomeSolid.TASKS, "#e74c3c", this::openReservationManagement),
                        createMenuCard("Semua Reservasi", "Lihat semua reservasi sistem",
                                FontAwesomeSolid.LIST, "#9b59b6", this::showAllReservations),
                        createMenuCard("Statistik", "Lihat statistik penggunaan",
                                FontAwesomeSolid.CHART_BAR, "#1abc9c", this::showStatistics)
                );
                break;
            case CLEANING_STAFF:
                menuPane.getChildren().addAll(
                        createMenuCard("Jadwal Pembersihan", "Lihat ruangan yang perlu dibersihkan",
                                FontAwesomeSolid.CALENDAR_ALT, "#1abc9c", this::openRoomUseSchedule),
                        createMenuCard("Riwayat Pembersihan", "Lihat riwayat pembersihan",
                                FontAwesomeSolid.CHECK_CIRCLE, "#27ae60", this::showCleaningHistory)
                );
                break;
        }

        mainContent.getChildren().addAll(welcomeBox, statsBox, menuPane);
        root.setCenter(mainContent);
    }

    private HBox createQuickStats() {
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.CENTER);

        // Total reservasi hari ini
        int todayReservations = getTodayReservationsCount();
        VBox todayStats = createStatCard("Reservasi Hari Ini", String.valueOf(todayReservations), "#3498db");

        // Pending reservasi (untuk admin)
        if (user.role() == Roles.ADMIN_STAFF) {
            int pendingCount = reservationsModel.getAllPendingReservations().size();
            VBox pendingStats = createStatCard("Menunggu Persetujuan", String.valueOf(pendingCount), "#f39c12");
            statsBox.getChildren().addAll(todayStats, pendingStats);
        } else {
            // User reservasi (untuk loaner)
            int userReservations = reservationsModel.getReservationsByUserId(user.id()).size();
            VBox userStats = createStatCard("Total Reservasi Saya", String.valueOf(userReservations), "#2ecc71");
            statsBox.getChildren().addAll(todayStats, userStats);
        }

        return statsBox;
    }

    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(180, 80);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        card.getChildren().addAll(valueLabel, titleLabel);
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

    private VBox createMenuCard(String title, String description, FontAwesomeSolid icon, String color, Runnable action) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(250, 180);
        card.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-cursor: hand;"
        );

        // Efek bayangan
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.2));
        shadow.setOffsetX(0);
        shadow.setOffsetY(5);
        shadow.setRadius(15);
        card.setEffect(shadow);

        // Icon
        FontIcon cardIcon = new FontIcon(icon);
        cardIcon.setIconSize(40);
        cardIcon.setIconColor(Color.web(color));

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Description
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d; -fx-text-alignment: center;");
        descLabel.setWrapText(true);

        card.getChildren().addAll(cardIcon, titleLabel, descLabel);

        // Animasi hover
        card.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();

            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-cursor: hand;"
            );
        });

        card.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            card.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                            "-fx-background-radius: 15;" +
                            "-fx-cursor: hand;"
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
                        "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );

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
