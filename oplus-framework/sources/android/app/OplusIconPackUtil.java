package android.app;

import android.content.ComponentName;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.PathParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class OplusIconPackUtil {
    private static final int ADAPTIVE_ICON_SIZE = 100;
    public static final float BLUR_FACTOR = 0.010416667f;
    private static final String ICON_ADAPTIVE_PATH = "adaptive_path";
    private static final String ICON_BACK_TAG = "iconback";
    private static final String ICON_MASK_TAG = "iconmask";
    private static final String ICON_SCALE_TAG = "scale";
    private static final String ICON_UPON_TAG = "iconupon";
    private static final int MAX_ICON_SIZE = 200;
    private static final int MAX_RANDOM_INDEX = 1000;
    private static final String PACMAN_WEATHER_DRAWABLE_NAME = "ic_dynamic_weather_sunny_slice";
    private static final String PACMAN_WEATHER_PACKAGE_NAME = "net.oneplus.weather";
    private static final String PAC_MAN_ICON_PACK_PACKAGE_NAME = "com.oneplus.iconpack.pacman";
    private static final String TAG = "OplusIconPackUtil";
    private String mAdaptivePath;
    private final String mIconPackName;
    private final Resources mIconPackResource;
    private OplusIconNormalizer mIconUtils;
    private int mIndex;
    private float mFactor = 1.0f;
    private final List<BitmapDrawable> mIconBack = new ArrayList();
    private final List<BitmapDrawable> mIconMask = new ArrayList();
    private final List<BitmapDrawable> mIconUpon = new ArrayList();
    private final Map<String, Item> mIconPackItemCache = Collections.synchronizedMap(new ArrayMap());
    private final Map<String, Float> mIconBitmapScaleMap = Collections.synchronizedMap(new ArrayMap());
    private final Map<String, Drawable.ConstantState> mGenerateCache = Collections.synchronizedMap(new ArrayMap());

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Item {
        private static final int DEFAULT_DRAWABLE_ID = -1;
        int mDrawableId;
        String mDrawableName;

        private Item() {
            this.mDrawableId = -1;
        }
    }

    public OplusIconPackUtil(String iconPackName, Resources iconPackResources) {
        Log.d(TAG, "iconPackName:" + iconPackName);
        this.mIconPackName = iconPackName;
        this.mIconPackResource = iconPackResources;
        this.mIconUtils = new OplusIconNormalizer(200);
        Random random = new Random();
        this.mIndex = random.nextInt(1000);
    }

    public void getIconResMapFromXml() {
        int arrayId;
        Resources res;
        this.mIconBack.clear();
        this.mIconMask.clear();
        this.mIconUpon.clear();
        this.mIconPackItemCache.clear();
        this.mIconBitmapScaleMap.clear();
        this.mGenerateCache.clear();
        Resources res2 = this.mIconPackResource;
        String packageName = this.mIconPackName;
        XmlPullParser parser = null;
        InputStream inputStream = null;
        Map<String, Item> itemCache = this.mIconPackItemCache;
        try {
            inputStream = res2.getAssets().open("appfilter.xml");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
            parser.setInput(inputStream, "UTF-8");
        } catch (Exception e) {
            int resId = res2.getIdentifier("appfilter", "xml", packageName);
            if (resId != 0) {
                parser = res2.getXml(resId);
            }
        }
        try {
            try {
                if (parser != null) {
                    try {
                        loadResourcesFromXmlParser(parser, itemCache);
                    } catch (IOException e2) {
                        Log.e(TAG, "getIconResMapFromXml-->IOException:" + e2.getMessage());
                        if (!(parser instanceof XmlResourceParser)) {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }
                    } catch (XmlPullParserException e3) {
                        Log.e(TAG, "getIconResMapFromXml-->XmlPullParserException:" + e3.getMessage());
                        if (!(parser instanceof XmlResourceParser)) {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        }
                    }
                    if (!(parser instanceof XmlResourceParser)) {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                    ((XmlResourceParser) parser).close();
                }
            } catch (IOException e4) {
            }
            int arrayId2 = res2.getIdentifier("theme_iconpack", "array", packageName);
            if (arrayId2 == 0) {
                arrayId2 = res2.getIdentifier("icon_pack", "array", packageName);
            }
            if (arrayId2 == 0) {
                Log.e(TAG, "getIconResMapFromXml array id is 0");
                return;
            }
            String[] iconPack = res2.getStringArray(arrayId2);
            int length = iconPack.length;
            int i = 0;
            while (i < length) {
                String entry = iconPack[i];
                if (TextUtils.isEmpty(entry)) {
                    arrayId = arrayId2;
                    res = res2;
                } else {
                    String entry2 = entry.replaceAll("_", ".");
                    arrayId = arrayId2;
                    Item item = new Item();
                    item.mDrawableName = entry;
                    if (!itemCache.containsKey(entry2.toLowerCase())) {
                        itemCache.put(entry2.toLowerCase(), item);
                    }
                    int activityIndex = entry2.lastIndexOf(".");
                    if (activityIndex <= 0) {
                        res = res2;
                    } else if (activityIndex == entry2.length() - 1) {
                        res = res2;
                    } else {
                        String iconPackage = entry2.substring(0, activityIndex);
                        if (TextUtils.isEmpty(iconPackage)) {
                            res = res2;
                        } else {
                            String iconActivity = entry2.substring(activityIndex + 1);
                            if (TextUtils.isEmpty(iconActivity)) {
                                res = res2;
                            } else {
                                String iconPackage2 = iconPackage.toLowerCase();
                                res = res2;
                                ComponentName compName = new ComponentName(iconPackage2, iconPackage2 + "." + iconActivity.toLowerCase());
                                Item item2 = new Item();
                                item2.mDrawableName = entry;
                                itemCache.put(compName.flattenToShortString(), item2);
                                if (!itemCache.containsKey(compName.getPackageName())) {
                                    itemCache.put(compName.getPackageName(), item2);
                                }
                            }
                        }
                    }
                }
                i++;
                res2 = res;
                arrayId2 = arrayId;
            }
            Log.d(TAG, "getIconResMapFromXml cache size: " + itemCache.size());
        } catch (Throwable th) {
            if (parser instanceof XmlResourceParser) {
                ((XmlResourceParser) parser).close();
                throw th;
            }
            if (inputStream == null) {
                throw th;
            }
            try {
                inputStream.close();
                throw th;
            } catch (IOException e5) {
                throw th;
            }
        }
    }

    private void loadResourcesFromXmlParser(XmlPullParser parser, Map<String, Item> itemDrawable) throws XmlPullParserException, IOException {
        int next;
        ComponentName name;
        int eventType = parser.getEventType();
        do {
            if (eventType == 2 && !parseComposedIcon(parser) && parser.getName().equalsIgnoreCase("item")) {
                String component = parser.getAttributeValue(null, "component");
                String drawable = parser.getAttributeValue(null, "drawable");
                if (!TextUtils.isEmpty(component) && !TextUtils.isEmpty(drawable) && component.startsWith("ComponentInfo{") && component.endsWith("}") && component.length() >= 16 && drawable.length() != 0) {
                    String component2 = component.substring(14, component.length() - 1).toLowerCase();
                    if (!component2.contains("/")) {
                        name = new ComponentName(component2.toLowerCase(), "");
                    } else {
                        name = ComponentName.unflattenFromString(component2);
                    }
                    if (name != null) {
                        Item item = new Item();
                        item.mDrawableName = drawable;
                        if (!TextUtils.isEmpty(name.getClassName())) {
                            itemDrawable.put(name.flattenToShortString(), item);
                        }
                        if (!this.mIconPackItemCache.containsKey(name.getPackageName())) {
                            itemDrawable.put(name.getPackageName(), item);
                        }
                    }
                }
            }
            next = parser.next();
            eventType = next;
        } while (next != 1);
    }

    private boolean isComposedIconComponent(String tag) {
        return tag.equalsIgnoreCase(ICON_MASK_TAG) || tag.equalsIgnoreCase(ICON_BACK_TAG) || tag.equalsIgnoreCase(ICON_UPON_TAG) || tag.equalsIgnoreCase(ICON_SCALE_TAG) || tag.equalsIgnoreCase(ICON_ADAPTIVE_PATH);
    }

    private synchronized boolean parseComposedIcon(XmlPullParser parser) {
        String tag = parser.getName();
        if (!isComposedIconComponent(tag)) {
            return false;
        }
        if (parser.getAttributeCount() < 1) {
            return false;
        }
        if (tag.equalsIgnoreCase(ICON_BACK_TAG)) {
            parseIconXml(parser, this.mIconBack);
            Log.d(TAG, "parseComposedIcon mIconBack size:" + this.mIconBack.size());
        } else if (tag.equalsIgnoreCase(ICON_MASK_TAG)) {
            parseIconXml(parser, this.mIconMask);
        } else if (tag.equalsIgnoreCase(ICON_UPON_TAG)) {
            parseIconXml(parser, this.mIconUpon);
        } else if (tag.equalsIgnoreCase(ICON_SCALE_TAG)) {
            String factor = parser.getAttributeValue(null, "factor");
            if (factor == null && parser.getAttributeCount() == 1) {
                factor = parser.getAttributeValue(0);
            }
            if (factor != null) {
                this.mFactor = Float.parseFloat(factor);
            }
            Log.d(TAG, "parseComposedIcon factor: " + factor);
        } else if (tag.equalsIgnoreCase(ICON_ADAPTIVE_PATH)) {
            this.mAdaptivePath = parser.getAttributeValue(0);
            Log.d(TAG, "parseComposedIcon adaptivePath: " + (this.mAdaptivePath == null));
        }
        return true;
    }

    private void parseIconXml(XmlPullParser parser, List<BitmapDrawable> result) {
        int count = parser.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String icon = parser.getAttributeValue(i);
            BitmapDrawable drawable = loadBitmap(this.mIconPackResource, this.mIconPackName, icon);
            if (drawable != null) {
                result.add(drawable);
            }
        }
    }

    private BitmapDrawable loadBitmap(Resources iconPackRes, String packageName, String drawableName) {
        Drawable drawable = loadDrawable(iconPackRes, packageName, drawableName);
        if (drawable instanceof BitmapDrawable) {
            return (BitmapDrawable) drawable;
        }
        return null;
    }

    private Drawable loadDrawable(Resources iconPackRes, String packageName, String drawableName) {
        int id = iconPackRes.getIdentifier(drawableName, "drawable", packageName);
        if (id <= 0) {
            return null;
        }
        return iconPackRes.getDrawable(id, null);
    }

    public Drawable getDrawableIconForPackage(ComponentName componentName) {
        String drawableName;
        int id;
        if (componentName == null) {
            Log.e(TAG, "getDrawableIconForPackage componentName is null");
            return null;
        }
        Item item = null;
        if (!TextUtils.isEmpty(componentName.getClassName())) {
            Item item2 = this.mIconPackItemCache.get(componentName.flattenToString().toLowerCase());
            item = item2;
            if (item == null) {
                Item item3 = this.mIconPackItemCache.get(componentName.flattenToShortString().toLowerCase());
                item = item3;
            }
            if (item == null) {
                Item item4 = this.mIconPackItemCache.get(componentName.getPackageName().toLowerCase());
                item = item4;
            }
            if (item == null) {
                String mappingComponent = OplusIconPackMappingHelper.getMappingComponent(componentName.flattenToShortString(), componentName.getPackageName());
                if (mappingComponent != null) {
                    Item item5 = this.mIconPackItemCache.get(mappingComponent.toLowerCase());
                    item = item5;
                } else {
                    String mappingComponent2 = OplusIconPackMappingHelper.getMappingComponent(componentName.flattenToString(), componentName.getPackageName());
                    if (mappingComponent2 != null) {
                        Item item6 = this.mIconPackItemCache.get(mappingComponent2.toLowerCase());
                        item = item6;
                    }
                }
            }
        }
        if (item == null) {
            Item item7 = this.mIconPackItemCache.get(componentName.getPackageName());
            item = item7;
        }
        if (item != null) {
            int drawableId = item.mDrawableId;
            if (drawableId != -1) {
                id = drawableId;
            } else {
                if (PAC_MAN_ICON_PACK_PACKAGE_NAME.equals(this.mIconPackName) && PACMAN_WEATHER_PACKAGE_NAME.equals(componentName.getPackageName())) {
                    Log.d(TAG, "change compat drawable name for " + componentName.getPackageName());
                    drawableName = PACMAN_WEATHER_DRAWABLE_NAME;
                } else {
                    drawableName = item.mDrawableName;
                }
                id = this.mIconPackResource.getIdentifier(drawableName, "drawable", this.mIconPackName);
            }
            if (id > 0) {
                item.mDrawableId = id;
                Log.d(TAG, "getDrawableIconForPackage getDrawable id:" + id);
                return this.mIconPackResource.getDrawable(id, null);
            }
        }
        String key = TextUtils.isEmpty(componentName.getClassName()) ? componentName.getPackageName() : componentName.flattenToShortString();
        Drawable.ConstantState constantState = this.mGenerateCache.get(key);
        if (constantState != null) {
            Log.d(TAG, "getDrawableIconForPackage getDrawable id is 0, try mGenerateCache get OK!");
            return constantState.newDrawable();
        }
        Log.d(TAG, "getDrawableIconForPackage getDrawable id is 0, try mGenerateCache get null");
        return null;
    }

    public boolean hasGenerateIconPack() {
        return ((this.mIconBack.isEmpty() || this.mIconMask.isEmpty()) && TextUtils.isEmpty(this.mAdaptivePath)) ? false : true;
    }

    public Drawable generateIconPackDrawable(ComponentName componentName, Drawable icon) {
        Drawable icon2;
        BitmapDrawable bitmapDrawable;
        BitmapDrawable iconMask;
        if ((this.mIconBack.isEmpty() && this.mAdaptivePath == null) || componentName == null) {
            Log.e(TAG, "generateIconPackDrawable has null:" + this.mIconBack.isEmpty() + "   path:" + (this.mAdaptivePath == null) + "  CN:" + (componentName == null));
            return icon;
        }
        String key = TextUtils.isEmpty(componentName.getClassName()) ? componentName.getPackageName() : componentName.flattenToShortString();
        Drawable.ConstantState constantState = this.mGenerateCache.get(key);
        if (constantState != null) {
            return constantState.newDrawable();
        }
        if (this.mAdaptivePath != null) {
            this.mFactor = 0.97f;
            icon2 = new BitmapDrawable(this.mIconPackResource, createAdaptivePathIcon(icon, this.mAdaptivePath, 200));
        } else {
            icon2 = icon;
        }
        BitmapDrawable bitmapDrawable2 = null;
        if (this.mIconBack.isEmpty()) {
            bitmapDrawable = null;
        } else {
            List<BitmapDrawable> list = this.mIconBack;
            bitmapDrawable = list.get(this.mIndex % list.size());
        }
        BitmapDrawable iconBack = bitmapDrawable;
        if (this.mIconMask.isEmpty()) {
            iconMask = null;
        } else {
            List<BitmapDrawable> list2 = this.mIconMask;
            iconMask = list2.get(this.mIndex % list2.size());
        }
        if (!this.mIconUpon.isEmpty()) {
            List<BitmapDrawable> list3 = this.mIconUpon;
            bitmapDrawable2 = list3.get(this.mIndex % list3.size());
        }
        BitmapDrawable iconUpon = bitmapDrawable2;
        float iconScale = getDrawableNormalizeScale(key, icon2);
        float iconBackScale = getDrawableNormalizeScale(ICON_BACK_TAG, iconBack);
        float iconUponScale = getDrawableNormalizeScale(ICON_UPON_TAG, iconUpon);
        float iconBackgroundScale = getIconBackgroundScale(iconBackScale, iconUponScale);
        Bitmap bitmap = createIconBitmap(icon2, this.mIconPackResource, iconBack, iconMask, iconUpon, iconScale * this.mFactor, iconBackgroundScale, 200);
        BitmapDrawable bitmapDrawable3 = new BitmapDrawable(this.mIconPackResource, bitmap);
        this.mGenerateCache.put(key, bitmapDrawable3.getConstantState());
        return bitmapDrawable3;
    }

    private float getDrawableNormalizeScale(String key, Drawable d) {
        if (TextUtils.isEmpty(key) || d == null) {
            return 0.0f;
        }
        if (!this.mIconBitmapScaleMap.containsKey(key)) {
            float scale = this.mIconUtils.getScale(d, null);
            if (isComposedIconComponent(key)) {
                Log.d(TAG, "getDrawableNormalizeScale key:" + key);
            }
            this.mIconBitmapScaleMap.put(key, Float.valueOf(scale));
            return scale;
        }
        return this.mIconBitmapScaleMap.get(key).floatValue();
    }

    private static float getIconBackgroundScale(float iconBackScale, float iconUponScale) {
        float iconBackgroundScale = iconBackScale;
        if (iconBackgroundScale == 0.0f) {
            iconBackgroundScale = iconUponScale;
        }
        if (iconBackgroundScale == 0.0f) {
            return 1.0f;
        }
        return iconBackgroundScale;
    }

    private Bitmap createIconBitmap(Drawable icon, Resources res, BitmapDrawable iconBack, BitmapDrawable iconMask, BitmapDrawable iconUpon, float scale, float iconMaskScale, int iconSize) {
        BitmapDrawable bitmapDrawable;
        Bitmap bitmap;
        Bitmap bitmap2;
        int iconSize2 = Math.max(1, iconSize);
        int width = iconSize2;
        int height = iconSize2;
        if (icon instanceof PaintDrawable) {
            PaintDrawable painter = (PaintDrawable) icon;
            painter.setIntrinsicWidth(width);
            painter.setIntrinsicHeight(height);
        } else if ((icon instanceof BitmapDrawable) && (bitmap = (bitmapDrawable = (BitmapDrawable) icon).getBitmap()) != null && bitmap.getDensity() == 0) {
            bitmapDrawable.setTargetDensity(res.getDisplayMetrics());
        }
        int sourceWidth = icon.getIntrinsicWidth();
        int sourceHeight = icon.getIntrinsicHeight();
        if (sourceWidth > 0 && sourceHeight > 0) {
            float ratio = sourceWidth / sourceHeight;
            if (sourceWidth > sourceHeight) {
                height = (int) (width / ratio);
            } else if (sourceHeight > sourceWidth) {
                width = (int) (height * ratio);
            }
        }
        Rect oldBounds = new Rect();
        Rect newBounds = new Rect();
        Canvas canvas = new Canvas();
        Bitmap bitmap3 = Bitmap.createBitmap(iconSize2, iconSize2, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap3);
        int left = (iconSize2 - width) / 2;
        int top = (iconSize2 - height) / 2;
        oldBounds.set(icon.getBounds());
        if (!(icon instanceof AdaptiveIconDrawable)) {
            icon.setBounds(left, top, left + width, top + height);
        } else {
            int offset = Math.max((int) Math.ceil(iconSize2 * 0.010416667f), Math.max(left, top));
            int size = Math.max(width, height) - (offset * 2);
            icon.setBounds(offset, offset, offset + size, offset + size);
        }
        canvas.save();
        canvas.scale(scale, scale, iconSize2 / 2.0f, iconSize2 / 2.0f);
        icon.draw(canvas);
        canvas.restore();
        icon.setBounds(oldBounds);
        newBounds.set(0, 0, iconSize2, iconSize2);
        if (iconMask == null) {
            bitmap2 = bitmap3;
        } else {
            oldBounds.set(iconMask.getBounds());
            if (iconMaskScale >= 1.0f) {
                bitmap2 = bitmap3;
            } else {
                Paint clearPaint = new Paint();
                clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                clearPaint.setColor(-1);
                float innerLeft = ((iconSize2 * (1.0f - iconMaskScale)) / 2.0f) + 1.0f;
                float innerTop = ((iconSize2 * (1.0f - iconMaskScale)) / 2.0f) + 1.0f;
                float innerRight = iconSize2 - innerLeft;
                float innerBottom = iconSize2 - innerTop;
                bitmap2 = bitmap3;
                canvas.drawRect(0.0f, 0.0f, iconSize2, innerTop, clearPaint);
                canvas.drawRect(0.0f, innerTop, innerLeft, iconSize2, clearPaint);
                canvas.drawRect(innerRight, innerTop, iconSize2, iconSize2, clearPaint);
                canvas.drawRect(innerLeft, innerBottom, innerRight, iconSize2, clearPaint);
            }
            canvas.save();
            canvas.scale(iconMaskScale, iconMaskScale, iconSize2 / 2.0f, iconSize2 / 2.0f);
            iconMask.setBounds(newBounds);
            iconMask.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            iconMask.draw(canvas);
            iconMask.setBounds(oldBounds);
            canvas.restore();
        }
        if (iconBack != null) {
            oldBounds.set(iconBack.getBounds());
            canvas.save();
            canvas.scale(iconMaskScale, iconMaskScale, iconSize2 / 2.0f, iconSize2 / 2.0f);
            iconBack.setBounds(newBounds);
            iconBack.getPaint().setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
            iconBack.draw(canvas);
            iconBack.setBounds(oldBounds);
            canvas.restore();
        }
        if (iconUpon != null) {
            oldBounds.set(iconUpon.getBounds());
            canvas.save();
            canvas.scale(iconMaskScale, iconMaskScale, iconSize2 / 2.0f, iconSize2 / 2.0f);
            iconUpon.setBounds(newBounds);
            iconUpon.draw(canvas);
            iconUpon.setBounds(oldBounds);
            canvas.restore();
        }
        canvas.setBitmap(null);
        return bitmap2;
    }

    private Bitmap createAdaptivePathIcon(Drawable drawable, String path, int iconSize) {
        Path path1 = PathParser.createPathFromPathData(path);
        Path result = new Path();
        Matrix matrix = new Matrix();
        matrix.setScale(iconSize / 100, iconSize / 100);
        path1.transform(matrix, result);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        canvas.save();
        canvas.clipPath(result);
        drawable.setBounds(0, 0, iconSize, iconSize);
        drawable.draw(canvas);
        canvas.restore();
        return bitmap;
    }
}
