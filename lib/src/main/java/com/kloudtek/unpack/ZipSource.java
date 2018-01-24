package com.kloudtek.unpack;

import com.kloudtek.unpack.Source;

import java.io.File;
import java.util.List;

public class ZipSource extends Source {
    private File file;

    public ZipSource(File file) {
        this.file = file;
    }

    @Override
    public void read() throws UnpackException {

    }
}
