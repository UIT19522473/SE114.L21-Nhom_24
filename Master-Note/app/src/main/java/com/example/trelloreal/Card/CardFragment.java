package com.example.trelloreal.Card;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
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

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.example.trelloreal.DataBase.clImage;
import com.example.trelloreal.DataBase.clLinkInternet;
import com.example.trelloreal.DataBase.clLinkPDF;
import com.example.trelloreal.MainActivity;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.trelloreal.R.color.DarkActionBar;
import static com.example.trelloreal.R.color.DarkBackGround;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment implements RecycleClickInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private clImage cellMain;
    private List<clDataCard> listCard;
    Adapter_Item adapterItem;

    TextView txtCard;
    EditText edtCard;
    //ListView lvCard;
    RecyclerView lvCard;
    LinearLayout lnNewCard;
    ImageView imgDoneCard;
    ImageView imgCloseCard;

    private Paint p = new Paint();

    private SimpleDateFormat defaultTIme = new SimpleDateFormat("dd-MM-yyyy HH:mm");


    private DatePickerDialog.OnDateSetListener dateSetListenerTimeStart;
    Calendar calStart;
    String timeStart = "";
    private int hourStart = 0, minuteStart = 0;

    private DatePickerDialog.OnDateSetListener dateSetListenerTimeEnd;
    Calendar calEnd;
    String timeEnd = "";
    private int hourEnd = 0, minuteEnd = 0;


    private Calendar getCalStart = Calendar.getInstance();

    saveShareprefrences pref;
    FrameLayout layoutCard;

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

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        setHasOptionsMenu(true);

        pref = new saveShareprefrences(getContext());
        AnhXa(view);
        SwitchDarkMode();
        getDataFromActivity();
        LoadDataFromFireBase();
        clickItemListViewCard();
        longClickItemListViewCard();

        AllViewClick();

        return view;
    }

    private void SwitchDarkMode() {

        if (pref.getState() == true) {
            DarkMode();
        } else {
            LightMode();
        }
    }

    private void DarkMode() {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(DarkActionBar)));
        layoutCard.setBackgroundColor(getResources().getColor(R.color.DarkBackGround));

        lnNewCard.setBackgroundColor(getResources().getColor(R.color.DarkSlideMenu));
        edtCard.setHintTextColor(Color.WHITE);
        edtCard.setTextColor(Color.WHITE);
        edtCard.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        imgDoneCard.setImageResource(R.drawable.ic_baseline_done_white);
        imgCloseCard.setImageResource(R.drawable.ic_baseline_close_24);

    }
    private void LightMode() {
        ActionBar bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.LightActionBar)));
        layoutCard.setBackgroundColor(getResources().getColor(R.color.LightBackGround));
        edtCard.setHintTextColor(Color.BLACK);
        edtCard.setTextColor(Color.BLACK);
        edtCard.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

        imgDoneCard.setImageResource(R.drawable.ic_baseline_done_24);
        imgCloseCard.setImageResource(R.drawable.ic_baseline_close_black);
    }


    private void AllViewClick() {
        txtCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clDataCard add = null;
                txtCard.setVisibility(View.INVISIBLE);

                lnNewCard.setVisibility(View.VISIBLE);

                //DialogEvent(add);
            }
        });

        imgCloseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnNewCard.setVisibility(View.INVISIBLE);
                txtCard.setVisibility(View.VISIBLE);
            }
        });

        imgDoneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<clLinkPDF> linkPDF = new ArrayList<>();
                List<clLinkInternet> linkInternet = new ArrayList<>();

                if (edtCard.getText().toString().isEmpty()) {
                    edtCard.setError("Bạn cần nhập tên thẻ");
                } else {
                    clCard data = new clCard("", edtCard.getText().toString(), "", "", "", "0","0","");
                    addTableFireBase(data, linkPDF, linkInternet);
                    edtCard.setText("");

                    lnNewCard.setVisibility(View.INVISIBLE);
                    txtCard.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void clickItemListViewCard() {

//        lvCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //Toast.makeText(getContext(), ""+listCard.get(position).getClCard().getNameEvent(), Toast.LENGTH_SHORT).show();
//                DialogEvent(listCard.get(position));
//            }
//        });
    }

    private void longClickItemListViewCard() {
//        lvCard.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                BottomSheetDialog bottomSheetDialog;
//                bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
//                bottomSheetDialog.setContentView(R.layout.btmdialogedit_screen);
//                bottomSheetDialog.show();
//                return true;
//            }
//        });
    }


    private void AnhXa(View view) {
        layoutCard = view.findViewById(R.id.layoutCard);

        txtCard = view.findViewById(R.id.txtAddCardFrt);
        edtCard = view.findViewById(R.id.EditCardFrt);
        lnNewCard = view.findViewById(R.id.lnAddCardFrt);
        imgDoneCard = view.findViewById(R.id.imgDoneCardFrt);
        imgCloseCard = view.findViewById(R.id.imgCloseFrt);

        lnNewCard.setVisibility(View.INVISIBLE);
        //edtCard.setVisibility(View.INVISIBLE);

        lvCard = view.findViewById(R.id.lvCardFrt);
        //-------

        lvCard.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lvCard.setLayoutManager(layoutManager);

        listCard = new ArrayList<>();

        adapterItem = new Adapter_Item(getContext(), listCard, R.layout.item_card, this,pref);

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(lvCard);

        lvCard.setAdapter(adapterItem);
        adapterItem.notifyDataSetChanged();
    }

    private void getDataFromActivity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            cellMain = (clImage) bundle.get("user");
        } else {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadDataFromFireBase() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + cellMain.getNameImage() +
                "/General/info/table/info/" + cellMain.getLink() + "/listcard");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listCard.clear();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    clCard text = postSnapshot.child("data/text").getValue(clCard.class);
                    List<clLinkPDF> listPDF = new ArrayList<>();
                    List<clLinkInternet> listInternet = new ArrayList<>();

                    listCard.add(new clDataCard(text, listPDF, listInternet));
                }
                adapterItem.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void addTableFireBase(clCard data, List<clLinkPDF> clLinkPDFS, List<clLinkInternet> clLinkInternets) {
        String id = "";

        if (data.getId().isEmpty()) {
            Date date = new Date();
            long timeMilli = date.getTime();
            id = String.valueOf(timeMilli);
            data.setId(id);
        } else {
            id = data.getId();
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + cellMain.getNameImage() + "/General/info");
        reference.child("table/info/" + cellMain.getLink() + "/listcard/" + id + "/data/text").setValue(data);

        for (int i = 0; i < clLinkPDFS.size(); i++) {
            reference.child("table/info/" + cellMain.getLink() + "/listcard/" + id + "/data/pdf").push().setValue(clLinkPDFS.get(i));
        }

        for (int j = 0; j < clLinkInternets.size(); j++) {
            reference.child("table/info/" + cellMain.getLink() + "/listcard/" + id + "/data/net").push().setValue(clLinkPDFS.get(j));
        }

    }

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
        //Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();

        String dataCheck = "0";
        if (check == true) {
            dataCheck = "1";
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + cellMain.getNameImage() + "/General/info");
        reference.child("table/info/" + cellMain.getLink() + "/listcard/" + listCard.get(position).getClCard().getId() + "/data/text/check").setValue(dataCheck);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
        super.onCreateOptionsMenu(menu,inflater);
    }


    private void ItemSearch(String charCheck) {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users/" + cellMain.getNameImage() +
                "/General/info/table/info/" + cellMain.getLink() + "/listcard");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listCard.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    clCard text = postSnapshot.child("data/text").getValue(clCard.class);
                    if (text.getNameEvent().contains(charCheck)) {
                        List<clLinkPDF> listPDF = new ArrayList<>();
                        List<clLinkInternet> listInternet = new ArrayList<>();

//                        for (DataSnapshot bot : postSnapshot.child("data/pdf").getChildren()) {
//                            listInternet.add(bot.getValue(clLinkInternet.class));
//                        }
//                        for (DataSnapshot rot : postSnapshot.child("data/net").getChildren()) {
//                            listPDF.add(rot.getValue(clLinkPDF.class));
//                        }

                        listCard.add(new clDataCard(text, listPDF, listInternet));
                        adapterItem.notifyDataSetChanged();
                    }else {
                        adapterItem.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}