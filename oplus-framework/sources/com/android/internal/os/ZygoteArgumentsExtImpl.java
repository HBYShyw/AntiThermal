package com.android.internal.os;

/* loaded from: classes.dex */
public class ZygoteArgumentsExtImpl implements IZygoteArgumentsExt {
    String[] mOplusHiddenApiExemptions = null;

    public boolean canParseArg(String arg) {
        if (arg.startsWith("--oplus-hidden-api-exemptions=")) {
            return true;
        }
        return false;
    }

    public void doParseArg(String arg) throws IllegalArgumentException {
        if (arg.startsWith("--oplus-hidden-api-exemptions=")) {
            if (this.mOplusHiddenApiExemptions != null) {
                throw new IllegalArgumentException("Duplicate oplus arg specified");
            }
            String[] params = getAssignmentList(arg);
            int length = params.length;
            this.mOplusHiddenApiExemptions = new String[length];
            for (int i = 0; i < length; i++) {
                this.mOplusHiddenApiExemptions[i] = params[i];
            }
        }
    }

    public String[] getOplusHiddenApiExemptions() {
        return this.mOplusHiddenApiExemptions;
    }

    private static String getAssignmentValue(String arg) {
        return arg.substring(arg.indexOf(61) + 1);
    }

    private static String[] getAssignmentList(String arg) {
        return getAssignmentValue(arg).split(",");
    }
}
