package android.content.res;

import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
public class ResourcesExt implements IResourcesExt {
    private Resources mResource;
    private IResourcesImplExt mResourcesImplExt = null;

    public ResourcesExt(Object resources) {
        this.mResource = null;
        this.mResource = (Resources) resources;
    }

    public void setResourceImplExt(IResourcesImplExt implExt) {
        this.mResourcesImplExt = implExt;
    }

    public Resources getResources() {
        return this.mResource;
    }

    public void setIsThemeChanged(boolean changed) {
        this.mResourcesImplExt.setIsThemeChanged(changed);
    }

    public boolean getThemeChanged() {
        return this.mResourcesImplExt.getThemeChanged();
    }

    public Drawable loadIcon(int id) {
        return loadIcon(id, null, true);
    }

    public CharSequence getThemeCharSequence(int id) {
        return this.mResourcesImplExt.getThemeCharSequence(id);
    }

    public Drawable loadIcon(int id, boolean useWrap) {
        return loadIcon(id, null, useWrap);
    }

    public Drawable loadIcon(int id, String str) {
        return loadIcon(id, str, true);
    }

    public void init(String name) {
        this.mResourcesImplExt.init(name);
    }

    public Drawable loadIcon(int id, String str, boolean useWrap) {
        return this.mResourcesImplExt.loadIcon(this.mResource, id, str, useWrap);
    }

    public Configuration getConfiguration() {
        return this.mResourcesImplExt.getConfiguration();
    }

    public Configuration getSystemConfiguration() {
        return this.mResourcesImplExt.getSystemConfiguration();
    }
}
