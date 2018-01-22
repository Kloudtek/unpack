package com.kloudtek.unpack;

public enum FileType {
    DIR, ZIP, TAR, TGZ, TBZ2;

    public String getExtension() {
        return name().toLowerCase();
    }
}
