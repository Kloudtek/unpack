package com.kloudtek.unpack;

import com.kloudtek.util.FileUtils;
import com.kloudtek.util.StringUtils;

import java.io.*;
import java.util.List;

public class FSSourceFile extends SourceFile {
    private File file;

    protected FSSourceFile(File file, String path) {
        super(file.getName(),path);
        this.file = file;
    }

    @Override
    protected InputStream createInputStream() throws UnpackException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new UnpackException(e.getMessage(),e);
        }
    }
}
