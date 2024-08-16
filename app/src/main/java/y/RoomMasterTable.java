package y;

/* compiled from: RoomMasterTable.java */
/* renamed from: y.c, reason: use source file name */
/* loaded from: classes.dex */
public class RoomMasterTable {
    public static String a(String str) {
        return "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '" + str + "')";
    }
}
