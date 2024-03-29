package com.example.supplycrate1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link merchAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class merchAccount extends Fragment {

    TextView storename, storeownername, storeemail, storephone, storelocation;
    ImageView merchImage;
    Button mlogout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public merchAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment merchAccount.
     */
    // TODO: Rename and change types and number of parameters
    public static merchAccount newInstance(String param1, String param2) {
        merchAccount fragment = new merchAccount();
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
        return inflater.inflate(R.layout.fragment_merch_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storename = getView().findViewById(R.id.custsettingname);
        storeownername = getView().findViewById(R.id.accbownername);
        storeemail = getView().findViewById(R.id.custsettingemail);
        storephone = getView().findViewById(R.id.custsettingphone);
        storelocation = getView().findViewById(R.id.custsettingaddress);
        mlogout = getView().findViewById(R.id.custacclogout);
        merchImage = getView().findViewById(R.id.merchImage);

        SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();

        String mmail  = mrchDetails.get(SessionManager.KEY_MERCHANTEMAIL);

        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);
        String mname = mrchDetails.get(SessionManager.KEY_MERCHANTNAME);
        String mphone = mrchDetails.get(SessionManager.KEY_MERCHANTPHONE);
        String mlocation = mrchDetails.get(SessionManager.KEY_MERCHANTLOCATION);

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Merchants").child(mbname);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storelocation.setText(snapshot.child("Location").getValue().toString());
                Picasso.get().load(Uri.parse(snapshot.child("merchantImageUrl").getValue().toString())).into(merchImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storename.setText(mbname);
        storeownername.setText(mname);
        storeemail.setText(mmail);
        storephone.setText(mphone);


        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logoutMerchantFromSession();
                startActivity(new Intent(getContext(), mainretailer2op.class));

            }
        });

    }
}