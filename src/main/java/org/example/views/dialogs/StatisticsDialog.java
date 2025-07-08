package org.example.views.dialogs;

import javafx.collections.FXCollections;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.List;

public class StatisticsDialog {

    public static void show(Stage parentStage, int[] reservationStats, List<String[]> roomUsageStats) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("SIPERU - Statistik Sistem");

        BorderPane root = new BorderPane();

        // Background gradient
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(1, Color.web("#764ba2"))
        );

        BackgroundFill backgroundFill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        // Header
        HBox header = new HBox();
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1);");

        FontIcon chartIcon = new FontIcon(FontAwesomeSolid.CHART_BAR);
        chartIcon.setIconSize(24);
        chartIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("Statistik Sistem");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox titleBox = new HBox(10, chartIcon, titleLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        header.getChildren().add(titleBox);
        root.setTop(header);

        // Main content dengan ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Statistik Reservasi
        VBox reservationStatsBox = createReservationStatsBox(reservationStats);

        // Statistik Penggunaan Ruangan
        VBox roomStatsBox = createRoomUsageStatsBox(roomUsageStats);

        // Statistik Tambahan
        VBox additionalStatsBox = createAdditionalStatsBox(reservationStats, roomUsageStats);

        mainContent.getChildren().addAll(reservationStatsBox, roomStatsBox, additionalStatsBox);
        scrollPane.setContent(mainContent);
        root.setCenter(scrollPane);

        // Footer dengan close button
        HBox footer = new HBox();
        footer.setPadding(new Insets(20));
        footer.setAlignment(Pos.CENTER);

        Button closeButton = new Button("Tutup");
        closeButton.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
        closeButton.setOnAction(e -> dialogStage.close());

        FontIcon closeIcon = new FontIcon(FontAwesomeSolid.TIMES);
        closeIcon.setIconSize(14);
        closeIcon.setIconColor(Color.WHITE);
        closeButton.setGraphic(closeIcon);

        footer.getChildren().add(closeButton);
        root.setBottom(footer);

        Scene scene = new Scene(root, 900, 700);
        dialogStage.setScene(scene);
        dialogStage.setResizable(true);
        dialogStage.showAndWait();
    }

    private static VBox createReservationStatsBox(int[] stats) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(25));
        container.setStyle(
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
        container.setEffect(shadow);

        // Header dengan icon
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER);

        FontIcon reservationIcon = new FontIcon(FontAwesomeSolid.CLIPBOARD_LIST);
        reservationIcon.setIconSize(20);
        reservationIcon.setIconColor(Color.web("#3498db"));

        Label titleLabel = new Label("Statistik Reservasi");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        headerBox.getChildren().addAll(reservationIcon, titleLabel);

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(15);
        statsGrid.setAlignment(Pos.CENTER);

        // Data statistik
        String[] labels = {"Total Reservasi", "Menunggu Persetujuan", "Disetujui", "Ditolak", "Selesai Dibersihkan"};
        String[] colors = {"#3498db", "#f39c12", "#27ae60", "#e74c3c", "#9b59b6"};

        for (int i = 0; i < stats.length; i++) {
            VBox statCard = createStatCard(labels[i], String.valueOf(stats[i]), colors[i]);
            statsGrid.add(statCard, i % 3, i / 3);
        }

        container.getChildren().addAll(headerBox, statsGrid);
        return container;
    }

    private static VBox createRoomUsageStatsBox(List<String[]> roomStats) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(25));
        container.setStyle(
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
        container.setEffect(shadow);

        // Header dengan icon
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER);

        FontIcon roomIcon = new FontIcon(FontAwesomeSolid.DOOR_OPEN);
        roomIcon.setIconSize(20);
        roomIcon.setIconColor(Color.web("#e67e22"));

        Label titleLabel = new Label("Statistik Penggunaan Ruangan");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        headerBox.getChildren().addAll(roomIcon, titleLabel);

        if (roomStats == null || roomStats.isEmpty()) {
            // Tampilkan pesan jika tidak ada data
            VBox emptyBox = new VBox(15);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(30));

            FontIcon emptyIcon = new FontIcon(FontAwesomeSolid.INBOX);
            emptyIcon.setIconSize(40);
            emptyIcon.setIconColor(Color.web("#bdc3c7"));

            Label emptyLabel = new Label("Belum ada data penggunaan ruangan");
            emptyLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");

            emptyBox.getChildren().addAll(emptyIcon, emptyLabel);
            container.getChildren().addAll(headerBox, emptyBox);
        } else {
            // Table untuk room usage
            TableView<String[]> table = new TableView<>();
            table.setPrefHeight(250);
            table.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-border-color: #ecf0f1;" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 8;"
            );

            TableColumn<String[], String> roomColumn = new TableColumn<>("Nama Ruangan");
            roomColumn.setCellValueFactory(data ->
                    javafx.beans.binding.Bindings.createStringBinding(() -> data.getValue()[0])
            );
            roomColumn.setPrefWidth(250);
            roomColumn.setStyle("-fx-alignment: CENTER-LEFT;");

            TableColumn<String[], String> usageColumn = new TableColumn<>("Jumlah Penggunaan");
            usageColumn.setCellValueFactory(data ->
                    javafx.beans.binding.Bindings.createStringBinding(() -> data.getValue()[1] + " kali")
            );
            usageColumn.setPrefWidth(180);
            usageColumn.setStyle("-fx-alignment: CENTER;");

            // Custom cell factory untuk usage column dengan warna
            usageColumn.setCellFactory(column -> new TableCell<String[], String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        // Ambil angka dari string "X kali"
                        int usage = Integer.parseInt(item.split(" ")[0]);
                        if (usage >= 3) {
                            setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;"); // Hijau untuk sering digunakan
                        } else if (usage >= 1) {
                            setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;"); // Orange untuk jarang digunakan
                        } else {
                            setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;"); // Merah untuk tidak digunakan
                        }
                        setAlignment(Pos.CENTER);
                    }
                }
            });

            table.getColumns().addAll(roomColumn, usageColumn);
            table.setItems(FXCollections.observableArrayList(roomStats));

            // Summary info
            HBox summaryBox = new HBox(20);
            summaryBox.setAlignment(Pos.CENTER);
            summaryBox.setPadding(new Insets(15, 0, 0, 0));

            // Ruangan paling populer
            String mostUsedRoom = "Tidak ada";
            int maxUsage = 0;
            for (String[] stat : roomStats) {
                int usage = Integer.parseInt(stat[1]);
                if (usage > maxUsage) {
                    maxUsage = usage;
                    mostUsedRoom = stat[0];
                }
            }

            VBox popularRoomBox = new VBox(5);
            popularRoomBox.setAlignment(Pos.CENTER);
            popularRoomBox.setStyle(
                    "-fx-background-color: #e8f5e8;" +
                            "-fx-padding: 10;" +
                            "-fx-background-radius: 8;"
            );

            Label popularLabel = new Label("Ruangan Terpopuler");
            popularLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

            Label popularRoom = new Label(mostUsedRoom);
            popularRoom.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

            Label popularUsage = new Label(maxUsage + " kali digunakan");
            popularUsage.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

            popularRoomBox.getChildren().addAll(popularLabel, popularRoom, popularUsage);

            // Total ruangan
            VBox totalRoomBox = new VBox(5);
            totalRoomBox.setAlignment(Pos.CENTER);
            totalRoomBox.setStyle(
                    "-fx-background-color: #e8f4fd;" +
                            "-fx-padding: 10;" +
                            "-fx-background-radius: 8;"
            );

            Label totalLabel = new Label("Total Ruangan");
            totalLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #3498db; -fx-font-weight: bold;");

            Label totalCount = new Label(String.valueOf(roomStats.size()));
            totalCount.setStyle("-fx-font-size: 14px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");

            Label totalDesc = new Label("ruangan tersedia");
            totalDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

            totalRoomBox.getChildren().addAll(totalLabel, totalCount, totalDesc);

            summaryBox.getChildren().addAll(popularRoomBox, totalRoomBox);

            container.getChildren().addAll(headerBox, table, summaryBox);
        }

        return container;
    }

    private static VBox createAdditionalStatsBox(int[] reservationStats, List<String[]> roomUsageStats) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(25));
        container.setStyle(
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
        container.setEffect(shadow);

        // Header dengan icon
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER);

        FontIcon analyticsIcon = new FontIcon(FontAwesomeSolid.CHART_PIE);
        analyticsIcon.setIconSize(20);
        analyticsIcon.setIconColor(Color.web("#8e44ad"));

        Label titleLabel = new Label("Analisis Sistem");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        headerBox.getChildren().addAll(analyticsIcon, titleLabel);

        // Analisis data
        GridPane analysisGrid = new GridPane();
        analysisGrid.setHgap(20);
        analysisGrid.setVgap(15);
        analysisGrid.setAlignment(Pos.CENTER);

        // Tingkat persetujuan
        int totalReservations = reservationStats[0];
        int approvedReservations = reservationStats[2];
        double approvalRate = totalReservations > 0 ? (double) approvedReservations / totalReservations * 100 : 0;

        VBox approvalBox = createAnalysisCard(
                "Tingkat Persetujuan",
                String.format("%.1f%%", approvalRate),
                "#27ae60",
                "dari total reservasi"
        );

        // Tingkat penolakan
        int rejectedReservations = reservationStats[3];
        double rejectionRate = totalReservations > 0 ? (double) rejectedReservations / totalReservations * 100 : 0;

        VBox rejectionBox = createAnalysisCard(
                "Tingkat Penolakan",
                String.format("%.1f%%", rejectionRate),
                "#e74c3c",
                "dari total reservasi"
        );

        // Efisiensi pembersihan
        int cleanedReservations = reservationStats[4];
        double cleaningRate = approvedReservations > 0 ? (double) cleanedReservations / approvedReservations * 100 : 0;

        VBox cleaningBox = createAnalysisCard(
                "Efisiensi Pembersihan",
                String.format("%.1f%%", cleaningRate),
                "#9b59b6",
                "ruangan dibersihkan"
        );

        // Utilisasi ruangan
        int totalRooms = roomUsageStats != null ? roomUsageStats.size() : 0;
        int usedRooms = 0;
        if (roomUsageStats != null) {
            for (String[] stat : roomUsageStats) {
                if (Integer.parseInt(stat[1]) > 0) {
                    usedRooms++;
                }
            }
        }
        double utilizationRate = totalRooms > 0 ? (double) usedRooms / totalRooms * 100 : 0;

        VBox utilizationBox = createAnalysisCard(
                "Utilisasi Ruangan",
                String.format("%.1f%%", utilizationRate),
                "#3498db",
                "ruangan digunakan"
        );

        analysisGrid.add(approvalBox, 0, 0);
        analysisGrid.add(rejectionBox, 1, 0);
        analysisGrid.add(cleaningBox, 0, 1);
        analysisGrid.add(utilizationBox, 1, 1);

        container.getChildren().addAll(headerBox, analysisGrid);
        return container;
    }

    private static VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(140, 90);
        card.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-text-alignment: center;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(120);

        card.getChildren().addAll(valueLabel, titleLabel);
        return card;
    }

    private static VBox createAnalysisCard(String title, String value, String color, String description) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(180, 120);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-width: 2;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(160);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #7f8c8d;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(160);

        card.getChildren().addAll(titleLabel, valueLabel, descLabel);
        return card;
    }
}
