package com.android.server.integrity.serializer;

import android.util.Xml;
import com.android.modules.utils.TypedXmlSerializer;
import com.android.server.integrity.model.RuleMetadata;
import com.android.server.integrity.parser.RuleMetadataParser;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class RuleMetadataSerializer {
    public static void serialize(RuleMetadata ruleMetadata, OutputStream outputStream) throws IOException {
        TypedXmlSerializer resolveSerializer = Xml.resolveSerializer(outputStream);
        serializeTaggedValue(resolveSerializer, RuleMetadataParser.RULE_PROVIDER_TAG, ruleMetadata.getRuleProvider());
        serializeTaggedValue(resolveSerializer, RuleMetadataParser.VERSION_TAG, ruleMetadata.getVersion());
        resolveSerializer.endDocument();
    }

    private static void serializeTaggedValue(TypedXmlSerializer typedXmlSerializer, String str, String str2) throws IOException {
        typedXmlSerializer.startTag((String) null, str);
        typedXmlSerializer.text(str2);
        typedXmlSerializer.endTag((String) null, str);
    }
}
