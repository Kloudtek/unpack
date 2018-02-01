package com.kloudtek.unpack;

import com.kloudtek.util.FileUtils;
import com.kloudtek.util.StringUtils;
import com.kloudtek.util.UnexpectedException;
import com.kloudtek.util.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;

public class ZipSource extends Source {
    private final File file;
    private final String extension;
    private java.util.zip.ZipFile zipFile;

    public ZipSource(File file, String extension) {
        this.file = file;
        this.extension = extension;
    }

    @Override
    public void read() throws UnpackException {
        try {
            zipFile = new java.util.zip.ZipFile(file);
            java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                FileUtils.SplitPath splitPath = FileUtils.splitFileNameFromParentPath(entry.getName(), '/');
                String name = splitPath.getFilename();
                String parentPath = splitPath.getParentPath();
                if (StringUtils.isNotBlank(parentPath)) {
                    StringBuilder pathBuilder = null;
                    for (String pathEl : parentPath.split("/")) {
                        if( StringUtils.isNotBlank(pathEl)) {
                            if( pathBuilder == null ) {
                                pathBuilder = new StringBuilder(pathEl);
                            } else {
                                pathBuilder.append('/').append(pathEl);
                            }
                            String fullParentPath = pathBuilder.toString();
                            if( getFile(fullParentPath) == null ) {
                                add(new SourceDirectory(pathEl, fullParentPath));
                            }
                        }
                    }
                }
                add(new ZipFile(name, zipFile, entry));
            }
        } catch (IOException e) {
            throw new UnpackException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
        IOUtils.close(zipFile);
    }
}
