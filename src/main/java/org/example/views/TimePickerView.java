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
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimePickerView {

    private final Stage stage;
    private final BorderPane root;
    private DatePicker startDatePicker;
    private ComboBox<String> startTimeComboBox;
    private DatePicker endDatePicker;
    private ComboBox<String> endTimeComboBox;
    private Button continueButton;
    private Button backButton;

    public TimePickerView(Stage stage) {
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

        setupContent();
        setupValidation();
    }

    private void setupContent() {
        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.CENTER);

        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        FontIcon clockIcon = new FontIcon(FontAwesomeSolid.CLOCK);
        clockIcon.setIconSize(50);
        clockIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("Pilih Waktu Reservasi");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label subtitleLabel = new Label("Tentukan kapan Anda ingin menggunakan ruangan");
        subtitleLabel.setStyle("-fx-text-fill: rgba(255, 255, 255, 0.9); -fx-font-size: 16px;");

        headerBox.getChildren().addAll(clockIcon, titleLabel, subtitleLabel);

        // Form container
        VBox formContainer = new VBox(25);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(40));
        formContainer.setMaxWidth(600);
        formContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;"
        );

        // Efek bayangan
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setOffsetX(0);
        shadow.setOffsetY(10);
        shadow.setRadius(20);
        formContainer.setEffect(shadow);

        // Form fields
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.setAlignment(Pos.CENTER);

        // Styling untuk labels
        String labelStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;";

        // Tanggal mulai
        Label startDateLabel = new Label("Tanggal Mulai:");
        startDateLabel.setStyle(labelStyle);

        startDatePicker = new DatePicker(LocalDate.now().plusDays(1));
        startDatePicker.setStyle("-fx-font-size: 14px;");
        startDatePicker.setPrefWidth(150);
        // Set minimum date to tomorrow
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now().plusDays(1))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        // Jam mulai
        Label startTimeLabel = new Label("Jam Mulai:");
        startTimeLabel.setStyle(labelStyle);

        startTimeComboBox = new ComboBox<>();
        startTimeComboBox.getItems().addAll(generateTimeSlots());
        startTimeComboBox.setPromptText("Pilih Jam");
        startTimeComboBox.setStyle("-fx-font-size: 14px;");
        startTimeComboBox.setPrefWidth(150);

        // Tanggal selesai
        Label endDateLabel = new Label("Tanggal Selesai:");
        endDateLabel.setStyle(labelStyle);

        endDatePicker = new DatePicker(LocalDate.now().plusDays(1));
        endDatePicker.setStyle("-fx-font-size: 14px;");
        endDatePicker.setPrefWidth(150);
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate startDate = startDatePicker.getValue();
                if (startDate != null && date.isBefore(startDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });

        // Jam selesai
        Label endTimeLabel = new Label("Jam Selesai:");
        endTimeLabel.setStyle(labelStyle);

        endTimeComboBox = new ComboBox<>();
        endTimeComboBox.getItems().addAll(generateTimeSlots());
        endTimeComboBox.setPromptText("Pilih Jam");
        endTimeComboBox.setStyle("-fx-font-size: 14px;");
        endTimeComboBox.setPrefWidth(150);

        // Menambahkan ke grid
        formGrid.add(startDateLabel, 0, 0);
        formGrid.add(startDatePicker, 1, 0);
        formGrid.add(startTimeLabel, 2, 0);
        formGrid.add(startTimeComboBox, 3, 0);

        formGrid.add(endDateLabel, 0, 1);
        formGrid.add(endDatePicker, 1, 1);
        formGrid.add(endTimeLabel, 2, 1);
        formGrid.add(endTimeComboBox, 3, 1);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        backButton = new Button("Kembali");
        backButton.setStyle(
                "-fx-background-color: #95a5a6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );

        continueButton = new Button("Lanjutkan");
        continueButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );

        // Icons untuk buttons
        FontIcon backIcon = new FontIcon(FontAwesomeSolid.ARROW_LEFT);
        backIcon.setIconSize(14);
        backIcon.setIconColor(Color.WHITE);
        backButton.setGraphic(backIcon);

        FontIcon continueIcon = new FontIcon(FontAwesomeSolid.ARROW_RIGHT);
        continueIcon.setIconSize(14);
        continueIcon.setIconColor(Color.WHITE);
        continueButton.setGraphic(continueIcon);

        buttonBox.getChildren().addAll(backButton, continueButton);

        // Info box
        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setStyle(
                "-fx-background-color: rgba(52, 152, 219, 0.1);" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );

        FontIcon infoIcon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
        infoIcon.setIconSize(20);
        infoIcon.setIconColor(Color.web("#3498db"));

        Label infoLabel = new Label("📋 Ketentuan Reservasi:");
        infoLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label infoLabel2 = new Label("• Jam operasional: 07:00 - 18:00");
        infoLabel2.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");

        Label infoLabel3 = new Label("• Reservasi minimal H+1 dari hari ini");
        infoLabel3.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");

        Label infoLabel4 = new Label("• Durasi minimal 30 menit");
        infoLabel4.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");

        infoBox.getChildren().addAll(infoIcon, infoLabel, infoLabel2, infoLabel3, infoLabel4);

        formContainer.getChildren().addAll(formGrid, infoBox, buttonBox);

        mainContent.getChildren().addAll(headerBox, formContainer);
        root.setCenter(mainContent);
    }

    private void setupValidation() {
        // Auto-update end date when start date changes
        startDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                endDatePicker.setValue(newDate);
                // Update end time options based on start time
                updateEndTimeOptions();
            }
        });

        // Update end time options when start time changes
        startTimeComboBox.valueProperty().addListener((obs, oldTime, newTime) -> {
            updateEndTimeOptions();
        });
    }

    private void updateEndTimeOptions() {
        String startTime = startTimeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startTime != null && startDate != null && endDate != null && startDate.equals(endDate)) {
            // Same day - filter end times to be after start time
            List<String> allTimes = generateTimeSlots();
            List<String> validEndTimes = new ArrayList<>();

            boolean foundStartTime = false;
            for (String time : allTimes) {
                if (foundStartTime) {
                    validEndTimes.add(time);
                }
                if (time.equals(startTime)) {
                    foundStartTime = true;
                }
            }

            endTimeComboBox.getItems().clear();
            endTimeComboBox.getItems().addAll(validEndTimes);
        } else {
            // Different day or no start time selected - show all times
            endTimeComboBox.getItems().clear();
            endTimeComboBox.getItems().addAll(generateTimeSlots());
        }
    }

    public void show() {
        Scene scene = new Scene(root, 900, 700);
        stage.setScene(scene);
        stage.setTitle("SIPERU - Pilih Waktu");
        stage.show();

        // Animasi fade in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private List<String> generateTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        java.time.LocalTime time = java.time.LocalTime.of(7, 0);
        java.time.LocalTime endTime = java.time.LocalTime.of(18, 0);

        while (!time.isAfter(endTime)) {
            timeSlots.add(time.format(DateTimeFormatter.ofPattern("HH:mm")));
            time = time.plusMinutes(30);
        }
        return timeSlots;
    }

    // Getters
    public DatePicker getStartDatePicker() { return startDatePicker; }
    public ComboBox<String> getStartTimeComboBox() { return startTimeComboBox; }
    public DatePicker getEndDatePicker() { return endDatePicker; }
    public ComboBox<String> getEndTimeComboBox() { return endTimeComboBox; }
    public Button getContinueButton() { return continueButton; }
    public Button getBackButton() { return backButton; }
}
