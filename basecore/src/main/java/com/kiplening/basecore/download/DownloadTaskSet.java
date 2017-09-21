package com.kiplening.basecore.download;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiplening on 20/09/2017 11:26 AM.
 */

public class DownloadTaskSet {
    private FileDownloadListener target;
    private boolean isSerial;


    private List<BaseDownloadTask.FinishListener> taskFinishListenerList;
    private Boolean isWifiRequired;
    private Object tag;
    private String directory;

    private BaseDownloadTask[] tasks;

    /**
     */
    public DownloadTaskSet(FileDownloadListener target) {
        if (target == null) {
            throw new IllegalArgumentException("create FileDownloadQueueSet must with valid target!");
        }
        this.target = target;
    }

    /**
     */
    public DownloadTaskSet downloadTogether(BaseDownloadTask... tasks) {
        this.isSerial = false;
        this.tasks = tasks;
        start();
        return this;

    }

    /**
     */
    public DownloadTaskSet downloadTogether(List<BaseDownloadTask> tasks) {
        this.isSerial = false;
        this.tasks = new BaseDownloadTask[tasks.size()];
        tasks.toArray(this.tasks);
        start();
        return this;

    }

    /**
     */
    public DownloadTaskSet downloadSequentially(BaseDownloadTask... tasks) {
        this.isSerial = true;
        this.tasks = tasks;
        start();
        return this;
    }

    /**
     */
    public DownloadTaskSet downloadSequentially(List<BaseDownloadTask> tasks) {
        this.isSerial = true;
        this.tasks = new BaseDownloadTask[tasks.size()];
        tasks.toArray(this.tasks);
        start();
        return this;
    }

    /**
     * Start tasks in a queue.
     */
    public void start() {
        for (BaseDownloadTask task : tasks) {
            task.setListener(target);

            if (tag != null) {
                task.setTag(tag);
            }

            if (taskFinishListenerList != null) {
                for (BaseDownloadTask.FinishListener finishListener : taskFinishListenerList) {
                    task.addFinishListener(finishListener);
                }
            }

            if (this.directory != null) {
                task.setPath(this.directory, true);
            }

            if (this.isWifiRequired != null) {
                task.setWifiRequired(this.isWifiRequired);
            }

        }
        if (isSerial){
            for (BaseDownloadTask task :
                    tasks) {

                task.setSync(true);
                task.start();
            }
        }else {
            for (BaseDownloadTask task:
                    tasks){
                task.start();
            }
        }

    }

    /**
     */
    public DownloadTaskSet setDirectory(String directory) {
        this.directory = directory;
        return this;
    }



    /**
     */
    public DownloadTaskSet setTag(final Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     */
    public DownloadTaskSet addTaskFinishListener(
            final BaseDownloadTask.FinishListener finishListener) {
        if (this.taskFinishListenerList == null) {
            this.taskFinishListenerList = new ArrayList<>();
        }

        this.taskFinishListenerList.add(finishListener);
        return this;
    }

    /**
     */
    public DownloadTaskSet setWifiRequired(boolean isWifiRequired) {
        this.isWifiRequired = isWifiRequired;
        return this;
    }
}
