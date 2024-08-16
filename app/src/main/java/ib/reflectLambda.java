package ib;

import cd.MemberDeserializer;
import gb.KDeclarationContainer;
import gb.KFunction;
import jb.EmptyContainerForLocal;
import jb.KFunctionImpl;
import jb.o0;
import jc.i;
import jc.t;
import kotlin.Metadata;
import lc.TypeTable;
import ma.o;
import nc.JvmMetadataVersion;
import nc.JvmProtoBufUtil;
import nc.f;
import pb.SimpleFunctionDescriptor;
import ya.p;
import za.FunctionReference;
import za.Reflection;
import za.k;

/* compiled from: reflectLambda.kt */
/* renamed from: ib.d, reason: use source file name */
/* loaded from: classes2.dex */
public final class reflectLambda {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: reflectLambda.kt */
    /* renamed from: ib.d$a */
    /* loaded from: classes2.dex */
    public /* synthetic */ class a extends FunctionReference implements p<MemberDeserializer, i, SimpleFunctionDescriptor> {

        /* renamed from: n, reason: collision with root package name */
        public static final a f12704n = new a();

        a() {
            super(2);
        }

        @Override // za.CallableReference
        public final KDeclarationContainer C() {
            return Reflection.b(MemberDeserializer.class);
        }

        @Override // za.CallableReference
        public final String E() {
            return "loadFunction(Lorg/jetbrains/kotlin/metadata/ProtoBuf$Function;)Lorg/jetbrains/kotlin/descriptors/SimpleFunctionDescriptor;";
        }

        @Override // ya.p
        /* renamed from: G, reason: merged with bridge method [inline-methods] */
        public final SimpleFunctionDescriptor invoke(MemberDeserializer memberDeserializer, i iVar) {
            k.e(memberDeserializer, "p0");
            k.e(iVar, "p1");
            return memberDeserializer.j(iVar);
        }

        @Override // za.CallableReference, gb.KCallable
        public final String getName() {
            return "loadFunction";
        }
    }

    public static final <R> KFunction<R> a(ma.c<? extends R> cVar) {
        k.e(cVar, "<this>");
        Metadata metadata = (Metadata) cVar.getClass().getAnnotation(Metadata.class);
        if (metadata == null) {
            return null;
        }
        String[] d12 = metadata.d1();
        if (d12.length == 0) {
            d12 = null;
        }
        if (d12 == null) {
            return null;
        }
        o<f, i> j10 = JvmProtoBufUtil.j(d12, metadata.d2());
        f a10 = j10.a();
        i b10 = j10.b();
        JvmMetadataVersion jvmMetadataVersion = new JvmMetadataVersion(metadata.mv(), (metadata.xi() & 8) != 0);
        Class<?> cls = cVar.getClass();
        t h02 = b10.h0();
        k.d(h02, "proto.typeTable");
        return new KFunctionImpl(EmptyContainerForLocal.f13191h, (SimpleFunctionDescriptor) o0.h(cls, b10, a10, new TypeTable(h02), jvmMetadataVersion, a.f12704n));
    }
}
