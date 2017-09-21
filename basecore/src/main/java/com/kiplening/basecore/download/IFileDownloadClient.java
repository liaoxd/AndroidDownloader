package com.kiplening.basecore.download;

/**
 * Created by kiplening on 18/09/2017 5:47 PM.
 */

public interface IFileDownloadClient {
    boolean startSync(BaseDownloadTask task);
    boolean startAsync(BaseDownloadTask task);
}
