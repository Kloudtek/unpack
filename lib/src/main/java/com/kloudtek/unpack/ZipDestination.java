package com.kloudtek.unpack;

import com.kloudtek.util.io.IOUtils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDestination extends Destination {
    private FileOutputStream fos;
    private ZipOutputStream os;

    public ZipDestination(File file) throws UnpackException {
        try {
            fos = new FileOutputStream(file);
            os = new ZipOutputStream(fos);
        } catch (FileNotFoundException e) {
            throw new UnpackException(e.getMessage(), e);
        }
    }

    @Override
    public void write(UFile sourceFile) throws UnpackException {
        if (sourceFile instanceof SourceFile) {
            try (InputStream sis = ((SourceFile) sourceFile).createInputStream()) {
                ZipEntry zipEntry = new ZipEntry(sourceFile.getPath());
                os.putNextEntry(zipEntry);
                IOUtils.copy(sis,os);
                os.closeEntry();
            } catch (IOException e) {
                throw new UnpackException(e);
            }
        }
    }

    @Override
    public void close() throws IOException {
        IOUtils.close(os,fos);
    }
}
