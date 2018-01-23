package com.kloudtek.unpack;

import com.kloudtek.util.FileUtils;
import com.kloudtek.util.TempDir;
import com.kloudtek.util.io.IOUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Unpacker {
    private Source source;
    private Destination destination;

    public Unpacker(File file, FileType fileType, File destination, FileType destinationType) throws UnpackException {
        this.source = Source.create(file, fileType);
        this.destination = Destination.create(destination, destinationType);
    }

    public void unpack() throws UnpackException {
    }
}
