package ru.sidey383;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Kind {
    DIGITS("digits"), LETTERS("letters");

    private final String value;

    Kind(String value) {
        this.value = value;
    }

    @Nullable
    public static Kind getKind(@NotNull String param) {
        for (Kind k : values()) {
            if (k.value.equalsIgnoreCase(param)) {
                return k;
            }
        }
        return null;
    }

}
