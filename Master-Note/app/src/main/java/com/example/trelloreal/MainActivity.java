package com.example.trelloreal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.example.trelloreal.AllCard_Show.activity_All_Card;
import com.example.trelloreal.BackGround.ChoseScreenActivity;
import com.example.trelloreal.Card.activity_Card;
import com.example.trelloreal.DataBase.clCellImage;
import com.example.trelloreal.DataBase.clImage;
import com.example.trelloreal.LoginAndSignUp.activity_login;
import com.example.trelloreal.Notify.activity_Notify;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.trelloreal.R.color.DarkActionBar;
import static com.example.trelloreal.R.color.DarkBackGround;
import static com.example.trelloreal.R.color.color1;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private TextView txtCard, txtBackGround, txtDarkMode, txtSignOut;
    private Switch swDarkMode;

    private static final String FILE_NAME = "myFile";
    GridView gridMain;
    List<clImage> ListCell;
    List<clCellImage> cellImageList;
    private clCellImage result;
    GridAdapter gridAdapter;

    ImageButton imgBtnAdd;

    ImageView imgDialog;
    Button btnOkDialog;
    EditText edtDialog;
    TextView txtLogOut;

    int getColor = 0;

    private String UserName;
    saveShareprefrences pref;
    private LinearLayout layout_main;
    private LinearLayout slideMenu;
    ImageView imgDarkMode,imgLogOut,imgBackGround,imgCard;
    TextView txtMessEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        this.setTitle("Bảng công việc");

        drawerLayout = findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        pref = new saveShareprefrences(this);

        getUserName();
        AnhXa();
        SwitchDarkMode();
        LoadData();
        clickDrawLayout();

        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddEvent(new clCellImage("", "", ""));
            }
        });

        LongClickItemGrvMain();
        ClickItemGrvMain();
    }


    private void checkSizeListData(){
        if(cellImageList.size()>0){
            txtMessEmpty.setVisibility(View.INVISIBLE);
        }else {
            txtMessEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void SwitchDarkMode() {

        if (pref.getState() == true) {
            swDarkMode.setChecked(true);
            DarkMode();
        } else {
            swDarkMode.setChecked(false);
            LightMode();
        }
        swDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pref.setState(true);
                    DarkMode();

                } else {
                    pref.setState(false);
                    LightMode();
                    layout_main.setBackgroundColor(Color.WHITE);
                }
            }
        });
    }

    private void DarkMode() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(DarkActionBar));
        layout_main.setBackgroundColor(getResources().getColor(DarkBackGround));
        slideMenu.setBackgroundColor(getResources().getColor(R.color.DarkSlideMenu));
        txtCard.setTextColor(Color.WHITE);
        txtBackGround.setTextColor(Color.WHITE);
        txtDarkMode.setTextColor(Color.WHITE);
        txtSignOut.setTextColor(Color.WHITE);

        imgDarkMode.setImageResource(R.drawable.darkmode_dark);
        imgLogOut.setImageResource(R.drawable.logout_dark);
        imgBackGround.setImageResource(R.drawable.ic_baseline_image_dark);
        imgCard.setImageResource(R.drawable.ic_baseline_credit_card_dark);
        txtMessEmpty.setTextColor(Color.WHITE);

    }

    private void LightMode() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.LightActionBar));
        layout_main.setBackgroundColor(getResources().getColor(R.color.LightBackGround));
        slideMenu.setBackgroundColor(getResources().getColor(R.color.LightSlideMenu));

        txtCard.setTextColor(Color.BLACK);
        txtBackGround.setTextColor(Color.BLACK);
        txtDarkMode.setTextColor(Color.BLACK);
        txtSignOut.setTextColor(Color.BLACK);

        imgDarkMode.setImageResource(R.drawable.darkmode);
        imgLogOut.setImageResource(R.drawable.logout);
        imgBackGround.setImageResource(R.drawable.ic_baseline_image_24);
        imgCard.setImageResource(R.drawable.ic_baseline_credit_card_24);

        txtMessEmpty.setTextColor(Color.BLACK);
    }

    private void clickDrawLayout() {

        txtCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_All_Card.class);
                intent.putExtra("User", UserName);
                startActivity(intent);
            }
        });

        txtBackGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ChoseScreenActivity.class);
                i.putExtra("nameUser", UserName);
                startActivity(i);
            }
        });

        txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("checkSave", "false");
                editor.apply();

                Intent intent = new Intent(MainActivity.this, activity_login.class);
                intent.putExtra("checkCache", false);
                startActivity(intent);
                finish();
            }
        });
    }


    private void ClickItemGrvMain() {
        gridMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clImage cellMain = new clImage(UserName, cellImageList.get(position).getId());
                Intent intent = new Intent(MainActivity.this, activity_Card.class);
                intent.putExtra("main", (Serializable) cellMain);
                startActivity(intent);
            }
        });
    }

    private void LongClickItemGrvMain() {
        gridMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BottomSheetDialog bottomSheetDialog;
                bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.btmdialogedit);

                ImageView imgOut = bottomSheetDialog.findViewById(R.id.imgBtmDlOut);
                TextView txtName = bottomSheetDialog.findViewById(R.id.txtBtmDlName);
                TextView txtEdit = bottomSheetDialog.findViewById(R.id.txtBtmDlEdit);
                TextView txtDelete = bottomSheetDialog.findViewById(R.id.txtBtmDlDelete);
                TextView txtExit = bottomSheetDialog.findViewById(R.id.txtBtmDlExit);
                bottomSheetDialog.show();

                txtName.setText(cellImageList.get(position).getName());
                txtEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogAddEvent(cellImageList.get(position));
                        bottomSheetDialog.dismiss();
                    }
                });
                txtDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Xóa bảng");
                        builder.setMessage("Bạn có chắc xóa bảng: " + cellImageList.get(position).getName());

                        // add the buttons
                        builder.setPositiveButton("Cancel", null);
                        builder.setNegativeButton("Ok", null);

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                                DatabaseReference reference = rootNode.getReference("users/" + UserName + "/General/info/table/info");
                                reference.child(String.valueOf(cellImageList.get(position).getId())).setValue(null);
                                gridAdapter.notifyDataSetChanged();

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

    private void LoadData() {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + UserName + "/General/info/table/info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                cellImageList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    clCellImage cell = postSnapshot.child("private").getValue(clCellImage.class);
                    cellImageList.add(cell);
                }
                gridAdapter.notifyDataSetChanged();
                checkSizeListData();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return;
    }

    private void AnhXa() {

        layout_main = findViewById(R.id.layout_main);
        slideMenu = findViewById(R.id.slideMenu);
        imgDarkMode = findViewById(R.id.imgDarkMode);
        imgLogOut = findViewById(R.id.imgLogOut);
        imgBackGround = findViewById(R.id.imgBackGround);
        imgCard = findViewById(R.id.imgCard);

        txtMessEmpty = findViewById(R.id.txtMessEmpty);

        imgBtnAdd = (ImageButton) findViewById(R.id.imgBtnAdd);
        gridMain = (GridView) findViewById(R.id.grvMain);
        cellImageList = new ArrayList<>();


        txtCard = findViewById(R.id.txtCard);
        txtBackGround = findViewById(R.id.txtBackGround);
        txtDarkMode = findViewById(R.id.txtDarkMode);
        txtSignOut = findViewById(R.id.txtSignOut);
        swDarkMode = findViewById(R.id.swDarkMode);

        //ListCell = new ArrayList<>();
        gridAdapter = new GridAdapter(MainActivity.this, R.layout.cell_grid, cellImageList);
        gridMain.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();
    }


    private void DialogAddEvent(clCellImage cell) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_new_cell);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        edtDialog = dialog.findViewById(R.id.edtDialog);
        imgDialog = dialog.findViewById(R.id.imgAddTable);
        btnOkDialog = dialog.findViewById(R.id.btnOkDialog);
        imgDialog.clearColorFilter();

        edtDialog.setText(cell.getName());
        Glide.with(MainActivity.this).load(cell.getLink()).into(imgDialog);
        imgDialog.setScaleType(ImageView.ScaleType.FIT_XY);

        imgDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dialog.getContext(), ChoseScreenActivity.class);
                i.putExtra("nameUser", UserName);
                getActivity().startActivityForResult(i, 1);
            }

            private Activity getActivity() {
                return MainActivity.this;
            }
        });
        btnOkDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = edtDialog.getText().toString();
                if (s.isEmpty()) {
                    Toast.makeText(MainActivity.this, "ban can nhap du thong tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (result != null && cell.getId().isEmpty()) {
                        Date date = new Date();
                        long timeMilli = date.getTime();
                        addTableFireBase(new clCellImage(String.valueOf(timeMilli), edtDialog.getText().toString(), result.getLink()));
                        result = null;
                    } else if (result != null && !(cell.getId().isEmpty())) {
                        clCellImage newCell = new clCellImage(cell.getId(), edtDialog.getText().toString(), result.getLink());
                        addTableFireBase(newCell);
                        result = null;
                    } else if (cell.getId().isEmpty() && result == null) {
                        Date date = new Date();
                        long timeMilli = date.getTime();
                        addTableFireBase(new clCellImage(String.valueOf(timeMilli), edtDialog.getText().toString(), "https://firebasestorage.googleapis.com/v0/b/list-note-8512d.appspot.com/o/Image%2Fno_img.png?alt=media&token=cc736e93-cd5c-4cb7-b85c-4087ea6f573c"));
                        result = null;
                    } else {
                        clCellImage newCell = new clCellImage(cell.getId(), edtDialog.getText().toString(), cell.getLink());
                        addTableFireBase(newCell);
                    }
                    dialog.dismiss();
                }
            }
        });
    }

    private void addTableFireBase(/*clImage data*/clCellImage data/*,long key*/) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + UserName + "/General/info");
        reference.child("table/info/" + data.getId() + "/private").setValue(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //result =(clCellImage)data.getSerializableExtra("background");
                result = (clCellImage) data.getSerializableExtra("screen");
                Glide.with(this).load(result.getLink().trim()).into(imgDialog);
            }
        }
    }

    private void getUserName() {
        UserName = "";
        Intent intent = getIntent();
        UserName = intent.getStringExtra("nameUser");


        if (UserName == null) {
            SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
            UserName = sharedPreferences.getString("username", "null");
        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.itemSearch);
        MenuItem Notify = menu.findItem(R.id.itemNotify);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ItemSearch(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.itemNotify:
                startActivityNotify();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startActivityNotify() {
        Intent intent = new Intent(MainActivity.this, activity_Notify.class);
        intent.putExtra("User", UserName);
        startActivity(intent);
    }

    private void ItemSearch(String check) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + UserName + "/General/info/table/info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                cellImageList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    clCellImage cell = postSnapshot.child("private").getValue(clCellImage.class);
                    if (cell.getName().toString().toLowerCase().contains(check.toLowerCase())) {
                        cellImageList.add(cell);
                        gridAdapter.notifyDataSetChanged();
                    } else {
                        gridAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return;
    }


}