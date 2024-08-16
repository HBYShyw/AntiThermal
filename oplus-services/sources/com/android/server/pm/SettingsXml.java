package com.android.server.pm;

import android.util.Slog;
import android.util.Xml;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class SettingsXml {
    private static final boolean DEBUG_THROW_EXCEPTIONS = false;
    private static final int DEFAULT_NUMBER = -1;
    private static final String FEATURE_INDENT = "http://xmlpull.org/v1/doc/features.html#indent-output";
    private static final String TAG = "SettingsXml";

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ChildSection extends ReadSection {
        boolean moveToNext();

        boolean moveToNext(String str);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface ReadSection extends AutoCloseable {
        ChildSection children();

        boolean getBoolean(String str);

        boolean getBoolean(String str, boolean z);

        String getDescription();

        int getInt(String str);

        int getInt(String str, int i);

        long getLong(String str);

        long getLong(String str, int i);

        String getName();

        String getString(String str);

        String getString(String str, String str2);

        boolean has(String str);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public interface WriteSection extends AutoCloseable {
        WriteSection attribute(String str, int i) throws IOException;

        WriteSection attribute(String str, long j) throws IOException;

        WriteSection attribute(String str, String str2) throws IOException;

        WriteSection attribute(String str, boolean z) throws IOException;

        @Override // java.lang.AutoCloseable
        void close() throws IOException;

        void finish() throws IOException;

        WriteSection startSection(String str) throws IOException;
    }

    public static Serializer serializer(TypedXmlSerializer typedXmlSerializer) {
        return new Serializer(typedXmlSerializer);
    }

    public static ReadSection parser(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
        return new ReadSectionImpl(typedXmlPullParser);
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class Serializer implements AutoCloseable {
        private final WriteSectionImpl mWriteSection;
        private final TypedXmlSerializer mXmlSerializer;

        private Serializer(TypedXmlSerializer typedXmlSerializer) {
            this.mXmlSerializer = typedXmlSerializer;
            this.mWriteSection = new WriteSectionImpl(typedXmlSerializer);
        }

        public WriteSection startSection(String str) throws IOException {
            return this.mWriteSection.startSection(str);
        }

        @Override // java.lang.AutoCloseable
        public void close() throws IOException {
            this.mWriteSection.closeCompletely();
            this.mXmlSerializer.flush();
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    public static class ReadSectionImpl implements ChildSection {
        private final Stack<Integer> mDepthStack;
        private final InputStream mInput;
        private final TypedXmlPullParser mParser;

        public ReadSectionImpl(InputStream inputStream) throws IOException, XmlPullParserException {
            this.mDepthStack = new Stack<>();
            this.mInput = inputStream;
            TypedXmlPullParser newFastPullParser = Xml.newFastPullParser();
            this.mParser = newFastPullParser;
            newFastPullParser.setInput(inputStream, StandardCharsets.UTF_8.name());
            moveToFirstTag();
        }

        public ReadSectionImpl(TypedXmlPullParser typedXmlPullParser) throws IOException, XmlPullParserException {
            this.mDepthStack = new Stack<>();
            this.mInput = null;
            this.mParser = typedXmlPullParser;
            moveToFirstTag();
        }

        private void moveToFirstTag() throws IOException, XmlPullParserException {
            int next;
            if (this.mParser.getEventType() == 2) {
                return;
            }
            do {
                next = this.mParser.next();
                if (next == 2) {
                    return;
                }
            } while (next != 1);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public String getName() {
            return this.mParser.getName();
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public String getDescription() {
            return this.mParser.getPositionDescription();
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public boolean has(String str) {
            return this.mParser.getAttributeValue((String) null, str) != null;
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public String getString(String str) {
            return this.mParser.getAttributeValue((String) null, str);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public String getString(String str, String str2) {
            String attributeValue = this.mParser.getAttributeValue((String) null, str);
            return attributeValue == null ? str2 : attributeValue;
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public boolean getBoolean(String str) {
            return getBoolean(str, false);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public boolean getBoolean(String str, boolean z) {
            return this.mParser.getAttributeBoolean((String) null, str, z);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public int getInt(String str) {
            return getInt(str, -1);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public int getInt(String str, int i) {
            return this.mParser.getAttributeInt((String) null, str, i);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public long getLong(String str) {
            return getLong(str, -1);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public long getLong(String str, int i) {
            return this.mParser.getAttributeLong((String) null, str, i);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public ChildSection children() {
            this.mDepthStack.push(Integer.valueOf(this.mParser.getDepth()));
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.ChildSection
        public boolean moveToNext() {
            return moveToNextInternal(null);
        }

        @Override // com.android.server.pm.SettingsXml.ChildSection
        public boolean moveToNext(String str) {
            return moveToNextInternal(str);
        }

        private boolean moveToNextInternal(String str) {
            try {
                int intValue = this.mDepthStack.peek().intValue();
                boolean z = false;
                while (!z) {
                    int next = this.mParser.next();
                    if (next == 1 || (next == 3 && this.mParser.getDepth() <= intValue)) {
                        break;
                    }
                    if (next == 2 && (str == null || str.equals(this.mParser.getName()))) {
                        z = true;
                    }
                }
                if (!z) {
                    this.mDepthStack.pop();
                }
                return z;
            } catch (Exception unused) {
                return false;
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() throws Exception {
            if (this.mDepthStack.isEmpty()) {
                Slog.wtf(SettingsXml.TAG, "Children depth stack was not empty, data may have been lost", new Exception());
            }
            InputStream inputStream = this.mInput;
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
    private static class WriteSectionImpl implements WriteSection {
        private final Stack<String> mTagStack;
        private final TypedXmlSerializer mXmlSerializer;

        private WriteSectionImpl(TypedXmlSerializer typedXmlSerializer) {
            this.mTagStack = new Stack<>();
            this.mXmlSerializer = typedXmlSerializer;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public WriteSection startSection(String str) throws IOException {
            this.mXmlSerializer.startTag((String) null, str);
            this.mTagStack.push(str);
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public WriteSection attribute(String str, String str2) throws IOException {
            if (str2 != null) {
                this.mXmlSerializer.attribute((String) null, str, str2);
            }
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public WriteSection attribute(String str, int i) throws IOException {
            if (i != -1) {
                this.mXmlSerializer.attributeInt((String) null, str, i);
            }
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public WriteSection attribute(String str, long j) throws IOException {
            if (j != -1) {
                this.mXmlSerializer.attributeLong((String) null, str, j);
            }
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public WriteSection attribute(String str, boolean z) throws IOException {
            if (z) {
                this.mXmlSerializer.attributeBoolean((String) null, str, z);
            }
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public void finish() throws IOException {
            close();
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection, java.lang.AutoCloseable
        public void close() throws IOException {
            this.mXmlSerializer.endTag((String) null, this.mTagStack.pop());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void closeCompletely() throws IOException {
            if (this.mTagStack != null) {
                while (!this.mTagStack.isEmpty()) {
                    close();
                }
            }
        }
    }
}
