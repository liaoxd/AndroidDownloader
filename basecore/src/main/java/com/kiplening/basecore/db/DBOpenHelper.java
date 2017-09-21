package com.kiplening.basecore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kiplening.basecore.download.DownloadTask;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by kiplening on 21/09/2017 4:40 PM.
 */

public class DBOpenHelper extends OrmLiteSqliteOpenHelper {
    private static final String TASK_TABLE = "taskTable";
    private static DBOpenHelper instance;
    private Map<String, Dao> daos;

    public static synchronized DBOpenHelper getInstance(Context context){
        if (instance == null){
            instance = new DBOpenHelper(context, TASK_TABLE, null, 1);
        }
        return instance;
    }
    private DBOpenHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        instance = this;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DownloadTask.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        // TODO: 21/09/2017 drop table
        try {
            TableUtils.dropTable(connectionSource,DownloadTask.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className))
        {
            dao = daos.get(className);
        }
        if (dao == null)
        {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close()
    {
        super.close();

        for (String key : daos.keySet())
        {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
