package com.oplus.wrapper.graphics;

import android.content.res.Resources;
import android.graphics.ImageDecoder;
import java.io.InputStream;

/* loaded from: classes.dex */
public class ImageDecoder {
    public static ImageDecoder.Source createSource(Resources res, InputStream is) {
        return android.graphics.ImageDecoder.createSource(res, is);
    }
}
