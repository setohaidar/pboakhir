package org.example.utils;

import javafx.event.ActionEvent;

@FunctionalInterface
public interface ActionListenerWithRoom {
    void onAction(org.example.data.Room room, ActionEvent event);
}

