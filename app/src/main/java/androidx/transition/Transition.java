package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import j.LongSparseArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/* loaded from: classes.dex */
public abstract class Transition implements Cloneable {
    static final boolean DBG = false;
    private static final String LOG_TAG = "Transition";
    private static final int MATCH_FIRST = 1;
    public static final int MATCH_ID = 3;
    private static final String MATCH_ID_STR = "id";
    public static final int MATCH_INSTANCE = 1;
    private static final String MATCH_INSTANCE_STR = "instance";
    public static final int MATCH_ITEM_ID = 4;
    private static final String MATCH_ITEM_ID_STR = "itemId";
    private static final int MATCH_LAST = 4;
    public static final int MATCH_NAME = 2;
    private static final String MATCH_NAME_STR = "name";
    private ArrayList<TransitionValues> mEndValuesList;
    private f mEpicenterCallback;
    private j.a<String, String> mNameOverrides;
    TransitionPropagation mPropagation;
    private ArrayList<TransitionValues> mStartValuesList;
    private static final int[] DEFAULT_MATCH_ORDER = {2, 1, 3, 4};
    private static final PathMotion STRAIGHT_PATH_MOTION = new a();
    private static ThreadLocal<j.a<Animator, d>> sRunningAnimators = new ThreadLocal<>();
    private String mName = getClass().getName();
    private long mStartDelay = -1;
    long mDuration = -1;
    private TimeInterpolator mInterpolator = null;
    ArrayList<Integer> mTargetIds = new ArrayList<>();
    ArrayList<View> mTargets = new ArrayList<>();
    private ArrayList<String> mTargetNames = null;
    private ArrayList<Class<?>> mTargetTypes = null;
    private ArrayList<Integer> mTargetIdExcludes = null;
    private ArrayList<View> mTargetExcludes = null;
    private ArrayList<Class<?>> mTargetTypeExcludes = null;
    private ArrayList<String> mTargetNameExcludes = null;
    private ArrayList<Integer> mTargetIdChildExcludes = null;
    private ArrayList<View> mTargetChildExcludes = null;
    private ArrayList<Class<?>> mTargetTypeChildExcludes = null;
    private TransitionValuesMaps mStartValues = new TransitionValuesMaps();
    private TransitionValuesMaps mEndValues = new TransitionValuesMaps();
    TransitionSet mParent = null;
    private int[] mMatchOrder = DEFAULT_MATCH_ORDER;
    private ViewGroup mSceneRoot = null;
    boolean mCanRemoveViews = false;
    ArrayList<Animator> mCurrentAnimators = new ArrayList<>();
    private int mNumInstances = 0;
    private boolean mPaused = false;
    private boolean mEnded = false;
    private ArrayList<g> mListeners = null;
    private ArrayList<Animator> mAnimators = new ArrayList<>();
    private PathMotion mPathMotion = STRAIGHT_PATH_MOTION;

    /* loaded from: classes.dex */
    static class a extends PathMotion {
        a() {
        }

        @Override // androidx.transition.PathMotion
        public Path a(float f10, float f11, float f12, float f13) {
            Path path = new Path();
            path.moveTo(f10, f11);
            path.lineTo(f12, f13);
            return path;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class b extends AnimatorListenerAdapter {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ j.a f4057a;

        b(j.a aVar) {
            this.f4057a = aVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f4057a.remove(animator);
            Transition.this.mCurrentAnimators.remove(animator);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            Transition.this.mCurrentAnimators.add(animator);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class c extends AnimatorListenerAdapter {
        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            Transition.this.end();
            animator.removeListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class d {

        /* renamed from: a, reason: collision with root package name */
        View f4060a;

        /* renamed from: b, reason: collision with root package name */
        String f4061b;

        /* renamed from: c, reason: collision with root package name */
        TransitionValues f4062c;

        /* renamed from: d, reason: collision with root package name */
        WindowIdImpl f4063d;

        /* renamed from: e, reason: collision with root package name */
        Transition f4064e;

        d(View view, String str, Transition transition, WindowIdImpl windowIdImpl, TransitionValues transitionValues) {
            this.f4060a = view;
            this.f4061b = str;
            this.f4062c = transitionValues;
            this.f4063d = windowIdImpl;
            this.f4064e = transition;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class e {
        static <T> ArrayList<T> a(ArrayList<T> arrayList, T t7) {
            if (arrayList == null) {
                arrayList = new ArrayList<>();
            }
            if (!arrayList.contains(t7)) {
                arrayList.add(t7);
            }
            return arrayList;
        }

        static <T> ArrayList<T> b(ArrayList<T> arrayList, T t7) {
            if (arrayList == null) {
                return arrayList;
            }
            arrayList.remove(t7);
            if (arrayList.isEmpty()) {
                return null;
            }
            return arrayList;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class f {
        public abstract Rect a(Transition transition);
    }

    /* loaded from: classes.dex */
    public interface g {
        void a(Transition transition);

        void b(Transition transition);

        void c(Transition transition);

        void d(Transition transition);

        void e(Transition transition);
    }

    public Transition() {
    }

    private void addUnmatched(j.a<View, TransitionValues> aVar, j.a<View, TransitionValues> aVar2) {
        for (int i10 = 0; i10 < aVar.size(); i10++) {
            TransitionValues n10 = aVar.n(i10);
            if (isValidTarget(n10.f4153b)) {
                this.mStartValuesList.add(n10);
                this.mEndValuesList.add(null);
            }
        }
        for (int i11 = 0; i11 < aVar2.size(); i11++) {
            TransitionValues n11 = aVar2.n(i11);
            if (isValidTarget(n11.f4153b)) {
                this.mEndValuesList.add(n11);
                this.mStartValuesList.add(null);
            }
        }
    }

    private static void addViewValues(TransitionValuesMaps transitionValuesMaps, View view, TransitionValues transitionValues) {
        transitionValuesMaps.f4155a.put(view, transitionValues);
        int id2 = view.getId();
        if (id2 >= 0) {
            if (transitionValuesMaps.f4156b.indexOfKey(id2) >= 0) {
                transitionValuesMaps.f4156b.put(id2, null);
            } else {
                transitionValuesMaps.f4156b.put(id2, view);
            }
        }
        String G = ViewCompat.G(view);
        if (G != null) {
            if (transitionValuesMaps.f4158d.containsKey(G)) {
                transitionValuesMaps.f4158d.put(G, null);
            } else {
                transitionValuesMaps.f4158d.put(G, view);
            }
        }
        if (view.getParent() instanceof ListView) {
            ListView listView = (ListView) view.getParent();
            if (listView.getAdapter().hasStableIds()) {
                long itemIdAtPosition = listView.getItemIdAtPosition(listView.getPositionForView(view));
                if (transitionValuesMaps.f4157c.g(itemIdAtPosition) >= 0) {
                    View e10 = transitionValuesMaps.f4157c.e(itemIdAtPosition);
                    if (e10 != null) {
                        ViewCompat.v0(e10, false);
                        transitionValuesMaps.f4157c.j(itemIdAtPosition, null);
                        return;
                    }
                    return;
                }
                ViewCompat.v0(view, true);
                transitionValuesMaps.f4157c.j(itemIdAtPosition, view);
            }
        }
    }

    private static boolean alreadyContains(int[] iArr, int i10) {
        int i11 = iArr[i10];
        for (int i12 = 0; i12 < i10; i12++) {
            if (iArr[i12] == i11) {
                return true;
            }
        }
        return false;
    }

    private void captureHierarchy(View view, boolean z10) {
        if (view == null) {
            return;
        }
        int id2 = view.getId();
        ArrayList<Integer> arrayList = this.mTargetIdExcludes;
        if (arrayList == null || !arrayList.contains(Integer.valueOf(id2))) {
            ArrayList<View> arrayList2 = this.mTargetExcludes;
            if (arrayList2 == null || !arrayList2.contains(view)) {
                ArrayList<Class<?>> arrayList3 = this.mTargetTypeExcludes;
                if (arrayList3 != null) {
                    int size = arrayList3.size();
                    for (int i10 = 0; i10 < size; i10++) {
                        if (this.mTargetTypeExcludes.get(i10).isInstance(view)) {
                            return;
                        }
                    }
                }
                if (view.getParent() instanceof ViewGroup) {
                    TransitionValues transitionValues = new TransitionValues(view);
                    if (z10) {
                        captureStartValues(transitionValues);
                    } else {
                        captureEndValues(transitionValues);
                    }
                    transitionValues.f4154c.add(this);
                    capturePropagationValues(transitionValues);
                    if (z10) {
                        addViewValues(this.mStartValues, view, transitionValues);
                    } else {
                        addViewValues(this.mEndValues, view, transitionValues);
                    }
                }
                if (view instanceof ViewGroup) {
                    ArrayList<Integer> arrayList4 = this.mTargetIdChildExcludes;
                    if (arrayList4 == null || !arrayList4.contains(Integer.valueOf(id2))) {
                        ArrayList<View> arrayList5 = this.mTargetChildExcludes;
                        if (arrayList5 == null || !arrayList5.contains(view)) {
                            ArrayList<Class<?>> arrayList6 = this.mTargetTypeChildExcludes;
                            if (arrayList6 != null) {
                                int size2 = arrayList6.size();
                                for (int i11 = 0; i11 < size2; i11++) {
                                    if (this.mTargetTypeChildExcludes.get(i11).isInstance(view)) {
                                        return;
                                    }
                                }
                            }
                            ViewGroup viewGroup = (ViewGroup) view;
                            for (int i12 = 0; i12 < viewGroup.getChildCount(); i12++) {
                                captureHierarchy(viewGroup.getChildAt(i12), z10);
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Integer> excludeId(ArrayList<Integer> arrayList, int i10, boolean z10) {
        if (i10 <= 0) {
            return arrayList;
        }
        if (z10) {
            return e.a(arrayList, Integer.valueOf(i10));
        }
        return e.b(arrayList, Integer.valueOf(i10));
    }

    private static <T> ArrayList<T> excludeObject(ArrayList<T> arrayList, T t7, boolean z10) {
        if (t7 == null) {
            return arrayList;
        }
        if (z10) {
            return e.a(arrayList, t7);
        }
        return e.b(arrayList, t7);
    }

    private ArrayList<Class<?>> excludeType(ArrayList<Class<?>> arrayList, Class<?> cls, boolean z10) {
        if (cls == null) {
            return arrayList;
        }
        if (z10) {
            return e.a(arrayList, cls);
        }
        return e.b(arrayList, cls);
    }

    private ArrayList<View> excludeView(ArrayList<View> arrayList, View view, boolean z10) {
        if (view == null) {
            return arrayList;
        }
        if (z10) {
            return e.a(arrayList, view);
        }
        return e.b(arrayList, view);
    }

    private static j.a<Animator, d> getRunningAnimators() {
        j.a<Animator, d> aVar = sRunningAnimators.get();
        if (aVar != null) {
            return aVar;
        }
        j.a<Animator, d> aVar2 = new j.a<>();
        sRunningAnimators.set(aVar2);
        return aVar2;
    }

    private static boolean isValidMatch(int i10) {
        return i10 >= 1 && i10 <= 4;
    }

    private static boolean isValueChanged(TransitionValues transitionValues, TransitionValues transitionValues2, String str) {
        Object obj = transitionValues.f4152a.get(str);
        Object obj2 = transitionValues2.f4152a.get(str);
        if (obj == null && obj2 == null) {
            return false;
        }
        if (obj == null || obj2 == null) {
            return true;
        }
        return true ^ obj.equals(obj2);
    }

    private void matchIds(j.a<View, TransitionValues> aVar, j.a<View, TransitionValues> aVar2, SparseArray<View> sparseArray, SparseArray<View> sparseArray2) {
        View view;
        int size = sparseArray.size();
        for (int i10 = 0; i10 < size; i10++) {
            View valueAt = sparseArray.valueAt(i10);
            if (valueAt != null && isValidTarget(valueAt) && (view = sparseArray2.get(sparseArray.keyAt(i10))) != null && isValidTarget(view)) {
                TransitionValues transitionValues = aVar.get(valueAt);
                TransitionValues transitionValues2 = aVar2.get(view);
                if (transitionValues != null && transitionValues2 != null) {
                    this.mStartValuesList.add(transitionValues);
                    this.mEndValuesList.add(transitionValues2);
                    aVar.remove(valueAt);
                    aVar2.remove(view);
                }
            }
        }
    }

    private void matchInstances(j.a<View, TransitionValues> aVar, j.a<View, TransitionValues> aVar2) {
        TransitionValues remove;
        for (int size = aVar.size() - 1; size >= 0; size--) {
            View j10 = aVar.j(size);
            if (j10 != null && isValidTarget(j10) && (remove = aVar2.remove(j10)) != null && isValidTarget(remove.f4153b)) {
                this.mStartValuesList.add(aVar.l(size));
                this.mEndValuesList.add(remove);
            }
        }
    }

    private void matchItemIds(j.a<View, TransitionValues> aVar, j.a<View, TransitionValues> aVar2, LongSparseArray<View> longSparseArray, LongSparseArray<View> longSparseArray2) {
        View e10;
        int n10 = longSparseArray.n();
        for (int i10 = 0; i10 < n10; i10++) {
            View o10 = longSparseArray.o(i10);
            if (o10 != null && isValidTarget(o10) && (e10 = longSparseArray2.e(longSparseArray.i(i10))) != null && isValidTarget(e10)) {
                TransitionValues transitionValues = aVar.get(o10);
                TransitionValues transitionValues2 = aVar2.get(e10);
                if (transitionValues != null && transitionValues2 != null) {
                    this.mStartValuesList.add(transitionValues);
                    this.mEndValuesList.add(transitionValues2);
                    aVar.remove(o10);
                    aVar2.remove(e10);
                }
            }
        }
    }

    private void matchNames(j.a<View, TransitionValues> aVar, j.a<View, TransitionValues> aVar2, j.a<String, View> aVar3, j.a<String, View> aVar4) {
        View view;
        int size = aVar3.size();
        for (int i10 = 0; i10 < size; i10++) {
            View n10 = aVar3.n(i10);
            if (n10 != null && isValidTarget(n10) && (view = aVar4.get(aVar3.j(i10))) != null && isValidTarget(view)) {
                TransitionValues transitionValues = aVar.get(n10);
                TransitionValues transitionValues2 = aVar2.get(view);
                if (transitionValues != null && transitionValues2 != null) {
                    this.mStartValuesList.add(transitionValues);
                    this.mEndValuesList.add(transitionValues2);
                    aVar.remove(n10);
                    aVar2.remove(view);
                }
            }
        }
    }

    private void matchStartAndEnd(TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2) {
        j.a<View, TransitionValues> aVar = new j.a<>(transitionValuesMaps.f4155a);
        j.a<View, TransitionValues> aVar2 = new j.a<>(transitionValuesMaps2.f4155a);
        int i10 = 0;
        while (true) {
            int[] iArr = this.mMatchOrder;
            if (i10 < iArr.length) {
                int i11 = iArr[i10];
                if (i11 == 1) {
                    matchInstances(aVar, aVar2);
                } else if (i11 == 2) {
                    matchNames(aVar, aVar2, transitionValuesMaps.f4158d, transitionValuesMaps2.f4158d);
                } else if (i11 == 3) {
                    matchIds(aVar, aVar2, transitionValuesMaps.f4156b, transitionValuesMaps2.f4156b);
                } else if (i11 == 4) {
                    matchItemIds(aVar, aVar2, transitionValuesMaps.f4157c, transitionValuesMaps2.f4157c);
                }
                i10++;
            } else {
                addUnmatched(aVar, aVar2);
                return;
            }
        }
    }

    private static int[] parseMatchOrder(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int[] iArr = new int[stringTokenizer.countTokens()];
        int i10 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String trim = stringTokenizer.nextToken().trim();
            if ("id".equalsIgnoreCase(trim)) {
                iArr[i10] = 3;
            } else if (MATCH_INSTANCE_STR.equalsIgnoreCase(trim)) {
                iArr[i10] = 1;
            } else if ("name".equalsIgnoreCase(trim)) {
                iArr[i10] = 2;
            } else if (MATCH_ITEM_ID_STR.equalsIgnoreCase(trim)) {
                iArr[i10] = 4;
            } else if (trim.isEmpty()) {
                int[] iArr2 = new int[iArr.length - 1];
                System.arraycopy(iArr, 0, iArr2, 0, i10);
                i10--;
                iArr = iArr2;
            } else {
                throw new InflateException("Unknown match type in matchOrder: '" + trim + "'");
            }
            i10++;
        }
        return iArr;
    }

    private void runAnimator(Animator animator, j.a<Animator, d> aVar) {
        if (animator != null) {
            animator.addListener(new b(aVar));
            animate(animator);
        }
    }

    public Transition addListener(g gVar) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        this.mListeners.add(gVar);
        return this;
    }

    public Transition addTarget(View view) {
        this.mTargets.add(view);
        return this;
    }

    protected void animate(Animator animator) {
        if (animator == null) {
            end();
            return;
        }
        if (getDuration() >= 0) {
            animator.setDuration(getDuration());
        }
        if (getStartDelay() >= 0) {
            animator.setStartDelay(getStartDelay() + animator.getStartDelay());
        }
        if (getInterpolator() != null) {
            animator.setInterpolator(getInterpolator());
        }
        animator.addListener(new c());
        animator.start();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cancel() {
        for (int size = this.mCurrentAnimators.size() - 1; size >= 0; size--) {
            this.mCurrentAnimators.get(size).cancel();
        }
        ArrayList<g> arrayList = this.mListeners;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
        int size2 = arrayList2.size();
        for (int i10 = 0; i10 < size2; i10++) {
            ((g) arrayList2.get(i10)).d(this);
        }
    }

    public abstract void captureEndValues(TransitionValues transitionValues);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void capturePropagationValues(TransitionValues transitionValues) {
        String[] b10;
        if (this.mPropagation == null || transitionValues.f4152a.isEmpty() || (b10 = this.mPropagation.b()) == null) {
            return;
        }
        boolean z10 = false;
        int i10 = 0;
        while (true) {
            if (i10 >= b10.length) {
                z10 = true;
                break;
            } else if (!transitionValues.f4152a.containsKey(b10[i10])) {
                break;
            } else {
                i10++;
            }
        }
        if (z10) {
            return;
        }
        this.mPropagation.a(transitionValues);
    }

    public abstract void captureStartValues(TransitionValues transitionValues);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void captureValues(ViewGroup viewGroup, boolean z10) {
        ArrayList<String> arrayList;
        ArrayList<Class<?>> arrayList2;
        j.a<String, String> aVar;
        clearValues(z10);
        if ((this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) || (((arrayList = this.mTargetNames) != null && !arrayList.isEmpty()) || ((arrayList2 = this.mTargetTypes) != null && !arrayList2.isEmpty()))) {
            captureHierarchy(viewGroup, z10);
        } else {
            for (int i10 = 0; i10 < this.mTargetIds.size(); i10++) {
                View findViewById = viewGroup.findViewById(this.mTargetIds.get(i10).intValue());
                if (findViewById != null) {
                    TransitionValues transitionValues = new TransitionValues(findViewById);
                    if (z10) {
                        captureStartValues(transitionValues);
                    } else {
                        captureEndValues(transitionValues);
                    }
                    transitionValues.f4154c.add(this);
                    capturePropagationValues(transitionValues);
                    if (z10) {
                        addViewValues(this.mStartValues, findViewById, transitionValues);
                    } else {
                        addViewValues(this.mEndValues, findViewById, transitionValues);
                    }
                }
            }
            for (int i11 = 0; i11 < this.mTargets.size(); i11++) {
                View view = this.mTargets.get(i11);
                TransitionValues transitionValues2 = new TransitionValues(view);
                if (z10) {
                    captureStartValues(transitionValues2);
                } else {
                    captureEndValues(transitionValues2);
                }
                transitionValues2.f4154c.add(this);
                capturePropagationValues(transitionValues2);
                if (z10) {
                    addViewValues(this.mStartValues, view, transitionValues2);
                } else {
                    addViewValues(this.mEndValues, view, transitionValues2);
                }
            }
        }
        if (z10 || (aVar = this.mNameOverrides) == null) {
            return;
        }
        int size = aVar.size();
        ArrayList arrayList3 = new ArrayList(size);
        for (int i12 = 0; i12 < size; i12++) {
            arrayList3.add(this.mStartValues.f4158d.remove(this.mNameOverrides.j(i12)));
        }
        for (int i13 = 0; i13 < size; i13++) {
            View view2 = (View) arrayList3.get(i13);
            if (view2 != null) {
                this.mStartValues.f4158d.put(this.mNameOverrides.n(i13), view2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearValues(boolean z10) {
        if (z10) {
            this.mStartValues.f4155a.clear();
            this.mStartValues.f4156b.clear();
            this.mStartValues.f4157c.a();
        } else {
            this.mEndValues.f4155a.clear();
            this.mEndValues.f4156b.clear();
            this.mEndValues.f4157c.a();
        }
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createAnimators(ViewGroup viewGroup, TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        Animator createAnimator;
        int i10;
        int i11;
        View view;
        Animator animator;
        TransitionValues transitionValues;
        Animator animator2;
        TransitionValues transitionValues2;
        j.a<Animator, d> runningAnimators = getRunningAnimators();
        SparseIntArray sparseIntArray = new SparseIntArray();
        int size = arrayList.size();
        long j10 = Long.MAX_VALUE;
        int i12 = 0;
        while (i12 < size) {
            TransitionValues transitionValues3 = arrayList.get(i12);
            TransitionValues transitionValues4 = arrayList2.get(i12);
            if (transitionValues3 != null && !transitionValues3.f4154c.contains(this)) {
                transitionValues3 = null;
            }
            if (transitionValues4 != null && !transitionValues4.f4154c.contains(this)) {
                transitionValues4 = null;
            }
            if (transitionValues3 != null || transitionValues4 != null) {
                if ((transitionValues3 == null || transitionValues4 == null || isTransitionRequired(transitionValues3, transitionValues4)) && (createAnimator = createAnimator(viewGroup, transitionValues3, transitionValues4)) != null) {
                    if (transitionValues4 != null) {
                        view = transitionValues4.f4153b;
                        String[] transitionProperties = getTransitionProperties();
                        if (transitionProperties != null && transitionProperties.length > 0) {
                            transitionValues2 = new TransitionValues(view);
                            i10 = size;
                            TransitionValues transitionValues5 = transitionValuesMaps2.f4155a.get(view);
                            if (transitionValues5 != null) {
                                int i13 = 0;
                                while (i13 < transitionProperties.length) {
                                    transitionValues2.f4152a.put(transitionProperties[i13], transitionValues5.f4152a.get(transitionProperties[i13]));
                                    i13++;
                                    i12 = i12;
                                    transitionValues5 = transitionValues5;
                                }
                            }
                            i11 = i12;
                            int size2 = runningAnimators.size();
                            int i14 = 0;
                            while (true) {
                                if (i14 >= size2) {
                                    animator2 = createAnimator;
                                    break;
                                }
                                d dVar = runningAnimators.get(runningAnimators.j(i14));
                                if (dVar.f4062c != null && dVar.f4060a == view && dVar.f4061b.equals(getName()) && dVar.f4062c.equals(transitionValues2)) {
                                    animator2 = null;
                                    break;
                                }
                                i14++;
                            }
                        } else {
                            i10 = size;
                            i11 = i12;
                            animator2 = createAnimator;
                            transitionValues2 = null;
                        }
                        animator = animator2;
                        transitionValues = transitionValues2;
                    } else {
                        i10 = size;
                        i11 = i12;
                        view = transitionValues3.f4153b;
                        animator = createAnimator;
                        transitionValues = null;
                    }
                    if (animator != null) {
                        TransitionPropagation transitionPropagation = this.mPropagation;
                        if (transitionPropagation != null) {
                            long c10 = transitionPropagation.c(viewGroup, this, transitionValues3, transitionValues4);
                            sparseIntArray.put(this.mAnimators.size(), (int) c10);
                            j10 = Math.min(c10, j10);
                        }
                        runningAnimators.put(animator, new d(view, getName(), this, d0.d(viewGroup), transitionValues));
                        this.mAnimators.add(animator);
                        j10 = j10;
                    }
                    i12 = i11 + 1;
                    size = i10;
                }
            }
            i10 = size;
            i11 = i12;
            i12 = i11 + 1;
            size = i10;
        }
        if (sparseIntArray.size() != 0) {
            for (int i15 = 0; i15 < sparseIntArray.size(); i15++) {
                Animator animator3 = this.mAnimators.get(sparseIntArray.keyAt(i15));
                animator3.setStartDelay((sparseIntArray.valueAt(i15) - j10) + animator3.getStartDelay());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void end() {
        int i10 = this.mNumInstances - 1;
        this.mNumInstances = i10;
        if (i10 == 0) {
            ArrayList<g> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size = arrayList2.size();
                for (int i11 = 0; i11 < size; i11++) {
                    ((g) arrayList2.get(i11)).c(this);
                }
            }
            for (int i12 = 0; i12 < this.mStartValues.f4157c.n(); i12++) {
                View o10 = this.mStartValues.f4157c.o(i12);
                if (o10 != null) {
                    ViewCompat.v0(o10, false);
                }
            }
            for (int i13 = 0; i13 < this.mEndValues.f4157c.n(); i13++) {
                View o11 = this.mEndValues.f4157c.o(i13);
                if (o11 != null) {
                    ViewCompat.v0(o11, false);
                }
            }
            this.mEnded = true;
        }
    }

    public Transition excludeChildren(View view, boolean z10) {
        this.mTargetChildExcludes = excludeView(this.mTargetChildExcludes, view, z10);
        return this;
    }

    public Transition excludeTarget(View view, boolean z10) {
        this.mTargetExcludes = excludeView(this.mTargetExcludes, view, z10);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public void forceToEnd(ViewGroup viewGroup) {
        j.a<Animator, d> runningAnimators = getRunningAnimators();
        int size = runningAnimators.size();
        if (viewGroup == null || size == 0) {
            return;
        }
        WindowIdImpl d10 = d0.d(viewGroup);
        j.a aVar = new j.a(runningAnimators);
        runningAnimators.clear();
        for (int i10 = size - 1; i10 >= 0; i10--) {
            d dVar = (d) aVar.n(i10);
            if (dVar.f4060a != null && d10 != null && d10.equals(dVar.f4063d)) {
                ((Animator) aVar.j(i10)).end();
            }
        }
    }

    public long getDuration() {
        return this.mDuration;
    }

    public Rect getEpicenter() {
        f fVar = this.mEpicenterCallback;
        if (fVar == null) {
            return null;
        }
        return fVar.a(this);
    }

    public f getEpicenterCallback() {
        return this.mEpicenterCallback;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TransitionValues getMatchedTransitionValues(View view, boolean z10) {
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getMatchedTransitionValues(view, z10);
        }
        ArrayList<TransitionValues> arrayList = z10 ? this.mStartValuesList : this.mEndValuesList;
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        int i10 = -1;
        int i11 = 0;
        while (true) {
            if (i11 >= size) {
                break;
            }
            TransitionValues transitionValues = arrayList.get(i11);
            if (transitionValues == null) {
                return null;
            }
            if (transitionValues.f4153b == view) {
                i10 = i11;
                break;
            }
            i11++;
        }
        if (i10 >= 0) {
            return (z10 ? this.mEndValuesList : this.mStartValuesList).get(i10);
        }
        return null;
    }

    public String getName() {
        return this.mName;
    }

    public PathMotion getPathMotion() {
        return this.mPathMotion;
    }

    public TransitionPropagation getPropagation() {
        return this.mPropagation;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public List<Integer> getTargetIds() {
        return this.mTargetIds;
    }

    public List<String> getTargetNames() {
        return this.mTargetNames;
    }

    public List<Class<?>> getTargetTypes() {
        return this.mTargetTypes;
    }

    public List<View> getTargets() {
        return this.mTargets;
    }

    public String[] getTransitionProperties() {
        return null;
    }

    public TransitionValues getTransitionValues(View view, boolean z10) {
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getTransitionValues(view, z10);
        }
        return (z10 ? this.mStartValues : this.mEndValues).f4155a.get(view);
    }

    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null || transitionValues2 == null) {
            return false;
        }
        String[] transitionProperties = getTransitionProperties();
        if (transitionProperties != null) {
            for (String str : transitionProperties) {
                if (!isValueChanged(transitionValues, transitionValues2, str)) {
                }
            }
            return false;
        }
        Iterator<String> it = transitionValues.f4152a.keySet().iterator();
        while (it.hasNext()) {
            if (isValueChanged(transitionValues, transitionValues2, it.next())) {
            }
        }
        return false;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidTarget(View view) {
        ArrayList<Class<?>> arrayList;
        ArrayList<String> arrayList2;
        int id2 = view.getId();
        ArrayList<Integer> arrayList3 = this.mTargetIdExcludes;
        if (arrayList3 != null && arrayList3.contains(Integer.valueOf(id2))) {
            return false;
        }
        ArrayList<View> arrayList4 = this.mTargetExcludes;
        if (arrayList4 != null && arrayList4.contains(view)) {
            return false;
        }
        ArrayList<Class<?>> arrayList5 = this.mTargetTypeExcludes;
        if (arrayList5 != null) {
            int size = arrayList5.size();
            for (int i10 = 0; i10 < size; i10++) {
                if (this.mTargetTypeExcludes.get(i10).isInstance(view)) {
                    return false;
                }
            }
        }
        if (this.mTargetNameExcludes != null && ViewCompat.G(view) != null && this.mTargetNameExcludes.contains(ViewCompat.G(view))) {
            return false;
        }
        if ((this.mTargetIds.size() == 0 && this.mTargets.size() == 0 && (((arrayList = this.mTargetTypes) == null || arrayList.isEmpty()) && ((arrayList2 = this.mTargetNames) == null || arrayList2.isEmpty()))) || this.mTargetIds.contains(Integer.valueOf(id2)) || this.mTargets.contains(view)) {
            return true;
        }
        ArrayList<String> arrayList6 = this.mTargetNames;
        if (arrayList6 != null && arrayList6.contains(ViewCompat.G(view))) {
            return true;
        }
        if (this.mTargetTypes != null) {
            for (int i11 = 0; i11 < this.mTargetTypes.size(); i11++) {
                if (this.mTargetTypes.get(i11).isInstance(view)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void pause(View view) {
        if (this.mEnded) {
            return;
        }
        j.a<Animator, d> runningAnimators = getRunningAnimators();
        int size = runningAnimators.size();
        WindowIdImpl d10 = d0.d(view);
        for (int i10 = size - 1; i10 >= 0; i10--) {
            d n10 = runningAnimators.n(i10);
            if (n10.f4060a != null && d10.equals(n10.f4063d)) {
                AnimatorUtils.b(runningAnimators.j(i10));
            }
        }
        ArrayList<g> arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
            int size2 = arrayList2.size();
            for (int i11 = 0; i11 < size2; i11++) {
                ((g) arrayList2.get(i11)).b(this);
            }
        }
        this.mPaused = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void playTransition(ViewGroup viewGroup) {
        d dVar;
        this.mStartValuesList = new ArrayList<>();
        this.mEndValuesList = new ArrayList<>();
        matchStartAndEnd(this.mStartValues, this.mEndValues);
        j.a<Animator, d> runningAnimators = getRunningAnimators();
        int size = runningAnimators.size();
        WindowIdImpl d10 = d0.d(viewGroup);
        for (int i10 = size - 1; i10 >= 0; i10--) {
            Animator j10 = runningAnimators.j(i10);
            if (j10 != null && (dVar = runningAnimators.get(j10)) != null && dVar.f4060a != null && d10.equals(dVar.f4063d)) {
                TransitionValues transitionValues = dVar.f4062c;
                View view = dVar.f4060a;
                TransitionValues transitionValues2 = getTransitionValues(view, true);
                TransitionValues matchedTransitionValues = getMatchedTransitionValues(view, true);
                if (transitionValues2 == null && matchedTransitionValues == null) {
                    matchedTransitionValues = this.mEndValues.f4155a.get(view);
                }
                if (!(transitionValues2 == null && matchedTransitionValues == null) && dVar.f4064e.isTransitionRequired(transitionValues, matchedTransitionValues)) {
                    if (!j10.isRunning() && !j10.isStarted()) {
                        runningAnimators.remove(j10);
                    } else {
                        j10.cancel();
                    }
                }
            }
        }
        createAnimators(viewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
        runAnimators();
    }

    public Transition removeListener(g gVar) {
        ArrayList<g> arrayList = this.mListeners;
        if (arrayList == null) {
            return this;
        }
        arrayList.remove(gVar);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
        }
        return this;
    }

    public Transition removeTarget(View view) {
        this.mTargets.remove(view);
        return this;
    }

    public void resume(View view) {
        if (this.mPaused) {
            if (!this.mEnded) {
                j.a<Animator, d> runningAnimators = getRunningAnimators();
                int size = runningAnimators.size();
                WindowIdImpl d10 = d0.d(view);
                for (int i10 = size - 1; i10 >= 0; i10--) {
                    d n10 = runningAnimators.n(i10);
                    if (n10.f4060a != null && d10.equals(n10.f4063d)) {
                        AnimatorUtils.c(runningAnimators.j(i10));
                    }
                }
                ArrayList<g> arrayList = this.mListeners;
                if (arrayList != null && arrayList.size() > 0) {
                    ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                    int size2 = arrayList2.size();
                    for (int i11 = 0; i11 < size2; i11++) {
                        ((g) arrayList2.get(i11)).e(this);
                    }
                }
            }
            this.mPaused = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runAnimators() {
        start();
        j.a<Animator, d> runningAnimators = getRunningAnimators();
        Iterator<Animator> it = this.mAnimators.iterator();
        while (it.hasNext()) {
            Animator next = it.next();
            if (runningAnimators.containsKey(next)) {
                start();
                runAnimator(next, runningAnimators);
            }
        }
        this.mAnimators.clear();
        end();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCanRemoveViews(boolean z10) {
        this.mCanRemoveViews = z10;
    }

    public Transition setDuration(long j10) {
        this.mDuration = j10;
        return this;
    }

    public void setEpicenterCallback(f fVar) {
        this.mEpicenterCallback = fVar;
    }

    public Transition setInterpolator(TimeInterpolator timeInterpolator) {
        this.mInterpolator = timeInterpolator;
        return this;
    }

    public void setMatchOrder(int... iArr) {
        if (iArr != null && iArr.length != 0) {
            for (int i10 = 0; i10 < iArr.length; i10++) {
                if (isValidMatch(iArr[i10])) {
                    if (alreadyContains(iArr, i10)) {
                        throw new IllegalArgumentException("matches contains a duplicate value");
                    }
                } else {
                    throw new IllegalArgumentException("matches contains invalid value");
                }
            }
            this.mMatchOrder = (int[]) iArr.clone();
            return;
        }
        this.mMatchOrder = DEFAULT_MATCH_ORDER;
    }

    public void setPathMotion(PathMotion pathMotion) {
        if (pathMotion == null) {
            this.mPathMotion = STRAIGHT_PATH_MOTION;
        } else {
            this.mPathMotion = pathMotion;
        }
    }

    public void setPropagation(TransitionPropagation transitionPropagation) {
        this.mPropagation = transitionPropagation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transition setSceneRoot(ViewGroup viewGroup) {
        this.mSceneRoot = viewGroup;
        return this;
    }

    public Transition setStartDelay(long j10) {
        this.mStartDelay = j10;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void start() {
        if (this.mNumInstances == 0) {
            ArrayList<g> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size = arrayList2.size();
                for (int i10 = 0; i10 < size; i10++) {
                    ((g) arrayList2.get(i10)).a(this);
                }
            }
            this.mEnded = false;
        }
        this.mNumInstances++;
    }

    public String toString() {
        return toString("");
    }

    public Transition addTarget(int i10) {
        if (i10 != 0) {
            this.mTargetIds.add(Integer.valueOf(i10));
        }
        return this;
    }

    @Override // 
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Transition mo0clone() {
        try {
            Transition transition = (Transition) super.clone();
            transition.mAnimators = new ArrayList<>();
            transition.mStartValues = new TransitionValuesMaps();
            transition.mEndValues = new TransitionValuesMaps();
            transition.mStartValuesList = null;
            transition.mEndValuesList = null;
            return transition;
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }

    public Transition excludeChildren(int i10, boolean z10) {
        this.mTargetIdChildExcludes = excludeId(this.mTargetIdChildExcludes, i10, z10);
        return this;
    }

    public Transition excludeTarget(int i10, boolean z10) {
        this.mTargetIdExcludes = excludeId(this.mTargetIdExcludes, i10, z10);
        return this;
    }

    public Transition removeTarget(int i10) {
        if (i10 != 0) {
            this.mTargetIds.remove(Integer.valueOf(i10));
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String toString(String str) {
        String str2 = str + getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + ": ";
        if (this.mDuration != -1) {
            str2 = str2 + "dur(" + this.mDuration + ") ";
        }
        if (this.mStartDelay != -1) {
            str2 = str2 + "dly(" + this.mStartDelay + ") ";
        }
        if (this.mInterpolator != null) {
            str2 = str2 + "interp(" + this.mInterpolator + ") ";
        }
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) {
            return str2;
        }
        String str3 = str2 + "tgts(";
        if (this.mTargetIds.size() > 0) {
            for (int i10 = 0; i10 < this.mTargetIds.size(); i10++) {
                if (i10 > 0) {
                    str3 = str3 + ", ";
                }
                str3 = str3 + this.mTargetIds.get(i10);
            }
        }
        if (this.mTargets.size() > 0) {
            for (int i11 = 0; i11 < this.mTargets.size(); i11++) {
                if (i11 > 0) {
                    str3 = str3 + ", ";
                }
                str3 = str3 + this.mTargets.get(i11);
            }
        }
        return str3 + ")";
    }

    public Transition addTarget(String str) {
        if (this.mTargetNames == null) {
            this.mTargetNames = new ArrayList<>();
        }
        this.mTargetNames.add(str);
        return this;
    }

    public Transition excludeChildren(Class<?> cls, boolean z10) {
        this.mTargetTypeChildExcludes = excludeType(this.mTargetTypeChildExcludes, cls, z10);
        return this;
    }

    public Transition excludeTarget(String str, boolean z10) {
        this.mTargetNameExcludes = excludeObject(this.mTargetNameExcludes, str, z10);
        return this;
    }

    public Transition removeTarget(String str) {
        ArrayList<String> arrayList = this.mTargetNames;
        if (arrayList != null) {
            arrayList.remove(str);
        }
        return this;
    }

    public Transition excludeTarget(Class<?> cls, boolean z10) {
        this.mTargetTypeExcludes = excludeType(this.mTargetTypeExcludes, cls, z10);
        return this;
    }

    public Transition removeTarget(Class<?> cls) {
        ArrayList<Class<?>> arrayList = this.mTargetTypes;
        if (arrayList != null) {
            arrayList.remove(cls);
        }
        return this;
    }

    public Transition addTarget(Class<?> cls) {
        if (this.mTargetTypes == null) {
            this.mTargetTypes = new ArrayList<>();
        }
        this.mTargetTypes.add(cls);
        return this;
    }

    @SuppressLint({"RestrictedApi"})
    public Transition(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.f4130c);
        XmlResourceParser xmlResourceParser = (XmlResourceParser) attributeSet;
        long g6 = TypedArrayUtils.g(obtainStyledAttributes, xmlResourceParser, "duration", 1, -1);
        if (g6 >= 0) {
            setDuration(g6);
        }
        long g10 = TypedArrayUtils.g(obtainStyledAttributes, xmlResourceParser, "startDelay", 2, -1);
        if (g10 > 0) {
            setStartDelay(g10);
        }
        int h10 = TypedArrayUtils.h(obtainStyledAttributes, xmlResourceParser, "interpolator", 0, 0);
        if (h10 > 0) {
            setInterpolator(AnimationUtils.loadInterpolator(context, h10));
        }
        String i10 = TypedArrayUtils.i(obtainStyledAttributes, xmlResourceParser, "matchOrder", 3);
        if (i10 != null) {
            setMatchOrder(parseMatchOrder(i10));
        }
        obtainStyledAttributes.recycle();
    }
}
