package de.htwBerlin.ois.Activities;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.osmdroid.tileprovider.util.StorageUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.htwBerlin.ois.FileStructure.MapFileSingleton;
import de.htwBerlin.ois.R;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    @BindView(R.id.buttonAboutOHDM) Button buttonAboutOHDM;
    @BindView(R.id.buttonStartNavigation) Button buttonStartNavigation;
    @BindView(R.id.buttonDownloadActivity) Button buttonDownloadActivity;
    @BindView(R.id.mapFileDropDown) Spinner spinnerMapFile;

    private Set<File> mapFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e){}

        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        fillDropDownFiles();
    }

    @Override
    protected void onStart(){
        fillDropDownFiles();
        super.onStart();
    }

    @Override
    protected void onResume(){
        fillDropDownFiles();
        super.onResume();
    }


    @OnClick(R.id.buttonAboutOHDM)
    public void buttonAboutOHDMClicked(View view){
        Log.i(TAG, "Button \"about\" pressed.");
        openAboutActivity();
    }

    @OnClick(R.id.buttonStartNavigation)
    public void buttonStartNavigationClicked(View view){
        Log.i(TAG, "Button \"startNavigation\" pressed");
        Log.i(TAG, "User has choosen " + spinnerMapFile.getSelectedItem().toString());

        for (File file : mapFiles){
            if (file.getName().equals(spinnerMapFile.getSelectedItem().toString())){
                Log.i(TAG, "using " + file.getName() + " as mapfile");
                MapFileSingleton mapFile = MapFileSingleton.getInstance();
                mapFile.setFile(file);
            }
        }
        openMainActivity();
    }

    @OnClick(R.id.buttonDownloadActivity)
    public void buttonMapDownloadActivityClicked(){
        Log.i(TAG, "Button \"MapDownloadActivity pressed");
        openMapDownloadActivity();
    }

    private void openMainActivity(){
        Log.i(TAG, "Directing to MainActivity.class");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openAboutActivity(){
        Log.i(TAG, "Directing to AboutActivity.class");
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void openMapDownloadActivity(){
        Log.i(TAG, "Directing to MapDownloadActivity.class");
        Intent intent = new Intent(this, MapDowloadActivity.class);
        startActivity(intent);
    }

    private void fillDropDownFiles(){
        mapFiles = findMapFiles();
        Log.i(TAG, "found " + mapFiles.size() + " .map-files");
        List<String> list = new ArrayList<String>();

        for (File file : mapFiles){
            list.add(file.getName());
            Log.i(TAG, "added " + file.getName() + " to dropdown");
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMapFile.setAdapter(dataAdapter);
    }

    protected static Set<File> findMapFiles() {
        Set<File> maps = new HashSet<>();
        List<StorageUtils.StorageInfo> storageList = StorageUtils.getStorageList();
        for (StorageUtils.StorageInfo info : storageList){
            Log.i(TAG, "elements: " + info.getDisplayName());
        }

        for (int i = 0; i < storageList.size(); i++) {
            File f = new File(storageList.get(i).path + File.separator + "osmdroid" + File.separator);
            if (f.exists()) {
                maps.addAll(scan(f));
            }
        }

        File[] downloadFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles();
        for (File downloadFile : downloadFiles){
            Log.i(TAG, "downloadfile: " + downloadFile.getName());
            if (downloadFile.getName().contains(".map")){
                maps.add(downloadFile);
            }
        }
        return maps;
    }

    static private Collection<? extends File> scan(File f) {
        List<File> ret = new ArrayList<>();
        File[] files = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().toLowerCase().endsWith(".map"))
                    return true;
                return false;
            }
        });
        if (files != null) {
            Collections.addAll(ret, files);
        }
        return ret;
    }
}
