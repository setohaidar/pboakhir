package org.example.data;

public record User(
    Integer id,
    String name,
    String email,
    Roles role
) {}
