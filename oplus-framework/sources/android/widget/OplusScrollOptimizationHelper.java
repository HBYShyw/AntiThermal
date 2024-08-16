package android.widget;

import android.os.SystemProperties;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.oplus.putt.PuttParams;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* loaded from: classes.dex */
public class OplusScrollOptimizationHelper implements IOplusScrollOptimizationHelper {
    private static final String OPT_CONFIG_FILE = "my_product/etc/scroll_opt.xml";
    private static final String TAG = "ScrollOptimizationHelper";
    private static boolean sDebugOpt = false;
    private static final int sInterLength = 4;
    private static float[] sInterpolator;
    private static PathInterpolator sPathInterpolator;
    private long mLastAbortTime;
    private long mLastFlingTime;
    private int mSavedVeloAccuCount;
    private int mVeloAccuCount;
    private static String sInterpolatorStr = "";
    private static boolean sVelocityAccu = true;
    private static boolean sOptEnable = false;
    private static float sVelocityAccuCoef = 2.0f;
    private static int sAccuCountThresh = 3;
    private static float sMaxAccuCoef = 6.0f;
    private static int sFlingAbortIntervalMax = 150;
    private static int sMaxContinuousInterval = 500;
    private static float sDurationMaxCoef = 1.0f;
    private static float sDurationMinCoef = 1.0f;
    private static float sDistanceMaxCoef = 1.0f;
    private static float sDistanceMinCoef = 1.0f;
    private static int sMaxVelocityForDuration = PuttParams.ACTION_EXIT_BY_REMOVE_TASK;
    private static int sMinVelocityForDuration = 5000;
    private static int sMaxVelocityForDistance = PuttParams.ACTION_EXIT_BY_REMOVE_TASK;
    private static int sMinVelocityForDistance = 5000;

    static {
        sInterpolator = null;
        sPathInterpolator = null;
        sDebugOpt = false;
        parseConfigFile();
        if (!sInterpolatorStr.equals("")) {
            String[] values = sInterpolatorStr.split(",");
            if (values.length >= 4) {
                sInterpolator = new float[4];
                for (int i = 0; i < 4; i++) {
                    try {
                        sInterpolator[i] = Float.valueOf(values[i]).floatValue();
                    } catch (Exception e) {
                        Log.d(TAG, "something wrong while parsing the interpolator!");
                        sInterpolator = null;
                    }
                }
            }
        }
        if (sInterpolator != null) {
            float[] fArr = sInterpolator;
            sPathInterpolator = new PathInterpolator(fArr[0], fArr[1], fArr[2], fArr[3]);
        }
        boolean z = SystemProperties.getBoolean("persist.op.overscroller.opt.debug", false);
        sDebugOpt = z;
        if (z) {
            if (sOptEnable) {
                StringBuilder sb = new StringBuilder("interpolator is: ");
                for (int i2 = 0; i2 < 4; i2++) {
                    sb.append(sInterpolator[i2] + ", ");
                }
                Log.d(TAG, sb.toString());
                debugScrollOpt();
                return;
            }
            Log.d(TAG, "will not debug for enable is false");
            return;
        }
        Log.d(TAG, "will not debug for debug is false");
    }

    private static void parseConfigFile() {
        File f = new File(OPT_CONFIG_FILE);
        if (!f.exists()) {
            Log.d(TAG, "can't find the config file for optimizaiton");
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        boolean z = false;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(f);
            NodeList nodeList = doc.getElementsByTagName("param");
            if (nodeList.getLength() == 0) {
                Log.d(TAG, "no param find, now will return!");
            }
            int i = 0;
            while (i < nodeList.getLength()) {
                Element element = (Element) nodeList.item(i);
                String parentNodeName = element.getParentNode().getNodeName();
                String name = element.getAttribute("name");
                String value = element.getAttribute("value");
                if (parentNodeName.equals("ScrollOptParameters")) {
                    if (name.equals("enable") && !Boolean.valueOf(value).booleanValue()) {
                        sOptEnable = z;
                        Log.d(TAG, "will not parse the config file for enable is false");
                        return;
                    }
                    sOptEnable = true;
                    if (name.equals("interpolatorStr")) {
                        sInterpolatorStr = value;
                    } else if (name.equals("veloAccuCoef")) {
                        sVelocityAccuCoef = Float.valueOf(value).floatValue();
                    } else if (name.equals("maxAccuCoef")) {
                        sMaxAccuCoef = Float.valueOf(value).floatValue();
                    } else if (name.equals("accuCountThresh")) {
                        sAccuCountThresh = Integer.valueOf(value).intValue();
                    } else if (name.equals("flingAbortIntervalMax")) {
                        sFlingAbortIntervalMax = Integer.valueOf(value).intValue();
                    } else if (name.equals("maxContinuousInterval")) {
                        sMaxContinuousInterval = Integer.valueOf(value).intValue();
                    }
                } else if (parentNodeName.equals("DistanceOpt")) {
                    if (name.equals("maxCoef")) {
                        sDistanceMaxCoef = Float.valueOf(value).floatValue();
                    } else if (name.equals("veloMax")) {
                        sMaxVelocityForDistance = Integer.valueOf(value).intValue();
                    } else if (name.equals("veloMin")) {
                        sMinVelocityForDistance = Integer.valueOf(value).intValue();
                    } else if (name.equals("minCoef")) {
                        sDistanceMinCoef = Float.valueOf(value).floatValue();
                    }
                } else if (parentNodeName.equals("DurationOpt")) {
                    if (name.equals("maxCoef")) {
                        sDurationMaxCoef = Float.valueOf(value).floatValue();
                    } else if (name.equals("veloMax")) {
                        sMaxVelocityForDuration = Integer.valueOf(value).intValue();
                    } else if (name.equals("veloMin")) {
                        sMinVelocityForDuration = Integer.valueOf(value).intValue();
                    } else if (name.equals("minCoef")) {
                        sDurationMinCoef = Float.valueOf(value).floatValue();
                    }
                }
                i++;
                z = false;
            }
        } catch (Exception e) {
            sOptEnable = false;
            Log.d(TAG, "someting error while parsing config, will disable the optimization");
        }
    }

    private static void debugScrollOpt() {
        StringBuilder sb = new StringBuilder();
        sb.append("enable: " + sOptEnable);
        sb.append("\ninterpolator" + sInterpolatorStr);
        sb.append("\nvelocityAccuCoef: " + sVelocityAccuCoef);
        sb.append("\nmaxAccuCoef: " + sMaxAccuCoef);
        sb.append("\naccuCountThreash: " + sAccuCountThresh);
        sb.append("\nflingabortIntervalMax: " + sFlingAbortIntervalMax);
        sb.append("\nmaxContinuousInterval: " + sMaxContinuousInterval);
        sb.append("\ndistanceCoefMax: " + sDistanceMaxCoef);
        sb.append("\ndistanceCoefMin：" + sDistanceMinCoef);
        sb.append("\nmaxVelocityFordis： " + sMaxVelocityForDistance);
        sb.append("\nminVelocityForDis: " + sMinVelocityForDistance);
        sb.append("\ndurationCoefMax: " + sDurationMaxCoef);
        sb.append("\ndurationCoefMin: " + sDurationMinCoef);
        sb.append("\nmaxVelocityForDur: " + sMaxVelocityForDuration);
        sb.append("\nminVelocityForDur: " + sMinVelocityForDuration);
        Log.d(TAG, sb.toString());
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public boolean enable() {
        return sOptEnable;
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public boolean interpolatorValid() {
        return sInterpolator != null;
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public Interpolator getInterpolator() {
        return sPathInterpolator;
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public void setFlingParam(long now, float currVelo, int velo, boolean finished) {
        if (Math.signum(velo) == Math.signum(currVelo)) {
            if (!finished) {
                this.mVeloAccuCount++;
            } else if (now - this.mLastAbortTime <= sFlingAbortIntervalMax && now - this.mLastFlingTime <= sMaxContinuousInterval) {
                this.mVeloAccuCount = this.mSavedVeloAccuCount + 1;
                if (sDebugOpt) {
                    Log.d(TAG, "setting mVeloAccuCount to: " + this.mVeloAccuCount);
                }
            } else {
                this.mVeloAccuCount = 0;
            }
        } else {
            this.mVeloAccuCount = 0;
        }
        this.mLastFlingTime = now;
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public double getCustomizedDistanceCoef(int velocity) {
        int i;
        int i2;
        int velocity2 = Math.abs(velocity);
        if (sVelocityAccu && (i = this.mVeloAccuCount) >= (i2 = sAccuCountThresh)) {
            double accuCoef = sVelocityAccuCoef * ((i - i2) + 1);
            if (accuCoef < getCustomizedDurationCoef(velocity2)) {
                accuCoef = getCustomizedDurationCoef(velocity2);
            }
            float f = sMaxAccuCoef;
            if (accuCoef >= f) {
                return f;
            }
            return accuCoef;
        }
        if (velocity2 < sMinVelocityForDistance) {
            return sDistanceMaxCoef;
        }
        if (velocity2 <= sMaxVelocityForDistance) {
            float f2 = sDistanceMaxCoef;
            double coef = (f2 - sDistanceMinCoef) / (r1 - r0);
            return f2 - ((velocity2 - r0) * coef);
        }
        return sDistanceMinCoef;
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public double getCustomizedDurationCoef(int velocity) {
        if (!sOptEnable) {
            return 1.0d;
        }
        int velocity2 = Math.abs(velocity);
        if (velocity2 < sMinVelocityForDuration) {
            return sDurationMaxCoef;
        }
        if (velocity2 < sMaxVelocityForDuration) {
            float f = sDurationMaxCoef;
            double coef = (f - sDurationMinCoef) / (r1 - r0);
            return f - ((velocity2 - r0) * coef);
        }
        return sDurationMinCoef;
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public void resetVeloAccuCount() {
        this.mVeloAccuCount = 0;
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public void saveCurrVeloAccuCount() {
        this.mSavedVeloAccuCount = this.mVeloAccuCount;
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public void setAbortTime(long time) {
        this.mLastAbortTime = time;
    }

    @Override // android.widget.IOplusScrollOptimizationHelper
    public float[] getInterpolatorValues() {
        return sInterpolator;
    }
}
