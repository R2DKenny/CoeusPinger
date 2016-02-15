package com.r2starbase.apo11o.coeuspinger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by apo11o.
 */
public class FileServerAsyncTask extends AsyncTask<Void, Void, String> {
    public static final String TAG = "FileServerAsyncTask";
    private Context ctx;

    public FileServerAsyncTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            ServerSocket ss = new ServerSocket(CoeusPinger.SERVER_PORT);
            Socket sock = ss.accept();
            final File f = new File(Environment.getExternalStorageDirectory() + "/"
                    + ctx.getPackageName() + "/wifip2pshared-" + System.currentTimeMillis()
                    + ".jpg");
            File dirs = new File(f.getParent());
            if (!dirs.exists()) {
                if (!dirs.mkdirs()) {
                    Log.d(TAG, "Directory not created");
                }
            }
            if (!f.createNewFile()) {
                Log.d(TAG, "New file creation failed");
            }
            InputStream is = sock.getInputStream();
            ContentTransferService.transferContent(is, new FileOutputStream(f));
            ss.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            Toast.makeText(this.ctx, "File stored: " + s, Toast.LENGTH_SHORT).show();
        }
    }
}