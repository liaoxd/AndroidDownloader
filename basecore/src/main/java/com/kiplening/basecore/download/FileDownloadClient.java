package com.kiplening.basecore.download;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kiplening on 18/09/2017 5:29 PM.
 */

public class FileDownloadClient implements IFileDownloadClient{
    private OkHttpClient mHttpClient = null;
    private Map<BaseDownloadTask, Call> requests = new HashMap<>();
    private static final String TEMP = ".temp";

    private static class SingletonHolder {
        private static final FileDownloadClient INSTANCE = new FileDownloadClient();
    }
    private FileDownloadClient (){
        mHttpClient = new OkHttpClient();
    }
    public static final FileDownloadClient getInstance() {
        return SingletonHolder.INSTANCE;
    }


    @Override
    public boolean startSync(final BaseDownloadTask task) {
        Request request = new Request.Builder()
                .url(task.getUrl())
                .build();
        Response response = null;
        try {
            task.setRunning(true);
            Call call = mHttpClient.newCall(request);

            requests.put(task, call);
            response = call.execute();
            task.setTotalBytes(response.body().contentLength());
            String filePath = saveFile(response.body().byteStream(), task.getPath(), task.getFilename()+TEMP);
            if (!TextUtils.isEmpty(filePath)){
                File file = new File(filePath);
                file.renameTo(new File(file.getParent() + File.separator + task.getFilename()));
            }
            task.setRunning(false);
            if (null != task.getListener()){
                task.getListener().completed(task);
            }
            if (task.getFinishListener() != null){
                for (BaseDownloadTask.FinishListener listener:
                        task.getFinishListener()) {
                    listener.over();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null){
                response.close();
            }
            requests.remove(task);
        }
        return false;
    }

    @Override
    public boolean startAsync(final BaseDownloadTask task) {
        Request request = new Request.Builder()
                .url(task.getUrl())
                .build();
        task.setRunning(true);
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                task.getListener().error(task, e);
                task.setRunning(false);
                requests.remove(task);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                task.setTotalBytes(response.body().contentLength());
                String filePath = saveFile(response.body().byteStream(), task.getPath(), task.getFilename()+TEMP);
                response.close();
                if (!TextUtils.isEmpty(filePath)){
                    File file = new File(filePath);
                    file.renameTo(new File(file.getParent() + File.separator + task.getFilename()));
                }
                task.setRunning(false);
                task.getListener().completed(task);
                if (task.getFinishListener() != null){
                    for (BaseDownloadTask.FinishListener listener:
                         task.getFinishListener()) {
                        listener.over();
                    }
                }
                requests.remove(task);
            }
        });
        return true;
    }

    /**
     * @param inputStream
     * @param savePath
     * @param fileName
     * @return 保存后的绝对路径
     * @throws IOException
     */
    private String saveFile(InputStream inputStream, String savePath, String fileName) throws IOException {
        File file = null;
        file = new File(savePath, fileName);
        if (file.exists()) {
            //旧模板直接删除
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream outputStream = new FileOutputStream(file);
        byte buffer[] = new byte[4096];
        int bufferLength;
        while ((bufferLength = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, bufferLength);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return file.getAbsolutePath();
    }

    public boolean cancelRequest(BaseDownloadTask task){
        if (task != null && requests.get(task) != null){
            requests.remove(task).cancel();
            return true;
        }
        return false;
    }
    public void cancelAllRequest(){
        mHttpClient.dispatcher().cancelAll();
    }
}
