package org.example.views.dialogs;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;

public class StartEndTimeModal extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private DateTimePicker endDateTimePicker;
    private DateTimePicker startDateTimePicker;

    public StartEndTimeModal(JComponent parentComponent) {
        super(parentComponent == null ? null : SwingUtilities.getWindowAncestor(parentComponent));

        setContentPane(contentPane);
        setModal(true);
        setLocationRelativeTo(parentComponent);

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonCancel.addActionListener(_ -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
            _ -> onCancel(),
            KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE,
                0
            ),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );

        // Set minimum selectable date for startDateTimePicker to 5 days after today
        LocalDate minStartDate = LocalDate.now().plusDays(5);
        DatePickerSettings startDateSettings = startDateTimePicker.getDatePicker().getSettings();
        startDateSettings.setDateRangeLimits(
            minStartDate,
            null
        );

        // Restrict time for start time picker to between 07:00 and 18:00
        TimePickerSettings startTimeSettings = startDateTimePicker.getTimePicker().getSettings();
        startTimeSettings.setVetoPolicy(time -> {
            LocalTime minTime = LocalTime.of(
                7,
                0
            );
            LocalTime maxTime = LocalTime.of(
                18,
                0
            );
            return !(time.isBefore(minTime) || time.isAfter(maxTime));
        });
        startTimeSettings.generatePotentialMenuTimes(
            TimePickerSettings.TimeIncrement.ThirtyMinutes,
            LocalTime.of(
                7,
                0
            ),
            LocalTime.of(
                18,
                0
            )
        );

        // Initially disable timePicker of startDateTimePicker endDateTimePicker
        startDateTimePicker.getTimePicker().setEnabled(false);
        endDateTimePicker.setEnabled(false);

        // get the time picker settings for endDateTimePicker
        TimePickerSettings endTimeSettings = endDateTimePicker.getTimePicker().getSettings();

        // Add listener to startDateTimePicker to enable and set up endDateTimePicker
        startDateTimePicker.addDateTimeChangeListener(_ -> {
            LocalDate selectedDate = startDateTimePicker.datePicker.getDate();
            LocalTime selectedTime = startDateTimePicker.timePicker.getTime();

            if (selectedDate != null) {
                endDateTimePicker.datePicker.setDate(selectedDate);

                startDateTimePicker.getTimePicker().setEnabled(true);

                if (selectedTime != null) {
                    endDateTimePicker.getTimePicker().setTime(selectedTime.plusMinutes(30));
                    endDateTimePicker.getTimePicker().setEnabled(true);

                    endTimeSettings.setVetoPolicy(time -> {
                        LocalTime minTime = selectedTime.plusMinutes(30);
                        LocalTime maxTime = LocalTime.of(
                            18,
                            0
                        );
                        return !(time.isBefore(minTime) || time.isAfter(maxTime));
                    });
                    endTimeSettings.generatePotentialMenuTimes(
                        TimePickerSettings.TimeIncrement.ThirtyMinutes,
                        selectedTime.plusMinutes(30),
                        LocalTime.of(
                            18,
                            0
                        )
                    );
                } else {
                    endDateTimePicker.getTimePicker().setTime(null);

                    endDateTimePicker.timePicker.setEnabled(false);
                }
            } else {
                endDateTimePicker.datePicker.setDate(null);

                startDateTimePicker.getTimePicker().setEnabled(false);
            }
        });
    }

    // for demo purposes
    public static void main(String[] args) {
        StartEndTimeModal dialog = new StartEndTimeModal(null);

        dialog.getButtonOK().addActionListener(_ -> dialog.dispose());

        dialog.pack();
        dialog.setVisible(true);
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    public DateTimePicker getEndDateTimePicker() {
        return endDateTimePicker;
    }

    public DateTimePicker getStartDateTimePicker() {
        return startDateTimePicker;
    }

    private void onCancel() {
        dispose();
    }
}
