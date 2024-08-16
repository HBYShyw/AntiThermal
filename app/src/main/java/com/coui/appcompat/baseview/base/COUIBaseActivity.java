package com.coui.appcompat.baseview.base;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import kotlin.Metadata;
import q1.a;
import za.k;

/* compiled from: COUIBaseActivity.kt */
@Metadata(bv = {}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\b&\u0018\u00002\u00020\u00012\u00020\u0002B\u0007¢\u0006\u0004\b'\u0010(J\u0012\u0010\u0006\u001a\u00020\u00052\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u0014J\b\u0010\u0007\u001a\u00020\u0005H\u0014J\u0010\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\bH\u0016J/\u0010\u0013\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\f2\u000e\u0010\u0010\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u000f0\u000e2\u0006\u0010\u0012\u001a\u00020\u0011H\u0016¢\u0006\u0004\b\u0013\u0010\u0014J\u0010\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\fH&J\u0010\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\fH\u0016J \u0010\u001b\u001a\u00020\u00052\u0016\u0010\u001a\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u0018j\b\u0012\u0004\u0012\u00020\u000f`\u0019H\u0016J \u0010\u001d\u001a\u00020\u00052\u0016\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u0018j\b\u0012\u0004\u0012\u00020\u000f`\u0019H\u0016J \u0010\u001f\u001a\u00020\u00052\u0016\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u0018j\b\u0012\u0004\u0012\u00020\u000f`\u0019H\u0016R\u0014\u0010 \u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b \u0010!R\u0014\u0010$\u001a\u00020\f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\"\u0010#R\u0014\u0010&\u001a\u00020\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b%\u0010!¨\u0006)"}, d2 = {"Lcom/coui/appcompat/baseview/base/COUIBaseActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "", "Landroid/os/Bundle;", "savedInstanceState", "Lma/f0;", "onCreate", "onDestroy", "Landroid/view/MenuItem;", "item", "", "onOptionsItemSelected", "", "requestCode", "", "", "permissions", "", "grantResults", "onRequestPermissionsResult", "(I[Ljava/lang/String;[I)V", "foldStatus", "g", "h", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "permissionGrantedList", "i", "permissionNotGrantedList", "j", "permissionRationaleList", "k", "isHomeAsUpEnabled", "()Z", "getStatusType", "()I", "statusType", "f", "needFoldObserver", "<init>", "()V", "coui-support-component_release"}, k = 1, mv = {1, 5, 1})
/* loaded from: classes.dex */
public abstract class COUIBaseActivity extends AppCompatActivity {

    /* renamed from: e, reason: collision with root package name */
    private a f5416e;

    public boolean f() {
        return true;
    }

    public abstract int g(int foldStatus);

    public int getStatusType() {
        return 0;
    }

    public void h(int i10) {
    }

    public void i(ArrayList<String> arrayList) {
        k.e(arrayList, "permissionGrantedList");
    }

    public boolean isHomeAsUpEnabled() {
        return true;
    }

    public void j(ArrayList<String> arrayList) {
        k.e(arrayList, "permissionNotGrantedList");
    }

    public void k(ArrayList<String> arrayList) {
        k.e(arrayList, "permissionRationaleList");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        a aVar = new a(this);
        this.f5416e = aVar;
        aVar.a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        a aVar = this.f5416e;
        if (aVar != null) {
            aVar.b();
        } else {
            k.s("activityDelegate");
            throw null;
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        k.e(item, "item");
        a aVar = this.f5416e;
        if (aVar != null) {
            aVar.c(item);
            return super.onOptionsItemSelected(item);
        }
        k.s("activityDelegate");
        throw null;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.view.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        k.e(permissions, "permissions");
        k.e(grantResults, "grantResults");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        a aVar = this.f5416e;
        if (aVar != null) {
            aVar.d(requestCode, permissions, grantResults);
        } else {
            k.s("activityDelegate");
            throw null;
        }
    }
}
