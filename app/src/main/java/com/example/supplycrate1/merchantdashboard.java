package com.example.supplycrate1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link merchantdashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class merchantdashboard extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;

    private Spinner dspinner;
    long  counter = 0;
    double reviewitr = 0.0;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public merchantdashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment merchantdashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static merchantdashboard newInstance(String param1, String param2) {
        merchantdashboard fragment = new merchantdashboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_merchantdashboard, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dspinner = (Spinner) getView().findViewById(R.id.durationspin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.duration, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dspinner.setAdapter(adapter);

        TextView dashorders = getView().findViewById(R.id.dashorders);
        TextView reviews = getView().findViewById(R.id.updatecategory);
        TextView reviewText = getView().findViewById(R.id.lastupdatedash);
        Toolbar merchdashtoolbar = getView().findViewById(R.id.merchdashtoolbar);

       SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();

        String mmail  = mrchDetails.get(SessionManager.KEY_MERCHANTEMAIL);
        String mpass  = mrchDetails.get(SessionManager.KEY_MERCHANTPASSWORD);
        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);
        String mname = mrchDetails.get(SessionManager.KEY_MERCHANTNAME);
        String mphone = mrchDetails.get(SessionManager.KEY_MERCHANTPHONE);

        merchdashtoolbar.setTitle(mbname);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Merchants").child(mbname);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("orders")){
                    dashorders.setText(String.valueOf(snapshot.child("orders").getChildrenCount()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("orders")){
                    reviews.setText("");
                    reviewText.setText("");
                    for(DataSnapshot dataSnapshot: snapshot.child("orders").getChildren()){
                        if(dataSnapshot.hasChild("review")){
                            String rev = dataSnapshot.child("review").getValue().toString();
                            float review = Float.valueOf(rev);
                            reviewitr += review;
                            counter++;
                        }
                    }
                    double overallreview = reviewitr/counter;
                    Toast.makeText(getContext(),String.valueOf(overallreview),Toast.LENGTH_SHORT).show();
                    reviews.setText(String.valueOf(overallreview)+"/5");
                    reviewText.setText("Overall reviews");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}