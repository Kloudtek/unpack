package com.kloudtek.unpack;

import java.io.File;

public class FSSourceDirectory extends SourceDirectory {
    private File file;

    public FSSourceDirectory(File file, String path) {
        super(file.getName(), path);
        this.file = file;
    }
}
