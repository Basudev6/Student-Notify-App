package com.example.studentnotifyapp.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.studentnotifyapp.BaseFragment;
import com.example.studentnotifyapp.R;
import com.example.studentnotifyapp.StudentData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeAdminFragment extends BaseFragment {

    private DatabaseReference reference;
    private TextView quotes,author;
    private String url = "https://api.api-ninjas.com/v1/quotes?category=knowledge";


    public HomeAdminFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        quotes = view.findViewById(R.id.txtquots);
        author = view.findViewById(R.id.txtauthor);
        getData();

        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int countStudent = 0;
                int countRegistration = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    StudentData data = dataSnapshot.getValue(StudentData.class);
                    if(data!=null && data.getStatus().equals(false))
                    {
                        ++countRegistration;
                    }
                    if(data!=null && data.getStatus().equals(true))
                    {
                        ++countStudent;
                    }

                }
                int remaningStudent = 28-countStudent-countRegistration;
                Pie pie = AnyChart.pie();

                List<DataEntry> data = new ArrayList<>();
                data.add(new ValueDataEntry("Students", countStudent));
                data.add(new ValueDataEntry("Signup Request", countRegistration));
                data.add(new ValueDataEntry("Remaining Students",remaningStudent ));


                pie.data(data);

                AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.any_chart_view);
                anyChartView.setChart(pie);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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