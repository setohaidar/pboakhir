package org.example.views.dialogs;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Modern JavaFX Date Time Picker Dialog - Replaces Swing StartEndTimeModal
 * Features beautiful UI, validation, and smooth animations
 */
public class DateTimePickerDialog {
    
    private Stage dialogStage;
    private DatePicker startDatePicker;
    private ComboBox<String> startTimeComboBox;
    private DatePicker endDatePicker;
    private ComboBox<String> endTimeComboBox;
    private Button okButton;
    private Button cancelButton;
    
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean okClicked = false;

    public DateTimePickerDialog(Stage owner) {
        createDialog(owner);
        setupValidation();
    }

    private void createDialog(Stage owner) {
        dialogStage = new Stage();
        dialogStage.setTitle("Pilih Waktu Reservasi");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(owner);
        dialogStage.initStyle(StageStyle.DECORATED);
        dialogStage.setResizable(false);

        // Main layout dengan gradient background
        BorderPane root = new BorderPane();
        
        // Background gradient
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(0.3, Color.web("#764ba2")),
                new Stop(0.7, Color.web("#f093fb")),
                new Stop(1, Color.web("#f5576c"))
        );

        BackgroundFill backgroundFill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        // Header
        VBox header = createHeader();
        
        // Content
        VBox content = createContent();
        
        // Footer dengan buttons
        HBox footer = createFooter();

        root.setTop(header);
        root.setCenter(content);
        root.setBottom(footer);

        Scene scene = new Scene(root, 600, 500);
        dialogStage.setScene(scene);
        
        // Center pada owner
        dialogStage.setOnShowing(e -> {
            if (owner != null) {
                dialogStage.setX(owner.getX() + (owner.getWidth() - dialogStage.getWidth()) / 2);
                dialogStage.setY(owner.getY() + (owner.getHeight() - dialogStage.getHeight()) / 2);
            }
        });
    }

    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setPadding(new Insets(30, 30, 20, 30));
        header.setAlignment(Pos.CENTER);

        FontIcon clockIcon = new FontIcon(FontAwesomeSolid.CLOCK);
        clockIcon.setIconSize(50);
        clockIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("Pilih Waktu Reservasi");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );

        Label subtitleLabel = new Label("Tentukan kapan Anda ingin menggunakan ruangan");
        subtitleLabel.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.9);" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        header.getChildren().addAll(clockIcon, titleLabel, subtitleLabel);
        return header;
    }

    private VBox createContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(20, 30, 20, 30));
        content.setAlignment(Pos.CENTER);

        // Form container
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.98);" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 10);"
        );

        // Form title
        Label formTitle = new Label("📅 Detail Waktu");
        formTitle.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        // Form grid
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Start Date
        VBox startDateContainer = createDateTimeContainer(
                "📅 Tanggal Mulai", 
                startDatePicker = new DatePicker(LocalDate.now().plusDays(1)),
                "🕐 Jam Mulai",
                startTimeComboBox = new ComboBox<>()
        );

        // End Date  
        VBox endDateContainer = createDateTimeContainer(
                "📅 Tanggal Selesai",
                endDatePicker = new DatePicker(LocalDate.now().plusDays(1)),
                "🕐 Jam Selesai", 
                endTimeComboBox = new ComboBox<>()
        );

        // Setup date pickers
        setupDatePickers();
        setupTimeComboBoxes();

        grid.add(startDateContainer, 0, 0);
        grid.add(endDateContainer, 1, 0);

        // Info section
        VBox infoSection = createInfoSection();

        formContainer.getChildren().addAll(formTitle, grid, infoSection);
        content.getChildren().add(formContainer);

        return content;
    }

    private VBox createDateTimeContainer(String dateLabel, DatePicker datePicker, String timeLabel, ComboBox<String> timeComboBox) {
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);

        // Date section
        VBox dateSection = new VBox(8);
        Label dateLabelNode = new Label(dateLabel);
        dateLabelNode.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #34495e;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        styleControl(datePicker);
        dateSection.getChildren().addAll(dateLabelNode, datePicker);

        // Time section
        VBox timeSection = new VBox(8);
        Label timeLabelNode = new Label(timeLabel);
        timeLabelNode.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #34495e;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;"
        );

        styleControl(timeComboBox);
        timeSection.getChildren().addAll(timeLabelNode, timeComboBox);

        container.getChildren().addAll(dateSection, timeSection);
        return container;
    }

    private void styleControl(Control control) {
        control.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                        "-fx-background-color: #f8f9fa;" +
                        "-fx-border-color: #e9ecef;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-width: 2;" +
                        "-fx-padding: 12;"
        );
        control.setPrefWidth(200);

        // Focus effect
        control.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                control.setStyle(
                        "-fx-font-size: 14px;" +
                                "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                                "-fx-background-color: white;" +
                                "-fx-border-color: #667eea;" +
                                "-fx-border-radius: 8;" +
                                "-fx-background-radius: 8;" +
                                "-fx-border-width: 2;" +
                                "-fx-padding: 12;" +
                                "-fx-effect: dropshadow(gaussian, rgba(102,126,234,0.25), 10, 0, 0, 0);"
                );
            } else {
                control.setStyle(
                        "-fx-font-size: 14px;" +
                                "-fx-font-family: 'Segoe UI', Arial, sans-serif;" +
                                "-fx-background-color: #f8f9fa;" +
                                "-fx-border-color: #e9ecef;" +
                                "-fx-border-radius: 8;" +
                                "-fx-background-radius: 8;" +
                                "-fx-border-width: 2;" +
                                "-fx-padding: 12;"
                );
            }
        });
    }

    private void setupDatePickers() {
        // Set minimum date to tomorrow
        LocalDate minDate = LocalDate.now().plusDays(1);
        
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(minDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffebee;");
                }
            }
        });

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
    }

    private void setupTimeComboBoxes() {
        List<String> timeSlots = generateTimeSlots();
        
        startTimeComboBox.getItems().addAll(timeSlots);
        startTimeComboBox.setPromptText("Pilih jam mulai");
        
        endTimeComboBox.getItems().addAll(timeSlots);
        endTimeComboBox.setPromptText("Pilih jam selesai");
    }

    private List<String> generateTimeSlots() {
        List<String> timeSlots = new ArrayList<>();
        LocalTime time = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        while (!time.isAfter(endTime)) {
            timeSlots.add(time.format(DateTimeFormatter.ofPattern("HH:mm")));
            time = time.plusMinutes(30);
        }
        return timeSlots;
    }

    private VBox createInfoSection() {
        VBox infoSection = new VBox(10);
        infoSection.setPadding(new Insets(15));
        infoSection.setStyle(
                "-fx-background-color: rgba(52, 152, 219, 0.1);" +
                        "-fx-border-color: #3498db;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        FontIcon infoIcon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
        infoIcon.setIconSize(16);
        infoIcon.setIconColor(Color.web("#3498db"));

        Label infoTitle = new Label("ℹ️ Ketentuan Reservasi");
        infoTitle.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #2c3e50;"
        );

        String[] infoTexts = {
                "• Jam operasional: 07:00 - 18:00",
                "• Reservasi minimal H+1 dari hari ini", 
                "• Durasi minimal 30 menit"
        };

        VBox infoList = new VBox(5);
        for (String text : infoTexts) {
            Label infoLabel = new Label(text);
            infoLabel.setStyle(
                    "-fx-font-size: 11px;" +
                            "-fx-text-fill: #34495e;"
            );
            infoList.getChildren().add(infoLabel);
        }

        infoSection.getChildren().addAll(infoTitle, infoList);
        return infoSection;
    }

    private HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(20, 30, 30, 30));
        footer.setAlignment(Pos.CENTER_RIGHT);

        cancelButton = createButton("Batal", FontAwesomeSolid.TIMES, "#6c757d");
        okButton = createButton("OK", FontAwesomeSolid.CHECK, "#27ae60");

        // Event handlers
        cancelButton.setOnAction(e -> {
            okClicked = false;
            dialogStage.close();
        });

        okButton.setOnAction(e -> {
            if (validateInput()) {
                okClicked = true;
                dialogStage.close();
            }
        });

        // Disable OK button initially
        okButton.disableProperty().bind(
                startDatePicker.valueProperty().isNull()
                        .or(startTimeComboBox.valueProperty().isNull())
                        .or(endDatePicker.valueProperty().isNull())
                        .or(endTimeComboBox.valueProperty().isNull())
        );

        footer.getChildren().addAll(cancelButton, okButton);
        return footer;
    }

    private Button createButton(String text, FontAwesomeSolid icon, String color) {
        Button button = new Button(text);
        
        FontIcon buttonIcon = new FontIcon(icon);
        buttonIcon.setIconSize(14);
        buttonIcon.setIconColor(Color.WHITE);
        button.setGraphic(buttonIcon);

        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 24;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );

        return button;
    }

    private boolean validateInput() {
        LocalDate startDate = startDatePicker.getValue();
        String startTimeStr = startTimeComboBox.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String endTimeStr = endTimeComboBox.getValue();

        if (startDate == null || startTimeStr == null || endDate == null || endTimeStr == null) {
            showError("Semua field harus diisi!");
            return false;
        }

        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);

        startDateTime = LocalDateTime.of(startDate, startTime);
        endDateTime = LocalDateTime.of(endDate, endTime);

        if (startDateTime.isAfter(endDateTime) || startDateTime.equals(endDateTime)) {
            showError("Waktu mulai harus sebelum waktu selesai!");
            return false;
        }

        if (startDateTime.isBefore(LocalDateTime.now().plusDays(1))) {
            showError("Reservasi minimal H+1 dari sekarang!");
            return false;
        }

        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(dialogStage);
        alert.showAndWait();
    }

    private void setupValidation() {
        // Update end date when start date changes
        startDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                endDatePicker.setValue(newDate);
            }
        });
    }

    /**
     * Show dialog and wait for result
     * @return Optional containing the result if OK was clicked
     */
    public Optional<DateTimeResult> showAndWait() {
        // Animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dialogStage.getScene().getRoot());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        dialogStage.showAndWait();

        if (okClicked && startDateTime != null && endDateTime != null) {
            return Optional.of(new DateTimeResult(startDateTime, endDateTime));
        }
        return Optional.empty();
    }

    /**
     * Result class for date time picker
     */
    public static class DateTimeResult {
        private final LocalDateTime startDateTime;
        private final LocalDateTime endDateTime;

        public DateTimeResult(LocalDateTime startDateTime, LocalDateTime endDateTime) {
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
        }

        public LocalDateTime getStartDateTime() {
            return startDateTime;
        }

        public LocalDateTime getEndDateTime() {
            return endDateTime;
        }
    }
}