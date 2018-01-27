package com.kloudtek.unpack.transformer;

import com.kloudtek.unpack.*;
import com.kloudtek.util.FileUtils;
import com.kloudtek.util.io.IOUtils;
import com.kloudtek.util.io.InMemInputFilterStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;

public class SetPropertyTransformer extends Transformer {
    private String path;
    private final HashMap<String, String> properties = new HashMap<>();

    public SetPropertyTransformer(String path, HashMap<String, String> properties) {
        this.path = path;
        this.properties.putAll(properties);
    }

    public SetPropertyTransformer(String path, String propertyKey, String propertyValue) {
        this.path = path;
        this.properties.put(propertyKey, propertyValue);
    }

    @Override
    public void apply(Source source, Destination destination) throws UnpackException {
        try {
            UFile file = source.getFile(path);
            Properties p = new Properties();
            if (file == null) {
                p.putAll(properties);
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                p.store(buf, "Created by Unpack");
                source.add(new InMemSourceFile(FileUtils.splitFileNameFromParentPath(path, '/').getFilename(), path, buf.toByteArray()));
            } else {
                if (file instanceof SourceFile) {
                    ((SourceFile) file).setInputStream(new InMemInputFilterStream(((SourceFile) file).getInputStream()) {
                        @Override
                        protected byte[] transform(byte[] data) throws IOException {
                            return IOUtils.toByteArray(os -> {
                                Properties p = new Properties();
                                p.load(new ByteArrayInputStream(data));
                                p.putAll(properties);
                                p.store(os,"Updated by Unpack");
                            });
                        }
                    });
                } else {
                    throw new UnpackException("Cannot set properties on " + path + " as it is not a file but instead is of type " + file.getClass().getName());
                }
            }
        } catch (IOException e) {
            throw new UnpackException(e.getMessage(), e);
        }
    }
}
