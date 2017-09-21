package com.kiplening.basecore.download;

import android.text.TextUtils;

/**
 * Created by kiplening on 18/09/2017 3:34 PM.
 */

public class FileDownloader {
    private FileDownloadClient mClient;

    private final static class HolderClass {
        private final static FileDownloader INSTANCE = new FileDownloader();
    }
    private FileDownloader(){
        mClient =  FileDownloadClient.getInstance();
    }
    public static FileDownloader getImpl() {
        return HolderClass.INSTANCE;
    }

    public BaseDownloadTask create(final String url) {
        return new DownloadTask(url);
    }


    public boolean start(final BaseDownloadTask task, final boolean isSync){
        if (checkTask(task)){
            return isSync?
                    mClient.startSync(task):
                    mClient.startAsync(task);
        }
        return false;
    }
//    public boolean start(final DownloadTaskSet tasks, final boolean isSerial) {
//
//        if (tasks == null) {
//            return false;
//        }
//
//        return isSerial ?
//                startQueueSerial(tasks) :
//                startQueueParallel(tasks);
//    }
//

    public boolean checkTask(final BaseDownloadTask task){
        if (task == null) {
            return false;
        }
        if (TextUtils.isEmpty(task.getPath())){
            return false;
//            throw new IllegalArgumentException("Start a FileDownloadTask without set path, " +
//                    "please use task.setPath(String) set a file path.");
        }
        return true;
    }
    public void cancel(final BaseDownloadTask task){
        mClient.cancelRequest(task);
    }
}
