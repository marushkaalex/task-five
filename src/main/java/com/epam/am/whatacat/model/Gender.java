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
}
