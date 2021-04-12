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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustDashboard extends Fragment {

    private ListView custctgrylist;
    private Button custlogout;
    private Toolbar custdashtoolbar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustDashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustDashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static CustDashboard newInstance(String param1, String param2) {
        CustDashboard fragment = new CustDashboard();
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
        return inflater.inflate(R.layout.fragment_cust_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _storename = userDetails.get(SessionManager.KEY_SELECTSTORENAME);

        custdashtoolbar = getView().findViewById(R.id.custdashtoolbar);
        custctgrylist = getView().findViewById(R.id.ctgrylistview);
        custlogout = getView().findViewById(R.id.customerlogoutbtn);

        custdashtoolbar.setTitle(_storename);
        if(_storename!=null){
            DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Merchants").child(_storename).child("Categories");
            List<String> custcategorieslist = new ArrayList<>();

            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(),custcategorieslist);

            dbrefer.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    custcategorieslist.add(snapshot.getValue(String.class));
                    categoryAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    categoryAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    custcategorieslist.remove(snapshot.getValue(String.class));
                    categoryAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            custctgrylist.setAdapter(categoryAdapter);

            custctgrylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String category = custcategorieslist.get(position).toString();
                    //Toast.makeText(getApplicationContext(),category,Toast.LENGTH_SHORT).show();
                    Intent intentdash = new Intent(getContext(),custProductpage.class);
                    intentdash.putExtra("StoreName",_storename);
                    intentdash.putExtra("Category",category);
                    startActivity(intentdash);
                }
            });

        }
        else{
            Toast.makeText(getContext(),"Please select the store first",Toast.LENGTH_SHORT).show();
        }






        custlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUserFromSession();
                startActivity(new Intent(getContext(), Login.class));
            }
        });


    }


}