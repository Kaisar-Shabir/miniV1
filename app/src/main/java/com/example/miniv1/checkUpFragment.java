package com.example.miniv1;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class checkUpFragment extends Fragment {

    Button selectBtn, uploadBtn;
    ImageView diseaseImg;
    StorageReference strRef;
    public Uri imgUri;
    private StorageTask uploadTask;
    Dialog loadingDialog;


    public checkUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_check_up, container, false);

//        ImageView loadingLogo = (ImageView) v.findViewById(R.id.img_loading);
//        TextView tv_homeFragment = (TextView) v.findViewById(R.id.textView);
//
//        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.animation);
//        YoYo.with(Techniques.BounceInDown).duration(1000).repeat(0).playOn(tv_homeFragment);
//
//        loadingLogo.startAnimation(animation);

        loadingDialog = new Dialog(getActivity());


        TextView tv_checkupFragment = (TextView) v.findViewById(R.id.textView);
        YoYo.with(Techniques.BounceInDown).duration(1000).repeat(0).playOn(tv_checkupFragment);

        selectBtn = (Button) v.findViewById(R.id.btn_select);
        uploadBtn = (Button) v.findViewById(R.id.btn_upload);
        diseaseImg = (ImageView) v.findViewById(R.id.disease_img);
        strRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "Upload in progress.", Toast.LENGTH_SHORT).show();
                }
                else {
                    FileUploader();
                }
            }
        });

        return v;
    }

    public void showLoadingDialog(View v) {

        loadingDialog.setContentView(R.layout.loading_dialogbox);

        Button cover_btn = (Button) loadingDialog.findViewById(R.id.btn_cover);
        ImageView img_loading = (ImageView) loadingDialog.findViewById(R.id.img_loading);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.animation);
        img_loading.startAnimation(animation);

        cover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Uploading file currently. Try again later.", Toast.LENGTH_SHORT).show();
            }
        });

        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.show();

    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 1);
    }

    private String getExtension(Uri imguri) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        try {
            return mimeTypeMap.getExtensionFromMimeType(cr.getType(imguri));
        }catch (Exception ex) {
            Toast.makeText(getActivity(), "Exception: " + ex, Toast.LENGTH_SHORT).show();
        }
        return ".png";
    }

    private void FileUploader() {
        StorageReference Ref = strRef.child(System.currentTimeMillis() + "." + getExtension(imgUri));
        showLoadingDialog(getView());

        try {
            uploadTask = Ref.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            // Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            loadingDialog.dismiss();

                            Toast.makeText(getActivity(),"Successfully send for analysis.",Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            startActivity(new Intent(getActivity(), homePage.class));

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            loadingDialog.dismiss();
                            Toast.makeText(getActivity(),"Something went wrong.",Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception sEX) {
            Toast.makeText(getActivity(), "Please select an image to be analysed", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            imgUri = data.getData();
            diseaseImg.setImageURI(imgUri);

        }
    }
}
