package de.htwBerlin.ois.Activities;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.htwBerlin.ois.FileStructure.MapFileSingleton;
import de.htwBerlin.ois.R;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    private static final String MAP_FILE_PATH = Environment.getExternalStorageDirectory().toString()+"/osmdroid";

    private Set<File> mapFiles;

    @BindView(R.id.mapFileDropDown) Spinner spinnerMapFile;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        fillDropDownFiles();

        bottom_navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        Menu menu = bottom_navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_about:
                        Intent aboutIntent = new Intent(StartActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;
                    case R.id.nav_navigation:
                        Log.i(TAG, "Button \"startNavigation\" pressed");
                        Log.i(TAG, "User has choosen " + spinnerMapFile.getSelectedItem().toString());

                        for (File file : mapFiles){
                            if (file.getName().equals(spinnerMapFile.getSelectedItem().toString())){
                                Log.i(TAG, "using " + file.getName() + " as mapfile");
                                MapFileSingleton mapFile = MapFileSingleton.getInstance();
                                mapFile.setFile(file);
                            }
                        }
                        Intent navigationIntent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(navigationIntent);
                        break;
                    case R.id.nav_download:
                        Intent downloadIntent = new Intent(StartActivity.this, MapDowloadActivity.class);
                        startActivity(downloadIntent);
                        break;
                }
                return false;
            }
        });
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

    private void fillDropDownFiles(){
        mapFiles = findMapFiles();
        Log.i(TAG, "found " + mapFiles.size() + " .map-files");
        List<String> list = new ArrayList<String>();

        if (mapFiles.size() > 0) {
            for (File file : mapFiles) {
                list.add(file.getName());
                Log.i(TAG, "added " + file.getName() + " to dropdown");
            }
        }

        // Sort list
        List<String> listSorted = list.stream().collect(Collectors.<String>toList());
        Collections.sort(listSorted, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSorted);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMapFile.setAdapter(dataAdapter);
    }

    protected Set<File> findMapFiles() {
        Set<File> maps = new HashSet<>();

        try {
            for (File osmfile : new File(MAP_FILE_PATH).listFiles()) {
                if (osmfile.getName().endsWith(".map")) {
                    Log.i(TAG, "osmfile: " + osmfile.getName());
                    maps.add(osmfile);
                }
            }
        } catch (NullPointerException e){
            Log.i(TAG, "no map fiels found.");
        }
        return maps;
    }
}
