package com.kloudtek.unpack;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class InMemSourceFile extends SourceFile {
    private final ByteArrayInputStream buf;

    public InMemSourceFile(String name, String path, byte[] data) {
        super(name, path);
        this.buf = new ByteArrayInputStream(data);
    }

    @Override
    protected InputStream createInputStream() {
        return buf;
    }
}
