package com.example.supplycrate1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link queue#newInstance} factory method to
 * create an instance of this fragment.
 */
public class queue extends Fragment {

    Button merchantlogoutbtn;
    TextView textView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public queue() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment queue.
     */
    // TODO: Rename and change types and number of parameters
    public static queue newInstance(String param1, String param2) {
        queue fragment = new queue();
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
        return inflater.inflate(R.layout.fragment_queue, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();

        String mmail  = mrchDetails.get(SessionManager.KEY_MERCHANTEMAIL);
        String mpass  = mrchDetails.get(SessionManager.KEY_MERCHANTPASSWORD);
        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);
        String mname = mrchDetails.get(SessionManager.KEY_MERCHANTNAME);
        String mphone = mrchDetails.get(SessionManager.KEY_MERCHANTPHONE);

        textView = getView().findViewById(R.id.textView12);
        textView.setText(mmail + "\n"+ mpass + "\n"+ mbname + "\n"+ mname + "\n"+ mphone);
        merchantlogoutbtn  = getView().findViewById(R.id.mrchlogoutbtn);
        merchantlogoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutMerchantFromSession();
                startActivity(new Intent(getContext(), mainretailer2op.class));
            }
        });

    }
}