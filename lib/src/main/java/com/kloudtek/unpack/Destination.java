package com.kloudtek.unpack;

import com.kloudtek.util.UnexpectedException;

import java.io.File;

public abstract class Destination {
    public static Destination create(File file, FileType fileType) throws UnpackException {
        switch (fileType) {
            case DIR:
                return new FSDestination(file);
            default:
                throw new UnexpectedException("Unexpected source type: " + fileType);
        }
    }

    public abstract void write(SourceFile sourceFile) throws UnpackException;
}
