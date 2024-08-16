package com.android.server.firewall;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.PatternMatcher;
import java.io.IOException;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
abstract class StringFilter implements Filter {
    private static final String ATTR_CONTAINS = "contains";
    private static final String ATTR_EQUALS = "equals";
    private static final String ATTR_IS_NULL = "isNull";
    private static final String ATTR_PATTERN = "pattern";
    private static final String ATTR_REGEX = "regex";
    private static final String ATTR_STARTS_WITH = "startsWith";
    private final ValueProvider mValueProvider;
    public static final ValueProvider COMPONENT = new ValueProvider("component") { // from class: com.android.server.firewall.StringFilter.1
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            if (componentName != null) {
                return componentName.flattenToString();
            }
            return null;
        }
    };
    public static final ValueProvider COMPONENT_NAME = new ValueProvider("component-name") { // from class: com.android.server.firewall.StringFilter.2
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            if (componentName != null) {
                return componentName.getClassName();
            }
            return null;
        }
    };
    public static final ValueProvider COMPONENT_PACKAGE = new ValueProvider("component-package") { // from class: com.android.server.firewall.StringFilter.3
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            if (componentName != null) {
                return componentName.getPackageName();
            }
            return null;
        }
    };
    public static final FilterFactory ACTION = new ValueProvider("action") { // from class: com.android.server.firewall.StringFilter.4
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            return intent.getAction();
        }
    };
    public static final ValueProvider DATA = new ValueProvider("data") { // from class: com.android.server.firewall.StringFilter.5
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            Uri data = intent.getData();
            if (data != null) {
                return data.toString();
            }
            return null;
        }
    };
    public static final ValueProvider MIME_TYPE = new ValueProvider("mime-type") { // from class: com.android.server.firewall.StringFilter.6
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            return str;
        }
    };
    public static final ValueProvider SCHEME = new ValueProvider("scheme") { // from class: com.android.server.firewall.StringFilter.7
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            Uri data = intent.getData();
            if (data != null) {
                return data.getScheme();
            }
            return null;
        }
    };
    public static final ValueProvider SSP = new ValueProvider("scheme-specific-part") { // from class: com.android.server.firewall.StringFilter.8
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            Uri data = intent.getData();
            if (data != null) {
                return data.getSchemeSpecificPart();
            }
            return null;
        }
    };
    public static final ValueProvider HOST = new ValueProvider("host") { // from class: com.android.server.firewall.StringFilter.9
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            Uri data = intent.getData();
            if (data != null) {
                return data.getHost();
            }
            return null;
        }
    };
    public static final ValueProvider PATH = new ValueProvider("path") { // from class: com.android.server.firewall.StringFilter.10
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public String getValue(ComponentName componentName, Intent intent, String str) {
            Uri data = intent.getData();
            if (data != null) {
                return data.getPath();
            }
            return null;
        }
    };

    protected abstract boolean matchesValue(String str);

    private StringFilter(ValueProvider valueProvider) {
        this.mValueProvider = valueProvider;
    }

    public static StringFilter readFromXml(ValueProvider valueProvider, XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        StringFilter stringFilter = null;
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            StringFilter filter = getFilter(valueProvider, xmlPullParser, i);
            if (filter != null) {
                if (stringFilter != null) {
                    throw new XmlPullParserException("Multiple string filter attributes found");
                }
                stringFilter = filter;
            }
        }
        return stringFilter == null ? new IsNullFilter(valueProvider, false) : stringFilter;
    }

    private static StringFilter getFilter(ValueProvider valueProvider, XmlPullParser xmlPullParser, int i) {
        String attributeName = xmlPullParser.getAttributeName(i);
        char charAt = attributeName.charAt(0);
        if (charAt == 'c') {
            if (attributeName.equals(ATTR_CONTAINS)) {
                return new ContainsFilter(valueProvider, xmlPullParser.getAttributeValue(i));
            }
            return null;
        }
        if (charAt == 'e') {
            if (attributeName.equals(ATTR_EQUALS)) {
                return new EqualsFilter(valueProvider, xmlPullParser.getAttributeValue(i));
            }
            return null;
        }
        if (charAt == 'i') {
            if (attributeName.equals(ATTR_IS_NULL)) {
                return new IsNullFilter(valueProvider, xmlPullParser.getAttributeValue(i));
            }
            return null;
        }
        if (charAt == 'p') {
            if (attributeName.equals(ATTR_PATTERN)) {
                return new PatternStringFilter(valueProvider, xmlPullParser.getAttributeValue(i));
            }
            return null;
        }
        if (charAt != 'r') {
            if (charAt == 's' && attributeName.equals(ATTR_STARTS_WITH)) {
                return new StartsWithFilter(valueProvider, xmlPullParser.getAttributeValue(i));
            }
            return null;
        }
        if (attributeName.equals(ATTR_REGEX)) {
            return new RegexFilter(valueProvider, xmlPullParser.getAttributeValue(i));
        }
        return null;
    }

    @Override // com.android.server.firewall.Filter
    public boolean matches(IntentFirewall intentFirewall, ComponentName componentName, Intent intent, int i, int i2, String str, int i3) {
        return matchesValue(this.mValueProvider.getValue(componentName, intent, str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static abstract class ValueProvider extends FilterFactory {
        public abstract String getValue(ComponentName componentName, Intent intent, String str);

        protected ValueProvider(String str) {
            super(str);
        }

        @Override // com.android.server.firewall.FilterFactory
        public Filter newFilter(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
            return StringFilter.readFromXml(this, xmlPullParser);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class EqualsFilter extends StringFilter {
        private final String mFilterValue;

        public EqualsFilter(ValueProvider valueProvider, String str) {
            super(valueProvider);
            this.mFilterValue = str;
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(String str) {
            return str != null && str.equals(this.mFilterValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class ContainsFilter extends StringFilter {
        private final String mFilterValue;

        public ContainsFilter(ValueProvider valueProvider, String str) {
            super(valueProvider);
            this.mFilterValue = str;
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(String str) {
            return str != null && str.contains(this.mFilterValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class StartsWithFilter extends StringFilter {
        private final String mFilterValue;

        public StartsWithFilter(ValueProvider valueProvider, String str) {
            super(valueProvider);
            this.mFilterValue = str;
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(String str) {
            return str != null && str.startsWith(this.mFilterValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class PatternStringFilter extends StringFilter {
        private final PatternMatcher mPattern;

        public PatternStringFilter(ValueProvider valueProvider, String str) {
            super(valueProvider);
            this.mPattern = new PatternMatcher(str, 2);
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(String str) {
            return str != null && this.mPattern.match(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class RegexFilter extends StringFilter {
        private final Pattern mPattern;

        public RegexFilter(ValueProvider valueProvider, String str) {
            super(valueProvider);
            this.mPattern = Pattern.compile(str);
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(String str) {
            return str != null && this.mPattern.matcher(str).matches();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public static class IsNullFilter extends StringFilter {
        private final boolean mIsNull;

        public IsNullFilter(ValueProvider valueProvider, String str) {
            super(valueProvider);
            this.mIsNull = Boolean.parseBoolean(str);
        }

        public IsNullFilter(ValueProvider valueProvider, boolean z) {
            super(valueProvider);
            this.mIsNull = z;
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(String str) {
            return (str == null) == this.mIsNull;
        }
    }
}
