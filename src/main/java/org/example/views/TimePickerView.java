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
        setupValidation();
    }

    private void setupHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(25, 40, 25, 40));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.15);" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Title dengan icon yang modern
        FontIcon clockIcon = new FontIcon(FontAwesomeSolid.CLOCK);
        clockIcon.setIconSize(28);
        clockIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("Pilih Waktu");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        Label subtitleLabel = new Label("Tentukan Jadwal Reservasi");
        subtitleLabel.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.8);" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.getChildren().addAll(
                new HBox(10, clockIcon, titleLabel),
                subtitleLabel
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(titleBox, spacer);
        root.setTop(header);
    }

    private void setupContent() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox mainContent = new VBox(40);
        mainContent.setPadding(new Insets(40, 60, 40, 60));
        mainContent.setAlignment(Pos.CENTER);

        // Welcome section
        VBox welcomeSection = createWelcomeSection();

        // Form container dengan styling modern
        VBox formContainer = createFormContainer();

        // Info section
        VBox infoSection = createInfoSection();

        mainContent.getChildren().addAll(welcomeSection, formContainer, infoSection);
        scrollPane.setContent(mainContent);
        root.setCenter(scrollPane);
    }

    private VBox createWelcomeSection() {
        VBox welcomeSection = new VBox(15);
        welcomeSection.setAlignment(Pos.CENTER);

        FontIcon clockIcon = new FontIcon(FontAwesomeSolid.CALENDAR_ALT);
        clockIcon.setIconSize(60);
        clockIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("Pilih Waktu Reservasi");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );

        Label subtitleLabel = new Label("Tentukan kapan Anda ingin menggunakan ruangan");
        subtitleLabel.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.9);" +
                        "-fx-font-size: 18px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        welcomeSection.getChildren().addAll(clockIcon, titleLabel, subtitleLabel);
        return welcomeSection;
    }

    private VBox createFormContainer() {
        VBox formContainer = new VBox(30);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(40));
        formContainer.setMaxWidth(700);
        formContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.98);" +
                        "-fx-background-radius: 25;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 25, 0, 0, 15);"
        );

        // Form title
        Label formTitle = new Label("📅 Detail Waktu Reservasi");
        formTitle.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        // Date and time grid
        GridPane formGrid = createFormGrid();

        // Buttons
        HBox buttonBox = createButtonBox();

        formContainer.getChildren().addAll(formTitle, formGrid, buttonBox);
        return formContainer;
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(25);
        grid.setVgap(25);
        grid.setAlignment(Pos.CENTER);

        // Styling untuk labels
        String labelStyle = 
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #34495e;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;";

        // Start Date
        VBox startDateContainer = new VBox(8);
        Label startDateLabel = new Label("📅 Tanggal Mulai");
        startDateLabel.setStyle(labelStyle);

        startDatePicker = new DatePicker(LocalDate.now().plusDays(1));
        stylePickerComponent(startDatePicker);
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now().plusDays(1))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffebee;");
                }
            }
        });

        startDateContainer.getChildren().addAll(startDateLabel, startDatePicker);

        // Start Time
        VBox startTimeContainer = new VBox(8);
        Label startTimeLabel = new Label("🕐 Jam Mulai");
        startTimeLabel.setStyle(labelStyle);

        startTimeComboBox = new ComboBox<>();
        startTimeComboBox.getItems().addAll(generateTimeSlots());
        startTimeComboBox.setPromptText("Pilih jam mulai");
        stylePickerComponent(startTimeComboBox);

        startTimeContainer.getChildren().addAll(startTimeLabel, startTimeComboBox);

        // End Date
        VBox endDateContainer = new VBox(8);
        Label endDateLabel = new Label("📅 Tanggal Selesai");
        endDateLabel.setStyle(labelStyle);

        endDatePicker = new DatePicker(LocalDate.now().plusDays(1));
        stylePickerComponent(endDatePicker);
        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate startDate = startDatePicker.getValue();
                if (startDate != null && date.isBefore(startDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffebee;");
                }
            }
        });

        endDateContainer.getChildren().addAll(endDateLabel, endDatePicker);

        // End Time
        VBox endTimeContainer = new VBox(8);
        Label endTimeLabel = new Label("🕐 Jam Selesai");
        endTimeLabel.setStyle(labelStyle);

        endTimeComboBox = new ComboBox<>();
        endTimeComboBox.getItems().addAll(generateTimeSlots());
        endTimeComboBox.setPromptText("Pilih jam selesai");
        stylePickerComponent(endTimeComboBox);

        endTimeContainer.getChildren().addAll(endTimeLabel, endTimeComboBox);

        grid.add(startDateContainer, 0, 0);
        grid.add(startTimeContainer, 1, 0);
        grid.add(endDateContainer, 0, 1);
        grid.add(endTimeContainer, 1, 1);

        return grid;
    }

    private void stylePickerComponent(Control component) {
        component.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-background-color: #f8f9fa;" +
                        "-fx-border-color: #e9ecef;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-width: 2;" +
                        "-fx-padding: 10;"
        );
        component.setPrefWidth(200);

        // Focus effect
        component.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                component.setStyle(
                        "-fx-font-size: 14px;" +
                                "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                                "-fx-background-color: white;" +
                                "-fx-border-color: #667eea;" +
                                "-fx-border-radius: 8;" +
                                "-fx-background-radius: 8;" +
                                "-fx-border-width: 2;" +
                                "-fx-padding: 10;" +
                                "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.25), 10, 0, 0, 0);"
                );
            } else {
                component.setStyle(
                        "-fx-font-size: 14px;" +
                                "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                                "-fx-background-color: #f8f9fa;" +
                                "-fx-border-color: #e9ecef;" +
                                "-fx-border-radius: 8;" +
                                "-fx-background-radius: 8;" +
                                "-fx-border-width: 2;" +
                                "-fx-padding: 10;"
                );
            }
        });
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        backButton = createModernButton("Kembali", FontAwesomeSolid.ARROW_LEFT, "#6c757d");
        continueButton = createModernButton("Lanjutkan", FontAwesomeSolid.ARROW_RIGHT, "#27ae60");

        buttonBox.getChildren().addAll(backButton, continueButton);
        return buttonBox;
    }

    private Button createModernButton(String text, FontAwesomeSolid icon, String color) {
        Button button = new Button(text);

        FontIcon buttonIcon = new FontIcon(icon);
        buttonIcon.setIconSize(16);
        buttonIcon.setIconColor(Color.WHITE);
        button.setGraphic(buttonIcon);

        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-padding: 15 30;" +
                        "-fx-background-radius: 30;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );

        // Hover animation
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();

            button.setStyle(
                    "-fx-background-color: derive(" + color + ", -10%);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                            "-fx-padding: 15 30;" +
                            "-fx-background-radius: 30;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 8);"
            );
        });

        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            button.setStyle(
                    "-fx-background-color: " + color + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                            "-fx-padding: 15 30;" +
                            "-fx-background-radius: 30;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);"
            );
        });

        return button;
    }

    private VBox createInfoSection() {
        VBox infoSection = new VBox(20);
        infoSection.setAlignment(Pos.CENTER);
        infoSection.setPadding(new Insets(25));
        infoSection.setMaxWidth(600);
        infoSection.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 10);"
        );

        // Info header
        HBox infoHeader = new HBox(10);
        infoHeader.setAlignment(Pos.CENTER);

        FontIcon infoIcon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
        infoIcon.setIconSize(24);
        infoIcon.setIconColor(Color.web("#3498db"));

        Label infoTitle = new Label("Ketentuan Reservasi");
        infoTitle.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        infoHeader.getChildren().addAll(infoIcon, infoTitle);

        // Info items
        VBox infoItems = new VBox(10);
        infoItems.setAlignment(Pos.CENTER_LEFT);

        String[] infoTexts = {
                "⏰ Jam operasional: 07:00 - 18:00",
                "📅 Reservasi minimal H+1 dari hari ini",
                "⏱️ Durasi minimal 30 menit",
                "✅ Konfirmasi dari admin diperlukan",
                "📱 Notifikasi akan dikirim setelah konfirmasi"
        };

        for (String text : infoTexts) {
            Label infoLabel = new Label(text);
            infoLabel.setStyle(
                    "-fx-font-size: 13px;" +
                            "-fx-text-fill: #34495e;" +
                            "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
            );
            infoItems.getChildren().add(infoLabel);
        }

        infoSection.getChildren().addAll(infoHeader, infoItems);
        return infoSection;
    }

    private void setupValidation() {
        // Auto-update end date when start date changes
        startDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                endDatePicker.setValue(newDate);
                updateEndTimeOptions();
            }
        });

        // Update end time options when start time changes
        startTimeComboBox.valueProperty().addListener((obs, oldTime, newTime) -> {
            updateEndTimeOptions();
        });

        // Real-time validation
        continueButton.disableProperty().bind(
                startDatePicker.valueProperty().isNull()
                        .or(startTimeComboBox.valueProperty().isNull())
                        .or(endDatePicker.valueProperty().isNull())
                        .or(endTimeComboBox.valueProperty().isNull())
        );
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
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("SIPERU - Pilih Waktu Reservasi");
        
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
