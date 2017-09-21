package com.kiplening.basecore.download;

/**
 * Created by kiplening on 18/09/2017 2:59 PM.
 */

public interface BaseDownloadTask {
    BaseDownloadTask setPath(final String path);

    BaseDownloadTask setPath(final String path, final boolean pathAsDirectory);

    BaseDownloadTask addFinishListener(final FinishListener finishListener);

    boolean removeFinishListener(final FinishListener finishListener);

    BaseDownloadTask setListener(final FileDownloadListener listener);

    FileDownloadListener getListener();

    BaseDownloadTask setWifiRequired(final boolean isWifiRequired);

    int start();

    boolean cancel();

    int getId();

    String getUrl();

    String getPath();

    Object getTag();

    BaseDownloadTask setTag(Object tag);

    boolean isPathAsDirectory();

    String getFilename();

    boolean isWifiRequired();

    boolean isSync();

    void setSync(boolean isSync);

    boolean isRunning();

    void setRunning(boolean isRunning);
    interface FinishListener{
        void over();
    }
}
