package com.rincliu.library.common.persistence.db;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class RLDBHelper extends SQLiteOpenHelper {
    private HashMap<String, HashSet<WeakReference<ContentObserver>>> observers = new HashMap<String, HashSet<WeakReference<ContentObserver>>>();

    public RLDBHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
    }

    protected void registerContentObserver(String table, ContentObserver observer) {
        try {
            HashSet<WeakReference<ContentObserver>> set = observers.get(table);
            if (set == null) {
                set = new HashSet<WeakReference<ContentObserver>>();
                observers.put(table, set);
            }
            set.add(new WeakReference<ContentObserver>(observer));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unregisterContentObserver(ContentObserver observer) {
        try {
            HashSet<WeakReference<ContentObserver>> trashSet = new HashSet<WeakReference<ContentObserver>>();
            for (HashSet<WeakReference<ContentObserver>> set : observers.values()) {
                for (WeakReference<ContentObserver> reference : set) {
                    if (reference.get() != null) {
                        if (reference.get() == observer) {
                            trashSet.add(reference);
                        }
                    } else {
                        trashSet.add(reference);
                    }
                }
                for (WeakReference<ContentObserver> reference : trashSet) {
                    set.remove(reference);
                }
                trashSet.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatchChange(String table) {
        try {
            HashSet<WeakReference<ContentObserver>> set = observers.get(table);
            if (set != null) {
                HashSet<WeakReference<ContentObserver>> trashSet = new HashSet<WeakReference<ContentObserver>>();
                for (WeakReference<ContentObserver> reference : set) {
                    if (reference.get() != null) {
                        reference.get().dispatchChange(true);
                    } else {
                        trashSet.add(reference);
                    }
                }
                for (WeakReference<ContentObserver> reference : trashSet) {
                    set.remove(reference);
                }
                trashSet.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean doInsert(SQLiteDatabase db, String table, ContentValues values) {
        long id = db.insert(table, null, values);
        if (id != -1) {
            dispatchChange(table);
            return true;
        }
        return false;
    }

    protected boolean doDelete(SQLiteDatabase db, String table, String whereClause, String[] whereArgs) {
        int count = db.delete(table, whereClause, whereArgs);
        if (count > 0) {
            dispatchChange(table);
            return true;
        }
        return false;
    }

    protected boolean doUpdate(SQLiteDatabase db, String table, ContentValues values, String whereClause,
            String[] whereArgs) {
        int count = db.update(table, values, whereClause, whereArgs);
        if (count > 0) {
            dispatchChange(table);
            return true;
        }
        return false;
    }

}
