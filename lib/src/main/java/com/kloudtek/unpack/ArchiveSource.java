package com.kloudtek.unpack;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class ArchiveSource extends Source {
    private final File file;
    private final String extension;
    private ArchiveInputStream is;

    public ArchiveSource(File file, String extension) {
        this.file = file;
        this.extension = extension;
    }

    @Override
    public void read() throws UnpackException {
        try (FileInputStream fis = new FileInputStream(file)) {
            is = new ArchiveStreamFactory().createArchiveInputStream(extension, fis);
            for (ArchiveEntry entry = is.getNextEntry(); entry != null; entry = is.getNextEntry()) {
                LinkedList<String> pathEls = new LinkedList<>(Arrays.asList(entry.getName().split("/")));
                pathEls.removeLast();
            }
        } catch (IOException | ArchiveException e) {
            throw new UnpackException(e.getMessage(), e);
        }
    }
}
