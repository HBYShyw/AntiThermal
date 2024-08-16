package com.oplus.util;

import android.content.res.Resources;
import android.telephony.PhoneNumberUtilsExtImpl;
import android.view.View;

/* loaded from: classes.dex */
public class OplusViewUtil {
    private static final String TAG = "OplusViewUtil";

    public static int makeUnspecifiedMeasureSpec() {
        return View.MeasureSpec.makeMeasureSpec(0, 0);
    }

    public static int makeAtMostMeasureSpec(int measureSize) {
        return View.MeasureSpec.makeMeasureSpec(measureSize, Integer.MIN_VALUE);
    }

    public static int makeExactlyMeasureSpec(int measureSize) {
        return View.MeasureSpec.makeMeasureSpec(measureSize, 1073741824);
    }

    public static String dumpViewDetail(View view) {
        StringBuilder out = new StringBuilder();
        try {
            out.append(view.getClass().getName());
            out.append('{');
            out.append(Integer.toHexString(System.identityHashCode(view)));
            out.append(' ');
            out.append(view.getLeft());
            out.append(PhoneNumberUtilsExtImpl.PAUSE);
            out.append(view.getTop());
            out.append('-');
            out.append(view.getRight());
            out.append(PhoneNumberUtilsExtImpl.PAUSE);
            out.append(view.getBottom());
            int id = view.getId();
            if (id != -1) {
                out.append(" #");
                out.append(Integer.toHexString(id));
                OplusResourcesUtil.dumpResourceInternal(view.getResources(), id, out, false);
            }
            out.append("}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toString();
    }

    public static String dumpView(View view) {
        StringBuilder out = new StringBuilder();
        out.append(view.getClass().getName());
        int id = view.getId();
        if (id != -1) {
            out.append("[");
            OplusResourcesUtil.dumpResourceInternal(view.getResources(), id, out, false);
            out.append("]");
        }
        return out.toString();
    }

    public static String getIdName(Resources res, int id) {
        StringBuilder out = new StringBuilder();
        if (id != -1) {
            OplusResourcesUtil.dumpResourceInternal(res, id, out, true);
        }
        return out.toString();
    }

    public static String getViewIdName(View view) {
        return getIdName(view.getResources(), view.getId());
    }
}
