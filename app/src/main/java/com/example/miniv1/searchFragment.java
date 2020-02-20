package com.example.miniv1;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class searchFragment extends Fragment {

    SearchableSpinner searchableSpinner;

    RecyclerView recyclerView;

    AdapterDiseases adapterDiseases;
    List<disease_info_model> diseaseList;

    String selectedDisease;


    public searchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);

//        ImageView loadingLogo = (ImageView) v.findViewById(R.id.img_loading);
        TextView tv_homeFragment = (TextView) v.findViewById(R.id.textView);
//
//        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.animation);
        YoYo.with(Techniques.BounceInDown).duration(1000).repeat(0).playOn(tv_homeFragment);


//        loadingLogo.startAnimation(animation);




        searchableSpinner = (SearchableSpinner) v.findViewById(R.id.searchable_spinner_diseases);





        //FULL SCREEN: HIF+DING THE STATUS BAR
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = (RecyclerView) v.findViewById(R.id.disease_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        diseaseList = new ArrayList<>();

        //startActivity(new Intent(MainActivity.this, countDownTimer.class));

        getAllDisease();


        return v;
    }

    private void getAllDisease() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("diseases");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                diseaseList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    disease_info_model userInfoModel = ds.getValue(disease_info_model.class);

                    diseaseList.add(userInfoModel);

                    adapterDiseases = new AdapterDiseases(getActivity(), diseaseList);

                    recyclerView.setAdapter(adapterDiseases);
                }


                List<String> disease_list = new ArrayList<>();

                for (disease_info_model item:diseaseList){
                    disease_list.add(item.getDisease());
                }
                //Log.i("kaisar", "onFirebaseLoadSuccess: " + name_list);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, disease_list);
                searchableSpinner.setAdapter(adapter);

                selectedDisease = searchableSpinner.getSelectedItem().toString();

                searchableSpinner.setTitle("Select Disease");


                searchableSpinner.onSearchableItemClicked(disease_list, searchableSpinner.getSelectedItemPosition());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
