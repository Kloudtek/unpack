package com.kloudtek.unpack;

import com.kloudtek.util.UnexpectedException;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public abstract class Destination implements Closeable {
    public static Destination create(File file, FileType fileType) throws UnpackException {
        switch (fileType) {
            case DIR:
                return new FSDestination(file);
            case ZIP:
                return new ZipDestination(file);
            default:
                throw new UnexpectedException("Unexpected source type: " + fileType);
        }
    }

    public abstract void write(UFile sourceFile) throws UnpackException;

    @Override
    public void close() throws IOException {
    }
}
