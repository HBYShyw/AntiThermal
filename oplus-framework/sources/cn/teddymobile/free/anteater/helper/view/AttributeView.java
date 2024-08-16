package cn.teddymobile.free.anteater.helper.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.teddymobile.free.anteater.helper.logger.Logger;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

/* loaded from: classes.dex */
public class AttributeView extends ScrollView {
    private static final String TAG = AttributeView.class.getSimpleName();
    private Object mAttribute;
    private Class<?> mClass;
    private LinearLayout mContainer;

    public AttributeView(Context context) {
        super(context);
        this.mContainer = null;
        this.mAttribute = null;
        this.mClass = null;
        initView();
    }

    public void updateModel(Object attribute, Class<?> clazz) {
        if (attribute != null && clazz != null) {
            this.mAttribute = attribute;
            this.mClass = clazz;
            addSuperClassTextView();
            addMethodTextView();
            addContextTextView();
            addIntentTextView();
            addListenerInfoTextView();
            addOnClickListenerTextView();
            addArrayTextView();
            addFieldTextView();
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
            final Class<?> superClazz = this.mClass.getSuperclass();
            TextView textView = HierarchyView.createTextView(getContext(), "Super = " + superClazz.getName());
            textView.setTextColor(-65281);
            textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    AttributeView attributeView = new AttributeView(AttributeView.this.getContext());
                    attributeView.updateModel(AttributeView.this.mAttribute, superClazz);
                    new AlertDialog.Builder(AttributeView.this.getContext()).setView(attributeView).create().show();
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.2
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View v) {
                    String info = String.format("{\"field_name\": \"[super]\", \"class_name\": \"%s\", \"leaf\": []}", superClazz.getName());
                    Logger.i(AttributeView.TAG, "Attribute = \n" + info);
                    return true;
                }
            });
            this.mContainer.addView(textView);
        }
    }

    private void addMethodTextView() {
        TextView textView = HierarchyView.createTextView(getContext(), "Methods");
        textView.setTextColor(-65281);
        textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MethodView methodView = new MethodView(AttributeView.this.getContext());
                methodView.updateModel(AttributeView.this.mAttribute, AttributeView.this.mClass);
                new AlertDialog.Builder(AttributeView.this.getContext()).setView(methodView).create().show();
            }
        });
        this.mContainer.addView(textView);
    }

    private void addContextTextView() {
        Object obj = this.mAttribute;
        if (obj instanceof View) {
            View view = (View) obj;
            try {
                Method method = View.class.getDeclaredMethod("getContext", new Class[0]);
                method.setAccessible(true);
                final Object contextObject = method.invoke(view, new Object[0]);
                TextView textView = HierarchyView.createTextView(getContext(), "Context = " + contextObject.getClass().getName());
                textView.setTextColor(-65281);
                textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.4
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        AttributeView attributeView = new AttributeView(AttributeView.this.getContext());
                        Object obj2 = contextObject;
                        attributeView.updateModel(obj2, obj2.getClass());
                        new AlertDialog.Builder(AttributeView.this.getContext()).setView(attributeView).create().show();
                    }
                });
                textView.setOnLongClickListener(new View.OnLongClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.5
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View v) {
                        String info = String.format("{\"field_name\": \"[context]\", \"class_name\": \"%s\", \"leaf\": []}", contextObject.getClass().getName());
                        Logger.i(AttributeView.TAG, "Attribute = \n" + info);
                        return true;
                    }
                });
                this.mContainer.addView(textView);
            } catch (Exception e) {
                Logger.w(TAG, e.getMessage(), e);
            }
        }
    }

    private void addIntentTextView() {
        Object obj = this.mAttribute;
        if (obj instanceof View) {
            View view = (View) obj;
            try {
                Method method = View.class.getDeclaredMethod("getContext", new Class[0]);
                method.setAccessible(true);
                Object contextObject = method.invoke(view, new Object[0]);
                if (contextObject instanceof Activity) {
                    Activity activity = (Activity) contextObject;
                    final Intent intent = activity.getIntent();
                    TextView textView = HierarchyView.createTextView(getContext(), "Intent = " + intent);
                    textView.setTextColor(-65281);
                    textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.6
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            if (intent != null) {
                                AttributeView attributeView = new AttributeView(AttributeView.this.getContext());
                                Intent intent2 = intent;
                                attributeView.updateModel(intent2, intent2.getClass());
                                new AlertDialog.Builder(AttributeView.this.getContext()).setView(attributeView).create().show();
                            }
                        }
                    });
                    this.mContainer.addView(textView);
                }
            } catch (Exception e) {
                Logger.w(TAG, e.getMessage(), e);
            }
        }
    }

    private void addListenerInfoTextView() {
        Object obj = this.mAttribute;
        if (obj instanceof View) {
            View view = (View) obj;
            try {
                Field field = View.class.getDeclaredField("mListenerInfo");
                field.setAccessible(true);
                final Object listenerInfo = field.get(view);
                TextView textView = HierarchyView.createTextView(getContext(), "ListenerInfo = " + listenerInfo);
                textView.setTextColor(-65281);
                textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.7
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (listenerInfo != null) {
                            AttributeView attributeView = new AttributeView(AttributeView.this.getContext());
                            Object obj2 = listenerInfo;
                            attributeView.updateModel(obj2, obj2.getClass());
                            new AlertDialog.Builder(AttributeView.this.getContext()).setView(attributeView).create().show();
                        }
                    }
                });
                this.mContainer.addView(textView);
            } catch (Exception e) {
                Logger.w(TAG, e.getMessage(), e);
            }
        }
    }

    private void addOnClickListenerTextView() {
        final Object onClickListener;
        Object obj = this.mAttribute;
        if (obj instanceof View) {
            View view = (View) obj;
            try {
                Field field = View.class.getDeclaredField("mListenerInfo");
                field.setAccessible(true);
                Object listenerInfo = field.get(view);
                if (listenerInfo != null) {
                    Field field2 = listenerInfo.getClass().getDeclaredField("mOnClickListener");
                    field2.setAccessible(true);
                    onClickListener = field2.get(listenerInfo);
                } else {
                    onClickListener = null;
                }
                TextView textView = HierarchyView.createTextView(getContext(), "OnClickListener = " + onClickListener);
                textView.setTextColor(-65281);
                textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.8
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (onClickListener != null) {
                            AttributeView attributeView = new AttributeView(AttributeView.this.getContext());
                            Object obj2 = onClickListener;
                            attributeView.updateModel(obj2, obj2.getClass());
                            new AlertDialog.Builder(AttributeView.this.getContext()).setView(attributeView).create().show();
                        }
                    }
                });
                textView.setOnLongClickListener(new View.OnLongClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.9
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View v) {
                        String info = String.format(Locale.getDefault(), "{\"field_name\": \"[onClickListener]\", \"class_name\": \"%s\", \"leaf\": []}", View.OnClickListener.class.getName());
                        Logger.i(AttributeView.TAG, "Attribute = \n" + info);
                        return true;
                    }
                });
                this.mContainer.addView(textView);
            } catch (Exception e) {
                Logger.w(TAG, e.getMessage(), e);
            }
        }
    }

    private void addArrayTextView() {
        if (this.mClass.isArray()) {
            for (int i = 0; i < Array.getLength(this.mAttribute); i++) {
                final int arrayIndex = i;
                final Object arrayItem = Array.get(this.mAttribute, i);
                String text = "[" + i + "] = " + arrayItem + (arrayItem != null ? " " + arrayItem.getClass().getSimpleName() : "");
                TextView textView = HierarchyView.createTextView(getContext(), text);
                textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.10
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (arrayItem != null) {
                            ((TextView) v).setTextColor(-65536);
                            AttributeView attributeView = new AttributeView(AttributeView.this.getContext());
                            Object obj = arrayItem;
                            attributeView.updateModel(obj, obj.getClass());
                            new AlertDialog.Builder(AttributeView.this.getContext()).setView(attributeView).create().show();
                        }
                    }
                });
                textView.setOnLongClickListener(new View.OnLongClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.11
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View v) {
                        Locale locale = Locale.getDefault();
                        Object[] objArr = new Object[2];
                        objArr[0] = Integer.valueOf(arrayIndex);
                        Object obj = arrayItem;
                        objArr[1] = obj != null ? obj.getClass().getName() : "?";
                        String info = String.format(locale, "{\"field_name\": \"[%d]\", \"class_name\": \"%s\", \"leaf\": []}", objArr);
                        Logger.i(AttributeView.TAG, "Attribute = \n" + info);
                        return true;
                    }
                });
                this.mContainer.addView(textView);
            }
        }
    }

    private void addFieldTextView() {
        if (!this.mClass.isArray()) {
            final Field[] fields = this.mClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                final int index = i;
                final Field field = fields[i];
                try {
                    field.setAccessible(true);
                    final Object value = field.get(this.mAttribute);
                    String text = field.getName() + " = " + value + (value != null ? " " + value.getClass().getSimpleName() : "");
                    TextView textView = HierarchyView.createTextView(getContext(), text);
                    textView.setOnClickListener(new View.OnClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.12
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            if (value != null) {
                                ((TextView) v).setTextColor(-65536);
                                AttributeView attributeView = new AttributeView(AttributeView.this.getContext());
                                Object obj = value;
                                attributeView.updateModel(obj, obj.getClass());
                                new AlertDialog.Builder(AttributeView.this.getContext()).setView(attributeView).create().show();
                            }
                        }
                    });
                    textView.setOnLongClickListener(new View.OnLongClickListener() { // from class: cn.teddymobile.free.anteater.helper.view.AttributeView.13
                        @Override // android.view.View.OnLongClickListener
                        public boolean onLongClick(View v) {
                            Locale locale = Locale.getDefault();
                            Object[] objArr = new Object[4];
                            objArr[0] = field.getName();
                            Object obj = value;
                            objArr[1] = obj != null ? obj.getClass().getName() : "?";
                            objArr[2] = Integer.valueOf(index);
                            objArr[3] = Integer.valueOf(fields.length);
                            String info = String.format(locale, "{\"field_name\": \"%s\", \"class_name\": \"%s\", \"field_index\": %d, \"parent_field_count\": %d, \"leaf\": []}", objArr);
                            Logger.i(AttributeView.TAG, "Attribute = \n" + info);
                            return true;
                        }
                    });
                    this.mContainer.addView(textView);
                } catch (Exception e) {
                    Logger.w(TAG, e.getMessage(), e);
                }
            }
        }
    }
}
