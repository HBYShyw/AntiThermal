package com.android.server.permission.access.appop;

import android.util.ArrayMap;
import android.util.Log;
import com.android.modules.utils.BinaryXmlPullParser;
import com.android.modules.utils.BinaryXmlSerializer;
import com.android.server.permission.access.AccessState;
import com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker;
import com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: BaseAppOpPersistence.kt */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public abstract class BaseAppOpPersistence {

    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = BaseAppOpPersistence.class.getSimpleName();

    public abstract void parseUserState(@NotNull BinaryXmlPullParser binaryXmlPullParser, @NotNull AccessState accessState, int i);

    public abstract void serializeUserState(@NotNull BinaryXmlSerializer binaryXmlSerializer, @NotNull AccessState accessState, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00a1, code lost:
    
        r0 = r10.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00a5, code lost:
    
        if (r0 == 1) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00a7, code lost:
    
        if (r0 == 2) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00a9, code lost:
    
        if (r0 == 3) goto L63;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void parseAppOps(@NotNull BinaryXmlPullParser binaryXmlPullParser, @NotNull ArrayMap<String, Integer> arrayMap) {
        int next;
        int next2;
        int next3;
        int eventType = binaryXmlPullParser.getEventType();
        if (eventType != 0 && eventType != 2) {
            throw new XmlPullParserException("Unexpected event type " + eventType);
        }
        do {
            next = binaryXmlPullParser.next();
            if (next == 1 || next == 2) {
                break;
            }
        } while (next != 3);
        while (true) {
            int eventType2 = binaryXmlPullParser.getEventType();
            if (eventType2 == 1) {
                return;
            }
            if (eventType2 != 2) {
                if (eventType2 == 3) {
                    return;
                }
                throw new XmlPullParserException("Unexpected event type " + eventType2);
            }
            int depth = binaryXmlPullParser.getDepth();
            if (Intrinsics.areEqual(binaryXmlPullParser.getName(), "app-op")) {
                parseAppOp(binaryXmlPullParser, arrayMap);
            } else {
                Log.w(LOG_TAG, "Ignoring unknown tag " + binaryXmlPullParser.getName() + " when parsing app-op state");
            }
            int depth2 = binaryXmlPullParser.getDepth();
            if (depth2 != depth) {
                throw new XmlPullParserException("Unexpected post-block depth " + depth2 + ", expected " + depth);
            }
            while (true) {
                int eventType3 = binaryXmlPullParser.getEventType();
                if (eventType3 == 2) {
                    do {
                        next3 = binaryXmlPullParser.next();
                        if (next3 != 1 && next3 != 2) {
                        }
                    } while (next3 != 3);
                } else {
                    if (eventType3 != 3) {
                        throw new XmlPullParserException("Unexpected event type " + eventType3);
                    }
                    if (binaryXmlPullParser.getDepth() <= depth) {
                        break;
                    }
                    do {
                        next2 = binaryXmlPullParser.next();
                        if (next2 != 1 && next2 != 2) {
                        }
                    } while (next2 != 3);
                }
            }
        }
    }

    private final void serializeAppOp(BinaryXmlSerializer binaryXmlSerializer, String str, int i) {
        binaryXmlSerializer.startTag((String) null, "app-op");
        binaryXmlSerializer.attributeInterned((String) null, "name", str);
        binaryXmlSerializer.attributeInt((String) null, "mode", i);
        binaryXmlSerializer.endTag((String) null, "app-op");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void serializeAppOps(@NotNull BinaryXmlSerializer binaryXmlSerializer, @NotNull ArrayMap<String, Integer> arrayMap) {
        int size = arrayMap.size();
        for (int i = 0; i < size; i++) {
            serializeAppOp(binaryXmlSerializer, arrayMap.keyAt(i), arrayMap.valueAt(i).intValue());
        }
    }

    /* compiled from: BaseAppOpPersistence.kt */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final void parseAppOp(BinaryXmlPullParser binaryXmlPullParser, ArrayMap<String, Integer> arrayMap) {
        String intern = binaryXmlPullParser.getAttributeValue(binaryXmlPullParser.getAttributeIndexOrThrow((String) null, "name")).intern();
        Intrinsics.checkNotNullExpressionValue(intern, "this as java.lang.String).intern()");
        arrayMap.put(intern, Integer.valueOf(binaryXmlPullParser.getAttributeInt((String) null, "mode")));
    }
}
