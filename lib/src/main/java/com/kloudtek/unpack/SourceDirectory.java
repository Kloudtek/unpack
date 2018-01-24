package com.kloudtek.unpack;

import java.util.ArrayList;
import java.util.List;

public abstract class SourceDirectory extends SourceFile {
    private final List<SourceFile> files = new ArrayList<>();

    public SourceDirectory(String name, SourceDirectory parent) {
        super(name, parent);
    }

    public List<SourceFile> getFiles() {
        return files;
    }

    public void add(SourceFile srcFile) {
        files.add(srcFile);
    }
}
