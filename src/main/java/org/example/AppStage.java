package org.example;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * Modern JavaFX Stage Manager - Replaces the old Swing AppFrame
 * Provides singleton pattern for managing the primary application stage
 */
public class AppStage {
    private static Stage primaryStage;
    private static AppStage instance;

    private AppStage() {
        // Private constructor for singleton
    }

    /**
     * Initialize the primary stage
     * @param stage The primary stage from JavaFX Application
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
        instance = new AppStage();
        
        // Configure default stage properties
        setupDefaultStageProperties();
    }

    /**
     * Get singleton instance
     * @return AppStage instance
     */
    public static AppStage getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppStage not initialized. Call initialize() first.");
        }
        return instance;
    }

    /**
     * Get the primary stage
     * @return Primary JavaFX Stage
     */
    public static Stage getPrimaryStage() {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage not initialized.");
        }
        return primaryStage;
    }

    /**
     * Configure default stage properties
     */
    private static void setupDefaultStageProperties() {
        primaryStage.setTitle("SIPERU - Sistem Peminjaman Ruangan");
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Set application icon if available
        try {
            primaryStage.getIcons().add(new Image("/icon.png"));
        } catch (Exception e) {
            // Icon not found, continue without it
        }

        // Close application when stage is closed
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
    }

    /**
     * Show the stage
     */
    public void show() {
        primaryStage.show();
    }

    /**
     * Hide the stage
     */
    public void hide() {
        primaryStage.hide();
    }

    /**
     * Set scene to the primary stage
     * @param scene The scene to set
     */
    public void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    /**
     * Set stage title
     * @param title Window title
     */
    public void setTitle(String title) {
        primaryStage.setTitle(title);
    }

    /**
     * Center stage on screen
     */
    public void centerOnScreen() {
        primaryStage.centerOnScreen();
    }

    /**
     * Set stage size
     * @param width Window width
     * @param height Window height
     */
    public void setSize(double width, double height) {
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }

    /**
     * Maximize the stage
     */
    public void maximize() {
        primaryStage.setMaximized(true);
    }

    /**
     * Minimize the stage
     */
    public void minimize() {
        primaryStage.setIconified(true);
    }

    /**
     * Check if stage is maximized
     * @return true if maximized
     */
    public boolean isMaximized() {
        return primaryStage.isMaximized();
    }

    /**
     * Set resizable property
     * @param resizable true to allow resize
     */
    public void setResizable(boolean resizable) {
        primaryStage.setResizable(resizable);
    }
}