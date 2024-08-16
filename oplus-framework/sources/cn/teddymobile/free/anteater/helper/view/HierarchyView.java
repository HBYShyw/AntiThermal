package cn.teddymobile.free.anteater.helper.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.teddymobile.free.anteater.helper.logger.Logger;
import cn.teddymobile.free.anteater.helper.utils.ViewHierarchyUtils;
import com.oplus.bluetooth.OplusBluetoothClass;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class HierarchyView extends ScrollView {
    private static final int PADDING = 20;
    private static final String TAG = HierarchyView.class.getSimpleName();
    private static final int TEXT_SIZE = 13;
    private int mChildIndex;
    private List<HierarchyView> mChildViewList;
    private LinearLayout mContainer;
    private boolean mExpanded;
    private View mRootView;

    public HierarchyView(Context context) {
        super(context);
        this.mContainer = null;
        this.mRootView = null;
        this.mChildIndex = 0;
        this.mChildViewList = null;
        this.mExpanded = false;
        initView();
    }

    public static TextView createTextView(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(13.0f);
        textView.setBackgroundColor(-1);
        textView.setTextColor(OplusBluetoothClass.Device.UNKNOWN);
        textView.setPadding(20, 20, 20, 20);
        textView.setBreakStrategy(0);
        return textView;
    }

    public void updateModel(View rootView, int childIndex) {
        if (rootView != null) {
            this.mRootView = rootView;
            this.mChildIndex = childIndex;
            addRootTextView();
            addChildrenTextView();
        }
    }

    private void initView() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.mContainer = linearLayout;
        linearLayout.setOrientation(1);
        addView(this.mContainer);
    }

    private void addRootTextView() {
        String text = "(" + this.mChildIndex + ")\n" + this.mRootView.getClass().getName();
        TextView textview = createTextView(getContext(), text);
        textview.setTextColor(this.mExpanded ? -65536 : OplusBluetoothClass.Device.UNKNOWN);
        textview.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.HierarchyView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                HierarchyView.this.mExpanded = !r0.mExpanded;
                for (ViewGroup childView : HierarchyView.this.mChildViewList) {
                    childView.setVisibility(HierarchyView.this.mExpanded ? 0 : 8);
                }
                ((TextView) v).setTextColor(HierarchyView.this.mExpanded ? -65536 : OplusBluetoothClass.Device.UNKNOWN);
            }
        });
        textview.setOnLongClickListener(new View.OnLongClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.HierarchyView.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View v) {
                AttributeView attributeView = new AttributeView(HierarchyView.this.getContext());
                attributeView.updateModel(HierarchyView.this.mRootView, HierarchyView.this.mRootView.getClass());
                AlertDialog alertDialog = new AlertDialog.Builder(HierarchyView.this.getContext()).setView(attributeView).create();
                alertDialog.show();
                HierarchyView hierarchyView = HierarchyView.this;
                hierarchyView.logIdAndResName(hierarchyView.mRootView);
                HierarchyView hierarchyView2 = HierarchyView.this;
                hierarchyView2.logViewHierarchy(hierarchyView2.mRootView);
                HierarchyView hierarchyView3 = HierarchyView.this;
                hierarchyView3.logWebView(hierarchyView3.mRootView);
                return true;
            }
        });
        this.mContainer.addView(textview);
    }

    private void addChildrenTextView() {
        this.mChildViewList = new ArrayList();
        View view = this.mRootView;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                HierarchyView hierarchyView = new HierarchyView(getContext());
                hierarchyView.updateModel(childView, i);
                hierarchyView.setPadding(30, 0, 0, 0);
                hierarchyView.setVisibility(8);
                this.mChildViewList.add(hierarchyView);
                this.mContainer.addView(hierarchyView);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logIdAndResName(View view) {
        int id = view.getId();
        String resName = null;
        if (id != -1) {
            try {
                resName = getResources().getResourceName(id);
            } catch (Exception e) {
            }
        }
        String str = TAG;
        Logger.i(str, "id = " + id);
        Logger.i(str, "resName = " + resName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logViewHierarchy(View view) {
        List<String> classList = new ArrayList<>();
        List<Integer> indexList = new ArrayList<>();
        View currentView = view;
        while (!currentView.getClass().getName().endsWith("DecorView")) {
            ViewGroup parent = (ViewGroup) currentView.getParent();
            classList.add(currentView.getClass().getName());
            indexList.add(Integer.valueOf(parent.indexOfChild(currentView)));
            currentView = parent;
        }
        StringBuilder info = new StringBuilder("[");
        for (int i = classList.size() - 1; i >= 0; i--) {
            String className = classList.get(i);
            int index = indexList.get(i).intValue();
            info.append(String.format(Locale.getDefault(), "{\"class_name\": \"%s\",\"child_index\": %d}", className, Integer.valueOf(index)));
            if (i != 0) {
                info.append(",");
            }
        }
        info.append("]");
        Logger.i(TAG, "ViewHierarchy = \n" + info.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logWebView(View view) {
        View view2 = ViewHierarchyUtils.retrieveWebView(view);
        if (view2 != null) {
            if (view2 instanceof WebView) {
                WebView webView = (WebView) view2;
                WebSettings webSettings = webView.getSettings();
                webSettings.setAllowFileAccess(false);
                webSettings.setAllowContentAccess(false);
                String str = TAG;
                Logger.i(str, view2.getClass().getName() + " is a native WebView");
                Logger.i(str, "Title = " + webView.getTitle());
                Logger.i(str, "WebView Hierarchy : ");
                logViewHierarchy(view2);
                return;
            }
            String str2 = TAG;
            Logger.i(str2, view2.getClass().getName() + " is a ThirdParty WebView ");
            try {
                Method method = view2.getClass().getMethod("getTitle", new Class[0]);
                method.setAccessible(true);
                Logger.i(str2, "Title = " + method.invoke(view2, new Object[0]));
                view2.getClass().getMethod("getUrl", new Class[0]).setAccessible(true);
            } catch (Exception e) {
                Logger.w(TAG, e.getMessage(), e);
            }
            Logger.i(TAG, "WebView Hierarchy :");
            logViewHierarchy(view2);
        }
    }
}
