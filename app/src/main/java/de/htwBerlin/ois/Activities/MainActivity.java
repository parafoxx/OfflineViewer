package de.htwBerlin.ois.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.android.view.MapView;

import java.io.File;

import de.htwBerlin.ois.FileStructure.MapFileSingleton;
import de.htwBerlin.ois.R;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    public static final int READ_EXTERNAL_STORAGE = 112;

    private MapView mapView = null;
    private File mapFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(getApplication());

        setContentView(R.layout.activity_main);
        mapView = new MapView(this);
        setContentView(mapView);

        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);

        TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        //MapDataStore mapDataStore = new MapFile(mapFile);
        File ohdmFile = MapFileSingleton.getInstance().getFile();

        Log.i(TAG, "Using : " + ohdmFile.getName() + " with path " + ohdmFile.getPath());

        MapDataStore mapDataStore = new MapFile(ohdmFile);

        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);

        mapView.getLayerManager().getLayers().add(tileRendererLayer);
        //mapView.setCenter(new LatLong(52.517037, 13.38886));
        mapView.setZoomLevel((byte) 12);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    protected void readSDcardDownloadedFiles() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
        } else {
            this.mapFile = MapFileSingleton.getInstance().getFile();
            //this.mapFile = new File(Environment.getExternalStorageDirectory()+"/osmdroid/", MAP_FILE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "Cannot proceed. Exiting.",Toast.LENGTH_LONG).show();
            openStartActivity();

        }
    }

    private void openStartActivity(){
        Log.i(TAG, "Directing to HomeActivity.class");
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }


}
