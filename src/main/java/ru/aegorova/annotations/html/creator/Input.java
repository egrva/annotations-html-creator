package ru.aegorova.annotations.html.creator;

public class Input {
    private String type;
    private String name;
    private String placeholder;

    Input(String type, String name, String placeholder) {
        this.name = name;
        this.placeholder = placeholder;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPlaceholder() {
        return placeholder;
    }
}
