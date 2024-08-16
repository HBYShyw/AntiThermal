package com.android.server.pm.verify.domain;

import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Patterns;
import com.android.server.SystemConfig;
import com.android.server.compat.PlatformCompat;
import com.android.server.pm.pkg.AndroidPackage;
import com.android.server.pm.pkg.component.ParsedActivity;
import com.android.server.pm.pkg.component.ParsedIntentInfo;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes2.dex */
public class DomainVerificationCollector {
    private static final int MAX_DOMAINS_BYTE_SIZE = 1048576;
    public static final long RESTRICT_DOMAINS = 175408749;
    private final Matcher mDomainMatcher = DOMAIN_NAME_WITH_WILDCARD.matcher("");
    private final PlatformCompat mPlatformCompat;
    private final SystemConfig mSystemConfig;
    private static final Pattern DOMAIN_NAME_WITH_WILDCARD = Pattern.compile("(\\*\\.)?" + Patterns.DOMAIN_NAME.pattern());
    private static final BiFunction<ArraySet<String>, String, Boolean> ARRAY_SET_COLLECTOR = new BiFunction() { // from class: com.android.server.pm.verify.domain.DomainVerificationCollector$$ExternalSyntheticLambda2
        @Override // java.util.function.BiFunction
        public final Object apply(Object obj, Object obj2) {
            Boolean lambda$static$0;
            lambda$static$0 = DomainVerificationCollector.lambda$static$0((ArraySet) obj, (String) obj2);
            return lambda$static$0;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$static$0(ArraySet arraySet, String str) {
        arraySet.add(str);
        return null;
    }

    public DomainVerificationCollector(PlatformCompat platformCompat, SystemConfig systemConfig) {
        this.mPlatformCompat = platformCompat;
        this.mSystemConfig = systemConfig;
    }

    public ArraySet<String> collectAllWebDomains(AndroidPackage androidPackage) {
        return collectDomains(androidPackage, false, true);
    }

    public ArraySet<String> collectValidAutoVerifyDomains(AndroidPackage androidPackage) {
        return collectDomains(androidPackage, true, true);
    }

    public ArraySet<String> collectInvalidAutoVerifyDomains(AndroidPackage androidPackage) {
        return collectDomains(androidPackage, true, false);
    }

    public boolean containsWebDomain(AndroidPackage androidPackage, final String str) {
        return collectDomains(androidPackage, false, true, null, new BiFunction() { // from class: com.android.server.pm.verify.domain.DomainVerificationCollector$$ExternalSyntheticLambda1
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Boolean lambda$containsWebDomain$1;
                lambda$containsWebDomain$1 = DomainVerificationCollector.lambda$containsWebDomain$1(str, (Void) obj, (String) obj2);
                return lambda$containsWebDomain$1;
            }
        }) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$containsWebDomain$1(String str, Void r1, String str2) {
        if (Objects.equals(str, str2)) {
            return Boolean.TRUE;
        }
        return null;
    }

    public boolean containsAutoVerifyDomain(AndroidPackage androidPackage, final String str) {
        return collectDomains(androidPackage, true, true, null, new BiFunction() { // from class: com.android.server.pm.verify.domain.DomainVerificationCollector$$ExternalSyntheticLambda0
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                Boolean lambda$containsAutoVerifyDomain$2;
                lambda$containsAutoVerifyDomain$2 = DomainVerificationCollector.lambda$containsAutoVerifyDomain$2(str, (Void) obj, (String) obj2);
                return lambda$containsAutoVerifyDomain$2;
            }
        }) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$containsAutoVerifyDomain$2(String str, Void r1, String str2) {
        if (Objects.equals(str, str2)) {
            return Boolean.TRUE;
        }
        return null;
    }

    private ArraySet<String> collectDomains(AndroidPackage androidPackage, boolean z, boolean z2) {
        ArraySet<String> arraySet = new ArraySet<>();
        collectDomains(androidPackage, z, z2, arraySet, ARRAY_SET_COLLECTOR);
        return arraySet;
    }

    private <InitialValue, ReturnValue> ReturnValue collectDomains(AndroidPackage androidPackage, boolean z, boolean z2, InitialValue initialvalue, BiFunction<InitialValue, String, ReturnValue> biFunction) {
        if (DomainVerificationUtils.isChangeEnabled(this.mPlatformCompat, androidPackage, RESTRICT_DOMAINS)) {
            return (ReturnValue) collectDomainsInternal(androidPackage, z, z2, initialvalue, biFunction);
        }
        return (ReturnValue) collectDomainsLegacy(androidPackage, z, z2, initialvalue, biFunction);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v0 */
    /* JADX WARN: Type inference failed for: r11v1, types: [int] */
    /* JADX WARN: Type inference failed for: r11v3 */
    /* JADX WARN: Type inference failed for: r12v2, types: [android.content.IntentFilter] */
    /* JADX WARN: Type inference failed for: r14v0 */
    /* JADX WARN: Type inference failed for: r14v1, types: [int] */
    /* JADX WARN: Type inference failed for: r14v3 */
    private <InitialValue, ReturnValue> ReturnValue collectDomainsLegacy(AndroidPackage androidPackage, boolean z, boolean z2, InitialValue initialvalue, BiFunction<InitialValue, String, ReturnValue> biFunction) {
        InitialValue initialvalue2;
        BiFunction<InitialValue, String, ReturnValue> biFunction2;
        boolean z3;
        if (!z) {
            return (ReturnValue) collectDomainsInternal(androidPackage, false, true, initialvalue, biFunction);
        }
        List<ParsedActivity> activities = androidPackage.getActivities();
        int size = activities.size();
        boolean contains = this.mSystemConfig.getLinkedApps().contains(androidPackage.getPackageName());
        boolean z4 = false;
        if (!contains) {
            for (int i = 0; i < size && !contains; i++) {
                List<ParsedIntentInfo> intents = activities.get(i).getIntents();
                int size2 = intents.size();
                for (int i2 = 0; i2 < size2 && !contains; i2++) {
                    contains = intents.get(i2).getIntentFilter().needsVerification();
                }
            }
            if (!contains) {
                return null;
            }
        }
        int i3 = 0;
        int i4 = 0;
        boolean z5 = true;
        while (i3 < size && z5) {
            List<ParsedIntentInfo> intents2 = activities.get(i3).getIntents();
            int size3 = intents2.size();
            for (?? r11 = z4; r11 < size3 && z5; r11++) {
                ?? intentFilter = intents2.get(r11).getIntentFilter();
                if (intentFilter.handlesWebUris(z4)) {
                    int countDataAuthorities = intentFilter.countDataAuthorities();
                    for (?? r14 = z4; r14 < countDataAuthorities; r14++) {
                        String host = intentFilter.getDataAuthority(r14).getHost();
                        if (isValidHost(host) == z2) {
                            i4 += byteSizeOf(host);
                            if (i4 < 1048576) {
                                initialvalue2 = initialvalue;
                                biFunction2 = biFunction;
                                z3 = true;
                            } else {
                                initialvalue2 = initialvalue;
                                biFunction2 = biFunction;
                                z3 = false;
                            }
                            ReturnValue apply = biFunction2.apply(initialvalue2, host);
                            if (apply != null) {
                                return apply;
                            }
                            z5 = z3;
                        }
                    }
                }
                z4 = false;
            }
            i3++;
            z4 = false;
        }
        return null;
    }

    private <InitialValue, ReturnValue> ReturnValue collectDomainsInternal(AndroidPackage androidPackage, boolean z, boolean z2, InitialValue initialvalue, BiFunction<InitialValue, String, ReturnValue> biFunction) {
        InitialValue initialvalue2;
        BiFunction<InitialValue, String, ReturnValue> biFunction2;
        boolean z3;
        List<ParsedActivity> activities = androidPackage.getActivities();
        int size = activities.size();
        boolean z4 = true;
        int i = 0;
        for (int i2 = 0; i2 < size && z4; i2++) {
            List<ParsedIntentInfo> intents = activities.get(i2).getIntents();
            int size2 = intents.size();
            for (int i3 = 0; i3 < size2 && z4; i3++) {
                IntentFilter intentFilter = intents.get(i3).getIntentFilter();
                if ((!z || intentFilter.getAutoVerify()) && intentFilter.hasCategory("android.intent.category.DEFAULT") && intentFilter.handlesWebUris(z)) {
                    int countDataAuthorities = intentFilter.countDataAuthorities();
                    for (int i4 = 0; i4 < countDataAuthorities && z4; i4++) {
                        String host = intentFilter.getDataAuthority(i4).getHost();
                        if (isValidHost(host) == z2) {
                            i += byteSizeOf(host);
                            if (i < 1048576) {
                                initialvalue2 = initialvalue;
                                biFunction2 = biFunction;
                                z3 = true;
                            } else {
                                initialvalue2 = initialvalue;
                                biFunction2 = biFunction;
                                z3 = false;
                            }
                            ReturnValue apply = biFunction2.apply(initialvalue2, host);
                            if (apply != null) {
                                return apply;
                            }
                            z4 = z3;
                        }
                    }
                }
            }
        }
        return null;
    }

    private int byteSizeOf(String str) {
        return android.content.pm.verify.domain.DomainVerificationUtils.estimatedByteSizeOf(str);
    }

    private boolean isValidHost(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        this.mDomainMatcher.reset(str);
        return this.mDomainMatcher.matches();
    }
}
