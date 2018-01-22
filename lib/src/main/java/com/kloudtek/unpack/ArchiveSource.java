package com.kloudtek.unpack;

import com.kloudtek.unpack.Source;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

import java.util.List;

public class ArchiveSource extends Source {
    public ArchiveSource() {
        CompressorStreamFactory compressorStreamFactory = new CompressorStreamFactory();
    }

    @Override
    public List<Source> getFiles() {
        return null;
    }
}
