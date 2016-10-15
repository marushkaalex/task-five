package com.epam.am.whatacat.model;

public enum Role {
    ADMIN(1, "role.admin"), USER(2, "role.user"), MODERATOR(3, "role.moderator");

    private long id;
    private String titleKey;

    Role(long id, String titleKey) {
        this.id = id;
        this.titleKey = titleKey;
    }

    public long getId() {
        return id;
    }

    public String getTitleKey() {
        return titleKey;
    }
}
