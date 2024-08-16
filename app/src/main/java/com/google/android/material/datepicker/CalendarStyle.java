package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Paint;
import com.google.android.material.R$attr;
import com.google.android.material.R$styleable;
import z3.MaterialAttributes;
import z3.MaterialResources;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CalendarStyle.java */
/* renamed from: com.google.android.material.datepicker.b, reason: use source file name */
/* loaded from: classes.dex */
public final class CalendarStyle {

    /* renamed from: a, reason: collision with root package name */
    final CalendarItemStyle f8690a;

    /* renamed from: b, reason: collision with root package name */
    final CalendarItemStyle f8691b;

    /* renamed from: c, reason: collision with root package name */
    final CalendarItemStyle f8692c;

    /* renamed from: d, reason: collision with root package name */
    final CalendarItemStyle f8693d;

    /* renamed from: e, reason: collision with root package name */
    final CalendarItemStyle f8694e;

    /* renamed from: f, reason: collision with root package name */
    final CalendarItemStyle f8695f;

    /* renamed from: g, reason: collision with root package name */
    final CalendarItemStyle f8696g;

    /* renamed from: h, reason: collision with root package name */
    final Paint f8697h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CalendarStyle(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(MaterialAttributes.d(context, R$attr.materialCalendarStyle, MaterialCalendar.class.getCanonicalName()), R$styleable.MaterialCalendar);
        this.f8690a = CalendarItemStyle.a(context, obtainStyledAttributes.getResourceId(R$styleable.MaterialCalendar_dayStyle, 0));
        this.f8696g = CalendarItemStyle.a(context, obtainStyledAttributes.getResourceId(R$styleable.MaterialCalendar_dayInvalidStyle, 0));
        this.f8691b = CalendarItemStyle.a(context, obtainStyledAttributes.getResourceId(R$styleable.MaterialCalendar_daySelectedStyle, 0));
        this.f8692c = CalendarItemStyle.a(context, obtainStyledAttributes.getResourceId(R$styleable.MaterialCalendar_dayTodayStyle, 0));
        ColorStateList a10 = MaterialResources.a(context, obtainStyledAttributes, R$styleable.MaterialCalendar_rangeFillColor);
        this.f8693d = CalendarItemStyle.a(context, obtainStyledAttributes.getResourceId(R$styleable.MaterialCalendar_yearStyle, 0));
        this.f8694e = CalendarItemStyle.a(context, obtainStyledAttributes.getResourceId(R$styleable.MaterialCalendar_yearSelectedStyle, 0));
        this.f8695f = CalendarItemStyle.a(context, obtainStyledAttributes.getResourceId(R$styleable.MaterialCalendar_yearTodayStyle, 0));
        Paint paint = new Paint();
        this.f8697h = paint;
        paint.setColor(a10.getDefaultColor());
        obtainStyledAttributes.recycle();
    }
}
