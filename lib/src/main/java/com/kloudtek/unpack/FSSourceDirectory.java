package com.kloudtek.unpack;

import java.io.File;

public class FSSourceDirectory extends SourceDirectory {
    private final File file;

    public FSSourceDirectory(File file, SourceDirectory parent) {
        super(file.getName(), parent);
        this.file = file;
    }
}
