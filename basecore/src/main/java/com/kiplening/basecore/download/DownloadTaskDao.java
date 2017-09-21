package com.kiplening.basecore.download;

import android.content.Context;

import com.kiplening.basecore.db.DBOpenHelper;

import java.sql.SQLException;

/**
 * Created by kiplening on 21/09/2017 5:18 PM.
 */

public class DownloadTaskDao {
    private Context context;

    public DownloadTaskDao(Context context) {
        this.context = context;
    }

    public void add(DownloadTask task) {
        try {
            DBOpenHelper.getInstance(context).getDao(DownloadTaskDao.class).create(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(DownloadTask task){
        try {
            DBOpenHelper.getInstance(context).getDao(DownloadTaskDao.class).update(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
