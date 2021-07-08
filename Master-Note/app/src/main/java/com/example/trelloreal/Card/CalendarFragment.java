package com.example.trelloreal.Card;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.example.trelloreal.R.color.DarkActionBar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements RecycleClickInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //private SimpleDateFormat defaultTIme = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private SimpleDateFormat defaultTIme = new SimpleDateFormat("MM-yyyy");

    private clImage cellMain;
    private CalendarView cldViewFrt;
    private RecyclerView rcViewFrt;

    private List<clDataCard> listCard;
    Adapter_Item adapterItem;
    List<EventDay> events;

    saveShareprefrences pref;
    FrameLayout layoutCalendar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        setHasOptionsMenu(true);

        getDataFromActivity();
        pref = new saveShareprefrences(getContext());

        AnhXa(view);
        SwitchDarkMode();
        showEventByDate();
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_today, menu);

       // MenuItem item = menu.findItem(R.id.item_Today);

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.title_Today:
                Today();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Today(){
        try {
            cldViewFrt.setDate(Calendar.getInstance().getTime());
            Toast.makeText(getContext(), "today", Toast.LENGTH_SHORT).show();
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
    }

    private void SwitchDarkMode() {

        if (pref.getState() == true) {
            DarkMode();
        } else {
            LightMode();
        }
    }

    @SuppressLint("ResourceType")
    private void DarkMode() {
        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(DarkActionBar)));
        layoutCalendar.setBackgroundColor(getResources().getColor(R.color.DarkBackGround));

        cldViewFrt.setHeaderColor(getResources().getColor(DarkActionBar));

    }

    @SuppressLint("ResourceType")
    private void LightMode() {
        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.LightActionBar)));
        layoutCalendar.setBackgroundColor(getResources().getColor(R.color.LightBackGround));
        cldViewFrt.setHeaderColor(getResources().getColor(R.color.LightActionBar));
    }

    private void AnhXa(View view) {
        layoutCalendar = view.findViewById(R.id.layoutCalendar);
        cldViewFrt = view.findViewById(R.id.cldCalendarFrt);
        rcViewFrt = view.findViewById(R.id.rcvCalendarFrt);
        events = new ArrayList<>();

        rcViewFrt.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcViewFrt.setLayoutManager(layoutManager);


        listCard = new ArrayList<>();
        adapterItem = new Adapter_Item(getContext(), listCard, R.layout.item_card, CalendarFragment.this, pref);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcViewFrt);
        rcViewFrt.setAdapter(adapterItem);
        adapterItem.notifyDataSetChanged();
    }


    private void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            cellMain = (clImage) bundle.get("user");
        } else {
            Toast.makeText(getActivity(), "khong co j", Toast.LENGTH_SHORT).show();
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Xóa thẻ");
            builder.setMessage("Bạn có chắc xóa thẻ: " + listCard.get(viewHolder.getAdapterPosition()).getClCard().getNameEvent());

            // add the buttons
            builder.setPositiveButton("Cancel", null);
            builder.setNegativeButton("Ok", null);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapterItem.notifyDataSetChanged();
                }
            });

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
                    DatabaseReference reference = rootNode.getReference("users/" + cellMain.getNameImage() + "/General/info");
                    reference.child("table/info/" + cellMain.getLink() + "/listcard/" + listCard.get(viewHolder.getAdapterPosition()).getClCard().getId()).setValue(null);
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

                        Drawable icon = AppCompatResources.getDrawable(getContext(), R.drawable.ic_delete_item);
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
    public void onItemClick(int position) {

        Intent intent = new Intent(getActivity(), ActivityShowEvent.class);

        clDataCard clDataCard = listCard.get(position);
        clImage user = cellMain;

        Bundle mBundle = new Bundle();
        mBundle.putSerializable("main", clDataCard);
        mBundle.putSerializable("user", user);

        intent.putExtras(mBundle);

        startActivity(intent);
    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void swipeItem(int position) {
        // Toast.makeText(getContext(), "delete: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkedBox(int position, boolean check) {

        String dataCheck = "0";
        if (check == true) {
            dataCheck = "1";
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + cellMain.getNameImage() + "/General/info");
        reference.child("table/info/" + cellMain.getLink() + "/listcard/" + listCard.get(position).getClCard().getId() + "/data/text/check").setValue(dataCheck);
    }


    private void LoadDataFromFireBase(String charCheck) {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + cellMain.getNameImage() +
                "/General/info/table/info/" + cellMain.getLink() + "/listcard");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listCard.clear();
                events.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    clCard text = postSnapshot.child("data/text").getValue(clCard.class);
                    if (text.getStartEvent().contains(charCheck)) {

                        List<clLinkPDF> listPDF = new ArrayList<>();
                        List<clLinkInternet> listInternet = new ArrayList<>();

                        for (DataSnapshot bot : postSnapshot.child("data/pdf").getChildren()) {
                            listInternet.add(bot.getValue(clLinkInternet.class));
                        }
                        for (DataSnapshot rot : postSnapshot.child("data/net").getChildren()) {
                            listPDF.add(rot.getValue(clLinkPDF.class));
                        }

                        String[] check = LoadNoteEventByDate(text.getStartEvent().toString());
                        Calendar calendar = Calendar.getInstance();
                        Calendar calndr1 = (Calendar) Calendar.getInstance();
                        calndr1.set(Integer.parseInt(check[2]), Integer.parseInt(check[1]) - 1, Integer.parseInt(check[0]));
                        Date dt = calndr1.getTime();
                        calendar.setTime(dt);
                        events.add(new EventDay(calendar, R.drawable.iccircle));
                        listCard.add(new clDataCard(text, listPDF, listInternet));
                        // adapterItem.notifyDataSetChanged();
                    }
                }
                adapterItem.notifyDataSetChanged();
                cldViewFrt.setEvents(events);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void showEventByDate() /*throws OutOfDateRangeException*/ {

        Calendar cal = cldViewFrt.getCurrentPageDate();
        cal.add(Calendar.DATE, 1);
        Date date = cal.getTime();
        String s = defaultTIme.format(date);

        String[] checkString = s.split("(-)");
        int Month = Integer.parseInt(checkString[0]);
        int Year = Integer.parseInt(checkString[1]);
        LoadDataFromFireBase("" + Month + "-" + Year);
        cldViewFrt.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                listCard.clear();
                events.clear();
                adapterItem.notifyDataSetChanged();

                Calendar cal = cldViewFrt.getCurrentPageDate();
                cal.add(Calendar.DATE, 1);
                Date date = cal.getTime();

                String s1 = defaultTIme.format(date);
                String[] checkString = s1.split("(-)");
                int Month = Integer.parseInt(checkString[0]);
                int Year = Integer.parseInt(checkString[1]);
                LoadDataFromFireBase("" + Month + "-" + Year);

            }
        });

        cldViewFrt.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                listCard.clear();
                events.clear();
                adapterItem.notifyDataSetChanged();

                Calendar cal = cldViewFrt.getCurrentPageDate();
                cal.add(Calendar.DATE, 1);
                Date date = cal.getTime();

                String s2 = defaultTIme.format(date);
                String[] checkString = s2.split("(-)");
                int Month = Integer.parseInt(checkString[0]);
                int Year = Integer.parseInt(checkString[1]);
                LoadDataFromFireBase("" + Month + "-" + Year);
            }
        });


        cldViewFrt.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                for (EventDay ev : events) {
                    if (eventDay == ev) {
                        Toast.makeText(getContext(), "Bạn có sự kiện", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Bạn không có thẻ nào hết hạn ngày " + eventDay.getCalendar().get(Calendar.DATE) + " tháng " + eventDay.getCalendar().get(Calendar.MONTH), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String[] LoadNoteEventByDate(String time) {
        String x = "Từ.25-6-2021 00:00\nĐến.25-6-2021 00:00";
        //List<EventDay> events = new ArrayList<>();
        String[] strDateEnd = time.split("(\n)");
        String[] strSlitEndDate = strDateEnd[1].split("[.]");
        String[] strSplitFinal = strSlitEndDate[1].split(" ");
//
        String[] DayMonthYear = strSplitFinal[0].split("(-)");
        return DayMonthYear;
    }
}