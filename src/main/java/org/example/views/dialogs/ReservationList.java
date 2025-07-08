package org.example.views.dialogs;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.data.Reservation;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReservationList {

    private final Stage stage;
    private final TableView<Reservation> reservationTable;
    private final Button actionButton;
    private final BorderPane root;

    public ReservationList(List<Reservation> reservations) {
        this.stage = new Stage();
        this.reservationTable = new TableView<>();
        this.actionButton = new Button();

        root = new BorderPane();

        // Background gradient
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#667eea")),
                new Stop(1, Color.web("#764ba2"))
        );

        BackgroundFill backgroundFill = new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY);
        root.setBackground(new Background(backgroundFill));

        setupHeader();
        setupTable(reservations);
        setupFooter();
    }

    private void setupHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(20, 30, 20, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1);");

        FontIcon listIcon = new FontIcon(FontAwesomeSolid.LIST);
        listIcon.setIconSize(24);
        listIcon.setIconColor(Color.WHITE);

        Label titleLabel = new Label("Daftar Reservasi");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        HBox titleBox = new HBox(10, listIcon, titleLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        header.getChildren().add(titleBox);
        root.setTop(header);
    }

    private void setupTable(List<Reservation> reservations) {
        VBox tableContainer = new VBox(20);
        tableContainer.setPadding(new Insets(20, 30, 20, 30));

        // Table styling
        reservationTable.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;"
        );

        // Efek bayangan untuk table
        DropShadow tableShadow = new DropShadow();
        tableShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        tableShadow.setOffsetX(0);
        tableShadow.setOffsetY(5);
        tableShadow.setRadius(15);
        reservationTable.setEffect(tableShadow);

        // Setup columns
        TableColumn<Reservation, String> submissionTimeCol = new TableColumn<>("Waktu Pengajuan");
        submissionTimeCol.setCellValueFactory(data -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    sdf.format(data.getValue().submissionTime())
            );
        });
        submissionTimeCol.setPrefWidth(150);

        TableColumn<Reservation, String> roomNameCol = new TableColumn<>("Ruangan");
        roomNameCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().room().name()
        ));
        roomNameCol.setPrefWidth(120);

        TableColumn<Reservation, String> userNameCol = new TableColumn<>("Peminjam");
        userNameCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().user().name()
        ));
        userNameCol.setPrefWidth(150);

        TableColumn<Reservation, String> purposeCol = new TableColumn<>("Tujuan");
        purposeCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().purpose()
        ));
        purposeCol.setPrefWidth(200);

        TableColumn<Reservation, String> startTimeCol = new TableColumn<>("Waktu Mulai");
        startTimeCol.setCellValueFactory(data -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    sdf.format(data.getValue().startTime())
            );
        });
        startTimeCol.setPrefWidth(150);

        TableColumn<Reservation, String> endTimeCol = new TableColumn<>("Waktu Selesai");
        endTimeCol.setCellValueFactory(data -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    sdf.format(data.getValue().endTime())
            );
        });
        endTimeCol.setPrefWidth(150);

        TableColumn<Reservation, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().confirmationStatus() != null ?
                        data.getValue().confirmationStatus().getStatusDescription() : "Menunggu"
        ));
        statusCol.setPrefWidth(120);

        // Custom cell factory untuk status dengan warna
        statusCol.setCellFactory(column -> new TableCell<Reservation, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Diterima":
                            setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                            break;
                        case "Ditolak":
                            setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                            break;
                        case "Menunggu":
                            setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-text-fill: #2c3e50;");
                    }
                }
            }
        });

        reservationTable.getColumns().addAll(
                submissionTimeCol, roomNameCol, userNameCol, purposeCol,
                startTimeCol, endTimeCol, statusCol
        );

        if (reservations == null || reservations.isEmpty()) {
            VBox emptyBox = new VBox(20);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(50));
            emptyBox.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-radius: 15;"
            );
            emptyBox.setEffect(tableShadow);

            FontIcon emptyIcon = new FontIcon(FontAwesomeSolid.INBOX);
            emptyIcon.setIconSize(50);
            emptyIcon.setIconColor(Color.web("#bdc3c7"));

            Label emptyLabel = new Label("Tidak ada data reservasi");
            emptyLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #7f8c8d;");

            emptyBox.getChildren().addAll(emptyIcon, emptyLabel);
            tableContainer.getChildren().add(emptyBox);
        } else {
            reservationTable.setItems(FXCollections.observableArrayList(reservations));
            tableContainer.getChildren().add(reservationTable);
        }

        root.setCenter(tableContainer);
    }

    private void setupFooter() {
        HBox footer = new HBox(15);
        footer.setPadding(new Insets(20, 30, 30, 30));
        footer.setAlignment(Pos.CENTER);

        actionButton.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;"
        );
        actionButton.setVisible(false);

        footer.getChildren().add(actionButton);
        root.setBottom(footer);
    }

    // Tambahkan method baru untuk back button
    public void addBackButton(Button backButton) {
        HBox footer = (HBox) root.getBottom();
        if (footer != null) {
            footer.getChildren().add(0, backButton);
        }
    }

    public void setTitle(String title) {
        stage.setTitle("SIPERU - " + title);

        // Update header title
        HBox header = (HBox) root.getTop();
        HBox titleBox = (HBox) header.getChildren().get(0);
        Label titleLabel = (Label) titleBox.getChildren().get(1);
        titleLabel.setText(title);
    }

    public void setAction(String actionName, Runnable action) {
        actionButton.setText(actionName);
        actionButton.setVisible(true);
        actionButton.setOnAction(e -> action.run());

        // Icon untuk action button
        FontIcon actionIcon = new FontIcon(FontAwesomeSolid.COG);
        actionIcon.setIconSize(14);
        actionIcon.setIconColor(Color.WHITE);
        actionButton.setGraphic(actionIcon);
    }

    public void show() {
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("SIPERU - Daftar Reservasi");
        stage.show();

        // Animasi fade in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public Scene getScene() {
        return stage.getScene();
    }

    public Reservation getSelectedReservation() {
        return reservationTable.getSelectionModel().getSelectedItem();
    }

    public void close() {
        stage.close();
    }

    public Button getActionButton() {
        return actionButton;
    }
}
