package com.epam.am.whatacat.model;

public enum Role {
    ADMIN(1), USER(2), MODERATOR(3);

    private long id;

    Role(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
