package de.htwBerlin.ois.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.htwBerlin.ois.FTP.AsyncResponse;
import de.htwBerlin.ois.FTP.FtpEndpointSingleton;
import de.htwBerlin.ois.FTP.FtpTaskFileListing;
import de.htwBerlin.ois.FileStructure.OhdmFile;
import de.htwBerlin.ois.FileStructure.OhdmFileAdapter;
import de.htwBerlin.ois.R;


public class MapDowloadActivity extends AppCompatActivity {

    private static final String TAG = "MapDowloadActivity";
    private static final String FTP_SERVER_IP = "78.31.65.32";
    private static final Integer FTP_PORT = 21;
    private static final String FTP_USER = "ohdm";
    private static final String FTP_PASSWORD = "ohdmapp";

    private ArrayList<OhdmFile> ohdmFiles;
    private FtpEndpointSingleton ftpEndpointSingleton;


    private FtpTaskFileListing ftpTaskFileListing = new FtpTaskFileListing(new AsyncResponse(){

        @Override
        public void getOhdmFiles(ArrayList<OhdmFile> files) {
            ohdmFiles.addAll(files);
            Log.i(TAG, "received " + files.size() + " files.");
            OhdmFileAdapter adapter = new OhdmFileAdapter(MapDowloadActivity.this, R.layout.adapter_view_layout, ohdmFiles);
            listView.setAdapter(adapter);
        }
    });

    @BindView(R.id.listView) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_dowload);

        ButterKnife.bind(this);
        ohdmFiles = new ArrayList<>();

        ftpEndpointSingleton = FtpEndpointSingleton.getInstance();
        ftpEndpointSingleton.setFtpPassword(FTP_PASSWORD);
        ftpEndpointSingleton.setFtpUser(FTP_USER);
        ftpEndpointSingleton.setServerIp(FTP_SERVER_IP);
        ftpEndpointSingleton.setServerPort(FTP_PORT);

        ftpTaskFileListing.execute();
    }

}
