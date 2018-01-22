package com.kloudtek.unpack;

import com.kloudtek.unpack.Destination;

import java.io.File;

public class FSDestination extends Destination {
    private File file;

    public FSDestination(File file) {
        this.file = file;
    }
}
