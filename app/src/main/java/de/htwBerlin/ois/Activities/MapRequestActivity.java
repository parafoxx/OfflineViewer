package de.htwBerlin.ois.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.htwBerlin.ois.R;

public class MapRequestActivity extends AppCompatActivity {

    private static final String TAG = "MapRequestActivity";

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottom_navigation;

    @BindView(R.id.buttonRequest)
    Button btnRequest;
    @BindView(R.id.textViewResponse)
    TextView textResponse;

    @BindView(R.id.TextCordx1)
    TextView textCordx1;
    @BindView(R.id.TextCordy1)
    TextView textCordy1;
    @BindView(R.id.TextCordx2)
    TextView textCordx2;
    @BindView(R.id.TextCordy2)
    TextView textCordy2;
    @BindView(R.id.TextCordx3)
    TextView textCordx3;
    @BindView(R.id.TextCordy3)
    TextView textCordy3;
    @BindView(R.id.TextCordx4)
    TextView textCordx4;
    @BindView(R.id.TextCordy4)
    TextView textCordy4;
    @BindView(R.id.editText7)
    TextView textDate;
    @BindView(R.id.TextMapname)
    TextView textMapname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_request);
        ButterKnife.bind(this);
        setUpBottomNavigation();

        btnRequest.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              btnRequest.setEnabled(false);
                                              sendRequest();

                                          }
                                      }

        );

    }

    private void setUpBottomNavigation() {
        Menu menu = bottom_navigation.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottom_navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_about:
                        Intent aboutIntent = new Intent(MapRequestActivity.this, AboutActivity.class);
                        startActivity(aboutIntent);
                        break;
                    case R.id.nav_navigation:
                        Intent navigationIntent = new Intent(MapRequestActivity.this, NavigationActivity.class);
                        startActivity(navigationIntent);
                        break;
                    case R.id.nav_download:
                        Intent downloadIntent = new Intent(MapRequestActivity.this, MapDownloadActivity.class);
                        startActivity(downloadIntent);
                        break;
                    case R.id.nav_home:
                        Intent startIntent = new Intent(MapRequestActivity.this, HomeActivity.class);
                        startActivity(startIntent);
                        break;

                }
                return false;
            }
        });
    }

    private void sendRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://141.45.146.117:5000/request/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        textResponse.setText("Response: " + response);
                        btnRequest.setEnabled(true);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("Error.Response", error.toString());
                        if (error.networkResponse != null) {
                            textResponse.setText("Error.Code: " + error.networkResponse.statusCode);
                        }
                        else {
                            textResponse.setText("Error: " + error.getLocalizedMessage());
                        }
                        btnRequest.setEnabled(true);

                    }
                }
        )



        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                addParam(params,"cord-x1", textCordx1.getText().toString());
                addParam(params,"cord-y1", textCordy1.getText().toString());
                addParam(params,"cord-x2", textCordx2.getText().toString());
                addParam(params,"cord-y2", textCordy2.getText().toString());
                addParam(params,"cord-x3", textCordx3.getText().toString());
                addParam(params,"cord-y3", textCordy3.getText().toString());
                addParam(params,"cord-x4", textCordx4.getText().toString());
                addParam(params,"cord-y4", textCordy4.getText().toString());

                addParam(params,"date", textDate.getText().toString());
                addParam(params,"mapname", textMapname.getText().toString());

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                //set timeout to 10 seconds
                10*1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
        textResponse.setText("Requesting...");
    }

    private void addParam(Map<String, String> params, String key, String value) {
        if (!value.isEmpty()) {
            params.put(key,value);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
