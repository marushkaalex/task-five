package com.epam.am.whatacat.model;

public enum Gender {
    MALE('m', "gender.male"),
    FEMALE('f', "gender.female"),
    UNDEFINED('u', "gender.undefined");

    private char key;
    private String titleKey;

    Gender(char key, String titleKey) {
        this.key = key;
        this.titleKey = titleKey;
    }

    public char getKey() {
        return key;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public static Gender forKey(char key) {
        for (Gender gender : values()) {
            if (gender.key == key) {
                return gender;
            }
        }

        throw new IllegalArgumentException();
    }
}
