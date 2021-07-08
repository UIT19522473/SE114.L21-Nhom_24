package com.example.trelloreal.Card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.trelloreal.DataBase.clCellImage;
import com.example.trelloreal.DataBase.clImage;
import com.example.trelloreal.DataBase.clLinkInternet;
import com.example.trelloreal.DataBase.clLinkPDF;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.example.trelloreal.R.color.DarkActionBar;

public class activity_Card extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    clImage cellMain;
    saveShareprefrences pref;

    private String nameTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_card);


        pref = new saveShareprefrences(this);
        getUserName();
        AnhXa();
        setupViewPager(mViewPager);
        SwitchDarkMode();
        getNameTableFireBase();


    }

    private void SwitchDarkMode() {

        if (pref.getState() == true) {
            DarkMode();
        } else {
            LightMode();
        }
    }

    private void DarkMode() {
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.DarkSlideMenu));
        mViewPager.setBackgroundColor(getResources().getColor(R.color.DarkSlideMenu));

        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#FF5722")));
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }

    private void LightMode() {
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.LightBackGround));
        mViewPager.setBackgroundColor(getResources().getColor(R.color.LightBackGround));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void AnhXa() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
    }

    private void getUserName() {
        Intent intent = getIntent();
        cellMain = (clImage) intent.getSerializableExtra("main");

        sendDataToCardFragment();
    }

    private void sendDataToCardFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("demo", "hello");
        CardFragment cardFragment = new CardFragment();
        cardFragment.setArguments(bundle);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());

        //bundle
        Bundle bundle = new Bundle();
        //bundle.putString("user","2017-10-19");
        bundle.putSerializable("user", cellMain);

        CardFragment frag_Card = new CardFragment();
        CalendarFragment frag_Calendar = new CalendarFragment();

        frag_Card.setArguments(bundle);
        frag_Calendar.setArguments(bundle);
        adapter.addFrag(frag_Card, "Thẻ");
        adapter.addFrag(frag_Calendar, "Lịch");
        viewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(viewPager);

    }

    private void getNameTableFireBase() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + cellMain.getNameImage() +
                "/General/info/table/info/" + cellMain.getLink() + "/private/name");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                nameTable = snapshot.getValue(String.class);
                getSupportActionBar().setTitle(nameTable);
               // Toast.makeText(activity_Card.this, ""+nameTable, Toast.LENGTH_SHORT).show();
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
