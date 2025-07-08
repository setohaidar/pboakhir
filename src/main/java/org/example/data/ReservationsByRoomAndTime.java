package org.example.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public record ReservationsByRoomAndTime(
    Room room,
    Date startTime,
    Date endTime,
    List<Reservation> reservations
) {
    /**
     * Groups reservation list by room and time.
     *
     * @param reservationList list of reservations to be grouped
     * @return list of grouped reservations by room and time
     */
    public static List<ReservationsByRoomAndTime> generateList(List<Reservation> reservationList) {
        if (reservationList == null || reservationList.isEmpty()) {
            return List.of();
        }

        return reservationList.stream().collect(Collectors.groupingBy(Reservation::room)).entrySet().stream().flatMap(roomEntry -> {
            List<Reservation> reservations = roomEntry.getValue().stream().sorted(Comparator.comparing(Reservation::startTime)).toList();
            List<ReservationsByRoomAndTime> groups = new ArrayList<>();
            List<Reservation> currentGroup = new ArrayList<>();
            Date groupStart = null;
            Date groupEnd = null;
            for (Reservation r : reservations) {
                if (currentGroup.isEmpty()) {
                    currentGroup.add(r);
                    groupStart = r.startTime();
                    groupEnd = r.endTime();
                } else {
                    // Check for overlap
                    if (!r.startTime().after(groupEnd)) {
                        currentGroup.add(r);
                        if (r.endTime().after(groupEnd)) {
                            groupEnd = r.endTime();
                        }
                    } else {
                        // No overlap, close current group
                        groups.add(new ReservationsByRoomAndTime(
                            roomEntry.getKey(),
                            groupStart,
                            groupEnd,
                            List.copyOf(currentGroup)
                        ));
                        currentGroup.clear();
                        currentGroup.add(r);
                        groupStart = r.startTime();
                        groupEnd = r.endTime();
                    }
                }
            }
            if (!currentGroup.isEmpty()) {
                groups.add(new ReservationsByRoomAndTime(
                    roomEntry.getKey(),
                    groupStart,
                    groupEnd,
                    List.copyOf(currentGroup)
                ));
            }
            return groups.stream();
        }).toList();
    }
}
