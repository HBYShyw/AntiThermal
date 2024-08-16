package com.android.server.display.config;

import java.io.IOException;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class BlockingZoneConfig {
    private BlockingZoneThreshold blockingZoneThreshold;
    private BigInteger defaultRefreshRate;

    public final BigInteger getDefaultRefreshRate() {
        return this.defaultRefreshRate;
    }

    boolean hasDefaultRefreshRate() {
        return this.defaultRefreshRate != null;
    }

    public final void setDefaultRefreshRate(BigInteger bigInteger) {
        this.defaultRefreshRate = bigInteger;
    }

    public final BlockingZoneThreshold getBlockingZoneThreshold() {
        return this.blockingZoneThreshold;
    }

    boolean hasBlockingZoneThreshold() {
        return this.blockingZoneThreshold != null;
    }

    public final void setBlockingZoneThreshold(BlockingZoneThreshold blockingZoneThreshold) {
        this.blockingZoneThreshold = blockingZoneThreshold;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BlockingZoneConfig read(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DatatypeConfigurationException {
        int next;
        BlockingZoneConfig blockingZoneConfig = new BlockingZoneConfig();
        xmlPullParser.getDepth();
        while (true) {
            next = xmlPullParser.next();
            if (next == 1 || next == 3) {
                break;
            }
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("defaultRefreshRate")) {
                    blockingZoneConfig.setDefaultRefreshRate(new BigInteger(XmlParser.readText(xmlPullParser)));
                } else if (name.equals("blockingZoneThreshold")) {
                    blockingZoneConfig.setBlockingZoneThreshold(BlockingZoneThreshold.read(xmlPullParser));
                } else {
                    XmlParser.skip(xmlPullParser);
                }
            }
        }
        if (next == 3) {
            return blockingZoneConfig;
        }
        throw new DatatypeConfigurationException("BlockingZoneConfig is not closed");
    }
}
