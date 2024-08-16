package com.android.server.appwidget;

import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.os.Build;
import android.text.TextUtils;
import android.util.Slog;
import com.android.modules.utils.TypedXmlPullParser;
import com.android.modules.utils.TypedXmlSerializer;
import java.io.IOException;
import java.util.Objects;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public class AppWidgetXmlUtil {
    private static final String ATTR_AUTO_ADVANCED_VIEW_ID = "auto_advance_view_id";
    private static final String ATTR_CONFIGURE = "configure";
    private static final String ATTR_DESCRIPTION_RES = "description_res";
    private static final String ATTR_ICON = "icon";
    private static final String ATTR_INITIAL_KEYGUARD_LAYOUT = "initial_keyguard_layout";
    private static final String ATTR_INITIAL_LAYOUT = "initial_layout";
    private static final String ATTR_LABEL = "label";
    private static final String ATTR_MAX_RESIZE_HEIGHT = "max_resize_height";
    private static final String ATTR_MAX_RESIZE_WIDTH = "max_resize_width";
    private static final String ATTR_MIN_HEIGHT = "min_height";
    private static final String ATTR_MIN_RESIZE_HEIGHT = "min_resize_height";
    private static final String ATTR_MIN_RESIZE_WIDTH = "min_resize_width";
    private static final String ATTR_MIN_WIDTH = "min_width";
    private static final String ATTR_OS_FINGERPRINT = "os_fingerprint";
    private static final String ATTR_PREVIEW_IMAGE = "preview_image";
    private static final String ATTR_PREVIEW_LAYOUT = "preview_layout";
    private static final String ATTR_PROVIDER_INHERITANCE = "provider_inheritance";
    private static final String ATTR_RESIZE_MODE = "resize_mode";
    private static final String ATTR_TARGET_CELL_HEIGHT = "target_cell_height";
    private static final String ATTR_TARGET_CELL_WIDTH = "target_cell_width";
    private static final String ATTR_UPDATE_PERIOD_MILLIS = "update_period_millis";
    private static final String ATTR_WIDGET_CATEGORY = "widget_category";
    private static final String ATTR_WIDGET_FEATURES = "widget_features";
    private static final String TAG = "AppWidgetXmlUtil";

    public static void writeAppWidgetProviderInfoLocked(TypedXmlSerializer typedXmlSerializer, AppWidgetProviderInfo appWidgetProviderInfo) throws IOException {
        Objects.requireNonNull(typedXmlSerializer);
        Objects.requireNonNull(appWidgetProviderInfo);
        typedXmlSerializer.attributeInt((String) null, ATTR_MIN_WIDTH, appWidgetProviderInfo.minWidth);
        typedXmlSerializer.attributeInt((String) null, ATTR_MIN_HEIGHT, appWidgetProviderInfo.minHeight);
        typedXmlSerializer.attributeInt((String) null, ATTR_MIN_RESIZE_WIDTH, appWidgetProviderInfo.minResizeWidth);
        typedXmlSerializer.attributeInt((String) null, ATTR_MIN_RESIZE_HEIGHT, appWidgetProviderInfo.minResizeHeight);
        typedXmlSerializer.attributeInt((String) null, ATTR_MAX_RESIZE_WIDTH, appWidgetProviderInfo.maxResizeWidth);
        typedXmlSerializer.attributeInt((String) null, ATTR_MAX_RESIZE_HEIGHT, appWidgetProviderInfo.maxResizeHeight);
        typedXmlSerializer.attributeInt((String) null, ATTR_TARGET_CELL_WIDTH, appWidgetProviderInfo.targetCellWidth);
        typedXmlSerializer.attributeInt((String) null, ATTR_TARGET_CELL_HEIGHT, appWidgetProviderInfo.targetCellHeight);
        typedXmlSerializer.attributeInt((String) null, ATTR_UPDATE_PERIOD_MILLIS, appWidgetProviderInfo.updatePeriodMillis);
        typedXmlSerializer.attributeInt((String) null, ATTR_INITIAL_LAYOUT, appWidgetProviderInfo.initialLayout);
        typedXmlSerializer.attributeInt((String) null, ATTR_INITIAL_KEYGUARD_LAYOUT, appWidgetProviderInfo.initialKeyguardLayout);
        ComponentName componentName = appWidgetProviderInfo.configure;
        if (componentName != null) {
            typedXmlSerializer.attribute((String) null, ATTR_CONFIGURE, componentName.flattenToShortString());
        }
        String str = appWidgetProviderInfo.label;
        if (str != null) {
            typedXmlSerializer.attribute((String) null, ATTR_LABEL, str);
        } else {
            Slog.e(TAG, "Label is empty in " + appWidgetProviderInfo.provider);
        }
        typedXmlSerializer.attributeInt((String) null, ATTR_ICON, appWidgetProviderInfo.icon);
        typedXmlSerializer.attributeInt((String) null, ATTR_PREVIEW_IMAGE, appWidgetProviderInfo.previewImage);
        typedXmlSerializer.attributeInt((String) null, ATTR_PREVIEW_LAYOUT, appWidgetProviderInfo.previewLayout);
        typedXmlSerializer.attributeInt((String) null, ATTR_AUTO_ADVANCED_VIEW_ID, appWidgetProviderInfo.autoAdvanceViewId);
        typedXmlSerializer.attributeInt((String) null, ATTR_RESIZE_MODE, appWidgetProviderInfo.resizeMode);
        typedXmlSerializer.attributeInt((String) null, ATTR_WIDGET_CATEGORY, appWidgetProviderInfo.widgetCategory);
        typedXmlSerializer.attributeInt((String) null, ATTR_WIDGET_FEATURES, appWidgetProviderInfo.widgetFeatures);
        typedXmlSerializer.attributeInt((String) null, ATTR_DESCRIPTION_RES, appWidgetProviderInfo.descriptionRes);
        typedXmlSerializer.attributeBoolean((String) null, ATTR_PROVIDER_INHERITANCE, appWidgetProviderInfo.isExtendedFromAppWidgetProvider);
        typedXmlSerializer.attribute((String) null, ATTR_OS_FINGERPRINT, Build.FINGERPRINT);
    }

    public static AppWidgetProviderInfo readAppWidgetProviderInfoLocked(TypedXmlPullParser typedXmlPullParser) {
        Objects.requireNonNull(typedXmlPullParser);
        if (!Build.FINGERPRINT.equals(typedXmlPullParser.getAttributeValue((String) null, ATTR_OS_FINGERPRINT))) {
            return null;
        }
        AppWidgetProviderInfo appWidgetProviderInfo = new AppWidgetProviderInfo();
        appWidgetProviderInfo.minWidth = typedXmlPullParser.getAttributeInt((String) null, ATTR_MIN_WIDTH, 0);
        appWidgetProviderInfo.minHeight = typedXmlPullParser.getAttributeInt((String) null, ATTR_MIN_HEIGHT, 0);
        appWidgetProviderInfo.minResizeWidth = typedXmlPullParser.getAttributeInt((String) null, ATTR_MIN_RESIZE_WIDTH, 0);
        appWidgetProviderInfo.minResizeHeight = typedXmlPullParser.getAttributeInt((String) null, ATTR_MIN_RESIZE_HEIGHT, 0);
        appWidgetProviderInfo.maxResizeWidth = typedXmlPullParser.getAttributeInt((String) null, ATTR_MAX_RESIZE_WIDTH, 0);
        appWidgetProviderInfo.maxResizeHeight = typedXmlPullParser.getAttributeInt((String) null, ATTR_MAX_RESIZE_HEIGHT, 0);
        appWidgetProviderInfo.targetCellWidth = typedXmlPullParser.getAttributeInt((String) null, ATTR_TARGET_CELL_WIDTH, 0);
        appWidgetProviderInfo.targetCellHeight = typedXmlPullParser.getAttributeInt((String) null, ATTR_TARGET_CELL_HEIGHT, 0);
        appWidgetProviderInfo.updatePeriodMillis = typedXmlPullParser.getAttributeInt((String) null, ATTR_UPDATE_PERIOD_MILLIS, 0);
        appWidgetProviderInfo.initialLayout = typedXmlPullParser.getAttributeInt((String) null, ATTR_INITIAL_LAYOUT, 0);
        appWidgetProviderInfo.initialKeyguardLayout = typedXmlPullParser.getAttributeInt((String) null, ATTR_INITIAL_KEYGUARD_LAYOUT, 0);
        String attributeValue = typedXmlPullParser.getAttributeValue((String) null, ATTR_CONFIGURE);
        if (!TextUtils.isEmpty(attributeValue)) {
            appWidgetProviderInfo.configure = ComponentName.unflattenFromString(attributeValue);
        }
        appWidgetProviderInfo.label = typedXmlPullParser.getAttributeValue((String) null, ATTR_LABEL);
        appWidgetProviderInfo.icon = typedXmlPullParser.getAttributeInt((String) null, ATTR_ICON, 0);
        appWidgetProviderInfo.previewImage = typedXmlPullParser.getAttributeInt((String) null, ATTR_PREVIEW_IMAGE, 0);
        appWidgetProviderInfo.previewLayout = typedXmlPullParser.getAttributeInt((String) null, ATTR_PREVIEW_LAYOUT, 0);
        appWidgetProviderInfo.autoAdvanceViewId = typedXmlPullParser.getAttributeInt((String) null, ATTR_AUTO_ADVANCED_VIEW_ID, 0);
        appWidgetProviderInfo.resizeMode = typedXmlPullParser.getAttributeInt((String) null, ATTR_RESIZE_MODE, 0);
        appWidgetProviderInfo.widgetCategory = typedXmlPullParser.getAttributeInt((String) null, ATTR_WIDGET_CATEGORY, 0);
        appWidgetProviderInfo.widgetFeatures = typedXmlPullParser.getAttributeInt((String) null, ATTR_WIDGET_FEATURES, 0);
        appWidgetProviderInfo.descriptionRes = typedXmlPullParser.getAttributeInt((String) null, ATTR_DESCRIPTION_RES, 0);
        appWidgetProviderInfo.isExtendedFromAppWidgetProvider = typedXmlPullParser.getAttributeBoolean((String) null, ATTR_PROVIDER_INHERITANCE, false);
        return appWidgetProviderInfo;
    }
}
