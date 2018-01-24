package com.kloudtek.unpack;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class SourceFile {
    protected String name;
    protected String path;
    protected SourceDirectory parent;

    protected SourceFile(String name, SourceDirectory parent) {
        this.name = name;
        this.path = parent != null ? parent.getPath() + "/"+name : name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public SourceDirectory getParent() {
        return parent;
    }

    public abstract InputStream createInputStream() throws UnpackException;
}
