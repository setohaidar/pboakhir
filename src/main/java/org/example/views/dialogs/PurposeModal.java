package org.example.views.dialogs;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Optional;

public class PurposeModal extends Dialog<String> {

    public PurposeModal(String roomName) {
        setTitle("Tujuan Reservasi");
        setHeaderText("Masukkan tujuan untuk reservasi ruangan: " + roomName);

        // Styling untuk dialog
        DialogPane dialogPane = getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;"
        );

        // Efek bayangan
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.3));
        shadow.setOffsetX(0);
        shadow.setOffsetY(10);
        shadow.setRadius(20);
        dialogPane.setEffect(shadow);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Icon dan label
        FontIcon purposeIcon = new FontIcon(FontAwesomeSolid.EDIT);
        purposeIcon.setIconSize(24);
        purposeIcon.setIconColor(Color.web("#3498db"));

        Label instructionLabel = new Label("Jelaskan tujuan penggunaan ruangan:");
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextArea purposeTextArea = new TextArea();
        purposeTextArea.setPromptText("Contoh: Rapat evaluasi proyek kuartal 3, Workshop pelatihan karyawan, dll.");
        purposeTextArea.setWrapText(true);
        purposeTextArea.setPrefRowCount(4);
        purposeTextArea.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-border-color: #bdc3c7;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;"
        );

        content.getChildren().addAll(purposeIcon, instructionLabel, purposeTextArea);
        getDialogPane().setContent(content);

        // Styling untuk buttons
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Simpan");
        okButton.setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 20;"
        );

        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("Batal");
        cancelButton.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 16;" +
                        "-fx-background-radius: 20;"
        );

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return purposeTextArea.getText();
            }
            return null;
        });
    }

    public static Optional<String> showAndGetPurpose(String roomName) {
        PurposeModal dialog = new PurposeModal(roomName);
        return dialog.showAndWait();
    }
}
