package com.kiplening.basecore.download;

import android.text.TextUtils;
import android.util.Log;


import com.kiplening.basecore.FileUtils;

import java.io.File;
import java.util.ArrayList;

import static com.kiplening.basecore.FileUtils.formatString;


/**
 * Created by kiplening on 18/09/2017 4:02 PM.
 */

public class DownloadTask implements BaseDownloadTask {
    private static final String DEFAULT_PATH = ".";
    private int mId;

    private ArrayList<FinishListener> mFinishListenerList;

    private final String mUrl;
    private String mPath;
    private String mFilename;
    private boolean mPathAsDirectory;


    private FileDownloadListener mListener = null;

    private Object mTag;

    private boolean mIsWifiRequired = false;

    private boolean isSync = false;
    private boolean isRunning = false;

    DownloadTask(final String url) {
        this.mUrl = url;
    }


    @Override
    public BaseDownloadTask setPath(final String path) {
        return setPath(path, false);
    }

    @Override
    public BaseDownloadTask setPath(final String path, final boolean pathAsDirectory) {
        this.mPath = path;
        this.mPathAsDirectory = pathAsDirectory;
//        if (pathAsDirectory) {
//            this.mFilename = null;
//        } else {
//            this.mFilename = new File(path).getName();
//        }

        return this;
    }

    @Override
    public BaseDownloadTask addFinishListener(final BaseDownloadTask.FinishListener finishListener) {
        if (mFinishListenerList == null) {
            mFinishListenerList = new ArrayList<>();
        }

        if (!mFinishListenerList.contains(finishListener)) {
            mFinishListenerList.add(finishListener);
        }
        return this;
    }

    @Override
    public boolean removeFinishListener(final BaseDownloadTask.FinishListener finishListener) {
        return mFinishListenerList != null && mFinishListenerList.remove(finishListener);
    }

    @Override
    public BaseDownloadTask setListener(FileDownloadListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    public FileDownloadListener getListener() {
        if (mListener == null){
            return new FileDownloadListener() {

                @Override
                protected void completed(BaseDownloadTask task) {

                }

                @Override
                protected void error(BaseDownloadTask task, Throwable e) {

                }
            };
        }
        return mListener;
    }


    @Override
    public BaseDownloadTask setWifiRequired(boolean isWifiRequired) {
        this.mIsWifiRequired = isWifiRequired;
        return this;
    }


    @Override
    public int start() {
        return startTaskUnchecked();
    }

    private int startTaskUnchecked() {
        if (isRunning()) {
            throw new IllegalStateException(
                    formatString("This task is running %d, if you" +
                            " want to start the same task, please create a new one by" +
                            " FileDownloader.create", getId()));
        }

        if (this.mPath == null) {
            mPath = DEFAULT_PATH;
            throw new IllegalStateException(
                    formatString("This task is without path, please set path by" +
                            " DownloadTask.setPath", getId()));
        }

        if (new File(mPath).isDirectory()){
            if (TextUtils.isEmpty(mFilename)){
                mFilename = getUrl().substring(getUrl().lastIndexOf("/")+1);
                if (TextUtils.isEmpty(mFilename)){
                    mFilename = System.currentTimeMillis()+"";
                }
            }
        }else {
            File file = new File(mPath);
            mFilename = file.getName();
            mPath = file.getPath();
        }

        Log.e("Main","start" + mId + ":" + getUrl());

        FileDownloader.getImpl().start(this, isSync);

        return getId();
    }

    // -------------- Another Operations ---------------------

    @Override
    public boolean cancel() {
        FileDownloader.getImpl().cancel(this);
        return true;
    }

    // ------------------- Get -----------------------

    @Override
    public int getId() {
        if (mId != 0) {
            return mId;
        }
        if (!TextUtils.isEmpty(mPath) && !TextUtils.isEmpty(mUrl)) {
            if (isPathAsDirectory()) {
                mId = FileUtils.md5(formatString("%sp%s@dir", mUrl, mPath)).hashCode();
                return mId;
            } else {
                mId = FileUtils.md5(formatString("%sp%s", mUrl, mPath)).hashCode();
                return mId;
            }
        }
        return 0;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public String getPath() {
        return mPath;
    }

    @Override
    public Object getTag() {
        return mTag;
    }

    @Override
    public BaseDownloadTask setTag(Object tag) {
        this.mTag = tag;
        return this;
    }

    @Override
    public boolean isPathAsDirectory() {
        return mPathAsDirectory;
    }

    @Override
    public String getFilename() {
        return mFilename;
    }

    @Override
    public boolean isWifiRequired() {
        return mIsWifiRequired;
    }

    @Override
    public boolean isSync() {
        return isSync;
    }

    @Override
    public void setSync(boolean sync) {
        Log.e("whattt","set sync");
        this.isSync = sync;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void setRunning(boolean isRunning){
        this.isRunning = isRunning;
    }
}
