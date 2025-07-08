package org.example.views.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.data.Reservation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReservationPickerModal {

    private Reservation selectedReservation;

    public ReservationPickerModal(Stage parentStage, List<Reservation> reservationList, Runnable onCancel, Runnable onConfirm) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("Pilih Reservasi");

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #43cea2, #185a9d);");

        Label label = new Label("Pilih Reservasi");
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> reservationOptions = new ComboBox<>();
        reservationOptions.setMinWidth(300);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH.mm");
        for (Reservation reservation : reservationList) {
            String option = reservation.user().name() + " - " + sdf.format(reservation.submissionTime());
            reservationOptions.getItems().add(option);
        }

        Button buttonOK = new Button("OK");
        Button buttonCancel = new Button("Batal");

        styleButton(buttonOK);
        styleButton(buttonCancel);

        buttonOK.setOnAction(e -> {
            int selectedIndex = reservationOptions.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                selectedReservation = reservationList.get(selectedIndex);
                onConfirm.run();
                dialogStage.close();
            } else {
                showAlert(Alert.AlertType.WARNING, "Silakan pilih reservasi terlebih dahulu.");
            }
        });

        buttonCancel.setOnAction(e -> {
            onCancel.run();
            dialogStage.close();
        });

        HBox buttonBox = new HBox(10, buttonOK, buttonCancel);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(label, reservationOptions, buttonBox);

        Scene scene = new Scene(root, 400, 250);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    public Reservation getSelectedReservation() {
        return selectedReservation;
    }

    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: white; -fx-text-fill: #333333; -fx-font-size: 14px; -fx-padding: 8 20 8 20; -fx-background-radius: 8;");
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
