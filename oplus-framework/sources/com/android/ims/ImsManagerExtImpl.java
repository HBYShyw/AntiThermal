package com.android.ims;

import android.content.Context;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.OplusFeature;
import com.android.telephony.Rlog;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class ImsManagerExtImpl implements IImsManagerExt {
    private static final String CONFIG_SUPPORT_VIWIFI_BOOL = "config_oplus_support_viwifi_bool";
    private static final boolean DBG;
    private static final boolean SDEBUG;
    private static final boolean SECURE_DBG;
    private static final boolean SWITCH_LOG;
    private static final String TAG = "ImsManagerExtImpl";
    private CarrierConfigManager mConfigManager;
    private Context mContext;
    public Executor mExecutor;
    private ImsManager mImsManager;
    private int mPhoneId;
    private OplusSesImsManagerExt mSesImsManagerExt;

    static {
        boolean z = false;
        boolean z2 = SystemProperties.getBoolean("persist.sys.assert.panic", false);
        SWITCH_LOG = z2;
        boolean z3 = SystemProperties.getBoolean("persist.radio.securitylog.debug", false);
        SECURE_DBG = z3;
        if (z2 && z3) {
            z = true;
        }
        SDEBUG = z;
        DBG = z;
    }

    public ImsManagerExtImpl(Object base) {
        this(((ImsManager) base).getWrapper().getContext(), ((ImsManager) base).getWrapper().getPhoneId(), (ImsManager) base, ((ImsManager) base).getWrapper().getCarrierConfigManager(), ((ImsManager) base).getWrapper().getExecutor());
    }

    public ImsManagerExtImpl(Context context, int phoneId, ImsManager imsManager, CarrierConfigManager carrierConfigManager, Executor executor) {
        this.mSesImsManagerExt = null;
        this.mContext = context;
        this.mPhoneId = phoneId;
        this.mImsManager = imsManager;
        if (imsManager == null) {
            this.mImsManager = ImsManager.getInstance(context, phoneId);
        }
        this.mConfigManager = carrierConfigManager;
        if (carrierConfigManager == null) {
            this.mConfigManager = (CarrierConfigManager) context.getSystemService("carrier_config");
        }
        this.mExecutor = executor;
        this.mSesImsManagerExt = new OplusSesImsManagerExt(this.mContext);
        log("OplusImsManagerExt init!!");
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0192  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateImsUserAgent() {
        String androidVer;
        String mccmnc;
        String IMEISV;
        String softVersion;
        String softVersion2;
        String deviceOemName;
        String imsUserAgent;
        final String imsUserAgentFinal;
        final ImsManagerExtImpl imsManagerExtImpl;
        PersistableBundle b = null;
        CarrierConfigManager carrierConfigManager = this.mConfigManager;
        if (carrierConfigManager != null) {
            b = carrierConfigManager.getConfigForSubId(getSubId(this.mPhoneId));
        }
        String imsUserAgent2 = b != null ? b.getString("carrier_ims_user_agent") : "";
        boolean z = SDEBUG;
        if (z) {
            log("updateImsUserAgent imsUserAgent = " + imsUserAgent2);
        }
        if (imsUserAgent2 != null && !imsUserAgent2.isEmpty()) {
            String model = SystemProperties.get("ro.product.model", "");
            String displayVersion = SystemProperties.get("ro.build.display.full_id", "");
            String displayId = SystemProperties.get("ro.build.display.id", " ");
            String productName = SystemProperties.get("ro.build.product", "");
            String buildVersion = SystemProperties.get("ro.build.version.ota", "");
            String buildDate = SystemProperties.get("ro.build.date.Ymd");
            String buildDateInHM = SystemProperties.get("ro.build.date.YmdHM");
            String buildVersionRelease = SystemProperties.get("ro.build.version.release");
            String deviceOemName2 = SystemProperties.get("ro.product.brand");
            String romVersion = SystemProperties.get("ro.rom.version");
            int subId = getSubId(this.mPhoneId);
            String romVersion2 = romVersion.replace(' ', '_');
            String buildDateInHM2 = buildDateInHM.substring(2);
            String buildVersionOtaReplace = buildVersion.replace('_', '-');
            if (SystemProperties.getBoolean("persist.version.confidential", false)) {
                androidVer = "Android" + SystemProperties.get("ro.product.androidver", Build.VERSION.RELEASE);
            } else {
                androidVer = "Android" + Build.VERSION.RELEASE;
            }
            TelephonyManager tm = (TelephonyManager) this.mContext.getSystemService("phone");
            if (tm == null) {
                mccmnc = "";
                IMEISV = "";
            } else {
                IMEISV = tm.getImei() + tm.getDeviceSoftwareVersion();
                String mccmnc2 = tm.getSimOperator(subId);
                mccmnc = mccmnc2;
            }
            if (!displayVersion.isEmpty()) {
                Pattern pattern = Pattern.compile("_([A-Z]\\.\\d\\d)_([0-9]*\\d)");
                Matcher matcher = pattern.matcher(displayVersion);
                if (matcher.find()) {
                    String softVersion3 = matcher.group(1);
                    softVersion2 = matcher.group(2);
                    softVersion = softVersion3;
                    String mccmnc3 = mccmnc;
                    if (!OplusFeature.OPLUS_FEATURE_UST_UA) {
                        imsUserAgentFinal = imsUserAgent2.replace("[M]", model).replace("[B]", buildDate).replace("[A]", androidVer).replace("[S]", softVersion).replace("[D]", softVersion2);
                        deviceOemName = deviceOemName2;
                        imsUserAgent = displayId;
                    } else if (OplusFeature.OPLUS_FEATURE_USV_UA) {
                        imsUserAgentFinal = imsUserAgent2.replace("[P]", productName).replace("[V]", buildVersion).replace("[M]", model).replace("[A]", androidVer).replace("[S]", softVersion).replace("[D]", softVersion2);
                        deviceOemName = deviceOemName2;
                        imsUserAgent = displayId;
                    } else {
                        String replace = imsUserAgent2.replace("[M]", model).replace("[A]", androidVer).replace("[S]", softVersion).replace("[D]", softVersion2).replace("[I]", IMEISV).replace("[R]", romVersion2).replace("[E]", buildVersionRelease).replace("[V]", buildVersion).replace("[O]", buildVersionOtaReplace).replace("[T]", buildDate).replace("[H]", buildDateInHM2).replace("[P]", productName);
                        deviceOemName = deviceOemName2;
                        imsUserAgent = displayId;
                        imsUserAgentFinal = replace.replace("[B]", deviceOemName).replace("[N]", mccmnc3).replace("[L]", imsUserAgent);
                    }
                    if (z) {
                        imsManagerExtImpl = this;
                    } else {
                        imsManagerExtImpl = this;
                        imsManagerExtImpl.log("updateImsUserAgent imsUserAgentFinal = " + imsUserAgentFinal);
                    }
                    imsManagerExtImpl.mExecutor.execute(new Runnable() { // from class: com.android.ims.ImsManagerExtImpl$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            ImsManagerExtImpl.this.lambda$updateImsUserAgent$0(imsUserAgentFinal);
                        }
                    });
                }
            }
            softVersion = "A.01";
            softVersion2 = "20000101";
            String mccmnc32 = mccmnc;
            if (!OplusFeature.OPLUS_FEATURE_UST_UA) {
            }
            if (z) {
            }
            imsManagerExtImpl.mExecutor.execute(new Runnable() { // from class: com.android.ims.ImsManagerExtImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImsManagerExtImpl.this.lambda$updateImsUserAgent$0(imsUserAgentFinal);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateImsUserAgent$0(String imsUserAgentFinal) {
        try {
            this.mImsManager.getConfigInterface().setProvisionedStringValue(12, imsUserAgentFinal);
        } catch (Exception e) {
            loge(e.toString());
        } catch (ImsException e2) {
            loge(e2.toString());
        }
    }

    public boolean getBooleanCarrierConfigForSlot(String key, boolean defValue) {
        int subId = getSubId(this.mPhoneId);
        PersistableBundle b = null;
        CarrierConfigManager carrierConfigManager = this.mConfigManager;
        if (carrierConfigManager != null) {
            b = carrierConfigManager.getConfigForSubId(subId);
        }
        if (b != null) {
            return b.getBoolean(key, defValue);
        }
        return defValue;
    }

    public boolean isViwifEnabledByPlatform() {
        return getBooleanCarrierConfigForSlot(CONFIG_SUPPORT_VIWIFI_BOOL, false);
    }

    public boolean isFeatureEnabledByPlatformExt(int feature) {
        Context context = this.mContext;
        if (context == null) {
            loge("Invalid: context=" + this.mContext + ", return true");
            return true;
        }
        OplusSesImsManagerExt oplusSesImsManagerExt = this.mSesImsManagerExt;
        if (oplusSesImsManagerExt == null) {
            loge("mSesImsManagerExt == null, return true");
            return true;
        }
        boolean isEnabled = oplusSesImsManagerExt.isFeatureEnabledByPlatform(context, feature, this.mPhoneId);
        return isEnabled;
    }

    public void setShowConfListWithoutCep(ImsManager mgr, ImsCall imsCall) {
        log("setShowConfListWithoutCep: imsCall = " + imsCall);
        boolean withoutCep = mgr.getWrapper().getBooleanCarrierConfig("carrier_show_ims_conference_list_without_cep_config");
        imsCall.getWrapper().setShowConfListWithoutCep(withoutCep);
    }

    public static int getSubId(int phoneId) {
        int[] subIds = SubscriptionManager.getSubId(phoneId);
        if (subIds == null || subIds.length < 1) {
            return -1;
        }
        int subId = subIds[0];
        return subId;
    }

    private void log(String s) {
        Rlog.d("ImsManagerExtImpl [" + this.mPhoneId + "]", s);
    }

    private void loge(String s) {
        Rlog.e("ImsManagerExtImpl [" + this.mPhoneId + "]", s);
    }
}
