package com.kloudtek.unpack;

import java.io.InputStream;

public abstract class SourceFile extends UFile {
    protected InputStream is;

    protected SourceFile(String name, String path) {
        super(name, path);
    }

    protected abstract InputStream createInputStream() throws UnpackException;

    public InputStream getInputStream() throws UnpackException {
        if( is == null ) {
            is = createInputStream();
        }
        return is;
    }

    public void setInputStream(InputStream is) {
        this.is = is;
    }
}
