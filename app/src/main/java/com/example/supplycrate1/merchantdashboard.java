package com.example.supplycrate1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link merchantdashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class merchantdashboard extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;

    private Spinner dspinner;


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
        dspinner.setOnItemSelectedListener(this);



       SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();

        String mmail  = mrchDetails.get(SessionManager.KEY_MERCHANTEMAIL);
        String mpass  = mrchDetails.get(SessionManager.KEY_MERCHANTPASSWORD);
        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);
        String mname = mrchDetails.get(SessionManager.KEY_MERCHANTNAME);
        String mphone = mrchDetails.get(SessionManager.KEY_MERCHANTPHONE);





    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}