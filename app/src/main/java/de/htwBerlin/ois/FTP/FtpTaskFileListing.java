package de.htwBerlin.ois.FTP;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.htwBerlin.ois.FileStructure.OhdmFile;


public class FtpTaskFileListing extends AsyncTask<Void, Void, String> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");

    private static final String TAG = "FtpTaskFileListing";
    private ArrayList<OhdmFile> ohdmFiles;
    private FTPClient ftpClient;
    private AsyncResponse delegate = null;

    public FtpTaskFileListing(AsyncResponse asyncResponse) {
        this.delegate = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        ftpClient = new FTPClient();
        ohdmFiles = new ArrayList<>();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            ftpClient.connect(FtpEndpointSingleton.getInstance().getServerIp(), FtpEndpointSingleton.getInstance().getServerPort());
            ftpClient.login(FtpEndpointSingleton.getInstance().getFtpUser(), FtpEndpointSingleton.getInstance().getFtpPassword());
            ftpClient.enterLocalPassiveMode();
            Log.i(TAG, "Reply Code: " + ftpClient.getReplyCode());

            boolean status = ftpClient.changeWorkingDirectory("ohdm");
            Log.i(TAG, "change working dir to ohdm: " + status);


            for (FTPFile ftpFile : ftpClient.listFiles()) {
                Date date = ftpFile.getTimestamp().getTime();
                OhdmFile ohdm = new OhdmFile(ftpFile.getName(), (ftpFile.getSize() / 1024), sdf.format(date.getTime()), Boolean.FALSE);
                ohdmFiles.add(ohdm);
                Log.i(TAG, ohdm.toString());
            }

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
    protected void onPostExecute(String result) {
        delegate.getOhdmFiles(this.ohdmFiles);
    }

    @Override
    protected void onProgressUpdate(Void... params) {

    }
}
