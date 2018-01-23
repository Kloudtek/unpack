package com.kloudtek.unpack;

import com.kloudtek.util.UnexpectedException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Source {
    protected List<SourceFile> files = new ArrayList<>();
    protected List<SourceFile> allFiles = new ArrayList<>();

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
