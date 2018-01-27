package com.kloudtek.unpack;

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
                Matcher m = pathPattern.matcher(entry.getName());
                if (!m.find()) {
                    throw new UnexpectedException("path matching failed: " + entry.getName());
                }
                String name = m.group(2);
                String parentPath = m.group(1);
                if (StringUtils.isNotBlank(parentPath)) {
                    parentPath = parentPath.substring(0,parentPath.length()-1);
                    StringBuilder pathBuilder = null;
                    for (String pathEl : parentPath.split("/")) {
                        if( StringUtils.isNotBlank(pathEl)) {
                            if( pathBuilder == null ) {
                                pathBuilder = new StringBuilder(pathEl);
                            } else {
                                pathBuilder.append('/').append(pathEl);
                            }
                            add(new SourceDirectory(pathEl,pathBuilder.toString()));
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
