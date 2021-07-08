package com.example.trelloreal.AllCard_Show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trelloreal.Card.ActivityShowEvent;
import com.example.trelloreal.Card.Adapter_Item;
import com.example.trelloreal.Card.clCard;
import com.example.trelloreal.Card.clDataCard;
import com.example.trelloreal.DataBase.clCellImage;
import com.example.trelloreal.DataBase.clImage;
import com.example.trelloreal.DataBase.clLinkInternet;
import com.example.trelloreal.DataBase.clLinkPDF;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.example.trelloreal.R.color.DarkActionBar;
import static com.example.trelloreal.R.color.DarkBackGround;

public class activity_All_Card extends AppCompatActivity implements Interface_ClickAllCard{

    RecyclerView rcvGroup;
    List<clTable_Card> listTableCard;
    Adapter_Table_Card adapterTable_Card;


    FrameLayout layout_AllCard;

    private String User = "";
    private clDataCard Card = null;

    private TextView txtMessEmptyAllCard;

    saveShareprefrences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_all_card);
        this.setTitle("Các thẻ của tôi");

        pref = new saveShareprefrences(this);

        AnhXa();
        SwitchDarkMode();
        getUserFromMain();
        loadDataFromFireBase();
    }

    private void checkSizeListData(){
        if(listTableCard.size()>0){
            txtMessEmptyAllCard.setVisibility(View.INVISIBLE);
        }else {
            txtMessEmptyAllCard.setVisibility(View.VISIBLE);
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
        layout_AllCard.setBackgroundColor(getResources().getColor(DarkBackGround));
        txtMessEmptyAllCard.setTextColor(Color.WHITE);
        //slideMenu.setBackgroundColor(getResources().getColor(R.color.DarkSlideMenu));

    }
    private void LightMode() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.LightActionBar));
        layout_AllCard.setBackgroundColor(getResources().getColor(R.color.LightBackGround));
        txtMessEmptyAllCard.setTextColor(Color.BLACK);
        //slideMenu.setBackgroundColor(getResources().getColor(R.color.LightSlideMenu));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



    private void getUserFromMain() {
        Intent intent = getIntent();
        User = intent.getStringExtra("User");
    }

    private void AnhXa() {

        txtMessEmptyAllCard = findViewById(R.id.txtMessEmptyAllCard);
        layout_AllCard = findViewById(R.id.layout_AllCard);

        rcvGroup = findViewById(R.id.rcv_group);

        rcvGroup.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity_All_Card.this, LinearLayoutManager.VERTICAL, false);
        rcvGroup.setLayoutManager(layoutManager);

        listTableCard = new ArrayList<>();

        adapterTable_Card = new Adapter_Table_Card(activity_All_Card.this,listTableCard,this,pref);

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvGroup);

        rcvGroup.setAdapter(adapterTable_Card);
        adapterTable_Card.notifyDataSetChanged();
    }

    private void loadDataFromFireBase(){
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + User +
                "/General/info/table/info");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listTableCard.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    clCellImage table = postSnapshot.child("private").getValue(clCellImage.class);
                    listTableCard.add(new clTable_Card(0,table,null));
                    for(DataSnapshot dataCard : postSnapshot.child("listcard").getChildren()){
                        clCard card = dataCard.child("data/text").getValue(clCard.class);
                        listTableCard.add(new clTable_Card(1,table,card));
                    }
                }
                adapterTable_Card.notifyDataSetChanged();
                reference.removeEventListener(this);
                checkSizeListData();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClickItemAllCard(int position) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + User +
                "/General/info/table/info/" + listTableCard.get(position).getTable().getId() + "/listcard/" + listTableCard.get(position).getCard().getId());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //listCard.clear();

                clCard text = snapshot.child("data/text").getValue(clCard.class);
                List<clLinkPDF> listPDF = new ArrayList<>();
                List<clLinkInternet> listInternet = new ArrayList<>();

                for (DataSnapshot bot : snapshot.child("data/pdf").getChildren()) {
                    listInternet.add(bot.getValue(clLinkInternet.class));
                }
                for (DataSnapshot rot : snapshot.child("data/net").getChildren()) {
                    listPDF.add(rot.getValue(clLinkPDF.class));
                }
                clImage user = new clImage(User, listTableCard.get(position).getTable().getId());
                Card = new clDataCard(text, listPDF, listInternet);

                Intent intent = new Intent(activity_All_Card.this, ActivityShowEvent.class);

                Bundle mBundle = new Bundle();
                mBundle.putSerializable("main", Card);
                mBundle.putSerializable("user", user);

                intent.putExtras(mBundle);
                startActivity(intent);
                if (Card != null) {
                    reference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void checkedBox(int position, boolean check) {

        String dataCheck = "0";
        if (check == true) {
            dataCheck = "1";
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User + "/General/info");
        reference.child("table/info/" + listTableCard.get(position).getTable().getId() + "/listcard/" + listTableCard.get(position).getCard().getId() + "/data/text/check").setValue(dataCheck);




    }

    private void ItemSearch(String charCheck) {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + User +
                "/General/info/table/info");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listTableCard.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    clCellImage table = postSnapshot.child("private").getValue(clCellImage.class);
                    listTableCard.add(new clTable_Card(0,table,null));
                    for(DataSnapshot dataCard : postSnapshot.child("listcard").getChildren()){
                        clCard card = dataCard.child("data/text").getValue(clCard.class);
                        if (card.getNameEvent().contains(charCheck)) {
                            listTableCard.add(new clTable_Card(1,table,card));
                        }
                        adapterTable_Card.notifyDataSetChanged();
                    }
                }
                adapterTable_Card.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.itemSearchCard);

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



    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(listTableCard.get(viewHolder.getAdapterPosition()).getCard()!= null){
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_All_Card.this);
                builder.setTitle("Xóa thẻ");
                builder.setMessage("Bạn có chắc xóa thẻ: " + listTableCard.get(viewHolder.getAdapterPosition()).getCard().getNameEvent());

                // add the buttons
                builder.setPositiveButton("Cancel", null);
                builder.setNegativeButton("Ok", null);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterTable_Card.notifyDataSetChanged();
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                        DatabaseReference reference = rootNode.getReference("users/" + User + "/General/info");
                        reference.child("table/info/" + listTableCard.get(viewHolder.getAdapterPosition()).getTable().getId() + "/listcard/" + listTableCard.get(viewHolder.getAdapterPosition()).getCard().getId()).setValue(null);

                        loadDataFromFireBase();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (listTableCard.get(viewHolder.getAdapterPosition()).getCard()== null) return 0;
            return super.getSwipeDirs(recyclerView, viewHolder);
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

                        Drawable icon = AppCompatResources.getDrawable(activity_All_Card.this, R.drawable.ic_delete_item);
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
}