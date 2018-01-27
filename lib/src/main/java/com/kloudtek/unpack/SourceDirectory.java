package com.kloudtek.unpack;

import java.util.ArrayList;
import java.util.List;

public class SourceDirectory extends UFile {
    private final List<UFile> files = new ArrayList<>();

    public SourceDirectory(String name, String path) {
        super(name, path);
    }

    public List<UFile> getFiles() {
        return files;
    }

    public void add(UFile srcFile) {
        files.add(srcFile);
    }

    public boolean contains(UFile file) {
        return files.contains(file);
    }

}
