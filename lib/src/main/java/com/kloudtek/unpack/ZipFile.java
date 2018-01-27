package com.kloudtek.unpack;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipFile extends SourceFile {
    private final java.util.zip.ZipFile zipFile;
    private final ZipEntry archiveEntry;

    public ZipFile(String name, java.util.zip.ZipFile zipFile, ZipEntry archiveEntry) {
        super(name,archiveEntry.getName());
        this.zipFile = zipFile;
        this.archiveEntry = archiveEntry;
    }

    @Override
    public InputStream createInputStream() throws UnpackException {
        try {
            return zipFile.getInputStream(archiveEntry);
        } catch (IOException e) {
            throw new UnpackException(e);
        }
    }
}
