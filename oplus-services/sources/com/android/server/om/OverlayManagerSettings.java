package com.android.server.om;

import android.content.om.OverlayIdentifier;
import android.content.om.OverlayInfo;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Pair;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.XmlUtils;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.om.OverlayManagerSettings;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public final class OverlayManagerSettings {
    private final ArrayList<SettingsItem> mItems = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public OverlayInfo init(OverlayIdentifier overlayIdentifier, int i, String str, String str2, String str3, boolean z, boolean z2, int i2, String str4, boolean z3) {
        remove(overlayIdentifier, i);
        SettingsItem settingsItem = new SettingsItem(overlayIdentifier, i, str, str2, str3, -1, z2, z, i2, str4, z3);
        insert(settingsItem);
        return settingsItem.getOverlayInfo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean remove(OverlayIdentifier overlayIdentifier, int i) {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            return false;
        }
        this.mItems.remove(select);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OverlayInfo getOverlayInfo(OverlayIdentifier overlayIdentifier, int i) throws BadKeyException {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            throw new BadKeyException(overlayIdentifier, i);
        }
        return this.mItems.get(select).getOverlayInfo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OverlayInfo getNullableOverlayInfo(OverlayIdentifier overlayIdentifier, int i) {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            return null;
        }
        return this.mItems.get(select).getOverlayInfo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setBaseCodePath(OverlayIdentifier overlayIdentifier, int i, String str) throws BadKeyException {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            throw new BadKeyException(overlayIdentifier, i);
        }
        return this.mItems.get(select).setBaseCodePath(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setCategory(OverlayIdentifier overlayIdentifier, int i, String str) throws BadKeyException {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            throw new BadKeyException(overlayIdentifier, i);
        }
        return this.mItems.get(select).setCategory(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getEnabled(OverlayIdentifier overlayIdentifier, int i) throws BadKeyException {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            throw new BadKeyException(overlayIdentifier, i);
        }
        return this.mItems.get(select).isEnabled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setEnabled(OverlayIdentifier overlayIdentifier, int i, boolean z) throws BadKeyException {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            throw new BadKeyException(overlayIdentifier, i);
        }
        return this.mItems.get(select).setEnabled(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getState(OverlayIdentifier overlayIdentifier, int i) throws BadKeyException {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            throw new BadKeyException(overlayIdentifier, i);
        }
        return this.mItems.get(select).getState();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setState(OverlayIdentifier overlayIdentifier, int i, int i2) throws BadKeyException {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            throw new BadKeyException(overlayIdentifier, i);
        }
        return this.mItems.get(select).setState(i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<OverlayInfo> getOverlaysForTarget(String str, int i) {
        return CollectionUtils.map(selectWhereTarget(str, i), new Function() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                OverlayInfo overlayInfo;
                overlayInfo = ((OverlayManagerSettings.SettingsItem) obj).getOverlayInfo();
                return overlayInfo;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayMap<String, List<OverlayInfo>> getOverlaysForUser(int i) {
        List<SettingsItem> selectWhereUser = selectWhereUser(i);
        ArrayMap<String, List<OverlayInfo>> arrayMap = new ArrayMap<>();
        int size = selectWhereUser.size();
        for (int i2 = 0; i2 < size; i2++) {
            SettingsItem settingsItem = selectWhereUser.get(i2);
            arrayMap.computeIfAbsent(settingsItem.mTargetPackageName, new Function() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda4
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    List lambda$getOverlaysForUser$0;
                    lambda$getOverlaysForUser$0 = OverlayManagerSettings.lambda$getOverlaysForUser$0((String) obj);
                    return lambda$getOverlaysForUser$0;
                }
            }).add(settingsItem.getOverlayInfo());
        }
        return arrayMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ List lambda$getOverlaysForUser$0(String str) {
        return new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<String> getAllBaseCodePaths() {
        final ArraySet arraySet = new ArraySet();
        this.mItems.forEach(new Consumer() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                OverlayManagerSettings.lambda$getAllBaseCodePaths$1(arraySet, (OverlayManagerSettings.SettingsItem) obj);
            }
        });
        return arraySet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getAllBaseCodePaths$1(Set set, SettingsItem settingsItem) {
        set.add(settingsItem.mBaseCodePath);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<Pair<OverlayIdentifier, String>> getAllIdentifiersAndBaseCodePaths() {
        final ArraySet arraySet = new ArraySet();
        this.mItems.forEach(new Consumer() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                OverlayManagerSettings.lambda$getAllIdentifiersAndBaseCodePaths$2(arraySet, (OverlayManagerSettings.SettingsItem) obj);
            }
        });
        return arraySet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$getAllIdentifiersAndBaseCodePaths$2(Set set, SettingsItem settingsItem) {
        set.add(new Pair(settingsItem.mOverlay, settingsItem.mBaseCodePath));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeIf$3(Predicate predicate, int i, OverlayInfo overlayInfo) {
        return predicate.test(overlayInfo) && overlayInfo.userId == i;
    }

    List<OverlayInfo> removeIf(final Predicate<OverlayInfo> predicate, final int i) {
        return removeIf(new Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$removeIf$3;
                lambda$removeIf$3 = OverlayManagerSettings.lambda$removeIf$3(predicate, i, (OverlayInfo) obj);
                return lambda$removeIf$3;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<OverlayInfo> removeIf(Predicate<OverlayInfo> predicate) {
        List list = null;
        for (int size = this.mItems.size() - 1; size >= 0; size--) {
            OverlayInfo overlayInfo = this.mItems.get(size).getOverlayInfo();
            if (predicate.test(overlayInfo)) {
                this.mItems.remove(size);
                list = CollectionUtils.add(list, overlayInfo);
            }
        }
        return CollectionUtils.emptyIfNull(list);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] getUsers() {
        return this.mItems.stream().mapToInt(new ToIntFunction() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda2
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                int userId;
                userId = ((OverlayManagerSettings.SettingsItem) obj).getUserId();
                return userId;
            }
        }).distinct().toArray();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeUser(final int i) {
        return this.mItems.removeIf(new Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$removeUser$4;
                lambda$removeUser$4 = OverlayManagerSettings.lambda$removeUser$4(i, (OverlayManagerSettings.SettingsItem) obj);
                return lambda$removeUser$4;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeUser$4(int i, SettingsItem settingsItem) {
        if (settingsItem.getUserId() != i) {
            return false;
        }
        if (!OverlayManagerService.DEBUG) {
            return true;
        }
        Slog.d("OverlayManager", "Removing overlay " + settingsItem.mOverlay + " for user " + i + " from settings because user was removed");
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPriority(OverlayIdentifier overlayIdentifier, int i, int i2) throws BadKeyException {
        int select = select(overlayIdentifier, i);
        if (select < 0) {
            throw new BadKeyException(overlayIdentifier, i);
        }
        SettingsItem settingsItem = this.mItems.get(select);
        this.mItems.remove(select);
        settingsItem.setPriority(i2);
        insert(settingsItem);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setPriority(OverlayIdentifier overlayIdentifier, OverlayIdentifier overlayIdentifier2, int i) {
        int select;
        int select2;
        if (overlayIdentifier.equals(overlayIdentifier2) || (select = select(overlayIdentifier, i)) < 0 || (select2 = select(overlayIdentifier2, i)) < 0) {
            return false;
        }
        SettingsItem settingsItem = this.mItems.get(select);
        if (!settingsItem.getTargetPackageName().equals(this.mItems.get(select2).getTargetPackageName())) {
            return false;
        }
        this.mItems.remove(select);
        int select3 = select(overlayIdentifier2, i) + 1;
        this.mItems.add(select3, settingsItem);
        return select != select3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setLowestPriority(OverlayIdentifier overlayIdentifier, int i) {
        int select = select(overlayIdentifier, i);
        if (select <= 0) {
            return false;
        }
        SettingsItem settingsItem = this.mItems.get(select);
        this.mItems.remove(settingsItem);
        this.mItems.add(0, settingsItem);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setHighestPriority(OverlayIdentifier overlayIdentifier, int i) {
        int select = select(overlayIdentifier, i);
        if (select < 0 || select == this.mItems.size() - 1) {
            return false;
        }
        SettingsItem settingsItem = this.mItems.get(select);
        this.mItems.remove(select);
        this.mItems.add(settingsItem);
        return true;
    }

    private void insert(SettingsItem settingsItem) {
        int size = this.mItems.size() - 1;
        while (size >= 0 && this.mItems.get(size).mPriority > settingsItem.getPriority()) {
            size--;
        }
        this.mItems.add(size + 1, settingsItem);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter, final DumpState dumpState) {
        Stream stream = this.mItems.stream();
        if (dumpState.getUserId() != -1) {
            stream = stream.filter(new Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda10
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$dump$5;
                    lambda$dump$5 = OverlayManagerSettings.lambda$dump$5(DumpState.this, (OverlayManagerSettings.SettingsItem) obj);
                    return lambda$dump$5;
                }
            });
        }
        if (dumpState.getPackageName() != null) {
            stream = stream.filter(new Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda11
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$dump$6;
                    lambda$dump$6 = OverlayManagerSettings.lambda$dump$6(DumpState.this, (OverlayManagerSettings.SettingsItem) obj);
                    return lambda$dump$6;
                }
            });
        }
        if (dumpState.getOverlayName() != null) {
            stream = stream.filter(new Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda12
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$dump$7;
                    lambda$dump$7 = OverlayManagerSettings.lambda$dump$7(DumpState.this, (OverlayManagerSettings.SettingsItem) obj);
                    return lambda$dump$7;
                }
            });
        }
        final IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        if (dumpState.getField() != null) {
            stream.forEach(new Consumer() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda13
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    OverlayManagerSettings.this.lambda$dump$8(indentingPrintWriter, dumpState, (OverlayManagerSettings.SettingsItem) obj);
                }
            });
        } else {
            stream.forEach(new Consumer() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda14
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    OverlayManagerSettings.this.lambda$dump$9(indentingPrintWriter, (OverlayManagerSettings.SettingsItem) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$dump$5(DumpState dumpState, SettingsItem settingsItem) {
        return settingsItem.mUserId == dumpState.getUserId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$dump$6(DumpState dumpState, SettingsItem settingsItem) {
        return settingsItem.mOverlay.getPackageName().equals(dumpState.getPackageName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$dump$7(DumpState dumpState, SettingsItem settingsItem) {
        return settingsItem.mOverlay.getOverlayName().equals(dumpState.getOverlayName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$8(IndentingPrintWriter indentingPrintWriter, DumpState dumpState, SettingsItem settingsItem) {
        dumpSettingsItemField(indentingPrintWriter, settingsItem, dumpState.getField());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dumpSettingsItem, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$9(IndentingPrintWriter indentingPrintWriter, SettingsItem settingsItem) {
        indentingPrintWriter.println(settingsItem.mOverlay + ":" + settingsItem.getUserId() + " {");
        indentingPrintWriter.increaseIndent();
        StringBuilder sb = new StringBuilder();
        sb.append("mPackageName...........: ");
        sb.append(settingsItem.mOverlay.getPackageName());
        indentingPrintWriter.println(sb.toString());
        indentingPrintWriter.println("mOverlayName...........: " + settingsItem.mOverlay.getOverlayName());
        indentingPrintWriter.println("mUserId................: " + settingsItem.getUserId());
        indentingPrintWriter.println("mTargetPackageName.....: " + settingsItem.getTargetPackageName());
        indentingPrintWriter.println("mTargetOverlayableName.: " + settingsItem.getTargetOverlayableName());
        indentingPrintWriter.println("mBaseCodePath..........: " + settingsItem.getBaseCodePath());
        indentingPrintWriter.println("mState.................: " + OverlayInfo.stateToString(settingsItem.getState()));
        indentingPrintWriter.println("mIsEnabled.............: " + settingsItem.isEnabled());
        indentingPrintWriter.println("mIsMutable.............: " + settingsItem.isMutable());
        indentingPrintWriter.println("mPriority..............: " + settingsItem.mPriority);
        indentingPrintWriter.println("mCategory..............: " + settingsItem.mCategory);
        indentingPrintWriter.println("mIsFabricated..........: " + settingsItem.mIsFabricated);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("}");
    }

    private void dumpSettingsItemField(IndentingPrintWriter indentingPrintWriter, SettingsItem settingsItem, String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1750736508:
                if (str.equals("targetoverlayablename")) {
                    c = 0;
                    break;
                }
                break;
            case -1248283232:
                if (str.equals("targetpackagename")) {
                    c = 1;
                    break;
                }
                break;
            case -1165461084:
                if (str.equals("priority")) {
                    c = 2;
                    break;
                }
                break;
            case -836029914:
                if (str.equals("userid")) {
                    c = 3;
                    break;
                }
                break;
            case -831052100:
                if (str.equals("ismutable")) {
                    c = 4;
                    break;
                }
                break;
            case -405989669:
                if (str.equals("overlayname")) {
                    c = 5;
                    break;
                }
                break;
            case 50511102:
                if (str.equals("category")) {
                    c = 6;
                    break;
                }
                break;
            case 109757585:
                if (str.equals("state")) {
                    c = 7;
                    break;
                }
                break;
            case 440941271:
                if (str.equals("isenabled")) {
                    c = '\b';
                    break;
                }
                break;
            case 909712337:
                if (str.equals("packagename")) {
                    c = '\t';
                    break;
                }
                break;
            case 1693907299:
                if (str.equals("basecodepath")) {
                    c = '\n';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                indentingPrintWriter.println(settingsItem.mTargetOverlayableName);
                return;
            case 1:
                indentingPrintWriter.println(settingsItem.mTargetPackageName);
                return;
            case 2:
                indentingPrintWriter.println(settingsItem.mPriority);
                return;
            case 3:
                indentingPrintWriter.println(settingsItem.mUserId);
                return;
            case 4:
                indentingPrintWriter.println(settingsItem.mIsMutable);
                return;
            case 5:
                indentingPrintWriter.println(settingsItem.mOverlay.getOverlayName());
                return;
            case 6:
                indentingPrintWriter.println(settingsItem.mCategory);
                return;
            case 7:
                indentingPrintWriter.println(OverlayInfo.stateToString(settingsItem.mState));
                return;
            case '\b':
                indentingPrintWriter.println(settingsItem.mIsEnabled);
                return;
            case '\t':
                indentingPrintWriter.println(settingsItem.mOverlay.getPackageName());
                return;
            case '\n':
                indentingPrintWriter.println(settingsItem.mBaseCodePath);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restore(InputStream inputStream) throws IOException, XmlPullParserException {
        Serializer.restore(this.mItems, inputStream);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void persist(OutputStream outputStream) throws IOException, XmlPullParserException {
        Serializer.persist(this.mItems, outputStream);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class Serializer {
        private static final String ATTR_BASE_CODE_PATH = "baseCodePath";
        private static final String ATTR_CATEGORY = "category";
        private static final String ATTR_IS_ENABLED = "isEnabled";
        private static final String ATTR_IS_FABRICATED = "fabricated";
        private static final String ATTR_IS_STATIC = "isStatic";
        private static final String ATTR_OVERLAY_NAME = "overlayName";
        private static final String ATTR_PACKAGE_NAME = "packageName";
        private static final String ATTR_PRIORITY = "priority";
        private static final String ATTR_STATE = "state";
        private static final String ATTR_TARGET_OVERLAYABLE_NAME = "targetOverlayableName";
        private static final String ATTR_TARGET_PACKAGE_NAME = "targetPackageName";
        private static final String ATTR_USER_ID = "userId";
        private static final String ATTR_VERSION = "version";

        @VisibleForTesting
        static final int CURRENT_VERSION = 4;
        private static final String TAG_ITEM = "item";
        private static final String TAG_OVERLAYS = "overlays";

        Serializer() {
        }

        public static void restore(ArrayList<SettingsItem> arrayList, InputStream inputStream) throws IOException, XmlPullParserException {
            arrayList.clear();
            TypedXmlPullParser resolvePullParser = Xml.resolvePullParser(inputStream);
            XmlUtils.beginDocument(resolvePullParser, TAG_OVERLAYS);
            int attributeInt = resolvePullParser.getAttributeInt((String) null, ATTR_VERSION);
            if (attributeInt != 4) {
                upgrade(attributeInt);
            }
            int depth = resolvePullParser.getDepth();
            while (XmlUtils.nextElementWithin(resolvePullParser, depth)) {
                if ("item".equals(resolvePullParser.getName())) {
                    arrayList.add(restoreRow(resolvePullParser, depth + 1));
                }
            }
        }

        private static void upgrade(int i) throws XmlPullParserException {
            if (i == 0 || i == 1 || i == 2) {
                throw new XmlPullParserException("old version " + i + "; ignoring");
            }
            if (i == 3) {
                return;
            }
            throw new XmlPullParserException("unrecognized version " + i);
        }

        private static SettingsItem restoreRow(TypedXmlPullParser typedXmlPullParser, int i) throws IOException, XmlPullParserException {
            return new SettingsItem(new OverlayIdentifier(XmlUtils.readStringAttribute(typedXmlPullParser, "packageName"), XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_OVERLAY_NAME)), typedXmlPullParser.getAttributeInt((String) null, "userId"), XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_TARGET_PACKAGE_NAME), XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_TARGET_OVERLAYABLE_NAME), XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_BASE_CODE_PATH), typedXmlPullParser.getAttributeInt((String) null, "state"), typedXmlPullParser.getAttributeBoolean((String) null, ATTR_IS_ENABLED, false), !typedXmlPullParser.getAttributeBoolean((String) null, ATTR_IS_STATIC, false), typedXmlPullParser.getAttributeInt((String) null, ATTR_PRIORITY), XmlUtils.readStringAttribute(typedXmlPullParser, ATTR_CATEGORY), typedXmlPullParser.getAttributeBoolean((String) null, ATTR_IS_FABRICATED, false));
        }

        public static void persist(ArrayList<SettingsItem> arrayList, OutputStream outputStream) throws IOException, XmlPullParserException {
            TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(outputStream);
            resolveSerializer.startDocument((String) null, Boolean.TRUE);
            resolveSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            resolveSerializer.startTag((String) null, TAG_OVERLAYS);
            resolveSerializer.attributeInt((String) null, ATTR_VERSION, 4);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                persistRow(resolveSerializer, arrayList.get(i));
            }
            resolveSerializer.endTag((String) null, TAG_OVERLAYS);
            resolveSerializer.endDocument();
        }

        private static void persistRow(TypedXmlSerializer typedXmlSerializer, SettingsItem settingsItem) throws IOException {
            typedXmlSerializer.startTag((String) null, "item");
            XmlUtils.writeStringAttribute(typedXmlSerializer, "packageName", settingsItem.mOverlay.getPackageName());
            XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_OVERLAY_NAME, settingsItem.mOverlay.getOverlayName());
            typedXmlSerializer.attributeInt((String) null, "userId", settingsItem.mUserId);
            XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_TARGET_PACKAGE_NAME, settingsItem.mTargetPackageName);
            XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_TARGET_OVERLAYABLE_NAME, settingsItem.mTargetOverlayableName);
            XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_BASE_CODE_PATH, settingsItem.mBaseCodePath);
            typedXmlSerializer.attributeInt((String) null, "state", settingsItem.mState);
            XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_IS_ENABLED, settingsItem.mIsEnabled);
            XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_IS_STATIC, !settingsItem.mIsMutable);
            typedXmlSerializer.attributeInt((String) null, ATTR_PRIORITY, settingsItem.mPriority);
            XmlUtils.writeStringAttribute(typedXmlSerializer, ATTR_CATEGORY, settingsItem.mCategory);
            XmlUtils.writeBooleanAttribute(typedXmlSerializer, ATTR_IS_FABRICATED, settingsItem.mIsFabricated);
            typedXmlSerializer.endTag((String) null, "item");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class SettingsItem {
        private String mBaseCodePath;
        private OverlayInfo mCache = null;
        private String mCategory;
        private boolean mIsEnabled;
        private boolean mIsFabricated;
        private boolean mIsMutable;
        private final OverlayIdentifier mOverlay;
        private int mPriority;
        private int mState;
        private final String mTargetOverlayableName;
        private final String mTargetPackageName;
        private final int mUserId;

        SettingsItem(OverlayIdentifier overlayIdentifier, int i, String str, String str2, String str3, int i2, boolean z, boolean z2, int i3, String str4, boolean z3) {
            this.mOverlay = overlayIdentifier;
            this.mUserId = i;
            this.mTargetPackageName = str;
            this.mTargetOverlayableName = str2;
            this.mBaseCodePath = str3;
            this.mState = i2;
            this.mIsEnabled = z;
            this.mCategory = str4;
            this.mIsMutable = z2;
            this.mPriority = i3;
            this.mIsFabricated = z3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getTargetPackageName() {
            return this.mTargetPackageName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getTargetOverlayableName() {
            return this.mTargetOverlayableName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getUserId() {
            return this.mUserId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getBaseCodePath() {
            return this.mBaseCodePath;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setBaseCodePath(String str) {
            if (this.mBaseCodePath.equals(str)) {
                return false;
            }
            this.mBaseCodePath = str;
            invalidateCache();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getState() {
            return this.mState;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setState(int i) {
            if (this.mState == i) {
                return false;
            }
            this.mState = i;
            invalidateCache();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEnabled() {
            return this.mIsEnabled;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setEnabled(boolean z) {
            if (!this.mIsMutable || this.mIsEnabled == z) {
                return false;
            }
            this.mIsEnabled = z;
            invalidateCache();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setCategory(String str) {
            if (Objects.equals(this.mCategory, str)) {
                return false;
            }
            this.mCategory = str == null ? null : str.intern();
            invalidateCache();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public OverlayInfo getOverlayInfo() {
            if (this.mCache == null) {
                this.mCache = new OverlayInfo(this.mOverlay.getPackageName(), this.mOverlay.getOverlayName(), this.mTargetPackageName, this.mTargetOverlayableName, this.mCategory, this.mBaseCodePath, this.mState, this.mUserId, this.mPriority, this.mIsMutable, this.mIsFabricated);
            }
            return this.mCache;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPriority(int i) {
            this.mPriority = i;
            invalidateCache();
        }

        private void invalidateCache() {
            this.mCache = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isMutable() {
            return this.mIsMutable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getPriority() {
            return this.mPriority;
        }
    }

    private int select(OverlayIdentifier overlayIdentifier, int i) {
        int size = this.mItems.size();
        for (int i2 = 0; i2 < size; i2++) {
            SettingsItem settingsItem = this.mItems.get(i2);
            if (settingsItem.mUserId == i && settingsItem.mOverlay.equals(overlayIdentifier)) {
                return i2;
            }
        }
        return -1;
    }

    private List<SettingsItem> selectWhereUser(final int i) {
        ArrayList arrayList = new ArrayList();
        CollectionUtils.addIf(this.mItems, arrayList, new Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$selectWhereUser$10;
                lambda$selectWhereUser$10 = OverlayManagerSettings.lambda$selectWhereUser$10(i, (OverlayManagerSettings.SettingsItem) obj);
                return lambda$selectWhereUser$10;
            }
        });
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$selectWhereUser$10(int i, SettingsItem settingsItem) {
        return settingsItem.mUserId == i;
    }

    private List<SettingsItem> selectWhereOverlay(final String str, int i) {
        List<SettingsItem> selectWhereUser = selectWhereUser(i);
        selectWhereUser.removeIf(new Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$selectWhereOverlay$11;
                lambda$selectWhereOverlay$11 = OverlayManagerSettings.lambda$selectWhereOverlay$11(str, (OverlayManagerSettings.SettingsItem) obj);
                return lambda$selectWhereOverlay$11;
            }
        });
        return selectWhereUser;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$selectWhereOverlay$11(String str, SettingsItem settingsItem) {
        return !settingsItem.mOverlay.getPackageName().equals(str);
    }

    private List<SettingsItem> selectWhereTarget(final String str, int i) {
        List<SettingsItem> selectWhereUser = selectWhereUser(i);
        selectWhereUser.removeIf(new Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$selectWhereTarget$12;
                lambda$selectWhereTarget$12 = OverlayManagerSettings.lambda$selectWhereTarget$12(str, (OverlayManagerSettings.SettingsItem) obj);
                return lambda$selectWhereTarget$12;
            }
        });
        return selectWhereUser;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$selectWhereTarget$12(String str, SettingsItem settingsItem) {
        return !settingsItem.getTargetPackageName().equals(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static final class BadKeyException extends Exception {
        BadKeyException(OverlayIdentifier overlayIdentifier, int i) {
            super("Bad key '" + overlayIdentifier + "' for user " + i);
        }
    }
}
