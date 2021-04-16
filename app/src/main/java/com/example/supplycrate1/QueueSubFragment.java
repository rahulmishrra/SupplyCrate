package com.example.supplycrate1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Use the {@link QueueSubFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueueSubFragment extends Fragment {

    private TextView tokennum,textView;
    private Button refreshbtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QueueSubFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QueueSubFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QueueSubFragment newInstance(String param1, String param2) {
        QueueSubFragment fragment = new QueueSubFragment();
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
        return inflater.inflate(R.layout.fragment_queue_sub, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokennum = getView().findViewById(R.id.custtokennumber);
        refreshbtn = getView().findViewById(R.id.custrefreshbtn);
        textView = getView().findViewById(R.id.textView21);

        SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _custname = userDetails.get(SessionManager.KEY_NAME);
        String _storename = userDetails.get(SessionManager.KEY_SELECTSTORENAME);

        if(_storename!=null){
            DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Merchants").child(_storename);
            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild("Queue")){
                        for(DataSnapshot dataSnapshot: snapshot.child("Queue").getChildren()){
                            String customer = dataSnapshot.child("customerName").getValue().toString();
                            Toast.makeText(getContext(),customer,Toast.LENGTH_SHORT).show();
                            if(customer.equals(_custname)){

                                tokennum.setText(dataSnapshot.child("token").getValue().toString());
                            }

                        }
                    }
                    else{
                        textView.setText("You do not have any take away from "+_storename);
                        tokennum.setVisibility(View.INVISIBLE);
                        refreshbtn.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            refreshbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbr.child("Queue").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int tokencount = 0;
                            for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                String customer = dataSnapshot.child("customerName").getValue().toString();
                                tokencount += 1;
                                if(customer.equals(_custname)){

                                    String orderid = dataSnapshot.child("orderId").getValue(String.class);
                                    //Toast.makeText(getContext(),orderid,Toast.LENGTH_SHORT).show();
                                    dbr.child("Queue").child(orderid).child("token").setValue(String.valueOf(tokencount));
                                    tokennum.setText(String.valueOf(tokencount));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

        }
        else {
            Toast.makeText(getContext(),"Please select the store first",Toast.LENGTH_SHORT).show();
        }



    }
}