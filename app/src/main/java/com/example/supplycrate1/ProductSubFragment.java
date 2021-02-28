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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductSubFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductSubFragment extends Fragment {

    private FloatingActionButton prdctfrmbtn;
    private ListView prdctListview;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductSubFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductSubFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductSubFragment newInstance(String param1, String param2) {
        ProductSubFragment fragment = new ProductSubFragment();
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
        return inflater.inflate(R.layout.fragment_product_sub, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prdctfrmbtn = getView().findViewById(R.id.productbtn);
        prdctListview = getView().findViewById(R.id.procductlistview);

        List<String> productlist,productunit;
        List<String> productprice;
        productlist = new ArrayList<>();
        productunit = new ArrayList<>();
        productprice = new ArrayList<>();


        ProductAdapter productAdapter = new ProductAdapter(getContext(),productlist,productunit,productprice);

        FirebaseDatabase fdb = FirebaseDatabase.getInstance();
        DatabaseReference dbreference = fdb.getReference("Pradeep").child("Products");

        dbreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                productlist.add(snapshot.getValue(ProductHelper.class).getProductName());
                productunit.add(snapshot.getValue(ProductHelper.class).getProductUnit());
                productprice.add(snapshot.getValue(ProductHelper.class).getSellingprice());
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                productlist.remove(snapshot.getValue(ProductHelper.class).getProductName());
                productunit.remove(snapshot.getValue(ProductHelper.class).getProductUnit());
                productprice.remove(snapshot.getValue(ProductHelper.class).getSellingprice());
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        prdctListview.setAdapter(productAdapter);

        prdctfrmbtn.setOnClickListener(v -> startActivity(new Intent(getActivity(),ProductForm.class)));
    }
}