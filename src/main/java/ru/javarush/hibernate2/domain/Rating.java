package ru.javarush.hibernate2.domain;

public enum Rating {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC("NC-17");

    private final String value;

    Rating(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
