package com.example.trelloreal.LoginAndSignUp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trelloreal.DataBase.clImage;
import com.example.trelloreal.DataBase.clUser;
import com.example.trelloreal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpTabFragment.
     */

    private FirebaseAuth mAuth;
    EditText edtUser, edtPassWord, edtRe_PassWord;
    Button imgBtnSignUp;
    ArrayList<Uri> ListDefaultImage;

    // TODO: Rename and change types and number of parameters
    public static SignUpTabFragment newInstance(String param1, String param2) {
        SignUpTabFragment fragment = new SignUpTabFragment();
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
        View v = inflater.inflate(R.layout.fragment_sign_up_tab, container, false);

        mAuth = FirebaseAuth.getInstance();

        edtUser = v.findViewById(R.id.edtUserSU);
        edtPassWord = v.findViewById(R.id.edtPassWordSU);
        edtRe_PassWord = v.findViewById(R.id.edtRe_PassWordSU);
        imgBtnSignUp = v.findViewById(R.id.imgBtnSignUpSU);

        ListDefaultImage = new ArrayList<>();

        Uri imageUri1 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img1);
        Uri imageUri2 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img2);
        Uri imageUri3 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img3);
        Uri imageUri4 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img4);
        Uri imageUri5 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img5);
        Uri imageUri6 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img6);
        Uri imageUri7 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img7);
        Uri imageUri8 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img8);
        Uri imageUri9 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img9);
        Uri imageUri10 = Uri.parse("android.resource://com.example.trelloreal/" + R.drawable.img10);
        ListDefaultImage.add(imageUri1);
        ListDefaultImage.add(imageUri2);
        ListDefaultImage.add(imageUri3);
        ListDefaultImage.add(imageUri4);
        ListDefaultImage.add(imageUri5);
        ListDefaultImage.add(imageUri6);
        ListDefaultImage.add(imageUri7);
        ListDefaultImage.add(imageUri8);
        ListDefaultImage.add(imageUri9);
        ListDefaultImage.add(imageUri10);

        imgBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatePassword()) {
                    SignUpEmail();
                }
            }
        });

        return v;
    }

    private void LoadDataDefaultFireBase(String UserName) {
        ArrayList<String> arrayList = new ArrayList<>();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("image");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (int i = 0; i < ListDefaultImage.size(); i++) {
                    final ProgressDialog dialog = new ProgressDialog(getContext());
                    dialog.setTitle("Đăng kí thành công");
                    dialog.show();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    //StorageReference uploader = storage.getReference().child(UserName+"/image/"+"img "+i+".png");
                    // StorageReference uploader = storage.getReference().child(UserName+"/image/"+"img "+i+".png");

                    Calendar calendar = Calendar.getInstance();
                    String name = String.valueOf(calendar.getTimeInMillis());
                    StorageReference uploader = storage.getReference().child(UserName + "/image/" + name);

                    int finalI = i;
                    uploader.putFile(ListDefaultImage.get(i))
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                                            DatabaseReference reference = rootNode.getReference("users/" + UserName + "/General/image");
                                            clImage clImage = new clImage("img" + finalI, /*arrayList.get(finalI)*/uri.toString());
                                            reference.child(name).setValue(clImage);
                                        }
                                    });
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                                    float percent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                    dialog.setMessage("Uploaded :" + (int) percent + "%");

                                    if(percent == 100){
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                /**
                 * Chuyen tab sau khi sign in
                 */

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private Boolean validateUsername() {
        String user = edtUser.getText().toString();
        if (user.isEmpty()) {
            edtUser.setError("Cần nhập đủ thông tin");
            return false;
        } else {
            if (!checkChar(user)) {
                edtUser.setError("Tên đăng nhập không hợp lệ");
                return false;
            } else {
                return true;
            }
        }

    }

    private Boolean validatePassword() {
        String password = edtPassWord.getText().toString();
        String re_password = edtRe_PassWord.getText().toString();
        if (password.isEmpty() || re_password.isEmpty()) {
            edtRe_PassWord.setError("mật khẩu không hợp lệ");
            edtPassWord.setError("mật khẩu không hợp lệ");
            return false;
        }
        if (!password.equals(re_password)) {
            edtRe_PassWord.setError("mật khẩu không khớp");
            return false;
        } else {
            return true;
        }
    }

    private Boolean checkChar(String name) {
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == '.' || name.charAt(i) == '?' || name.charAt(i) == '!' || name.charAt(i) == '+'
                    || name.charAt(i) == '-' || name.charAt(i) == '$' || name.charAt(i) == '%' || name.charAt(i) == '^') {
                return false;
            }
        }
        return true;
    }

    private void SignUpEmail() {

        String email = edtUser.getText().toString().toLowerCase();
        String password = edtPassWord.getText().toString();

        if(email.isEmpty()||password.isEmpty()||edtRe_PassWord.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();

            edtUser.setError("Cần nhập đủ thông tin");
            edtPassWord.setError("Cần nhập đủ thông tin");
            edtRe_PassWord.setError("Cần nhập đủ thông tin");
        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener( (Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                CreateRealTimeFireBase();
                                LoadDataDefaultFireBase(TakeNameUser(email));
                            } else {
                                Toast.makeText(getContext(), "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                                edtUser.setError("Tài khoản đã có người sử dụng hoặc không hợp lệ");
                                edtPassWord.setError("Mật khẩu cần có 6 kí tự");
                            }
                        }
                    });
        }

    }

    private void CreateRealTimeFireBase() {
        String password = edtPassWord.getText().toString();
        String usName = "";

        usName = TakeNameUser(edtUser.getText().toString());
       // Toast.makeText(getActivity(), "" + usName, Toast.LENGTH_SHORT).show();
        String re_password = edtRe_PassWord.getText().toString();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");

        clUser clUser = new clUser(usName, password);
        reference.child(usName).child("General/info/private").setValue(clUser);

//        Intent intent = new Intent(activity_sign_up.this, activity_login.class);
//        startActivity(intent);
    }

    private String TakeNameUser(String sName) {
        String[] ArrUsername = sName.split("\\.");
        String usName = "";
        for (int i = 0; i < ArrUsername.length; i++) {
            usName += ArrUsername[i];
        }
        return usName;
    }

}