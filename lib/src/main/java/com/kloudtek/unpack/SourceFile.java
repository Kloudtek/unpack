package com.kloudtek.unpack;

import java.util.ArrayList;
import java.util.List;

public class SourceFile {
    protected String name;
    protected String path;
    protected String destination;
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

    public String getDestination() {
        return destination;
    }

    public SourceDirectory getParent() {
        return parent;
    }
}
