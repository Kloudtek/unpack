package com.kloudtek.unpack;

import com.kloudtek.util.CircularDependencyException;
import com.kloudtek.util.SortUtils;
import com.kloudtek.util.TopologicalSortComparator;
import com.kloudtek.util.UnexpectedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public abstract class Source implements Closeable {
    protected List<UFile> files = new ArrayList<>();
    protected LinkedHashMap<String, UFile> filesIdx = new LinkedHashMap<>();

    public abstract void read() throws UnpackException;

    public List<UFile> getFiles() {
        return files;
    }

    public static Source create(File file, FileType fileType) throws UnpackException {
        switch (fileType) {
            case DIR:
                return new FSSource(file);
            case ZIP:
                return new ZipSource(file, fileType.getExtension());
            default:
                throw new UnexpectedException("Unexpected source type: " + fileType);
        }
    }

    @Override
    public void close() throws IOException {
    }

    public void add(UFile file) {
        files.add(file);
        filesIdx.put(file.getPath(),file);
    }

    public void refreshPathIndex() {
        filesIdx.clear();
        for (UFile file : files) {
            filesIdx.put(file.getPath(),file);
        }
    }

    public void sort() {
        try {
            files = SortUtils.topologicalSort(files, new TopologicalSortComparator<UFile>() {
                @Override
                public SortUtils.TopologicalSortRelationship getRelationship(UFile source, UFile target) {
                    return source.getPath().startsWith(target.getPath()) ? SortUtils.TopologicalSortRelationship.STRONG : SortUtils.TopologicalSortRelationship.NONE;
                }

                @Override
                public String getObjectRepresentation(UFile object) {
                    return object.getPath();
                }
            });
        } catch (CircularDependencyException e) {
            throw new UnexpectedException(e.getMessage(),e);
        }
    }

    @Nullable
    public UFile getFile(@NotNull String path) {
        return filesIdx.get(path);
    }

    private class UFileWrapper {
        private UFile file;
        private UFileWrapper parent;
    }
}
