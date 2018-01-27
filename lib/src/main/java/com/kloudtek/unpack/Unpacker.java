package com.kloudtek.unpack;

import com.kloudtek.unpack.transformer.SetPropertyTransformer;
import com.kloudtek.unpack.transformer.Transformer;
import com.kloudtek.util.io.IOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Unpacker {
    private final List<Transformer> transformers = new ArrayList<>();
    private Source source;
    private Destination destination;

    public Unpacker(File file, FileType fileType, File destination, FileType destinationType) throws UnpackException {
        this.source = Source.create(file, fileType);
        this.destination = Destination.create(destination, destinationType);
    }

    public void unpack() throws UnpackException {
        try {
            source.read();
            for (Transformer transformer : transformers) {
                transformer.apply(source,destination);
            }
            source.sort();
            for (UFile sourceFile : source.getFiles()) {
                destination.write(sourceFile);
            }
        } finally {
            IOUtils.close(source,destination);
        }
    }

    public void addTransformer(SetPropertyTransformer transformer) {
        transformers.add(transformer);
    }
}
