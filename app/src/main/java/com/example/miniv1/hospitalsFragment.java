package com.example.miniv1;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


/**
 * A simple {@link Fragment} subclass.
 */
public class hospitalsFragment extends Fragment {


    public hospitalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hospitals, container, false);

        ImageView loadingLogo = (ImageView) v.findViewById(R.id.img_loading);
        TextView tv_homeFragment = (TextView) v.findViewById(R.id.textView);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.animation);
        YoYo.with(Techniques.BounceInDown).duration(1000).repeat(0).playOn(tv_homeFragment);


        loadingLogo.startAnimation(animation);

        return v;
    }

}
