package com.kloudtek.unpack;

import com.kloudtek.unpack.Source;

import java.io.File;
import java.util.List;

public class ZipSource extends Source {
    private File file;

    public ZipSource(File file) {
        this.file = file;
        path = "";
    }

    @Override
    public List<Source> getFiles() {
        return null;
    }
}
