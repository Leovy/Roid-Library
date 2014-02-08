/**
 * Copyright (c) 2013-2014, Rinc Liu (http://rincliu.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    private final HashMap<String, HashSet<WeakReference<ContentObserver>>> observers = new HashMap<String, HashSet<WeakReference<ContentObserver>>>();

    /**
     * [constructor]
     * 
     * @param context
     * @param dbName
     * @param dbVersion
     */
    public RLDBHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
    }

    /**
     * [description]
     * 
     * @param table
     * @param observer
     * @see []
     */
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

    /**
     * [description]
     * 
     * @param observer
     * @see []
     */
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

    /**
     * [description]
     * 
     * @param db
     * @param table
     * @param values
     * @return
     * @see []
     */
    protected boolean doInsert(SQLiteDatabase db, String table, ContentValues values) {
        long id = db.insert(table, null, values);
        if (id != -1) {
            dispatchChange(table);
            return true;
        }
        return false;
    }

    /**
     * [description]
     * 
     * @param db
     * @param table
     * @param whereClause
     * @param whereArgs
     * @return
     * @see []
     */
    protected boolean doDelete(SQLiteDatabase db, String table, String whereClause, String[] whereArgs) {
        int count = db.delete(table, whereClause, whereArgs);
        if (count > 0) {
            dispatchChange(table);
            return true;
        }
        return false;
    }

    /**
     * [description]
     * 
     * @param db
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     * @see []
     */
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
