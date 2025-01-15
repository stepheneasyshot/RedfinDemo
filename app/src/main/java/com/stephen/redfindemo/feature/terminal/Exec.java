package com.stephen.redfindemo.feature.terminal;

import java.io.IOException;

public class Exec {
    static native void setPtyWindowSizeInternal(int fd, int row, int col, int xpixel, int ypixel) throws IOException;

    static native void setPtyUTF8ModeInternal(int fd, boolean utf8Mode) throws IOException;
}

