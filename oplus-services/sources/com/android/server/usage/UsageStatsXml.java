package com.android.server.usage;

import android.util.Slog;
import android.util.Xml;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class UsageStatsXml {
    static final String CHECKED_IN_SUFFIX = "-c";
    private static final String TAG = "UsageStatsXml";
    private static final String USAGESTATS_TAG = "usagestats";
    private static final String VERSION_ATTR = "version";

    public static void read(InputStream inputStream, IntervalStats intervalStats) throws IOException {
        XmlPullParser newPullParser = Xml.newPullParser();
        try {
            newPullParser.setInput(inputStream, "utf-8");
            XmlUtils.beginDocument(newPullParser, USAGESTATS_TAG);
            String attributeValue = newPullParser.getAttributeValue(null, VERSION_ATTR);
            try {
                if (Integer.parseInt(attributeValue) == 1) {
                    UsageStatsXmlV1.read(newPullParser, intervalStats);
                    return;
                }
                Slog.e(TAG, "Unrecognized version " + attributeValue);
                throw new IOException("Unrecognized version " + attributeValue);
            } catch (NumberFormatException e) {
                Slog.e(TAG, "Bad version");
                throw new IOException(e);
            }
        } catch (XmlPullParserException e2) {
            Slog.e(TAG, "Failed to parse Xml", e2);
            throw new IOException(e2);
        }
    }
}
