package com.kiplening.downloader;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kiplening.basecore.download.BaseDownloadTask;
import com.kiplening.basecore.download.DownloadTaskSet;
import com.kiplening.basecore.download.FileDownloadListener;
import com.kiplening.basecore.download.FileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mButton1 = (Button) findViewById(R.id.start);
        mButton1.setOnClickListener(this);
        mButton2 = (Button) findViewById(R.id.batch);
        mButton2.setOnClickListener(this);
        mButton3 = (Button) findViewById(R.id.batch_parallel);
        mButton3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                FileDownloader.getImpl().create("http://n.sinaimg.cn/sports/2_ori/upload/4f160954/20170920/ZetO-fykymwm9865263.gif")
                        .setPath(context.getExternalCacheDir().getAbsolutePath()+ File.separator)
                        .start();
                break;
            case R.id.batch:
                final DownloadTaskSet taskSet = new DownloadTaskSet(new FileDownloadListener() {
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        //Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                        Log.i("Main","done");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {

                    }
                });

                final List<BaseDownloadTask> tasks = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    tasks.add(FileDownloader.getImpl().create(Constant.URLS[i])
                            .setPath(context.getExternalCacheDir().getAbsolutePath() + File.separator));
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        taskSet.downloadSequentially(tasks);
                    }
                });
                thread.start();
                break;
            case R.id.batch_parallel:
                DownloadTaskSet taskSet2 = new DownloadTaskSet(new FileDownloadListener() {
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.i("Main","done");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {

                    }
                });

                final List<BaseDownloadTask> tasks2 = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    tasks2.add(FileDownloader.getImpl().create(Constant.URLS[i])
                            .setPath(context.getExternalCacheDir().getAbsolutePath() + File.separator)
                    );
                }
                taskSet2.downloadTogether(tasks2);
                break;
        }
    }

    private static class Constant {
        public static final String[] URLS = new String[5];
        static {
            URLS[0] = "http://n.sinaimg.cn/sports/2_ori/upload/4f160954/20170920/qpLw-fykymwm9865264.gif";
            URLS[1] = "http://n.sinaimg.cn/sports/2_ori/upload/4f160954/20170920/9w7C-fykymwm9865266.gif";
            URLS[2] = "http://n.sinaimg.cn/sports/2_ori/upload/4f160954/20170920/bzcl-fykywuc7943200.gif";
            URLS[3] = "http://n.sinaimg.cn/sports/2_ori/upload/4f160954/20170920/LKk--fykymwk3115620.gif";
            URLS[4] = "http://n.sinaimg.cn/sports/2_ori/upload/4f160954/20170920/2IKJ-fykymwk3115622.gif";
        }
    }
}
