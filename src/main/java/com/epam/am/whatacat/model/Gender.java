package com.epam.am.whatacat.model;

public enum Gender {
    MALE('m'), FEMALE('f'), UNDEFINED('u');
    char key;

    Gender(char key) {
        this.key = key;
    }
}
