package com.kloudtek.unpack;

import com.kloudtek.util.UnexpectedException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Source {
    protected List<SourceFile> files = new ArrayList<>();
    protected List<SourceFile> allFiles = new ArrayList<>();

    public abstract void read() throws UnpackException;

    public List<SourceFile> getFiles() {
        return files;
    }

    public void setFiles(List<SourceFile> files) {
        this.files = files;
    }

    public List<SourceFile> getAllFiles() {
        return allFiles;
    }

    public void setAllFiles(List<SourceFile> allFiles) {
        this.allFiles = allFiles;
    }

    public static Source create(File file, FileType fileType) throws UnpackException {
        switch (fileType) {
            case DIR:
                return new FSSource(file);
            case ZIP:
                return new ZipSource(file);
            default:
                throw new UnexpectedException("Unexpected source type: "+fileType);
        }
    }
}
