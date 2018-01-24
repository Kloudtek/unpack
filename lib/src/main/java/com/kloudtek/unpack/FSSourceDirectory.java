package com.kloudtek.unpack;

import java.io.File;
import java.io.InputStream;

public class FSSourceDirectory extends SourceDirectory {
    private final File file;

    public FSSourceDirectory(File file, SourceDirectory parent) {
        super(file.getName(), parent);
        this.file = file;
    }

    @Override
    public InputStream createInputStream() throws UnpackException {
        return null;
    }
}
