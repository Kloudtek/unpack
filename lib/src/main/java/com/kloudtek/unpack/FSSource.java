package com.kloudtek.unpack;

import com.kloudtek.util.FileUtils;

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
                throw new UnpackException("Source directory doesn't exist: " + file.getPath());
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
        UFile srcFile;
        String path = parent != null ? parent.getPath()+"/"+file.getName() : file.getName();
        if (file.isDirectory()) {
            FSSourceDirectory dir = new FSSourceDirectory(file, path);
            for (File f : FileUtils.listFileInDir(file)) {
                listFiles(f, dir);
            }
            srcFile = dir;
        } else {
            srcFile = new FSSourceFile(file, path);
        }
        add(srcFile);
    }
}
