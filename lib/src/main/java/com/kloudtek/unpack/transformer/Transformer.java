package com.kloudtek.unpack.transformer;

import com.kloudtek.unpack.Destination;
import com.kloudtek.unpack.Source;
import com.kloudtek.unpack.UnpackException;

public abstract class Transformer {
    public abstract void apply(Source source, Destination destination) throws UnpackException;
}
