package com.kloudtek.unpack;

import com.kloudtek.util.FileUtils;
import com.kloudtek.util.StringUtils;

import java.io.File;
import java.io.IOException;

public class FSSource extends Source {
    private File file;

    public FSSource(File file) throws UnpackException {
        this.file = file;
    }

    @Override
    public void read() throws UnpackException {
        try {
            if (!file.exists()) {
                FileUtils.mkdir(file);
            } else if (!file.isDirectory()) {
                throw new UnpackException("File isn't a directory: " + file.getPath());
            }
            for (File f : FileUtils.listFileInDir(file)) {
                listFiles(f, null);
            }
        } catch (IOException e) {
            throw new UnpackException(e.getMessage(), e);
        }
    }

    private void listFiles(File file, FSSourceDirectory parent) throws IOException {
        SourceFile srcFile;
        if (file.isDirectory()) {
            FSSourceDirectory dir = new FSSourceDirectory(file, parent);
            for (File f : FileUtils.listFileInDir(file)) {
                listFiles(f, dir);
            }
            srcFile = dir;
        } else {
            srcFile = new FSSourceFile(file, parent);
        }
        if (parent == null) {
            files.add(srcFile);
        } else {
            parent.add(srcFile);
        }
        allFiles.add(srcFile);
    }
}
