package cn.teddymobile.free.anteater.helper.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.teddymobile.free.anteater.helper.logger.Logger;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class MethodView extends ScrollView {
    private static final String TAG = MethodView.class.getSimpleName();
    private Object mAttribute;
    private Class<?> mClass;
    private LinearLayout mContainer;

    public MethodView(Context context) {
        super(context);
        initView();
    }

    public void updateModel(Object attribute, Class<?> clazz) {
        if (attribute != null && clazz != null) {
            this.mAttribute = attribute;
            this.mClass = clazz;
            addSuperClassTextView();
            addMethodTextView();
        }
    }

    private void initView() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.mContainer = linearLayout;
        linearLayout.setOrientation(1);
        addView(this.mContainer);
    }

    private void addSuperClassTextView() {
        if (this.mClass.getSuperclass() != null) {
            final Class<?> superClass = this.mClass.getSuperclass();
            TextView textView = HierarchyView.createTextView(getContext(), "Super = " + superClass.getName());
            textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.MethodView.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    MethodView methodView = new MethodView(MethodView.this.getContext());
                    methodView.updateModel(MethodView.this.mAttribute, superClass);
                    new AlertDialog.Builder(MethodView.this.getContext()).setView(methodView).create().show();
                }
            });
            this.mContainer.addView(textView);
        }
    }

    private void addMethodTextView() {
        Method[] methods = this.mClass.getDeclaredMethods();
        for (final Method method : methods) {
            TextView textView = HierarchyView.createTextView(getContext(), method.getName());
            textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.MethodView.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (method.getParameterTypes().length == 0) {
                        try {
                            method.setAccessible(true);
                            String str = MethodView.TAG;
                            StringBuilder append = new StringBuilder().append("Invoke method result = ");
                            Method method2 = method;
                            Logger.i(str, append.append(method2.invoke(method2, MethodView.this.mAttribute)).toString());
                        } catch (Exception e) {
                            Logger.w(MethodView.TAG, e.getMessage(), e);
                        }
                    }
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.MethodView.3
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View v) {
                    Logger.i(MethodView.TAG, "Method params :");
                    for (Class<?> paramClass : method.getParameterTypes()) {
                        Logger.i(MethodView.TAG, paramClass.getName());
                    }
                    Logger.i(MethodView.TAG, "Method return :");
                    Logger.i(MethodView.TAG, method.getReturnType().getName());
                    return true;
                }
            });
            this.mContainer.addView(textView);
        }
    }
}
