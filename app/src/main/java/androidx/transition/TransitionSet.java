package androidx.transition;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.Transition;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class TransitionSet extends Transition {

    /* renamed from: e, reason: collision with root package name */
    private ArrayList<Transition> f4065e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f4066f;

    /* renamed from: g, reason: collision with root package name */
    int f4067g;

    /* renamed from: h, reason: collision with root package name */
    boolean f4068h;

    /* renamed from: i, reason: collision with root package name */
    private int f4069i;

    /* loaded from: classes.dex */
    class a extends TransitionListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ Transition f4070a;

        a(Transition transition) {
            this.f4070a = transition;
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            this.f4070a.runAnimators();
            transition.removeListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class b extends TransitionListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        TransitionSet f4072a;

        b(TransitionSet transitionSet) {
            this.f4072a = transitionSet;
        }

        @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.g
        public void a(Transition transition) {
            TransitionSet transitionSet = this.f4072a;
            if (transitionSet.f4068h) {
                return;
            }
            transitionSet.start();
            this.f4072a.f4068h = true;
        }

        @Override // androidx.transition.Transition.g
        public void c(Transition transition) {
            TransitionSet transitionSet = this.f4072a;
            int i10 = transitionSet.f4067g - 1;
            transitionSet.f4067g = i10;
            if (i10 == 0) {
                transitionSet.f4068h = false;
                transitionSet.end();
            }
            transition.removeListener(this);
        }
    }

    public TransitionSet() {
        this.f4065e = new ArrayList<>();
        this.f4066f = true;
        this.f4068h = false;
        this.f4069i = 0;
    }

    private void g(Transition transition) {
        this.f4065e.add(transition);
        transition.mParent = this;
    }

    private void u() {
        b bVar = new b(this);
        Iterator<Transition> it = this.f4065e.iterator();
        while (it.hasNext()) {
            it.next().addListener(bVar);
        }
        this.f4067g = this.f4065e.size();
    }

    @Override // androidx.transition.Transition
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public TransitionSet addListener(Transition.g gVar) {
        return (TransitionSet) super.addListener(gVar);
    }

    @Override // androidx.transition.Transition
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public TransitionSet addTarget(int i10) {
        for (int i11 = 0; i11 < this.f4065e.size(); i11++) {
            this.f4065e.get(i11).addTarget(i10);
        }
        return (TransitionSet) super.addTarget(i10);
    }

    @Override // androidx.transition.Transition
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public TransitionSet addTarget(View view) {
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            this.f4065e.get(i10).addTarget(view);
        }
        return (TransitionSet) super.addTarget(view);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.transition.Transition
    public void cancel() {
        super.cancel();
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4065e.get(i10).cancel();
        }
    }

    @Override // androidx.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.f4153b)) {
            Iterator<Transition> it = this.f4065e.iterator();
            while (it.hasNext()) {
                Transition next = it.next();
                if (next.isValidTarget(transitionValues.f4153b)) {
                    next.captureEndValues(transitionValues);
                    transitionValues.f4154c.add(next);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.transition.Transition
    public void capturePropagationValues(TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4065e.get(i10).capturePropagationValues(transitionValues);
        }
    }

    @Override // androidx.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        if (isValidTarget(transitionValues.f4153b)) {
            Iterator<Transition> it = this.f4065e.iterator();
            while (it.hasNext()) {
                Transition next = it.next();
                if (next.isValidTarget(transitionValues.f4153b)) {
                    next.captureStartValues(transitionValues);
                    transitionValues.f4154c.add(next);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.transition.Transition
    public void createAnimators(ViewGroup viewGroup, TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        long startDelay = getStartDelay();
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            Transition transition = this.f4065e.get(i10);
            if (startDelay > 0 && (this.f4066f || i10 == 0)) {
                long startDelay2 = transition.getStartDelay();
                if (startDelay2 > 0) {
                    transition.setStartDelay(startDelay2 + startDelay);
                } else {
                    transition.setStartDelay(startDelay);
                }
            }
            transition.createAnimators(viewGroup, transitionValuesMaps, transitionValuesMaps2, arrayList, arrayList2);
        }
    }

    @Override // androidx.transition.Transition
    /* renamed from: d, reason: merged with bridge method [inline-methods] */
    public TransitionSet addTarget(Class<?> cls) {
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            this.f4065e.get(i10).addTarget(cls);
        }
        return (TransitionSet) super.addTarget(cls);
    }

    @Override // androidx.transition.Transition
    /* renamed from: e, reason: merged with bridge method [inline-methods] */
    public TransitionSet addTarget(String str) {
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            this.f4065e.get(i10).addTarget(str);
        }
        return (TransitionSet) super.addTarget(str);
    }

    @Override // androidx.transition.Transition
    public Transition excludeTarget(View view, boolean z10) {
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            this.f4065e.get(i10).excludeTarget(view, z10);
        }
        return super.excludeTarget(view, z10);
    }

    public TransitionSet f(Transition transition) {
        g(transition);
        long j10 = this.mDuration;
        if (j10 >= 0) {
            transition.setDuration(j10);
        }
        if ((this.f4069i & 1) != 0) {
            transition.setInterpolator(getInterpolator());
        }
        if ((this.f4069i & 2) != 0) {
            transition.setPropagation(getPropagation());
        }
        if ((this.f4069i & 4) != 0) {
            transition.setPathMotion(getPathMotion());
        }
        if ((this.f4069i & 8) != 0) {
            transition.setEpicenterCallback(getEpicenterCallback());
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.transition.Transition
    public void forceToEnd(ViewGroup viewGroup) {
        super.forceToEnd(viewGroup);
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4065e.get(i10).forceToEnd(viewGroup);
        }
    }

    public Transition h(int i10) {
        if (i10 < 0 || i10 >= this.f4065e.size()) {
            return null;
        }
        return this.f4065e.get(i10);
    }

    public int i() {
        return this.f4065e.size();
    }

    @Override // androidx.transition.Transition
    /* renamed from: j, reason: merged with bridge method [inline-methods] */
    public TransitionSet removeListener(Transition.g gVar) {
        return (TransitionSet) super.removeListener(gVar);
    }

    @Override // androidx.transition.Transition
    /* renamed from: k, reason: merged with bridge method [inline-methods] */
    public TransitionSet removeTarget(int i10) {
        for (int i11 = 0; i11 < this.f4065e.size(); i11++) {
            this.f4065e.get(i11).removeTarget(i10);
        }
        return (TransitionSet) super.removeTarget(i10);
    }

    @Override // androidx.transition.Transition
    /* renamed from: l, reason: merged with bridge method [inline-methods] */
    public TransitionSet removeTarget(View view) {
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            this.f4065e.get(i10).removeTarget(view);
        }
        return (TransitionSet) super.removeTarget(view);
    }

    @Override // androidx.transition.Transition
    /* renamed from: n, reason: merged with bridge method [inline-methods] */
    public TransitionSet removeTarget(Class<?> cls) {
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            this.f4065e.get(i10).removeTarget(cls);
        }
        return (TransitionSet) super.removeTarget(cls);
    }

    @Override // androidx.transition.Transition
    /* renamed from: o, reason: merged with bridge method [inline-methods] */
    public TransitionSet removeTarget(String str) {
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            this.f4065e.get(i10).removeTarget(str);
        }
        return (TransitionSet) super.removeTarget(str);
    }

    @Override // androidx.transition.Transition
    /* renamed from: p, reason: merged with bridge method [inline-methods] */
    public TransitionSet setDuration(long j10) {
        ArrayList<Transition> arrayList;
        super.setDuration(j10);
        if (this.mDuration >= 0 && (arrayList = this.f4065e) != null) {
            int size = arrayList.size();
            for (int i10 = 0; i10 < size; i10++) {
                this.f4065e.get(i10).setDuration(j10);
            }
        }
        return this;
    }

    @Override // androidx.transition.Transition
    public void pause(View view) {
        super.pause(view);
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4065e.get(i10).pause(view);
        }
    }

    @Override // androidx.transition.Transition
    /* renamed from: q, reason: merged with bridge method [inline-methods] */
    public TransitionSet setInterpolator(TimeInterpolator timeInterpolator) {
        this.f4069i |= 1;
        ArrayList<Transition> arrayList = this.f4065e;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i10 = 0; i10 < size; i10++) {
                this.f4065e.get(i10).setInterpolator(timeInterpolator);
            }
        }
        return (TransitionSet) super.setInterpolator(timeInterpolator);
    }

    public TransitionSet r(int i10) {
        if (i10 == 0) {
            this.f4066f = true;
        } else if (i10 == 1) {
            this.f4066f = false;
        } else {
            throw new AndroidRuntimeException("Invalid parameter for TransitionSet ordering: " + i10);
        }
        return this;
    }

    @Override // androidx.transition.Transition
    public void resume(View view) {
        super.resume(view);
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4065e.get(i10).resume(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.transition.Transition
    public void runAnimators() {
        if (this.f4065e.isEmpty()) {
            start();
            end();
            return;
        }
        u();
        if (!this.f4066f) {
            for (int i10 = 1; i10 < this.f4065e.size(); i10++) {
                this.f4065e.get(i10 - 1).addListener(new a(this.f4065e.get(i10)));
            }
            Transition transition = this.f4065e.get(0);
            if (transition != null) {
                transition.runAnimators();
                return;
            }
            return;
        }
        Iterator<Transition> it = this.f4065e.iterator();
        while (it.hasNext()) {
            it.next().runAnimators();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.transition.Transition
    /* renamed from: s, reason: merged with bridge method [inline-methods] */
    public TransitionSet setSceneRoot(ViewGroup viewGroup) {
        super.setSceneRoot(viewGroup);
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4065e.get(i10).setSceneRoot(viewGroup);
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.transition.Transition
    public void setCanRemoveViews(boolean z10) {
        super.setCanRemoveViews(z10);
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4065e.get(i10).setCanRemoveViews(z10);
        }
    }

    @Override // androidx.transition.Transition
    public void setEpicenterCallback(Transition.f fVar) {
        super.setEpicenterCallback(fVar);
        this.f4069i |= 8;
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4065e.get(i10).setEpicenterCallback(fVar);
        }
    }

    @Override // androidx.transition.Transition
    public void setPathMotion(PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        this.f4069i |= 4;
        if (this.f4065e != null) {
            for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
                this.f4065e.get(i10).setPathMotion(pathMotion);
            }
        }
    }

    @Override // androidx.transition.Transition
    public void setPropagation(TransitionPropagation transitionPropagation) {
        super.setPropagation(transitionPropagation);
        this.f4069i |= 2;
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            this.f4065e.get(i10).setPropagation(transitionPropagation);
        }
    }

    @Override // androidx.transition.Transition
    /* renamed from: t, reason: merged with bridge method [inline-methods] */
    public TransitionSet setStartDelay(long j10) {
        return (TransitionSet) super.setStartDelay(j10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.transition.Transition
    public String toString(String str) {
        String transition = super.toString(str);
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(transition);
            sb2.append("\n");
            sb2.append(this.f4065e.get(i10).toString(str + "  "));
            transition = sb2.toString();
        }
        return transition;
    }

    @Override // androidx.transition.Transition
    /* renamed from: clone */
    public Transition mo0clone() {
        TransitionSet transitionSet = (TransitionSet) super.mo0clone();
        transitionSet.f4065e = new ArrayList<>();
        int size = this.f4065e.size();
        for (int i10 = 0; i10 < size; i10++) {
            transitionSet.g(this.f4065e.get(i10).mo0clone());
        }
        return transitionSet;
    }

    @Override // androidx.transition.Transition
    public Transition excludeTarget(String str, boolean z10) {
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            this.f4065e.get(i10).excludeTarget(str, z10);
        }
        return super.excludeTarget(str, z10);
    }

    @SuppressLint({"RestrictedApi"})
    public TransitionSet(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f4065e = new ArrayList<>();
        this.f4066f = true;
        this.f4068h = false;
        this.f4069i = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.f4136i);
        r(TypedArrayUtils.g(obtainStyledAttributes, (XmlResourceParser) attributeSet, "transitionOrdering", 0, 0));
        obtainStyledAttributes.recycle();
    }

    @Override // androidx.transition.Transition
    public Transition excludeTarget(int i10, boolean z10) {
        for (int i11 = 0; i11 < this.f4065e.size(); i11++) {
            this.f4065e.get(i11).excludeTarget(i10, z10);
        }
        return super.excludeTarget(i10, z10);
    }

    @Override // androidx.transition.Transition
    public Transition excludeTarget(Class<?> cls, boolean z10) {
        for (int i10 = 0; i10 < this.f4065e.size(); i10++) {
            this.f4065e.get(i10).excludeTarget(cls, z10);
        }
        return super.excludeTarget(cls, z10);
    }
}
