package com.example.studentnotifyapp.Student;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.studentnotifyapp.BaseFragment;
import com.example.studentnotifyapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ViewPdfFragment extends BaseFragment {


    EditText searchNote;
    private RecyclerView pdfRecycler;
    private DatabaseReference reference;
    private List<PdfData> list;
    private PdfAdapter adapter;
    private ProgressDialog pd;
    public ViewPdfFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_pdf, container, false);

        pd= new ProgressDialog(getContext());
        pd.setTitle("Please wait...");
        pd.setMessage("loading pdf");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        pdfRecycler = v.findViewById(R.id.pdfRecycler);
        reference = FirebaseDatabase.getInstance().getReference().child("pdf");

        searchNote = v.findViewById(R.id.search);
        getData();
        return v;
    }
    public void getData()
    {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PdfData data = dataSnapshot.getValue(PdfData.class);
                    list.add(data);
                }
                adapter = new PdfAdapter(getContext(),list);
                pdfRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                pd.dismiss();
                pdfRecycler.setAdapter(adapter);
                searchNote.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                pd.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        searchNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<PdfData> filterList = new ArrayList<>();
        for(PdfData item : list)
        {
            if(item.getPdfTitle().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
        adapter.Filteredlist(filterList);
    }
}