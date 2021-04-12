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
import android.widget.ListView;
import android.widget.TextView;
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
 * Use the {@link orders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class orders extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment orders.
     */
    // TODO: Rename and change types and number of parameters
    public static orders newInstance(String param1, String param2) {
        orders fragment = new orders();
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
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = getView().findViewById(R.id.orderlist);
        Toolbar ordertool = getView().findViewById(R.id.mrchordertoolbar);

        ordertool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        List<String> Custnamelist = new ArrayList<>();
        List<String> orderPricelist = new ArrayList<>();
        List<String> orderIdlist = new ArrayList<>();
        List<String> orderDatelist = new ArrayList<>();
        List<String> orderStatuslist = new ArrayList<>();



        orderAdapter adapter = new orderAdapter(getContext(),Custnamelist,orderPricelist,orderIdlist,orderDatelist,orderStatuslist);

        SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_MERCHANT);
        HashMap<String,String> mrchDetails = sessionManager.getMerchantDetailFromSession();
        String mbname = mrchDetails.get(SessionManager.KEY_MERCHANTBNAME);


        FirebaseDatabase firedb = FirebaseDatabase.getInstance();
        DatabaseReference dataref = firedb.getReference("Merchants").child(mbname).child("orders");

        dataref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Custnamelist.add(snapshot.child("customerName").getValue(String.class));
                orderPricelist.add(snapshot.child("ordertotal").getValue(String.class));
                orderIdlist.add(snapshot.child("orderId").getValue(String.class));
                orderDatelist.add(snapshot.child("Date").getValue(String.class));
                orderStatuslist.add(snapshot.child("orderStatus").getValue(String.class));
               // Toast.makeText(getContext(),snapshot.child("customer").getValue(String.class),Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Custnamelist.remove(snapshot.child("customerName").getValue(String.class));
                orderPricelist.remove(snapshot.child("ordertotal").getValue(String.class));
                orderIdlist.remove(snapshot.child("orderId").getValue(String.class));
                orderDatelist.remove(snapshot.child("Date").getValue(String.class));
                orderStatuslist.remove(snapshot.child("orderStatus").getValue(String.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String orderid =  orderIdlist.get(position);
                Intent orderdetails = new Intent(getContext(),mrchOrderDetails.class);
                orderdetails.putExtra("OrderId",orderid);
                startActivity(orderdetails);
            }
        });
    }
}