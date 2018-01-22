package com.kloudtek.unpack;

import com.kloudtek.unpack.Destination;
import com.kloudtek.unpack.Source;
import com.kloudtek.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FSSource extends Source {
    private File file;
    private String path;

    protected FSSource(File file, String path) {
        this.file = file;
        this.path = path;
    }

    public FSSource(File file) throws IOException {
        this.file = file;
        path = "";
        listFiles(file,path);
    }

    @Override
    public List<Source> getFiles() {
        return null;
    }

    private void listFiles(File file, String path) throws IOException {
        if( file.isDirectory() ) {
            for (File f : FileUtils.listFileInDir(file)) {
                files.add(new FSSource())
            }
        } else {
            files.add(new FSSource(file,path));
        }
    }
}
