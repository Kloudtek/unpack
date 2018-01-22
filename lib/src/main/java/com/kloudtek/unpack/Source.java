package com.kloudtek.unpack;

import com.kloudtek.util.UnexpectedException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Source {
    protected String path;
    protected String destination;
    protected Source parent;
    protected List<Source> files = new ArrayList<>();

    public abstract List<Source> getFiles();

    public static Source create(File file, FileType fileType) {
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
