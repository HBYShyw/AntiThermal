package com.oplus.app;

import android.util.ArraySet;

/* loaded from: classes.dex */
public class OplusAppStaticFeatureData {
    private String mApplicationName;
    private float[] mIconHistogram;
    private String mPackageName;
    private String mSignature;
    private ArraySet<String> mPermissions = new ArraySet<>();
    private ArraySet<String> mFeatures = new ArraySet<>();
    private ArraySet<String> mActivities = new ArraySet<>();
    private ArraySet<String> mServices = new ArraySet<>();
    private ArraySet<String> mReceivers = new ArraySet<>();
    private ArraySet<String> mProviders = new ArraySet<>();
    private ArraySet<String> mLibraries = new ArraySet<>();
    private ArraySet<String> mProcesses = new ArraySet<>();

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public void setSignature(String signature) {
        this.mSignature = signature;
    }

    public String getSignature() {
        return this.mSignature;
    }

    public void setApplicationName(String applicationName) {
        this.mApplicationName = applicationName;
    }

    public String getApplicationName() {
        return this.mApplicationName;
    }

    public void setPermissions(ArraySet<String> permissions) {
        this.mPermissions.clear();
        this.mPermissions.addAll((ArraySet<? extends String>) permissions);
    }

    public ArraySet<String> getPermissions() {
        return this.mPermissions;
    }

    public void setFeatures(ArraySet<String> features) {
        this.mFeatures.clear();
        this.mFeatures.addAll((ArraySet<? extends String>) features);
    }

    public ArraySet<String> getFeatures() {
        return this.mFeatures;
    }

    public void setActivities(ArraySet<String> activities) {
        this.mActivities.clear();
        this.mActivities.addAll((ArraySet<? extends String>) activities);
    }

    public ArraySet<String> getActivities() {
        return this.mActivities;
    }

    public void setServices(ArraySet<String> services) {
        this.mServices.clear();
        this.mServices.addAll((ArraySet<? extends String>) services);
    }

    public ArraySet<String> getServices() {
        return this.mServices;
    }

    public void setReceivers(ArraySet<String> receivers) {
        this.mReceivers.clear();
        this.mReceivers.addAll((ArraySet<? extends String>) receivers);
    }

    public ArraySet<String> getReceivers() {
        return this.mReceivers;
    }

    public void setProviders(ArraySet<String> providers) {
        this.mProviders.clear();
        this.mProviders.addAll((ArraySet<? extends String>) providers);
    }

    public ArraySet<String> getProviders() {
        return this.mProviders;
    }

    public void setLibraries(ArraySet<String> libraries) {
        this.mLibraries.clear();
        this.mLibraries.addAll((ArraySet<? extends String>) libraries);
    }

    public ArraySet<String> getLibraries() {
        return this.mLibraries;
    }

    public void setProcesses(ArraySet<String> processes) {
        this.mProcesses.clear();
        this.mProcesses.addAll((ArraySet<? extends String>) processes);
    }

    public ArraySet<String> getProcesses() {
        return this.mProcesses;
    }

    public void setIconHistogram(float[] iconHistogram) {
        this.mIconHistogram = iconHistogram;
    }

    public float[] getIconHistogram() {
        return this.mIconHistogram;
    }
}
