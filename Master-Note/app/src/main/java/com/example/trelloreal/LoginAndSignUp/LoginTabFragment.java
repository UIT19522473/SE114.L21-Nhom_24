package com.example.trelloreal.LoginAndSignUp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trelloreal.MainActivity;
import com.example.trelloreal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginTabFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginTabFragment.
     */
    private static final String FILE_NAME = "myFile";
    EditText edtUser, edtPassWord;
    TextView txtResetPass;
    Button imgBtnSignIn;
    CheckBox ckbRememberPass;

    boolean checkCache = false;
    SharedPreferences preferences;
    private FirebaseAuth mAuth;

    // TODO: Rename and change types and number of parameters
    public static LoginTabFragment newInstance(String param1, String param2) {
        LoginTabFragment fragment = new LoginTabFragment();
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
        View v = inflater.inflate(R.layout.fragment_login_tab, container, false);

        mAuth = FirebaseAuth.getInstance();
        edtUser = v.findViewById(R.id.edtUserLI);
        edtPassWord = v.findViewById(R.id.edtPassWordLI);
        imgBtnSignIn = v.findViewById(R.id.imgBtnSignInLI);
        txtResetPass = v.findViewById(R.id.txtResetPass);
        ckbRememberPass = v.findViewById(R.id.ckbRememberPass);

        Intent intent = getActivity().getIntent();
        checkCache = intent.getBooleanExtra("checkCache",false);
        ckbRememberPass.setChecked(checkCache);

        preferences =  getActivity().getSharedPreferences(FILE_NAME, getActivity().MODE_PRIVATE);
        String username = preferences.getString("usernameRemember", "");
        String password = preferences.getString("PassWordRemember", "");
        String checkSave = preferences.getString("checkSave","false");

        if(checkSave.equals("true")){
            if (!username.isEmpty() && !password.isEmpty() && username != null && password != null) {
                ckbRememberPass.setChecked(true);
                SignInEmailAuto(username, password);
            }
        }else if(checkSave.equals("false")){
            password = "";
            ckbRememberPass.setChecked(false);
        }

        edtUser.setText(username);
        edtPassWord.setText(password);

        imgBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInEmail();
            }
        });

        txtResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passDialog = new AlertDialog.Builder(v.getContext());
                passDialog.setTitle("Reset mậu khẩu");
                passDialog.setMessage("Nhập email cần reset mật khẩu");
                passDialog.setView(resetMail);

                passDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Link reset đã gửi vô email của bạn", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(getContext(), "Lỗi! reset link không được gửi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                passDialog.create().show();
            }
        });

        return v;
    }

    private void SignInEmailAuto(String emailIn, String passwordIn) {
        String email = emailIn.toLowerCase();
        String password = passwordIn.toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( (Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                            IntentMainActivity(TakeNameUser(email));

                        } else {
                            Toast.makeText(getContext(), "dang nhap thất bại", Toast.LENGTH_SHORT).show();
                            edtUser.setError("thông tin tài khoản không chính xác");
                            edtPassWord.setError("thông tin mật khẩu không chính xác");
                        }
                    }
                });
    }

    private void SignInEmail() {
        String email = edtUser.getText().toString().toLowerCase();
        String password = edtPassWord.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            edtUser.setError("Bạn cần nhập đủ thông tin");
            edtPassWord.setError("Bạn cần nhập đủ thông tin");
            Toast.makeText(getContext(), "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( (Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                saveAccount(TakeNameUser(email), email, password);
                            } else {
                                Toast.makeText(getContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                edtUser.setError("thông tin tài khoản không chính xác");
                                edtPassWord.setError("thông tin mật khẩu không chính xác");
                            }

                        }
                    });
        }

    }

    private void saveAccount(String UserMain, String UserLogin, String PassWordLogin) {
        if (ckbRememberPass.isChecked()) {
            StoredDataUsingHaredPref(UserMain, UserLogin, PassWordLogin);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } else if (!ckbRememberPass.isChecked()) {
            StoredDataUsingHaredPref("", "", "");
            IntentMainActivity(UserMain);
        }

    }

    private void StoredDataUsingHaredPref(String user, String UserLogin, String PassWordLogin) {
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences(FILE_NAME, getActivity().MODE_PRIVATE).edit();
        editor.putString("username", user);
        editor.putString("usernameRemember", UserLogin);
        editor.putString("PassWordRemember", PassWordLogin);
        editor.putString("checkSave", "true");
        editor.apply();
    }

    private void IntentMainActivity(String nameUser) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("nameUser", nameUser);
        startActivity(intent);
    }

    private String TakeNameUser(String sName) {
        //String password = edtPassWord.getText().toString();
        String[] ArrUsername = sName.split("\\.");
        String usName = "";
        for (int i = 0; i < ArrUsername.length; i++) {
            usName += ArrUsername[i];
        }
        return usName;
    }
}