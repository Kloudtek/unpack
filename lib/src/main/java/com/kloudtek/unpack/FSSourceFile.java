package com.kloudtek.unpack;

import com.kloudtek.util.FileUtils;
import com.kloudtek.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FSSourceFile extends SourceFile {
    private File file;
    private String path;

    protected FSSourceFile(File file, FSSourceDirectory parent) {
        super(file.getName(),parent);
    }
}
