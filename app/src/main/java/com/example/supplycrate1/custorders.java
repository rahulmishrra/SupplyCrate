package com.example.supplycrate1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link custorders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class custorders extends Fragment {

    private ListView cartlistview;
    TextView custcarttotal;
    long count=0;
    int counter = 0;
    DatabaseReference dtbref;
    List<String> cartprdlist = new ArrayList<>();
    List<String> cartprdunit = new ArrayList<>();
    List<String> cartprdprice = new ArrayList<>();
    List<String> cartprdctgry = new ArrayList<>();
    List<String> cartprdqnty = new ArrayList<>();
    List<String> cartprdimgurl = new ArrayList<>();
    List<String> cartkey = new ArrayList<>();

    Button checkoutbtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public custorders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment custorders.
     */
    // TODO: Rename and change types and number of parameters
    public static custorders newInstance(String param1, String param2) {
        custorders fragment = new custorders();
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
        return inflater.inflate(R.layout.fragment_custorders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartlistview = getView().findViewById(R.id.cartlistview);
        checkoutbtn = getView().findViewById(R.id.checkoutbtn);

        custcarttotal = getView().findViewById(R.id.custtotalcart);

        SessionManager sessionManager = new SessionManager(getContext(),SessionManager.SESSION_CUSTOMER);
        HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

        String _custemail = userDetails.get(SessionManager.KEY_EMAIL);
        String _custpassword = userDetails.get(SessionManager.KEY_PASSWORD);
        String _custname = userDetails.get(SessionManager.KEY_NAME);
        String _storename = userDetails.get(SessionManager.KEY_SELECTSTORENAME);

        if(_storename!=null){
            custCartAdapter cartAdapter = new custCartAdapter(getContext(),cartprdlist,cartprdunit,cartprdprice,cartprdctgry,cartprdqnty,cartprdimgurl,cartkey,_custname);

            dtbref = FirebaseDatabase.getInstance().getReference("Customers").child(_custname).child("Cart").child(_storename);
            DatabaseReference adtbref = FirebaseDatabase.getInstance().getReference("Customers").child(_custname).child("Cart");

            adtbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(_storename)){
                        getCartValues(cartAdapter,_storename);
                    }
                    else{
                        //Toast.makeText(getContext(),"NO PRODUCTS FOUND...",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(getContext(),"Please select the store first",Toast.LENGTH_SHORT).show();
        }




    }

    private void getCartValues(ArrayAdapter cartAdapter, String storename) {

        dtbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                cartprdlist.add(snapshot.getValue(CartHelper.class).getProductname());
                cartprdunit.add(snapshot.getValue(CartHelper.class).getProductunit());
                cartprdprice.add(snapshot.getValue(CartHelper.class).getProductprice());
                cartprdctgry.add(snapshot.getValue(CartHelper.class).getProductCategory());
                cartprdqnty.add(snapshot.getValue(CartHelper.class).getProductQuantity());
                cartprdimgurl.add(snapshot.getValue(CartHelper.class).getProductImageUrl());
                cartkey.add(snapshot.getValue(CartHelper.class).getProductKey());
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                cartprdlist.remove(snapshot.getValue(CartHelper.class).getProductname());
                cartprdunit.remove(snapshot.getValue(CartHelper.class).getProductunit());
                cartprdprice.remove(snapshot.getValue(CartHelper.class).getProductprice());
                cartprdctgry.remove(snapshot.getValue(CartHelper.class).getProductCategory());
                cartprdqnty.remove(snapshot.getValue(CartHelper.class).getProductQuantity());
                cartprdimgurl.remove(snapshot.getValue(CartHelper.class).getProductImageUrl());
                cartkey.remove(snapshot.getValue(CartHelper.class).getProductKey());
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cartlistview.setAdapter(cartAdapter);

        cartlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _custproductkey = cartkey.get(position).toString();
                Intent intentProductdetails = new Intent(getContext(),ProjectDetailsPage.class);
                intentProductdetails.putExtra("Productkey",_custproductkey);

                startActivity(intentProductdetails);
            }
        });

        dtbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 count =  snapshot.getChildrenCount();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(count!=0){
            checkout(count,storename);
        }


    }

    private void checkout(long counts, String _storename) {
        //Toast.makeText(getContext(),String.valueOf(counts),Toast.LENGTH_SHORT).show();

        String[] productkies = new String[(int)counts];
        String[] quantities = new String[(int)counts];



        dtbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    quantities[i] = dataSnapshot.getValue(CartHelper.class).getProductQuantity();
                    productkies[i] = dataSnapshot.getValue(CartHelper.class).getProductKey();
                    i++;
                    String price = dataSnapshot.getValue(CartHelper.class).getProductprice();
                    String quantity = dataSnapshot.getValue(CartHelper.class).getProductQuantity();
                    int _pricen = Integer.valueOf(price);
                    int _quantityn  = Integer.valueOf(quantity);
                    counter += _pricen*_quantityn;
                }

                custcarttotal.setText("Item Total: "+"\u20B9"+String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(getContext(),String.valueOf(productkies.length),Toast.LENGTH_SHORT).show();
                Intent orderIntent = new Intent(getActivity(),OrderDetailsPage.class);
                orderIntent.putExtra("Item total",String.valueOf(counter));
                orderIntent.putExtra("StoreName",_storename);
                orderIntent.putExtra("Productkey",productkies);
                orderIntent.putExtra("Quantity",quantities);
                startActivity(orderIntent);

                //Toast.makeText(getContext(),String.valueOf(counter),Toast.LENGTH_SHORT).show();
            }
        });



    }
}