package org.example.data;

import java.util.List;
import java.util.stream.Collectors;

public record ReservationsByRoom(
    Room room,
    List<Reservation> reservations
) {
    /**
     * Groups reservation list by room.
     *
     * @param reservationList list of reservations to be grouped
     * @return list of grouped reservations by room
     */
    public static List<ReservationsByRoom> generateList(List<Reservation> reservationList) {
        if (reservationList == null || reservationList.isEmpty()) {
            return List.of();
        }
        
        return reservationList.stream().collect(Collectors.groupingBy(Reservation::room)).entrySet().stream().map(entry -> new ReservationsByRoom(
            entry.getKey(),
            entry.getValue()
        )).toList();
    }
}
