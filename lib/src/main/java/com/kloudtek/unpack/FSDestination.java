package com.kloudtek.unpack;

import com.kloudtek.util.FileUtils;
import com.kloudtek.util.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FSDestination extends Destination {
    private File file;

    public FSDestination(File file) throws UnpackException {
        this.file = file;
        try {
            FileUtils.mkdirs(file);
        } catch (IOException e) {
            throw new UnpackException(e.getMessage(),e);
        }
    }

    @Override
    public void write(UFile sourceFile) throws UnpackException {
        try {
            File destFile = new File(file.getAbsolutePath()+File.separator+sourceFile.getPath().replace("/",File.separator));
            if( sourceFile instanceof SourceDirectory ) {
                FileUtils.mkdir(destFile);
            } else if( sourceFile instanceof SourceFile ) {
                try( InputStream is = ((SourceFile) sourceFile).createInputStream(); FileOutputStream os = new FileOutputStream(destFile)) {
                    IOUtils.copy(is,os);
                }
            }
        } catch (IOException e) {
            throw new UnpackException(e.getMessage(), e);
        }
    }
}
