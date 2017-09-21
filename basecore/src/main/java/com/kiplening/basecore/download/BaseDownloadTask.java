package com.kiplening.basecore.download;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiplening on 18/09/2017 2:59 PM.
 */

public interface BaseDownloadTask {
    BaseDownloadTask setPath(final String path);

    BaseDownloadTask setPath(final String path, final boolean pathAsDirectory);

    BaseDownloadTask addFinishListener(final FinishListener finishListener);

    ArrayList<FinishListener> getFinishListener();

    boolean removeFinishListener(final FinishListener finishListener);

    BaseDownloadTask setListener(final FileDownloadListener listener);

    FileDownloadListener getListener();

    BaseDownloadTask setWifiRequired(final boolean isWifiRequired);

    int start();

    boolean cancel();

    int getTaskId();

    long getSoFarBytes();

    long getTotalBytes();

    BaseDownloadTask setSoFarBytes(long soFarBytes);

    BaseDownloadTask setTotalBytes(long totalBytes);

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

    boolean isSupportBreakPoint();

    BaseDownloadTask setSupportBreakPoint(boolean supportBreakPoint);

    void setRunning(boolean isRunning);
    interface FinishListener{
        void over();
    }
}
