package android.widget;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.widget.RemoteViews;
import com.oplus.oms.split.common.SplitConstants;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@RemoteViews.RemoteView
/* loaded from: classes.dex */
public class OplusDateTimeView extends DateTimeView {
    private LocalDateTime mLocalTime;
    private long mTimeMillis;

    public OplusDateTimeView(Context context) {
        this(context, null);
    }

    public OplusDateTimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @RemotableViewMethod
    public void setTime(long timeMillis) {
        this.mTimeMillis = timeMillis;
        LocalDateTime dateTime = toLocalDateTime(timeMillis, ZoneId.systemDefault());
        this.mLocalTime = dateTime.withSecond(0);
        super.setTime(timeMillis);
    }

    void update() {
        if (this.mLocalTime == null || getVisibility() == 8) {
            return;
        }
        super.update();
        if (isShowRelativeTime()) {
            updateColorRelativeTime();
        }
    }

    private void updateColorRelativeTime() {
        int i;
        String result;
        int i2;
        int i3;
        int i4;
        long now = System.currentTimeMillis();
        long duration = Math.abs(now - this.mTimeMillis);
        boolean past = now >= this.mTimeMillis;
        if (duration < 60000) {
            return;
        }
        if (duration < SplitConstants.ONE_HOUR) {
            int count = (int) (duration / 60000);
            Resources resources = getContext().getResources();
            if (past) {
                i4 = R.string.font_family_body_1_material;
            } else {
                i4 = R.string.font_family_body_2_material;
            }
            result = String.format(resources.getQuantityString(i4, count), Integer.valueOf(count));
        } else if (duration < 86400000) {
            int count2 = (int) (duration / SplitConstants.ONE_HOUR);
            Resources resources2 = getContext().getResources();
            if (past) {
                i3 = R.string.fingerprint_name_template;
            } else {
                i3 = R.string.fingerprints;
            }
            result = String.format(resources2.getQuantityString(i3, count2), Integer.valueOf(count2));
        } else if (duration < 31449600000L) {
            TimeZone timeZone = TimeZone.getDefault();
            int count3 = Math.max(Math.abs(dayDistance(timeZone, this.mTimeMillis, now)), 1);
            Resources resources3 = getContext().getResources();
            if (past) {
                i2 = R.string.fingerprint_error_security_update_required;
            } else {
                i2 = R.string.fingerprint_error_timeout;
            }
            result = String.format(resources3.getQuantityString(i2, count3), Integer.valueOf(count3));
        } else {
            int count4 = (int) (duration / 31449600000L);
            Resources resources4 = getContext().getResources();
            if (past) {
                i = R.string.font_family_display_1_material;
            } else {
                i = R.string.font_family_display_2_material;
            }
            result = String.format(resources4.getQuantityString(i, count4), Integer.valueOf(count4));
        }
        setText(result);
    }

    private static int dayDistance(TimeZone timeZone, long startTime, long endTime) {
        return Time.getJulianDay(endTime, timeZone.getOffset(endTime) / 1000) - Time.getJulianDay(startTime, timeZone.getOffset(startTime) / 1000);
    }

    private static LocalDateTime toLocalDateTime(long timeMillis, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(timeMillis);
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}
