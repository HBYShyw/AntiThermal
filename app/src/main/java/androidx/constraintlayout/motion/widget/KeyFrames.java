package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: KeyFrames.java */
/* renamed from: androidx.constraintlayout.motion.widget.h, reason: use source file name */
/* loaded from: classes.dex */
public class KeyFrames {

    /* renamed from: b, reason: collision with root package name */
    static HashMap<String, Constructor<? extends c>> f1552b;

    /* renamed from: a, reason: collision with root package name */
    private HashMap<Integer, ArrayList<c>> f1553a = new HashMap<>();

    static {
        HashMap<String, Constructor<? extends c>> hashMap = new HashMap<>();
        f1552b = hashMap;
        try {
            hashMap.put("KeyAttribute", KeyAttributes.class.getConstructor(new Class[0]));
            f1552b.put("KeyPosition", KeyPosition.class.getConstructor(new Class[0]));
            f1552b.put("KeyCycle", KeyCycle.class.getConstructor(new Class[0]));
            f1552b.put("KeyTimeCycle", KeyTimeCycle.class.getConstructor(new Class[0]));
            f1552b.put("KeyTrigger", KeyTrigger.class.getConstructor(new Class[0]));
        } catch (NoSuchMethodException e10) {
            Log.e("KeyFrames", "unable to load", e10);
        }
    }

    public KeyFrames(Context context, XmlPullParser xmlPullParser) {
        c cVar;
        Exception e10;
        HashMap<String, ConstraintAttribute> hashMap;
        c cVar2 = null;
        try {
            int eventType = xmlPullParser.getEventType();
            while (eventType != 1) {
                if (eventType != 2) {
                    if (eventType == 3 && "KeyFrameSet".equals(xmlPullParser.getName())) {
                        return;
                    }
                } else {
                    String name = xmlPullParser.getName();
                    if (f1552b.containsKey(name)) {
                        try {
                            cVar = f1552b.get(name).newInstance(new Object[0]);
                            try {
                                cVar.c(context, Xml.asAttributeSet(xmlPullParser));
                                b(cVar);
                            } catch (Exception e11) {
                                e10 = e11;
                                Log.e("KeyFrames", "unable to create ", e10);
                                cVar2 = cVar;
                                eventType = xmlPullParser.next();
                            }
                        } catch (Exception e12) {
                            cVar = cVar2;
                            e10 = e12;
                        }
                        cVar2 = cVar;
                    } else if (name.equalsIgnoreCase("CustomAttribute") && cVar2 != null && (hashMap = cVar2.f1486e) != null) {
                        ConstraintAttribute.g(context, xmlPullParser, hashMap);
                    }
                }
                eventType = xmlPullParser.next();
            }
        } catch (IOException e13) {
            e13.printStackTrace();
        } catch (XmlPullParserException e14) {
            e14.printStackTrace();
        }
    }

    private void b(c cVar) {
        if (!this.f1553a.containsKey(Integer.valueOf(cVar.f1483b))) {
            this.f1553a.put(Integer.valueOf(cVar.f1483b), new ArrayList<>());
        }
        this.f1553a.get(Integer.valueOf(cVar.f1483b)).add(cVar);
    }

    public void a(MotionController motionController) {
        ArrayList<c> arrayList = this.f1553a.get(Integer.valueOf(motionController.f1630b));
        if (arrayList != null) {
            motionController.b(arrayList);
        }
        ArrayList<c> arrayList2 = this.f1553a.get(-1);
        if (arrayList2 != null) {
            Iterator<c> it = arrayList2.iterator();
            while (it.hasNext()) {
                c next = it.next();
                if (next.d(((ConstraintLayout.LayoutParams) motionController.f1629a.getLayoutParams()).V)) {
                    motionController.a(next);
                }
            }
        }
    }
}
