package com.example.codzilla.flashydai;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    int myBalance;
    int tokenBalance;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final TextView tv1 = (TextView)findViewById(R.id.myBalance);
            final TextView tv2 = (TextView)findViewById(R.id.tokenBalance);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);


                    tv1.setVisibility(View.INVISIBLE);

                    tv2.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                   // mTextMessage.setText(R.string.title_dashboard);

                    tv1.setVisibility(View.VISIBLE);

                    tv2.setVisibility(View.VISIBLE);
                    return true;
                //case R.id.navigation_notifications:
                //    mTextMessage.setText(R.string.title_notifications);
                //    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView tv1 = (TextView)findViewById(R.id.myBalance);
        final TextView tv2 = (TextView)findViewById(R.id.tokenBalance);

        tv1.setVisibility(View.INVISIBLE);

        tv2.setVisibility(View.INVISIBLE);
        String url = "192.168.14.162:3001/getEthBalance/?address=0xA8EA8a05e594Eb6be813eEeddD0eCb67cB93fb1A";

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        final RequestQueue queue = new RequestQueue(cache, network);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            myBalance = Integer.parseInt(response.get("myBalance").toString());
                            tv1.setText("My Ethereum Balance: " + myBalance);
                            tokenBalance  = Integer.parseInt(response.get("tokenBalance").toString());
                            tv2.setText("My Dai Balance: " + tokenBalance);
                            //Toast.makeText(MainActivity.this, tokenBalance, Toast.LENGTH_LONG).show();
                        } catch(JSONException e) {
                            //Toast.makeText(MainActivity.this, "Your Message", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };

        queue.add(getRequest);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
