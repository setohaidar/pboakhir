package org.example.data;

public enum Roles {
    LOANER("Peminjam"),
    ADMIN_STAFF("Staf Administrasi"),
    CLEANING_STAFF("Staf Kebersihan");

    private final String roleName;

    Roles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
