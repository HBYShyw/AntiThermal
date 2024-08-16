package com.oplus.widget;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.oplus.Telephony;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.ViewDebug;
import android.view.ViewHierarchyEncoder;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.android.internal.R;
import java.util.Calendar;
import java.util.TimeZone;
import libcore.icu.LocaleData;

@RemoteViews.RemoteView
/* loaded from: classes.dex */
public class OplusTextClock extends TextView {

    @Deprecated
    public static final CharSequence DEFAULT_FORMAT_12_HOUR = "h:mm a";

    @Deprecated
    public static final CharSequence DEFAULT_FORMAT_24_HOUR = "H:mm";
    private CharSequence mDescFormat;
    private CharSequence mDescFormat12;
    private CharSequence mDescFormat24;

    @ViewDebug.ExportedProperty
    private CharSequence mFormat;
    private CharSequence mFormat12;
    private CharSequence mFormat24;
    private ContentObserver mFormatChangeObserver;

    @ViewDebug.ExportedProperty
    private boolean mHasSeconds;
    private final BroadcastReceiver mIntentReceiver;
    private boolean mRegistered;
    private boolean mShouldRunTicker;
    private boolean mShowCurrentUserTime;
    private boolean mStopTicking;
    private final Runnable mTicker;
    private Calendar mTime;
    private String mTimeZone;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            OplusTextClock.this.chooseFormat();
            OplusTextClock.this.onTimeChanged();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            OplusTextClock.this.chooseFormat();
            OplusTextClock.this.onTimeChanged();
        }
    }

    public OplusTextClock(Context context) {
        super(context);
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.oplus.widget.OplusTextClock.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (OplusTextClock.this.mStopTicking) {
                    return;
                }
                if (OplusTextClock.this.mTimeZone == null && "android.intent.action.TIMEZONE_CHANGED".equals(intent.getAction())) {
                    String timeZone = intent.getStringExtra("time-zone");
                    OplusTextClock.this.createTime(timeZone);
                }
                OplusTextClock.this.onTimeChanged();
            }
        };
        this.mTicker = new Runnable() { // from class: com.oplus.widget.OplusTextClock.2
            @Override // java.lang.Runnable
            public void run() {
                if (OplusTextClock.this.mStopTicking) {
                    return;
                }
                OplusTextClock.this.onTimeChanged();
                long now = SystemClock.uptimeMillis();
                long next = (1000 - (now % 1000)) + now;
                OplusTextClock.this.getHandler().postAtTime(OplusTextClock.this.mTicker, next);
            }
        };
        init();
    }

    public OplusTextClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OplusTextClock(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public OplusTextClock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mIntentReceiver = new BroadcastReceiver() { // from class: com.oplus.widget.OplusTextClock.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (OplusTextClock.this.mStopTicking) {
                    return;
                }
                if (OplusTextClock.this.mTimeZone == null && "android.intent.action.TIMEZONE_CHANGED".equals(intent.getAction())) {
                    String timeZone = intent.getStringExtra("time-zone");
                    OplusTextClock.this.createTime(timeZone);
                }
                OplusTextClock.this.onTimeChanged();
            }
        };
        this.mTicker = new Runnable() { // from class: com.oplus.widget.OplusTextClock.2
            @Override // java.lang.Runnable
            public void run() {
                if (OplusTextClock.this.mStopTicking) {
                    return;
                }
                OplusTextClock.this.onTimeChanged();
                long now = SystemClock.uptimeMillis();
                long next = (1000 - (now % 1000)) + now;
                OplusTextClock.this.getHandler().postAtTime(OplusTextClock.this.mTicker, next);
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextClock, defStyleAttr, defStyleRes);
        try {
            this.mFormat12 = a.getText(0);
            this.mFormat24 = a.getText(1);
            this.mTimeZone = a.getString(2);
            a.recycle();
            init();
        } catch (Throwable th) {
            a.recycle();
            throw th;
        }
    }

    private void init() {
        if (this.mFormat12 == null || this.mFormat24 == null) {
            LocaleData ld = LocaleData.get(getContext().getResources().getConfiguration().locale);
            if (this.mFormat12 == null) {
                this.mFormat12 = ld.timeFormat_hm;
            }
            if (this.mFormat24 == null) {
                this.mFormat24 = ld.timeFormat_Hm;
            }
        }
        createTime(this.mTimeZone);
        chooseFormat();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createTime(String timeZone) {
        if (timeZone != null) {
            this.mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        } else {
            this.mTime = Calendar.getInstance();
        }
    }

    @ViewDebug.ExportedProperty
    public CharSequence getFormat12Hour() {
        return this.mFormat12;
    }

    @RemotableViewMethod
    public void setFormat12Hour(CharSequence format) {
        this.mFormat12 = format;
        chooseFormat();
        onTimeChanged();
    }

    public void setContentDescriptionFormat12Hour(CharSequence format) {
        this.mDescFormat12 = format;
        chooseFormat();
        onTimeChanged();
    }

    @ViewDebug.ExportedProperty
    public CharSequence getFormat24Hour() {
        return this.mFormat24;
    }

    @RemotableViewMethod
    public void setFormat24Hour(CharSequence format) {
        this.mFormat24 = format;
        chooseFormat();
        onTimeChanged();
    }

    public void setContentDescriptionFormat24Hour(CharSequence format) {
        this.mDescFormat24 = format;
        chooseFormat();
        onTimeChanged();
    }

    public void setShowCurrentUserTime(boolean showCurrentUserTime) {
        this.mShowCurrentUserTime = showCurrentUserTime;
        chooseFormat();
        onTimeChanged();
        unregisterObserver();
        registerObserver();
    }

    public void refresh() {
        onTimeChanged();
        invalidate();
    }

    public boolean is24HourModeEnabled() {
        if (this.mShowCurrentUserTime) {
            return DateFormat.is24HourFormat(getContext(), ActivityManager.getCurrentUser());
        }
        return DateFormat.is24HourFormat(getContext());
    }

    public String getTimeZone() {
        return this.mTimeZone;
    }

    @RemotableViewMethod
    public void setTimeZone(String timeZone) {
        this.mTimeZone = timeZone;
        createTime(timeZone);
        onTimeChanged();
    }

    public CharSequence getFormat() {
        return this.mFormat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void chooseFormat() {
        boolean format24Requested = is24HourModeEnabled();
        LocaleData ld = LocaleData.get(getContext().getResources().getConfiguration().locale);
        if (format24Requested) {
            CharSequence abc = abc(this.mFormat24, this.mFormat12, ld.timeFormat_Hm);
            this.mFormat = abc;
            this.mDescFormat = abc(this.mDescFormat24, this.mDescFormat12, abc);
        } else {
            CharSequence abc2 = abc(this.mFormat12, this.mFormat24, ld.timeFormat_hm);
            this.mFormat = abc2;
            this.mDescFormat = abc(this.mDescFormat12, this.mDescFormat24, abc2);
        }
        boolean hadSeconds = this.mHasSeconds;
        boolean hasSeconds = DateFormat.hasSeconds(this.mFormat);
        this.mHasSeconds = hasSeconds;
        if (this.mShouldRunTicker && hadSeconds != hasSeconds) {
            if (!hadSeconds) {
                this.mTicker.run();
            } else {
                getHandler().removeCallbacks(this.mTicker);
            }
        }
    }

    private static CharSequence abc(CharSequence a, CharSequence b, CharSequence c) {
        return a == null ? b == null ? c : b : a;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mRegistered) {
            this.mRegistered = true;
            registerReceiver();
            registerObserver();
            createTime(this.mTimeZone);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        boolean z = this.mShouldRunTicker;
        if (!z && isVisible) {
            this.mShouldRunTicker = true;
            if (this.mHasSeconds) {
                this.mTicker.run();
                return;
            } else {
                onTimeChanged();
                return;
            }
        }
        if (z && !isVisible) {
            this.mShouldRunTicker = false;
            getHandler().removeCallbacks(this.mTicker);
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mRegistered) {
            unregisterReceiver();
            unregisterObserver();
            this.mRegistered = false;
        }
    }

    public void disableClockTick() {
        this.mStopTicking = true;
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        filter.addAction("android.intent.action.TIME_SET");
        filter.addAction("android.intent.action.TIMEZONE_CHANGED");
        getContext().registerReceiverAsUser(this.mIntentReceiver, Process.myUserHandle(), filter, null, getHandler());
    }

    private void registerObserver() {
        if (this.mRegistered) {
            if (this.mFormatChangeObserver == null) {
                this.mFormatChangeObserver = new FormatChangeObserver(getHandler());
            }
            ContentResolver resolver = getContext().getContentResolver();
            Uri uri = Settings.System.getUriFor("time_12_24");
            if (this.mShowCurrentUserTime) {
                resolver.registerContentObserver(uri, true, this.mFormatChangeObserver, -1);
            } else {
                resolver.registerContentObserver(uri, true, this.mFormatChangeObserver);
            }
        }
    }

    private void unregisterReceiver() {
        getContext().unregisterReceiver(this.mIntentReceiver);
    }

    private void unregisterObserver() {
        if (this.mFormatChangeObserver != null) {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.unregisterContentObserver(this.mFormatChangeObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTimeChanged() {
        if (this.mShouldRunTicker) {
            this.mTime.setTimeInMillis(System.currentTimeMillis());
            String tempTime = (String) DateFormat.format(this.mFormat, this.mTime);
            SpannableStringBuilder style = new SpannableStringBuilder(tempTime);
            char[] temps = tempTime.toCharArray();
            for (int i = 0; i < temps.length; i++) {
                if ("1".equals(String.valueOf(temps[i]))) {
                    style.setSpan(new ForegroundColorSpan(-65536), i, i + 1, 34);
                }
            }
            setText(style);
            setContentDescription(DateFormat.format(this.mDescFormat, this.mTime));
        }
    }

    protected void encodeProperties(ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        CharSequence s = getFormat12Hour();
        stream.addProperty("format12Hour", s == null ? null : s.toString());
        CharSequence s2 = getFormat24Hour();
        stream.addProperty("format24Hour", s2 == null ? null : s2.toString());
        CharSequence charSequence = this.mFormat;
        stream.addProperty(Telephony.CellBroadcasts.MESSAGE_FORMAT, charSequence != null ? charSequence.toString() : null);
        stream.addProperty("hasSeconds", this.mHasSeconds);
    }
}
