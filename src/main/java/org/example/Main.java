// File: PBO_FIX/siperu/src/main/java/org/example/Main.java

package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controllers.LoginController;
import org.example.models.ReservationsModel;
import org.example.models.RoomsModel;
import org.example.models.UsersModel;
import org.flywaydb.core.Flyway;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize modern AppStage manager
        AppStage.initialize(primaryStage);

        // NOTE: DO NOT REMOVE THE LINES BETWEEN THESE COMMENTS
        Flyway flyway = Flyway.configure().dataSource(AppDataSource.getInstance()).load();
        flyway.migrate();
        // NOTE: DO NOT REMOVE THE LINES BETWEEN THESE COMMENTS

        // Show login page with modern JavaFX UI
        UsersModel usersModel = new UsersModel(AppDataSource.getInstance());
        RoomsModel roomsModel = new RoomsModel(AppDataSource.getInstance());
        ReservationsModel reservationsModel = new ReservationsModel(AppDataSource.getInstance());

        new LoginController(usersModel, roomsModel, reservationsModel, primaryStage);
    }

    public static void main(String[] args) {
        launch(args); // Wajib di JavaFX
    }
}