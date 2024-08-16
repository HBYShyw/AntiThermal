package android.sysprop;

import android.os.SystemProperties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

/* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
public final class SurfaceFlingerProperties {
    private SurfaceFlingerProperties() {
    }

    private static Boolean tryParseBoolean(String str) {
        if (str == null) {
            return null;
        }
        String lowerCase = str.toLowerCase(Locale.US);
        lowerCase.hashCode();
        char c = 65535;
        switch (lowerCase.hashCode()) {
            case 48:
                if (lowerCase.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (lowerCase.equals("1")) {
                    c = 1;
                    break;
                }
                break;
            case 3569038:
                if (lowerCase.equals("true")) {
                    c = 2;
                    break;
                }
                break;
            case 97196323:
                if (lowerCase.equals("false")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 3:
                return Boolean.FALSE;
            case 1:
            case 2:
                return Boolean.TRUE;
            default:
                return null;
        }
    }

    private static Integer tryParseInteger(String str) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static Integer tryParseUInt(String str) {
        try {
            return Integer.valueOf(Integer.parseUnsignedInt(str));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static Long tryParseLong(String str) {
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static Long tryParseULong(String str) {
        try {
            return Long.valueOf(Long.parseUnsignedLong(str));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Double tryParseDouble(String str) {
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private static String tryParseString(String str) {
        if ("".equals(str)) {
            return null;
        }
        return str;
    }

    private static <T extends Enum<T>> T tryParseEnum(Class<T> cls, String str) {
        try {
            return (T) Enum.valueOf(cls, str.toUpperCase(Locale.US));
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    private static <T> List<T> tryParseList(Function<String, T> function, String str) {
        if ("".equals(str)) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            StringBuilder sb = new StringBuilder();
            while (i < str.length() && str.charAt(i) != ',') {
                if (str.charAt(i) == '\\') {
                    i++;
                }
                if (i == str.length()) {
                    break;
                }
                sb.append(str.charAt(i));
                i++;
            }
            arrayList.add(function.apply(sb.toString()));
            if (i == str.length()) {
                return arrayList;
            }
            i++;
        }
    }

    private static <T extends Enum<T>> List<T> tryParseEnumList(Class<T> cls, String str) {
        if ("".equals(str)) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (String str2 : str.split(",")) {
            arrayList.add(tryParseEnum(cls, str2));
        }
        return arrayList;
    }

    private static String escape(String str) {
        return str.replaceAll("([\\\\,])", "\\\\$1");
    }

    private static <T> String formatList(List<T> list) {
        StringJoiner stringJoiner = new StringJoiner(",");
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T next = it.next();
            stringJoiner.add(next == null ? "" : escape(next.toString()));
        }
        return stringJoiner.toString();
    }

    private static String formatUIntList(List<Integer> list) {
        StringJoiner stringJoiner = new StringJoiner(",");
        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            Integer next = it.next();
            stringJoiner.add(next == null ? "" : escape(Integer.toUnsignedString(next.intValue())));
        }
        return stringJoiner.toString();
    }

    private static String formatULongList(List<Long> list) {
        StringJoiner stringJoiner = new StringJoiner(",");
        Iterator<Long> it = list.iterator();
        while (it.hasNext()) {
            Long next = it.next();
            stringJoiner.add(next == null ? "" : escape(Long.toUnsignedString(next.longValue())));
        }
        return stringJoiner.toString();
    }

    private static <T extends Enum<T>> String formatEnumList(List<T> list, Function<T, String> function) {
        StringJoiner stringJoiner = new StringJoiner(",");
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T next = it.next();
            stringJoiner.add(next == null ? "" : function.apply(next));
        }
        return stringJoiner.toString();
    }

    public static Optional<Long> vsync_event_phase_offset_ns() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.surface_flinger.vsync_event_phase_offset_ns")));
    }

    public static Optional<Long> vsync_sf_event_phase_offset_ns() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.surface_flinger.vsync_sf_event_phase_offset_ns")));
    }

    public static Optional<Boolean> use_context_priority() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.use_context_priority")));
    }

    public static Optional<Long> max_frame_buffer_acquired_buffers() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.surface_flinger.max_frame_buffer_acquired_buffers")));
    }

    public static Optional<Integer> max_graphics_width() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.surface_flinger.max_graphics_width")));
    }

    public static Optional<Integer> max_graphics_height() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.surface_flinger.max_graphics_height")));
    }

    public static Optional<Boolean> has_wide_color_display() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.has_wide_color_display")));
    }

    public static Optional<Boolean> running_without_sync_framework() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.running_without_sync_framework")));
    }

    public static Optional<Boolean> has_HDR_display() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.has_HDR_display")));
    }

    public static Optional<Long> present_time_offset_from_vsync_ns() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.surface_flinger.present_time_offset_from_vsync_ns")));
    }

    public static Optional<Boolean> force_hwc_copy_for_virtual_displays() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.force_hwc_copy_for_virtual_displays")));
    }

    public static Optional<Long> max_virtual_display_dimension() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.surface_flinger.max_virtual_display_dimension")));
    }

    public static Optional<Boolean> use_vr_flinger() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.use_vr_flinger")));
    }

    public static Optional<Boolean> start_graphics_allocator_service() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.start_graphics_allocator_service")));
    }

    /* loaded from: C:\Users\HuangYW\Desktop\Realme反编译\services\classes.dex */
    public enum primary_display_orientation_values {
        ORIENTATION_0("ORIENTATION_0"),
        ORIENTATION_90("ORIENTATION_90"),
        ORIENTATION_180("ORIENTATION_180"),
        ORIENTATION_270("ORIENTATION_270");

        private final String propValue;

        primary_display_orientation_values(String str) {
            this.propValue = str;
        }

        public String getPropValue() {
            return this.propValue;
        }
    }

    public static Optional<primary_display_orientation_values> primary_display_orientation() {
        return Optional.ofNullable((primary_display_orientation_values) tryParseEnum(primary_display_orientation_values.class, SystemProperties.get("ro.surface_flinger.primary_display_orientation")));
    }

    public static Optional<Boolean> use_color_management() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.use_color_management")));
    }

    public static Optional<Long> default_composition_dataspace() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.surface_flinger.default_composition_dataspace")));
    }

    public static Optional<Integer> default_composition_pixel_format() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.surface_flinger.default_composition_pixel_format")));
    }

    public static Optional<Long> wcg_composition_dataspace() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.surface_flinger.wcg_composition_dataspace")));
    }

    public static Optional<Integer> wcg_composition_pixel_format() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.surface_flinger.wcg_composition_pixel_format")));
    }

    public static Optional<Long> color_space_agnostic_dataspace() {
        return Optional.ofNullable(tryParseLong(SystemProperties.get("ro.surface_flinger.color_space_agnostic_dataspace")));
    }

    public static List<Double> display_primary_red() {
        return tryParseList(new Function() { // from class: android.sysprop.SurfaceFlingerProperties$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Double tryParseDouble;
                tryParseDouble = SurfaceFlingerProperties.tryParseDouble((String) obj);
                return tryParseDouble;
            }
        }, SystemProperties.get("ro.surface_flinger.display_primary_red"));
    }

    public static List<Double> display_primary_green() {
        return tryParseList(new Function() { // from class: android.sysprop.SurfaceFlingerProperties$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Double tryParseDouble;
                tryParseDouble = SurfaceFlingerProperties.tryParseDouble((String) obj);
                return tryParseDouble;
            }
        }, SystemProperties.get("ro.surface_flinger.display_primary_green"));
    }

    public static List<Double> display_primary_blue() {
        return tryParseList(new Function() { // from class: android.sysprop.SurfaceFlingerProperties$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Double tryParseDouble;
                tryParseDouble = SurfaceFlingerProperties.tryParseDouble((String) obj);
                return tryParseDouble;
            }
        }, SystemProperties.get("ro.surface_flinger.display_primary_blue"));
    }

    public static List<Double> display_primary_white() {
        return tryParseList(new Function() { // from class: android.sysprop.SurfaceFlingerProperties$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Double tryParseDouble;
                tryParseDouble = SurfaceFlingerProperties.tryParseDouble((String) obj);
                return tryParseDouble;
            }
        }, SystemProperties.get("ro.surface_flinger.display_primary_white"));
    }

    @Deprecated
    public static Optional<Boolean> refresh_rate_switching() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.refresh_rate_switching")));
    }

    public static Optional<Integer> set_idle_timer_ms() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.surface_flinger.set_idle_timer_ms")));
    }

    public static Optional<Integer> set_touch_timer_ms() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.surface_flinger.set_touch_timer_ms")));
    }

    public static Optional<Integer> set_display_power_timer_ms() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.surface_flinger.set_display_power_timer_ms")));
    }

    public static Optional<Boolean> use_content_detection_for_refresh_rate() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.use_content_detection_for_refresh_rate")));
    }

    @Deprecated
    public static Optional<Boolean> use_smart_90_for_video() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.use_smart_90_for_video")));
    }

    public static Optional<Boolean> enable_protected_contents() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.protected_contents")));
    }

    public static Optional<Boolean> support_kernel_idle_timer() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.support_kernel_idle_timer")));
    }

    public static Optional<Boolean> supports_background_blur() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.supports_background_blur")));
    }

    public static Optional<Integer> display_update_imminent_timeout_ms() {
        return Optional.ofNullable(tryParseInteger(SystemProperties.get("ro.surface_flinger.display_update_imminent_timeout_ms")));
    }

    public static Optional<Boolean> update_device_product_info_on_hotplug_reconnect() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.update_device_product_info_on_hotplug_reconnect")));
    }

    public static Optional<Boolean> enable_frame_rate_override() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.enable_frame_rate_override")));
    }

    public static Optional<Boolean> enable_layer_caching() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.enable_layer_caching")));
    }

    public static Optional<Boolean> enable_sdr_dimming() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.enable_sdr_dimming")));
    }

    public static Optional<Boolean> ignore_hdr_camera_layers() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.ignore_hdr_camera_layers")));
    }

    public static Optional<Boolean> clear_slots_with_set_layer_buffer() {
        return Optional.ofNullable(tryParseBoolean(SystemProperties.get("ro.surface_flinger.clear_slots_with_set_layer_buffer")));
    }
}
