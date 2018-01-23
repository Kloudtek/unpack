package com.kloudtek.unpack;

import com.kloudtek.util.FileUtils;
import com.kloudtek.util.StringUtils;

import java.io.File;
import java.io.IOException;

public class FSSource extends Source {
    public FSSource(File file) throws UnpackException {
        listFiles(file,null);
    }

    private void listFiles(File file, FSSourceDirectory parent ) throws UnpackException {
        try {
            SourceFile srcFile;
            if( file.isDirectory() ) {
                FSSourceDirectory dir = new FSSourceDirectory(file, parent);
                for (File f : FileUtils.listFileInDir(file)) {
                    listFiles(f, dir);
                }
                srcFile = dir;
            } else {
                srcFile = new FSSourceFile(file, parent);
            }
            if( parent == null ) {
                files.add(srcFile);
            }
            allFiles.add(srcFile);
        } catch (IOException e) {
            throw new UnpackException(e);
        }
    }
}
