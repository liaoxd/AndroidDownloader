package com.kiplening.basecore.download;

/**
 * Created by kiplening on 18/09/2017 2:55 PM.
 */

public abstract class FileDownloadListener {
    public FileDownloadListener() {
    }

    protected void pending(final BaseDownloadTask task, final int soFarBytes,
                                    final int totalBytes){

    }

    protected void started(final BaseDownloadTask task) {
    }


    protected void connected(final BaseDownloadTask task, final String etag,
                             final boolean isContinue, final int soFarBytes, final int totalBytes) {

    }


    protected void progress(final BaseDownloadTask task, final int soFarBytes,
                                     final int totalBytes){

    }


    protected void blockComplete(final BaseDownloadTask task) throws Throwable {
    }


    // ======================= The task is over, if execute below methods =======================


    protected abstract void completed(final BaseDownloadTask task);


    protected abstract void error(final BaseDownloadTask task, final Throwable e);

}
