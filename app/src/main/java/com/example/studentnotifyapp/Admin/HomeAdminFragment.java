package com.example.studentnotifyapp.Admin;

import static androidx.core.content.ContextCompat.registerReceiver;
import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studentnotifyapp.BaseFragment;
import com.example.studentnotifyapp.CheckInternet.InternetReceiver;
import com.example.studentnotifyapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HomeAdminFragment extends BaseFragment {

    private TextView quotes,author;
    private String url = "https://api.api-ninjas.com/v1/quotes?category=knowledge";


    public HomeAdminFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        quotes = view.findViewById(R.id.txtquots);
        author = view.findViewById(R.id.txtauthor);
        getData();
        return view;
    }

    private void getData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray quotArray = new JSONArray(response);
                            JSONObject quoteObject = quotArray.getJSONObject(0);

                            quotes.setText(quoteObject.getString("quote"));
                            author.setText(quoteObject.getString("author"));

                            quotes.setVisibility(View.VISIBLE);
                            author.setVisibility(View.VISIBLE);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {


        @Override
            public void onErrorResponse(VolleyError error) {
                // below line is use to display a error message.
            error.getMessage();
            }

        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-Api-Key", "T0TKKqQV2Uhl6NppDd5P6g==EgUACsRHSEnITKVx");
                return headers;
            }
        };


        queue.add(stringRequest);
    }





}