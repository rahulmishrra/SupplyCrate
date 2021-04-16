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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustDashboard extends Fragment {

    private ListView custctgrylist,offerlist,recommendedlist;
    private Button custlogout;
    private Toolbar custdashtoolbar;
    private long countctgry,countoffer,countrecom;

    List<String> custcategorieslist = new ArrayList<>();

    List<String> custproductofferkey = new ArrayList<>();
    List<Integer> custprdctoffers = new ArrayList<>();

    List<String> custproductrecomkey = new ArrayList<>();
    List<Integer> custrecomprdctsellcount = new ArrayList<>();

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
        offerlist = getView().findViewById(R.id.offerslistview);
        recommendedlist = getView().findViewById(R.id.recommendedlistview);
        TextView textviewover = getView().findViewById(R.id.textviewover);
        TextView textviewover2 = getView().findViewById(R.id.textviewover2);

        custdashtoolbar.setTitle(_storename);
        if(_storename!=null){
            DatabaseReference dbrefer = FirebaseDatabase.getInstance().getReference("Merchants").child(_storename);




            CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(),custcategorieslist);

            dbrefer.child("Categories").addChildEventListener(new ChildEventListener() {
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

            dbrefer.child("Categories").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    countctgry = snapshot.getChildrenCount();
                    setctgryHeight(countctgry);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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



            custProductofferAdapter productofferAdapter = new custProductofferAdapter(getContext(),custproductofferkey,_storename);

            dbrefer.child("Products").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String offer = snapshot.getValue(ProductHelper.class).getProductDiscount();
                    if(!offer.equals("0") && custprdctoffers.size()<5){
                        custprdctoffers.add(Integer.valueOf(offer));
                        //custproductofferkey.add(snapshot.getValue(ProductHelper.class).getProductkey());
                    }
                    productofferAdapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    productofferAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            ListSort listSort = new ListSort(custprdctoffers);

            dbrefer.child("Products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(custprdctoffers.size()==0){
                        offerlist.setVisibility(View.GONE);
                        textviewover.setVisibility(View.GONE);

                    }
                    custprdctoffers = listSort.getIntegerList();
                    setofferHeight(custprdctoffers.size());
                    for (int i=0;i<custprdctoffers.size();i++) {
                        String offervalue = String.valueOf(custprdctoffers.get(i));
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            String productkey = dataSnapshot.getValue(ProductHelper.class).getProductkey();
                            String offerhere= dataSnapshot.getValue(ProductHelper.class).getProductDiscount();
                            /**/
                            if(offervalue.equals(offerhere)){
                                if(i>0){
                                    if(custproductofferkey.get(i-1).equals(productkey)){
                                        continue;
                                    }
                                }
                                custproductofferkey.add(productkey);
                                break;
                            }
                        }
                    }
                    productofferAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            offerlist.setAdapter(productofferAdapter);
            offerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String _custproductkey = custproductofferkey.get(position).toString();
                    Intent intentProductdetails = new Intent(getContext(),ProjectDetailsPage.class);
                    intentProductdetails.putExtra("Productkey",_custproductkey);
                    intentProductdetails.putExtra("StoreName",_storename);
                    startActivity(intentProductdetails);

                }
            });

            custRecommendAdapter recommendAdapter = new custRecommendAdapter(getContext(),custproductrecomkey,_storename);

            dbrefer.child("Products").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String sell = snapshot.child("sellcount").getValue(String.class);
                    if(sell!=null && custrecomprdctsellcount.size()<5) {
                        custrecomprdctsellcount.add(Integer.valueOf(sell));
                    }

                    recommendAdapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    recommendAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            RecommendSort recommendSort = new RecommendSort(custrecomprdctsellcount);

            dbrefer.child("Products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(custprdctoffers.size()==0){
                        recommendedlist.setVisibility(View.GONE);
                        textviewover2.setVisibility(View.GONE);

                    }
                    custrecomprdctsellcount = recommendSort.getRecommendList();
                    setRecommendHeight(custrecomprdctsellcount.size());
                    for (int i=0;i<custrecomprdctsellcount.size();i++) {
                        String offervalue = String.valueOf(custrecomprdctsellcount.get(i));
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            String productkey = dataSnapshot.getValue(ProductHelper.class).getProductkey();
                            String offerhere= dataSnapshot.child("sellcount").getValue().toString();
                            /**/
                            if(offervalue.equals(offerhere)){
                                if(i>0){
                                    if(custproductrecomkey.get(i-1).equals(productkey)){
                                        continue;
                                    }
                                }
                                custproductrecomkey.add(productkey);
                                break;
                            }
                        }
                    }
                    recommendAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            recommendedlist.setAdapter(recommendAdapter);

            recommendedlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String _custproductkey = custproductrecomkey.get(position).toString();
                    Intent intentProductdetails = new Intent(getContext(),ProjectDetailsPage.class);
                    intentProductdetails.putExtra("Productkey",_custproductkey);
                    intentProductdetails.putExtra("StoreName",_storename);
                    startActivity(intentProductdetails);

                }
            });


        }
        else{
            Toast.makeText(getContext(),"Please select the store first",Toast.LENGTH_SHORT).show();
        }








    }

    private void setRecommendHeight(int size) {
        ViewGroup.LayoutParams list = recommendedlist.getLayoutParams();
        list.height = (int) (950*size);
        recommendedlist.setLayoutParams(list);
        recommendedlist.requestLayout();
    }

    private void setofferHeight(long countoffer) {

        ViewGroup.LayoutParams list = offerlist.getLayoutParams();
        list.height = (int) (950*countoffer);
        offerlist.setLayoutParams(list);
        offerlist.requestLayout();
    }

    private void setctgryHeight(long countctgry) {
        ViewGroup.LayoutParams list = custctgrylist.getLayoutParams();
        list.height = (int) (350*countctgry);
        custctgrylist.setLayoutParams(list);
        custctgrylist.requestLayout();
    }


}