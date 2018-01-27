package com.kloudtek.unpack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class UFile {
    protected String name;
    protected String path;

    public UFile(@NotNull String name, @NotNull String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public static String generatePath(@Nullable String parent, @NotNull String name) {
        return parent != null ? parent + "/" + name : name;
    }
}
