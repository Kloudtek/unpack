package com.kloudtek.unpack;

import com.kloudtek.util.UnexpectedException;

import java.io.File;
import java.util.*;

public abstract class Source {
    protected List<UFile> files = new ArrayList<>();
    protected LinkedHashMap<String, UFile> filesIdx = new LinkedHashMap<>();

    public abstract void read() throws UnpackException;

    public List<UFile> getFiles() {
        return files;
    }

    public static Source create(File file, FileType fileType) throws UnpackException {
        switch (fileType) {
            case DIR:
                return new FSSource(file);
            case ZIP:
                return new ArchiveSource(file, fileType.getExtension());
            default:
                throw new UnexpectedException("Unexpected source type: " + fileType);
        }
    }

    public void add(UFile file) {
        files.add(file);
        filesIdx.put(file.getPath(),file);
    }

    public void sort() {
        System.out.println();
    }

    private class UFileWrapper {
        private UFile file;
        private UFileWrapper parent;
    }
}
