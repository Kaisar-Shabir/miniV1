package com.example.miniv1;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class AdapterDiseases extends RecyclerView.Adapter<AdapterDiseases.MyHolder>{

    Dialog diseaseDialog;

    private Context context;
    List<disease_info_model> diseaseList;

    public AdapterDiseases(Context context, List<disease_info_model> diseaseList) {
        this.context = context;
        this.diseaseList = diseaseList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.disease_cardview, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        final String diseaseImage = diseaseList.get(position).getDisease_img();
        final String diseaseName = diseaseList.get(position).getDisease();
        final String diseaseInfo = diseaseList.get(position).getDisease_info();

        holder.diseaseTv.setText(diseaseName);


        try {
            Picasso.get().load(diseaseImage)
                    .placeholder(R.drawable.ic_search_black_24dp)
                    .into(holder.imageIv);
        }
        catch (Exception e) {

        }

        diseaseDialog = new Dialog(context);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diseaseDialog.setContentView(R.layout.disease_dialogbox);

                CircularImageView cimv = (CircularImageView) diseaseDialog.findViewById(R.id.disease_pic);
                JustifyTextView info = (JustifyTextView) diseaseDialog.findViewById(R.id.tv_disease_info);
                TextView tv = (TextView) diseaseDialog.findViewById(R.id.textView3);

                try {
                    Picasso.get().load(diseaseImage)
                            .placeholder(R.drawable.ic_search_black_24dp)
                            .into(cimv);
                    info.setText("\n\n\n\n\n\n\n" + diseaseInfo );
                    tv.setText(diseaseName);
                }
                catch (Exception e) {

                }

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation1);
                cimv.startAnimation(animation);
                tv.startAnimation(animation);
                info.startAnimation(animation);

                diseaseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                diseaseDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {


        CircularImageView imageIv;
        TextView diseaseTv;
        RelativeLayout parentLayout;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            diseaseTv = itemView.findViewById(R.id.diseaseName);
            imageIv = itemView.findViewById(R.id.disease_pic);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
