package com.kloudtek.unpack;

import java.util.ArrayList;
import java.util.List;

public class SourceDirectory extends SourceFile {
    private List<SourceFile> files = new ArrayList<>();

    public SourceDirectory(String name, SourceDirectory parent) {
        super(name, parent);
    }
}
