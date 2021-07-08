package com.example.trelloreal.Notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trelloreal.Card.ActivityShowEvent;
import com.example.trelloreal.Card.Adapter_PDF;
import com.example.trelloreal.Card.activity_Card;
import com.example.trelloreal.Card.clCard;
import com.example.trelloreal.Card.clDataCard;
import com.example.trelloreal.DataBase.clImage;
import com.example.trelloreal.DataBase.clLinkInternet;
import com.example.trelloreal.DataBase.clLinkPDF;
import com.example.trelloreal.DataBase.clRequest;
import com.example.trelloreal.MainActivity;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.trelloreal.R.color.DarkActionBar;
import static com.example.trelloreal.R.color.DarkBackGround;

public class activity_Notify extends AppCompatActivity implements Interface_ClickNotify {
    private String UserName;
    private RecyclerView rcvNotify;
    private List<clNotify> ListNotify;
    Adapter_Notify adapter_notify;

    private clImage cellMain;
    private clDataCard Card = null;
    private FrameLayout layoutNotify;
    saveShareprefrences pref;

    TextView txtMessEmptyNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_notify);
        this.setTitle("Thông báo");

        getUser();
        pref = new saveShareprefrences(this);
        AnhXa();

        SwitchDarkMode();
        getDataNotify();

    }

    private void checkSizeListData(){
        if(ListNotify.size()>0){
            txtMessEmptyNotify.setVisibility(View.INVISIBLE);
        }else {
            txtMessEmptyNotify.setVisibility(View.VISIBLE);
        }
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
        layoutNotify.setBackgroundColor(getResources().getColor(DarkBackGround));
        txtMessEmptyNotify.setTextColor(Color.WHITE);

    }

    private void LightMode() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.LightActionBar));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutNotify.setBackgroundColor(getResources().getColor(R.color.LightBackGround));
        txtMessEmptyNotify.setTextColor(Color.BLACK);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void AnhXa() {

        txtMessEmptyNotify = findViewById(R.id.txtMessEmptyNotify);

        layoutNotify = findViewById(R.id.layoutNotify);
        rcvNotify = findViewById(R.id.rcvNotify);
        ListNotify = new ArrayList<>();


        rcvNotify.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(activity_Notify.this, 1);
        rcvNotify.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvNotify.setLayoutManager(layoutManager);

        adapter_notify = new Adapter_Notify(activity_Notify.this, ListNotify, R.layout.item_notify, this,pref);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvNotify);

        rcvNotify.setAdapter(adapter_notify);
        adapter_notify.notifyDataSetChanged();
    }

    private void getUser() {
        Intent intent = getIntent();
        UserName = intent.getStringExtra("User");
    }

    private void getDataNotify() {

        FirebaseDatabase rootNode2 = FirebaseDatabase.getInstance();
        DatabaseReference reference2 = rootNode2.getReference("users/" + UserName + "/General/info/notify/general");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ListNotify.clear();
                for (DataSnapshot dataStart : snapshot.child("start").getChildren()) {
                    clNotify clNotify = dataStart.getValue(com.example.trelloreal.Notify.clNotify.class);
                    ListNotify.add(clNotify);
                }
                for (DataSnapshot dataStart : snapshot.child("end").getChildren()) {
                    clNotify clNotify = dataStart.getValue(com.example.trelloreal.Notify.clNotify.class);
                    ListNotify.add(clNotify);
                }
                adapter_notify.notifyDataSetChanged();
                checkSizeListData();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void LoadDataDefault() {

    }

    @Override
    public void onClickItemNotify(int position) {


        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + UserName +
                "/General/info/table/info/" + ListNotify.get(position).getIdTable() + "/listcard/" + ListNotify.get(position).getIdCard());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //listCard.clear();

                clCard text = snapshot.child("data/text").getValue(clCard.class);
                List<clLinkPDF> listPDF = new ArrayList<>();
                List<clLinkInternet> listInternet = new ArrayList<>();

//                for (DataSnapshot bot : snapshot.child("data/pdf").getChildren()) {
//                    listInternet.add(bot.getValue(clLinkInternet.class));
//                }
//                for (DataSnapshot rot : snapshot.child("data/net").getChildren()) {
//                    listPDF.add(rot.getValue(clLinkPDF.class));
//                }
                if(text!=null){
                    clImage user = new clImage(UserName, ListNotify.get(position).getIdTable());
                    Card = new clDataCard(text, listPDF, listInternet);

                    Intent intent = new Intent(activity_Notify.this, ActivityShowEvent.class);

                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("main", Card);
                    mBundle.putSerializable("user", user);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    reference.removeEventListener(this);
                }else if(text == null){
                    Toast.makeText(activity_Notify.this, "Thẻ không tồn tại", Toast.LENGTH_SHORT).show();
                    reference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClickDeleteNotify(int position) {

    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activity_Notify.this);
            builder.setTitle("Xóa thẻ");
            builder.setMessage("Bạn có chắc xóa thông báo của thẻ: " + ListNotify.get(viewHolder.getAdapterPosition()).getNameCard()+" từ bảng "+ListNotify.get(viewHolder.getAdapterPosition()).getNameTable());

            // add the buttons
            builder.setPositiveButton("Cancel", null);
            builder.setNegativeButton("Ok", null);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter_notify.notifyDataSetChanged();
                }
            });

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                    DatabaseReference reference = rootNode.getReference("users/" + UserName +
                            "/General/info/notify/general/start/" + ListNotify.get(viewHolder.getAdapterPosition()).getIdNotify());

                    reference.setValue(null);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            final int Direc_Right = 1;
            final int Direc_Left = 0;

            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {

                int direction = dX > 0 ? Direc_Right : Direc_Left;
                int absoluteDis = Math.abs((int) dX);

                switch (direction) {
                    case Direc_Right:

                        View itemView = viewHolder.itemView;
                        ColorDrawable bg = new ColorDrawable();
                        bg.setColor(Color.argb(255, 234, 109, 53));
                        bg.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        bg.draw(c);

                        Drawable icon = AppCompatResources.getDrawable(activity_Notify.this, R.drawable.ic_delete_item);
                        int top = ((itemView.getHeight() / 2) - (icon.getIntrinsicHeight() / 2)) + itemView.getTop();
                        icon.setBounds((0 - icon.getIntrinsicWidth()) + absoluteDis, top, 0 + absoluteDis, top + icon.getIntrinsicHeight());
                        icon.draw(c);

                        break;
                    case Direc_Left:
                        break;
                }
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notify, menu);

        //MenuItem item = menu.findItem(R.id.item_Delete);
        MenuItem title = menu.findItem(R.id.title_Delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.item_Delete:
            case R.id.title_Delete:
                deleteAllNotify();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAllNotify() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + UserName +
                "/General/info/notify/general/start");

        reference.setValue(null);
        Toast.makeText(this, "Đã xóa toàn bộ thông báo", Toast.LENGTH_SHORT).show();
    }
}