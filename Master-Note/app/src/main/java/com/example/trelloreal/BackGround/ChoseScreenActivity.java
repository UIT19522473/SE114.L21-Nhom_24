package com.example.trelloreal.BackGround;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trelloreal.DataBase.clCellImage;
import com.example.trelloreal.DataBase.clImage;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.trelloreal.R.color.DarkActionBar;
import static com.example.trelloreal.R.color.DarkBackGround;

public class ChoseScreenActivity extends AppCompatActivity {
    Context context;
    GridView gridView;
    ImageButton imageButton;

    //ArrayList<clImage> ListImage;
    Adapter_BackGround gridAdapter;
    private Uri mImageUri;
    private String uriSupper;

    private String UserName;
    List<clImage> arrayImage;
    List<clCellImage> cellImageList;

    saveShareprefrences pref;
    LinearLayout layoutBackGround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_chose_screen);

        this.setTitle("Hình ảnh nền");

        getUserName();
        pref = new saveShareprefrences(this);
        Intialize();
        SwitchDarkMode();
        LoadDataScreen();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
                //Toast.makeText(ChoseScreenActivity.this, ""+cellImageList.size(), Toast.LENGTH_SHORT).show();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("screen", (Serializable) cellImageList.get(position));
                setResult(RESULT_OK, resultIntent);
                ChoseScreenActivity.this.finish();
            }
        });

        gridviewLongClick();

    }
    private void SwitchDarkMode() {

        if (pref.getState() == true) {
            DarkMode();
        } else {
            LightMode();
        }
    }

    private void DarkMode() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(DarkActionBar));
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutBackGround.setBackgroundColor(getResources().getColor(DarkBackGround));

    }

    private void LightMode() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.LightActionBar));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutBackGround.setBackgroundColor(getResources().getColor(R.color.LightBackGround));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void getUserName() {
        Intent intent = getIntent();
        UserName = intent.getStringExtra("nameUser");
        //Toast.makeText(this, "" + UserName, Toast.LENGTH_SHORT).show();
    }

    private void LoadDataScreen() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + UserName + "/General/image");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                arrayImage.clear();
                cellImageList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(activity_login.this, ""+postSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    clImage clImage = postSnapshot.getValue(clImage.class);
                    arrayImage.add(postSnapshot.getValue(clImage.class));
                    //cellImageList.add(new clCellImage(snapshot.getKey().toString(),clImage.getNameImage(),clImage.getLink()));
                    cellImageList.add(new clCellImage(postSnapshot.getKey().toString(), clImage.getNameImage(), clImage.getLink()));
                    gridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void Intialize() {

        layoutBackGround = findViewById(R.id.layoutBackGround);
        gridView = findViewById(R.id.grvChoseScreen);
        imageButton = findViewById(R.id.imgBtnAddChoseScreen);
        arrayImage = new ArrayList<>();
        cellImageList = new ArrayList<>();
        gridAdapter = new Adapter_BackGround(ChoseScreenActivity.this, R.layout.cellback_ground, arrayImage);
        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
    }

    private void LoadImageForScreen() {
        gridAdapter = new Adapter_BackGround(this, R.layout.cell_grid, arrayImage);
        gridView.setAdapter(gridAdapter);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(intent, 113);
    }

    private void UploadFileToFireBase() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();

        Calendar calendar = Calendar.getInstance();
        String name = String.valueOf(calendar.getTimeInMillis());
        // Get the data from an ImageView as bytes
        //StorageReference mountainsRef = storageRef.child("Image/"+"image"+calendar.getTimeInMillis()+".png");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference().child(UserName + "/image/" + name);
        uploader.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ChoseScreenActivity.this, ""+taskSnapshot.getStorage().getDownloadUrl(), Toast.LENGTH_SHORT).show();
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                                DatabaseReference reference = rootNode.getReference("users/" + UserName + "/General/image");
                                reference.child(name).setValue(new clImage(name, String.valueOf(uri)));
                                //onBackPressed();
                                // Toast.makeText(ChoseScreenActivity.this, ""+uri, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        dialog.setMessage("Uploaded :" + (int) percent + "%");
                    }
                });
    }

    private Activity getActivity() {
        return ChoseScreenActivity.this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 113 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            uriSupper = mImageUri.toString().trim();

            UploadFileToFireBase();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void gridviewLongClick() {

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                BottomSheetDialog bottomSheetDialog;
                bottomSheetDialog = new BottomSheetDialog(ChoseScreenActivity.this, R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.btmdialogedit_screen);

                ImageView imgOut = bottomSheetDialog.findViewById(R.id.imgScreenDlOut);
                TextView txtName = bottomSheetDialog.findViewById(R.id.txtScreenDlName);
                TextView txtEdit = bottomSheetDialog.findViewById(R.id.txtScreenDlEdit);
                TextView txtDelete = bottomSheetDialog.findViewById(R.id.txtScreenDlDelete);
                TextView txtExit = bottomSheetDialog.findViewById(R.id.txtScreenDlExit);
                bottomSheetDialog.show();


                txtName.setText(cellImageList.get(i).getName());
                txtEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //DialogAddEvent(cellImageList.get(position));
//                        LinearLayout layout = new LinearLayout(ChoseScreenActivity.this);
//                        layout.setOrientation(LinearLayout.VERTICAL);
//
//
                        AlertDialog.Builder alert = new AlertDialog.Builder(ChoseScreenActivity.this);
                        final EditText edittext = new EditText(ChoseScreenActivity.this);
                        edittext.setHint("Nhập tên");

                        //alert.setMessage("Nhập tên");
                        alert.setTitle("Nhập mới cho :" + cellImageList.get(i).getName());

                        alert.setView(edittext);


                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                                DatabaseReference reference = rootNode.getReference("users/" + UserName + "/General/image/");
                                reference.child(cellImageList.get(i).getId()).setValue(new clImage(edittext.getText().toString(), cellImageList.get(i).getLink()));

                                //Toast.makeText(ChoseScreenActivity.this, ""+cellImageList.get(i).getId(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // what ever you want to do with No option.
                            }
                        });

                        alert.show();
                        bottomSheetDialog.dismiss();
                    }
                });

                txtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ChoseScreenActivity.this);
                        builder.setTitle("Xóa bảng");
                        builder.setMessage("Bạn có chắc xóa bảng: " + cellImageList.get(i).getName());

                        // add the buttons
//                        builder.setPositiveButton("Cancel", null);
//                        builder.setNegativeButton("Ok", null);

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(ChoseScreenActivity.this, "", Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                                DatabaseReference reference = rootNode.getReference("users/" + UserName + "/General/image/");


                                // reference.child(cellImageList.get(i).getId()).setValue(null);

                                // FirebaseStorage storage = FirebaseStorage.getInstance();

                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReference();
                                StorageReference desertRef = storageRef.child(UserName + "/image/" + cellImageList.get(i).getId());

                                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // File deleted successfully
                                        reference.child(cellImageList.get(i).getId()).setValue(null);
                                        Toast.makeText(ChoseScreenActivity.this, "Đã xóa hình", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Uh-oh, an error occurred!
                                    }
                                });
                                //StorageReference imageRef = storage.getReferenceFromUrl(cellImageList.get(i).getLink());

                                //StorageReference imageRef = storage.getReferenceFromUrl(cellImageList.get(i).getLink().trim());
//                                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(ChoseScreenActivity.this, "Đã xóa hình", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                                //Toast.makeText(ChoseScreenActivity.this, ""+cellImageList.get(i).getLink(), Toast.LENGTH_SHORT).show();
//                                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        reference.child(cellImageList.get(i).getId()).setValue(null);
//                                        Toast.makeText(ChoseScreenActivity.this, "Đã xóa hình", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
                                bottomSheetDialog.dismiss();
                            }
                        });
                        // create and show the alert dialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                imgOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                txtExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });

                return true;
            }
        });
    }
}