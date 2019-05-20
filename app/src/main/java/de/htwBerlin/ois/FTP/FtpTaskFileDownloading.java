package de.htwBerlin.ois.FTP;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import de.htwBerlin.ois.FileStructure.OhdmFile;

public class FtpTaskFileDownloading extends AsyncTask<OhdmFile, Integer, Long> {

    private static final String TAG = "FtpTaskFileListing";
    private FTPClient ftpClient;
    private ProgressBar progressBar;

    public FtpTaskFileDownloading(ProgressBar bar) {
        this.progressBar = bar;
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        ftpClient = new FTPClient();
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(OhdmFile... ohdmFile) {

        try {
            ftpClient.connect(FtpEndpointSingleton.getInstance().getServerIp(), FtpEndpointSingleton.getInstance().getServerPort());
            ftpClient.login(FtpEndpointSingleton.getInstance().getFtpUser(), FtpEndpointSingleton.getInstance().getFtpPassword());
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            Log.i(TAG, "Reply Code: " + ftpClient.getReplyCode());

            boolean status = ftpClient.changeWorkingDirectory("ohdm");
            Log.i(TAG, "change working dir to ohdm: " + status);

            File downloadFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), ohdmFile[0].getFilename());
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
            InputStream inputStream = ftpClient.retrieveFileStream(ohdmFile[0].getFilename());
            byte[] bytesArray = new byte[4096];

            Long total = 0L;
            Long fileSize = ohdmFile[0].getFileSize();
            int bytesRead = -1;

            while (-1 != (bytesRead = inputStream.read(bytesArray))) {
                Log.i(TAG, "bytes read: " + bytesRead);
                total += bytesRead;
                outputStream.write(bytesArray, 0, bytesRead);
                publishProgress((int) (total * 100 / fileSize));
            }

            if (ftpClient.completePendingCommand()) Log.i(TAG, "File Download successfull");

            outputStream.close();
            inputStream.close();

        } catch (SocketException e) {
            Log.e(TAG, "doInBackground, SocketException; " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground, IOException; " + e.getMessage());
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error in finally " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... params) {
        this.progressBar.setProgress(params[0]);
    }

    @Override
    protected void onPostExecute(Long params) {
    }
}
