package com.android.server.integrity.parser;

import android.content.integrity.Rule;
import java.util.List;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public interface RuleParser {
    List<Rule> parse(RandomAccessObject randomAccessObject, List<RuleIndexRange> list) throws RuleParseException;

    List<Rule> parse(byte[] bArr) throws RuleParseException;
}
