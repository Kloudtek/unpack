package com.kloudtek.unpack;

import java.io.InputStream;

public abstract class SourceFile extends UFile {
    protected SourceFile(String name, String path) {
        super(name, path);
    }

    public abstract InputStream createInputStream() throws UnpackException;
}
