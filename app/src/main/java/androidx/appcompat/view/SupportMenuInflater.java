package androidx.appcompat.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.R$styleable;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.t;
import androidx.core.view.ActionProvider;
import androidx.core.view.MenuItemCompat;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import p.SupportMenu;

/* compiled from: SupportMenuInflater.java */
/* renamed from: androidx.appcompat.view.g, reason: use source file name */
/* loaded from: classes.dex */
public class SupportMenuInflater extends MenuInflater {

    /* renamed from: e, reason: collision with root package name */
    static final Class<?>[] f578e;

    /* renamed from: f, reason: collision with root package name */
    static final Class<?>[] f579f;

    /* renamed from: a, reason: collision with root package name */
    final Object[] f580a;

    /* renamed from: b, reason: collision with root package name */
    final Object[] f581b;

    /* renamed from: c, reason: collision with root package name */
    Context f582c;

    /* renamed from: d, reason: collision with root package name */
    private Object f583d;

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SupportMenuInflater.java */
    /* renamed from: androidx.appcompat.view.g$a */
    /* loaded from: classes.dex */
    public static class a implements MenuItem.OnMenuItemClickListener {

        /* renamed from: g, reason: collision with root package name */
        private static final Class<?>[] f584g = {MenuItem.class};

        /* renamed from: e, reason: collision with root package name */
        private Object f585e;

        /* renamed from: f, reason: collision with root package name */
        private Method f586f;

        public a(Object obj, String str) {
            this.f585e = obj;
            Class<?> cls = obj.getClass();
            try {
                this.f586f = cls.getMethod(str, f584g);
            } catch (Exception e10) {
                InflateException inflateException = new InflateException("Couldn't resolve menu item onClick handler " + str + " in class " + cls.getName());
                inflateException.initCause(e10);
                throw inflateException;
            }
        }

        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            try {
                if (this.f586f.getReturnType() == Boolean.TYPE) {
                    return ((Boolean) this.f586f.invoke(this.f585e, menuItem)).booleanValue();
                }
                this.f586f.invoke(this.f585e, menuItem);
                return true;
            } catch (Exception e10) {
                throw new RuntimeException(e10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: SupportMenuInflater.java */
    /* renamed from: androidx.appcompat.view.g$b */
    /* loaded from: classes.dex */
    public class b {
        ActionProvider A;
        private CharSequence B;
        private CharSequence C;
        private ColorStateList D = null;
        private PorterDuff.Mode E = null;

        /* renamed from: a, reason: collision with root package name */
        private Menu f587a;

        /* renamed from: b, reason: collision with root package name */
        private int f588b;

        /* renamed from: c, reason: collision with root package name */
        private int f589c;

        /* renamed from: d, reason: collision with root package name */
        private int f590d;

        /* renamed from: e, reason: collision with root package name */
        private int f591e;

        /* renamed from: f, reason: collision with root package name */
        private boolean f592f;

        /* renamed from: g, reason: collision with root package name */
        private boolean f593g;

        /* renamed from: h, reason: collision with root package name */
        private boolean f594h;

        /* renamed from: i, reason: collision with root package name */
        private int f595i;

        /* renamed from: j, reason: collision with root package name */
        private int f596j;

        /* renamed from: k, reason: collision with root package name */
        private CharSequence f597k;

        /* renamed from: l, reason: collision with root package name */
        private CharSequence f598l;

        /* renamed from: m, reason: collision with root package name */
        private int f599m;

        /* renamed from: n, reason: collision with root package name */
        private char f600n;

        /* renamed from: o, reason: collision with root package name */
        private int f601o;

        /* renamed from: p, reason: collision with root package name */
        private char f602p;

        /* renamed from: q, reason: collision with root package name */
        private int f603q;

        /* renamed from: r, reason: collision with root package name */
        private int f604r;

        /* renamed from: s, reason: collision with root package name */
        private boolean f605s;

        /* renamed from: t, reason: collision with root package name */
        private boolean f606t;

        /* renamed from: u, reason: collision with root package name */
        private boolean f607u;

        /* renamed from: v, reason: collision with root package name */
        private int f608v;

        /* renamed from: w, reason: collision with root package name */
        private int f609w;

        /* renamed from: x, reason: collision with root package name */
        private String f610x;

        /* renamed from: y, reason: collision with root package name */
        private String f611y;

        /* renamed from: z, reason: collision with root package name */
        private String f612z;

        public b(Menu menu) {
            this.f587a = menu;
            h();
        }

        private char c(String str) {
            if (str == null) {
                return (char) 0;
            }
            return str.charAt(0);
        }

        private <T> T e(String str, Class<?>[] clsArr, Object[] objArr) {
            try {
                Constructor<?> constructor = Class.forName(str, false, SupportMenuInflater.this.f582c.getClassLoader()).getConstructor(clsArr);
                constructor.setAccessible(true);
                return (T) constructor.newInstance(objArr);
            } catch (Exception e10) {
                Log.w("SupportMenuInflater", "Cannot instantiate class: " + str, e10);
                return null;
            }
        }

        private void i(MenuItem menuItem) {
            boolean z10 = false;
            menuItem.setChecked(this.f605s).setVisible(this.f606t).setEnabled(this.f607u).setCheckable(this.f604r >= 1).setTitleCondensed(this.f598l).setIcon(this.f599m);
            int i10 = this.f608v;
            if (i10 >= 0) {
                menuItem.setShowAsAction(i10);
            }
            if (this.f612z != null) {
                if (!SupportMenuInflater.this.f582c.isRestricted()) {
                    menuItem.setOnMenuItemClickListener(new a(SupportMenuInflater.this.b(), this.f612z));
                } else {
                    throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
                }
            }
            if (this.f604r >= 2) {
                if (menuItem instanceof MenuItemImpl) {
                    ((MenuItemImpl) menuItem).s(true);
                } else if (menuItem instanceof MenuItemWrapperICS) {
                    ((MenuItemWrapperICS) menuItem).h(true);
                }
            }
            String str = this.f610x;
            if (str != null) {
                menuItem.setActionView((View) e(str, SupportMenuInflater.f578e, SupportMenuInflater.this.f580a));
                z10 = true;
            }
            int i11 = this.f609w;
            if (i11 > 0) {
                if (!z10) {
                    menuItem.setActionView(i11);
                } else {
                    Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
                }
            }
            ActionProvider actionProvider = this.A;
            if (actionProvider != null) {
                MenuItemCompat.a(menuItem, actionProvider);
            }
            MenuItemCompat.c(menuItem, this.B);
            MenuItemCompat.g(menuItem, this.C);
            MenuItemCompat.b(menuItem, this.f600n, this.f601o);
            MenuItemCompat.f(menuItem, this.f602p, this.f603q);
            PorterDuff.Mode mode = this.E;
            if (mode != null) {
                MenuItemCompat.e(menuItem, mode);
            }
            ColorStateList colorStateList = this.D;
            if (colorStateList != null) {
                MenuItemCompat.d(menuItem, colorStateList);
            }
        }

        public void a() {
            this.f594h = true;
            i(this.f587a.add(this.f588b, this.f595i, this.f596j, this.f597k));
        }

        public SubMenu b() {
            this.f594h = true;
            SubMenu addSubMenu = this.f587a.addSubMenu(this.f588b, this.f595i, this.f596j, this.f597k);
            i(addSubMenu.getItem());
            return addSubMenu;
        }

        public boolean d() {
            return this.f594h;
        }

        public void f(AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = SupportMenuInflater.this.f582c.obtainStyledAttributes(attributeSet, R$styleable.MenuGroup);
            this.f588b = obtainStyledAttributes.getResourceId(R$styleable.MenuGroup_android_id, 0);
            this.f589c = obtainStyledAttributes.getInt(R$styleable.MenuGroup_android_menuCategory, 0);
            this.f590d = obtainStyledAttributes.getInt(R$styleable.MenuGroup_android_orderInCategory, 0);
            this.f591e = obtainStyledAttributes.getInt(R$styleable.MenuGroup_android_checkableBehavior, 0);
            this.f592f = obtainStyledAttributes.getBoolean(R$styleable.MenuGroup_android_visible, true);
            this.f593g = obtainStyledAttributes.getBoolean(R$styleable.MenuGroup_android_enabled, true);
            obtainStyledAttributes.recycle();
        }

        public void g(AttributeSet attributeSet) {
            TintTypedArray v7 = TintTypedArray.v(SupportMenuInflater.this.f582c, attributeSet, R$styleable.MenuItem);
            this.f595i = v7.n(R$styleable.MenuItem_android_id, 0);
            this.f596j = (v7.k(R$styleable.MenuItem_android_menuCategory, this.f589c) & (-65536)) | (v7.k(R$styleable.MenuItem_android_orderInCategory, this.f590d) & 65535);
            this.f597k = v7.p(R$styleable.MenuItem_android_title);
            this.f598l = v7.p(R$styleable.MenuItem_android_titleCondensed);
            this.f599m = v7.n(R$styleable.MenuItem_android_icon, 0);
            this.f600n = c(v7.o(R$styleable.MenuItem_android_alphabeticShortcut));
            this.f601o = v7.k(R$styleable.MenuItem_alphabeticModifiers, 4096);
            this.f602p = c(v7.o(R$styleable.MenuItem_android_numericShortcut));
            this.f603q = v7.k(R$styleable.MenuItem_numericModifiers, 4096);
            int i10 = R$styleable.MenuItem_android_checkable;
            if (v7.s(i10)) {
                this.f604r = v7.a(i10, false) ? 1 : 0;
            } else {
                this.f604r = this.f591e;
            }
            this.f605s = v7.a(R$styleable.MenuItem_android_checked, false);
            this.f606t = v7.a(R$styleable.MenuItem_android_visible, this.f592f);
            this.f607u = v7.a(R$styleable.MenuItem_android_enabled, this.f593g);
            this.f608v = v7.k(R$styleable.MenuItem_showAsAction, -1);
            this.f612z = v7.o(R$styleable.MenuItem_android_onClick);
            this.f609w = v7.n(R$styleable.MenuItem_actionLayout, 0);
            this.f610x = v7.o(R$styleable.MenuItem_actionViewClass);
            String o10 = v7.o(R$styleable.MenuItem_actionProviderClass);
            this.f611y = o10;
            boolean z10 = o10 != null;
            if (z10 && this.f609w == 0 && this.f610x == null) {
                this.A = (ActionProvider) e(o10, SupportMenuInflater.f579f, SupportMenuInflater.this.f581b);
            } else {
                if (z10) {
                    Log.w("SupportMenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified.");
                }
                this.A = null;
            }
            this.B = v7.p(R$styleable.MenuItem_contentDescription);
            this.C = v7.p(R$styleable.MenuItem_tooltipText);
            int i11 = R$styleable.MenuItem_iconTintMode;
            if (v7.s(i11)) {
                this.E = t.d(v7.k(i11, -1), this.E);
            } else {
                this.E = null;
            }
            int i12 = R$styleable.MenuItem_iconTint;
            if (v7.s(i12)) {
                this.D = v7.c(i12);
            } else {
                this.D = null;
            }
            v7.x();
            this.f594h = false;
        }

        public void h() {
            this.f588b = 0;
            this.f589c = 0;
            this.f590d = 0;
            this.f591e = 0;
            this.f592f = true;
            this.f593g = true;
        }
    }

    static {
        Class<?>[] clsArr = {Context.class};
        f578e = clsArr;
        f579f = clsArr;
    }

    public SupportMenuInflater(Context context) {
        super(context);
        this.f582c = context;
        Object[] objArr = {context};
        this.f580a = objArr;
        this.f581b = objArr;
    }

    private Object a(Object obj) {
        return (!(obj instanceof Activity) && (obj instanceof ContextWrapper)) ? a(((ContextWrapper) obj).getBaseContext()) : obj;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0048, code lost:
    
        if (r15 == 2) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x004b, code lost:
    
        if (r15 == 3) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x004f, code lost:
    
        r15 = r13.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0053, code lost:
    
        if (r7 == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0059, code lost:
    
        if (r15.equals(r8) == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x005b, code lost:
    
        r8 = null;
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00b9, code lost:
    
        r15 = r13.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0062, code lost:
    
        if (r15.equals("group") == false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0064, code lost:
    
        r0.h();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x006c, code lost:
    
        if (r15.equals("item") == false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0072, code lost:
    
        if (r0.d() != false) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0074, code lost:
    
        r15 = r0.A;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0076, code lost:
    
        if (r15 == null) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x007c, code lost:
    
        if (r15.a() == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x007e, code lost:
    
        r0.b();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0082, code lost:
    
        r0.a();
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x008a, code lost:
    
        if (r15.equals("menu") == false) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x008c, code lost:
    
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x008e, code lost:
    
        if (r7 == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0091, code lost:
    
        r15 = r13.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0099, code lost:
    
        if (r15.equals("group") == false) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x009b, code lost:
    
        r0.f(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00a3, code lost:
    
        if (r15.equals("item") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00a5, code lost:
    
        r0.g(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00ad, code lost:
    
        if (r15.equals("menu") == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00af, code lost:
    
        c(r13, r14, r0.b());
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00b7, code lost:
    
        r8 = r15;
        r7 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00c5, code lost:
    
        throw new java.lang.RuntimeException("Unexpected end of document");
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00c6, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x003b, code lost:
    
        r8 = null;
        r6 = false;
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0040, code lost:
    
        if (r6 != false) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0042, code lost:
    
        if (r15 == 1) goto L61;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void c(XmlPullParser xmlPullParser, AttributeSet attributeSet, Menu menu) {
        b bVar = new b(menu);
        int eventType = xmlPullParser.getEventType();
        while (true) {
            if (eventType == 2) {
                String name = xmlPullParser.getName();
                if (name.equals("menu")) {
                    eventType = xmlPullParser.next();
                } else {
                    throw new RuntimeException("Expecting menu, got " + name);
                }
            } else {
                eventType = xmlPullParser.next();
                if (eventType == 1) {
                    break;
                }
            }
        }
    }

    Object b() {
        if (this.f583d == null) {
            this.f583d = a(this.f582c);
        }
        return this.f583d;
    }

    @Override // android.view.MenuInflater
    public void inflate(int i10, Menu menu) {
        if (!(menu instanceof SupportMenu)) {
            super.inflate(i10, menu);
            return;
        }
        XmlResourceParser xmlResourceParser = null;
        try {
            try {
                try {
                    xmlResourceParser = this.f582c.getResources().getLayout(i10);
                    c(xmlResourceParser, Xml.asAttributeSet(xmlResourceParser), menu);
                } catch (IOException e10) {
                    throw new InflateException("Error inflating menu XML", e10);
                }
            } catch (XmlPullParserException e11) {
                throw new InflateException("Error inflating menu XML", e11);
            }
        } finally {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
        }
    }
}
