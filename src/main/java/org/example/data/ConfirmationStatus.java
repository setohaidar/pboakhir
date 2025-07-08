package org.example.data;

public enum ConfirmationStatus {
    PENDING(
        "belum dikonfirmasi",
        null
    ),
    REJECTED(
        "ditolak",
        0
    ),
    ACCEPTED(
        "disetujui",
        1
    ),
    NOT_CONFIRMED(
        "tidak dikonfirmasi",
        2
    );

    private final String statusDescription;
    private final Integer statusCode;

    ConfirmationStatus(String statusDescription, Integer statusCode) {
        this.statusDescription = statusDescription;
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
