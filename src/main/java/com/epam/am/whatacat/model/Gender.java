package com.epam.am.whatacat.model;

public enum Gender {
    MALE('m'), FEMALE('f'), UNDEFINED('u');
    private char key;

    Gender(char key) {
        this.key = key;
    }

    public char getKey() {
        return key;
    }

    public static Gender of(char key) {
        for (Gender gender : values()) {
            if (gender.key == key) {
                return gender;
            }
        }

        throw new IllegalArgumentException();
    }
}
