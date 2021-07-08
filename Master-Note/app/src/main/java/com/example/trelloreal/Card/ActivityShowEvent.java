package com.example.trelloreal.Card;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.trelloreal.Notify.Reminder;
import com.example.trelloreal.DataBase.clImage;
import com.example.trelloreal.DataBase.clLinkInternet;
import com.example.trelloreal.DataBase.clLinkPDF;
import com.example.trelloreal.DataBase.clRequest;
import com.example.trelloreal.DataBase.clTable;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
import static com.example.trelloreal.R.color.DarkSlideMenu;

public class ActivityShowEvent extends AppCompatActivity implements Interface_ClickItemPDF, Interface_clickInternet {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    private clRequest clRes;

    private clDataCard cellMain;
    private clImage User;
    String s;

    private SimpleDateFormat defaultTIme = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    private DatePickerDialog.OnDateSetListener dateSetListenerTimeStart;
    Calendar calStart;
    String timeStart = "";
    private int hourStart = 0, minuteStart = 0;

    private DatePickerDialog.OnDateSetListener dateSetListenerTimeEnd;
    Calendar calEnd;
    String timeEnd = "";
    private int hourEnd = 0, minuteEnd = 0;


    List<clLinkPDF> listPDF;
    List<clLinkInternet> listInternet;


    ImageView imgClose;
    ImageView imgDone;
    Switch swNotify;
    EditText edtNameEvent;
    EditText edtDec;

    TextView txtStartEvent;
    ImageView imgClear;


    ImageView txtAddFile;
    RecyclerView lvAddFile;

    ImageView txtAddLink;
    RecyclerView lvAddLink;

    Adapter_PDF adapter_pdf;
    Adapter_Internet adapter_internet;


    private clTable cacheTable;
    private clCard cacheCard;

    LinearLayout lnShowEvent;
    LinearLayout lnTitleTop;
    LinearLayout lnChoseDate;
    LinearLayout lnAddFile;
    LinearLayout lnAddLink;

    ImageView imgTime, imgAttachFile, imgAttachLink;


    TextView txtAttachFile;
    TextView txtAttachLink;

    saveShareprefrences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        createNotificationChanel();
        this.setTitle("Sự kiện");

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDataFromCardFrg();

        pref = new saveShareprefrences(this);
        LoadDataDefaultRes();
        LoadTableAndCardFireBase();

        AnhXa();

        SwitchDarkMode();

        LoadDataFromFireBase();
        demo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
       // Toast.makeText(ActivityShowEvent.this,"Thanks for using application!!",Toast.LENGTH_LONG).show();

        String nameEvent = edtNameEvent.getText().toString();
        String description = edtDec.getText().toString();
        String startEvent = txtStartEvent.getText().toString();
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
        reference3.child("clRequest").setValue(new clRequest(clRes.getRes()));

        clCard data = null;
        if (cellMain == null) {
            data = new clCard("", nameEvent, description, startEvent, startEvent, "0", "0", "");
            addTableFireBase(data, listPDF, listInternet);
        } else {
            data = new clCard(cellMain.getClCard().getId(), nameEvent, description, startEvent, "", cellMain.getClCard().getCheck(), cellMain.getClCard().getCheck(), cellMain.getClCard().getRequest());
            addTableFireBase(data, listPDF, listInternet);

        }
        finish();
        return;
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
        //imgClose.setImageResource(R.drawable.ic_baseline_close_24);
        imgTime.setImageResource(R.drawable.ic_baseline_timer_light);
        imgClear.setImageResource(R.drawable.ic_baseline_settings_backup_restore_light);
        imgAttachFile.setImageResource(R.drawable.ic_baseline_attach_file_light);
        imgAttachLink.setImageResource(R.drawable.ic_baseline_link_light);
        txtAddFile.setImageResource(R.drawable.ic_baseline_add_light);
        txtAddLink.setImageResource(R.drawable.ic_baseline_add_light);

        lnShowEvent.setBackgroundColor(getResources().getColor(DarkBackGround));

        lnTitleTop.setBackgroundColor(getResources().getColor(DarkSlideMenu));
        lnChoseDate.setBackgroundColor(getResources().getColor(DarkSlideMenu));
        lnAddFile.setBackgroundColor(getResources().getColor(DarkSlideMenu));
        lnAddLink.setBackgroundColor(getResources().getColor(DarkSlideMenu));


        edtNameEvent.setBackgroundColor(getResources().getColor(DarkSlideMenu));
        edtNameEvent.setTextColor(Color.WHITE);

        edtDec.setBackgroundColor(getResources().getColor(DarkSlideMenu));
        edtDec.setTextColor(Color.WHITE);
        edtDec.setHintTextColor(Color.WHITE);

        txtStartEvent.setBackgroundColor(getResources().getColor(DarkSlideMenu));
        txtStartEvent.setTextColor(Color.WHITE);
        txtStartEvent.setHintTextColor(Color.WHITE);


        txtAttachFile.setTextColor(Color.WHITE);
        txtAttachLink.setTextColor(Color.WHITE);

    }

    private void LightMode() {
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.LightActionBar));
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lnShowEvent.setBackgroundColor(getResources().getColor(R.color.LightBackGround));
    }


    private void LoadTableAndCardFireBase() {
        DatabaseReference refSuper = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info/table/info/" + User.getLink());
        refSuper.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                cacheTable = snapshot.child("private").getValue(clTable.class);
                cacheCard = snapshot.child("listcard/" + cellMain.getClCard().getId() + "/data/text").getValue(clCard.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void getDataFromCardFrg() {
        Intent intent = getIntent();
        cellMain = (clDataCard) intent.getSerializableExtra("main");
        User = (clImage) intent.getSerializableExtra("user");
    }

    void AnhXa() {
        lnShowEvent = findViewById(R.id.lnShowEvent);
        lnTitleTop = findViewById(R.id.lnTitleTop);
        lnChoseDate = findViewById(R.id.lnChoseDate);
        lnAddFile = findViewById(R.id.lnAddFile);
        lnAddLink = findViewById(R.id.lnAddLink);

        txtAttachFile = findViewById(R.id.txtAttachFile);
        txtAttachLink = findViewById(R.id.txtAttachLink);

        imgTime = findViewById(R.id.imgTime);
        imgAttachFile = findViewById(R.id.imgAttachFile);
        imgAttachLink = findViewById(R.id.imgAttachLink);

        listPDF = new ArrayList<>();
        listInternet = new ArrayList<>();

        //imgClose = findViewById(R.id.imgCloseEvent);
        //imgDone = findViewById(R.id.imgDoneEvent);
        swNotify = findViewById(R.id.switchNotifyEvent);
        edtNameEvent = findViewById(R.id.edtNameEvent);
        edtDec = findViewById(R.id.edtDesEvent);

        txtStartEvent = findViewById(R.id.txtStartEvent);
        imgClear = findViewById(R.id.imgClearDate);

        txtAddFile = findViewById(R.id.imgBtnAddFileEvent);
        lvAddFile = findViewById(R.id.lvAddFileEvent);

        txtAddLink = findViewById(R.id.imgBtnAddLinkEvent);
        lvAddLink = findViewById(R.id.lvAddLinkEvent);

        //PDF
        lvAddFile.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(ActivityShowEvent.this, 1);
        lvAddFile.setLayoutManager(layoutManager);

        adapter_pdf = new Adapter_PDF(ActivityShowEvent.this, listPDF, R.layout.cell_pdf, this, pref);
        lvAddFile.setAdapter(adapter_pdf);
        adapter_pdf.notifyDataSetChanged();

        //Internet
        lvAddLink.setHasFixedSize(true);
        GridLayoutManager layoutManager2 = new GridLayoutManager(ActivityShowEvent.this, 1);
        lvAddLink.setLayoutManager(layoutManager2);

        adapter_internet = new Adapter_Internet(ActivityShowEvent.this, listInternet, R.layout.cell_link, this, pref);
        lvAddLink.setAdapter(adapter_internet);
        adapter_internet.notifyDataSetChanged();

    }

    void demo() {
        //swNotify.setO

        txtStartEvent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().isEmpty()) {
                    swNotify.setChecked(false);
                    swNotify.setEnabled(false);
                } else {
                    swNotify.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    swNotify.setChecked(false);
                    swNotify.setEnabled(false);
                } else {
                    swNotify.setEnabled(true);
                }
            }
        });

        txtStartEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(ActivityShowEvent.this);

                dialog.setContentView(R.layout.set_time_dialog);
                dialog.show();

                TextView txtDateStart = dialog.findViewById(R.id.txtSetTimeDateStart);
                TextView txtTimeStart = dialog.findViewById(R.id.txtSetTimeTimeStart);

                TextView txtDateEnd = dialog.findViewById(R.id.txtSetTimeDateEnd);
                TextView txtTimeEnd = dialog.findViewById(R.id.txtSetTimeTimeEnd);

                TextView txtOk = dialog.findViewById(R.id.txtSetTimeOk);
                TextView txtCancel = dialog.findViewById(R.id.txtSetTimeCancel);

                final String[] Date = {""};

                txtDateStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calStart = Calendar.getInstance();
                        if (!txtStartEvent.getText().toString().isEmpty()) {

                        }

                        int year = calStart.get(Calendar.YEAR);
                        int month = calStart.get(Calendar.MONTH);
                        int day = calStart.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dateDialog = new DatePickerDialog(
                                ActivityShowEvent.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                dateSetListenerTimeStart, year, month, day
                        );
                        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dateDialog.show();
                    }
                });

                dateSetListenerTimeStart = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "-" + month + "-" + year;
                        txtDateStart.setText(date);
                    }
                };

                txtTimeStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hourStart = hourOfDay;
                                minuteStart = minute;
                                txtTimeStart.setText(String.format(Locale.getDefault(), "%02d:%02d", hourStart, minuteStart));

                            }
                        };
                        int style = AlertDialog.THEME_HOLO_LIGHT;
                        TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityShowEvent.this, style, onTimeSetListener, hourStart, minuteStart, true);

                        timePickerDialog.setTitle("Chọn thời gian");
                        timePickerDialog.show();
                    }
                });

                txtDateEnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calStart = Calendar.getInstance();
                        if (!txtStartEvent.getText().toString().isEmpty()) {

                        }

                        int year = calStart.get(Calendar.YEAR);
                        int month = calStart.get(Calendar.MONTH);
                        int day = calStart.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dateDialog = new DatePickerDialog(
                                ActivityShowEvent.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                dateSetListenerTimeEnd, year, month, day
                        );
                        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dateDialog.show();
                    }
                });

                dateSetListenerTimeEnd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "-" + month + "-" + year;
                        txtDateEnd.setText(date);
                    }
                };

                txtTimeEnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hourEnd = hourOfDay;
                                minuteEnd = minute;
                                txtTimeEnd.setText(String.format(Locale.getDefault(), "%02d:%02d", hourEnd, minuteEnd));

                            }
                        };
                        int style = AlertDialog.THEME_HOLO_LIGHT;
                        TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityShowEvent.this, style, onTimeSetListener, hourEnd, minuteEnd, true);

                        timePickerDialog.setTitle("Chọn thời gian");
                        timePickerDialog.show();
                    }
                });


                txtOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String DateTrySt = txtDateStart.getText().toString() + " " + txtTimeStart.getText();
                        String DateTryEd = txtDateEnd.getText().toString() + " " + txtTimeEnd.getText();

                        if (txtDateStart.getText().toString().isEmpty() || txtDateEnd.getText().toString().isEmpty() ||
                                txtTimeStart.getText().toString().isEmpty() || txtTimeEnd.getText().toString().isEmpty()
                        ) {
                            Toast.makeText(ActivityShowEvent.this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_LONG).show();
                            txtDateEnd.setError("Nhập đủ thông tin");
                            txtTimeEnd.setError("Nhập đủ thông tin");
                            txtDateStart.setError("Nhập đủ thông tin");
                            txtTimeStart.setError("Nhập đủ thông tin");
                        } else {
                            java.util.Date dateStart = null;
                            Date dateEnd = null;
                            try {
                                dateStart = defaultTIme.parse(DateTrySt);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                dateEnd = defaultTIme.parse(DateTryEd);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (dateStart.getTime() <= dateEnd.getTime()) {

                                txtStartEvent.setText("Từ." + DateTrySt + "\n" + "Đến." + DateTryEd);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(ActivityShowEvent.this, "Bạn cần nhập ngày kết thúc sau ngày bắt đầu", Toast.LENGTH_LONG).show();
                                txtDateEnd.setError("Ngày kết thúc cần nhỏ hơn ngày bắt đầu");
                                txtTimeEnd.setError("Giờ kết thúc cần nhỏ hơn giờ bắt đầu");
                            }
                        }

                    }
                });
                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        txtAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });


        txtAddLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(ActivityShowEvent.this);
                dialog.setContentView(R.layout.dialog_link);
                dialog.show();

                EditText edtLink = dialog.findViewById(R.id.edtDialogPathLink);
                EditText edtName = dialog.findViewById(R.id.edtDialogNameLink);
                TextView txtCancel = dialog.findViewById(R.id.txtCancelLink);
                TextView txtOk = dialog.findViewById(R.id.txtOkLink);

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                txtOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = String.valueOf(System.currentTimeMillis());
                        if (edtLink.getText().toString().isEmpty() && edtName.getText().toString().isEmpty()) {
                            edtLink.setError("Hãy nhập link vào đây");

                        } else if (!edtLink.getText().toString().isEmpty() && edtName.getText().toString().isEmpty()) {
                            clLinkInternet clLinkInternet = new clLinkInternet(id, edtLink.getText().toString(), edtLink.getText().toString());

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
                            reference.child("table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/net/" + id).setValue(clLinkInternet);
                            dialog.dismiss();

                        } else if (!edtLink.getText().toString().isEmpty() && !edtName.getText().toString().isEmpty()) {
                            clLinkInternet clLinkInternet = new clLinkInternet(id, edtName.getText().toString(), edtLink.getText().toString());

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
                            reference.child("table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/net/" + id).setValue(clLinkInternet);
                            dialog.dismiss();

                        } else {
                            edtLink.setError("Hãy nhập link vào đây");
                        }
                    }
                });


            }
        });


        if (cellMain != null) {
            edtNameEvent.setText(cellMain.getClCard().getNameEvent());
            edtDec.setText(cellMain.getClCard().getDescription());
            txtStartEvent.setText(cellMain.getClCard().getStartEvent());
            if (cellMain.getClCard().getNotify().toString().equals("0")) {
                swNotify.setChecked(false);
            } else if (cellMain.getClCard().getNotify().toString().equals("1")) {
                swNotify.setChecked(true);
            }

        }


        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStartEvent.setText("");
            }
        });

        swNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Toast.makeText(ActivityShowEvent.this, "checked", Toast.LENGTH_SHORT).show();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
                    reference.child("table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/text/notify").setValue("1");

                    //thu nghiem
                    cellMain.getClCard().setRequest(clRes.getRes());
                    int x = Integer.parseInt(cellMain.getClCard().getRequest());
                    x = x + 3;
                    clRes.setRes("" + x);

                    //thu nghiem

                    cellMain.getClCard().setCheck("1");

                    List<String> lsTimeSt = returnDateStart(txtStartEvent.getText().toString());
                    setAlarmStart(lsTimeSt, "0");

                    List<String> lsTimeEd = returnDateEnd(txtStartEvent.getText().toString());
                    setAlarmStart(lsTimeEd, "1");
                } else {
                    //Toast.makeText(ActivityShowEvent.this, "no checked", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(ActivityShowEvent.this, "checked", Toast.LENGTH_SHORT).show();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
                    reference.child("table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/text/notify").setValue("0");


                    String id = cellMain.getClCard().getRequest();
                    cellMain.getClCard().setCheck("0");
                    if (!id.isEmpty()) {
                        setAlarmEnd(Integer.parseInt(id));
                    }
                }
            }
        });
//        imgClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String nameEvent = edtNameEvent.getText().toString();
//                String description = edtDec.getText().toString();
//                String startEvent = txtStartEvent.getText().toString();
//                DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
//                reference3.child("clRequest").setValue(new clRequest(clRes.getRes()));
//
//                clCard data = null;
//                if (cellMain == null) {
//                    data = new clCard("", nameEvent, description, startEvent, startEvent, "0", "0", "");
//                    addTableFireBase(data, listPDF, listInternet);
//                } else {
//                    data = new clCard(cellMain.getClCard().getId(), nameEvent, description, startEvent, "", cellMain.getClCard().getCheck(), cellMain.getClCard().getCheck(), cellMain.getClCard().getRequest());
//                    addTableFireBase(data, listPDF, listInternet);
//
//                }
//                finish();
//            }
//        });

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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
        reference.child("table/info/" + User.getLink() + "/listcard/" + id + "/data/text").setValue(data);
    }


    private void LoadDataFromFireBase() {

            FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
            DatabaseReference reference = rootNode.getReference("users/" + User.getNameImage() +
                    "/General/info/table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/pdf");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    listPDF.clear();

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        clLinkPDF PDF = postSnapshot.getValue(clLinkPDF.class);
                        listPDF.add(PDF);
                    }
                    adapter_pdf.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            FirebaseDatabase rootNode2 = FirebaseDatabase.getInstance();
            DatabaseReference reference2 = rootNode2.getReference("users/" + User.getNameImage() +
                    "/General/info/table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/net");

            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    listInternet.clear();

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        clLinkInternet internet = postSnapshot.getValue(clLinkInternet.class);
                        listInternet.add(internet);
                    }
                    adapter_internet.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

    }

    private void selectPDF() {
//        Intent intent = new Intent();
//        intent.setType("application/*");
//        intent.setAction(intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);

        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            final String[] nameFile = {""};

            ContentResolver cr = this.getContentResolver();
            String mime = cr.getType(data.getData());

            //  Toast.makeText(this, "" + mime, Toast.LENGTH_SHORT).show();

            nameFile[0] = DotFile(mime);

            Dialog dialog = new Dialog(ActivityShowEvent.this);

            dialog.setContentView(R.layout.dialog_file);
            dialog.show();

            EditText editText = dialog.findViewById(R.id.edtDialogFile);
            TextView txtCancel = dialog.findViewById(R.id.txtCancelFile);
            TextView txtOk = dialog.findViewById(R.id.txtOkFile);

            txtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            txtOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText.getText().toString().isEmpty()) {
                        editText.setError("Hãy nhập tên");
                    } else {
                        dialog.dismiss();
                        nameFile[0] = editText.getText().toString() + nameFile[0];
                        upLoadFileFireBase(data.getData(), nameFile[0]);

                    }
                }
            });
        }
    }

    private void upLoadFileFireBase(Uri data, String nameFile) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading...");
        progressDialog.show();
        String id = String.valueOf(System.currentTimeMillis());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference uploader = storage.getReference().child(User.getNameImage() + "/pdf/" + nameFile);

        uploader.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Toast.makeText(ActivityShowEvent.this, ""+ uri, Toast.LENGTH_SHORT).show();

                                uploader.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                    @Override
                                    public void onSuccess(StorageMetadata storageMetadata) {
                                        //String size = "" + storageMetadata.getSizeBytes();
                                        String size = "" + convertSize(Double.parseDouble(String.valueOf(storageMetadata.getSizeBytes())));

                                        clLinkPDF clLinkPDF = new clLinkPDF(id, nameFile, uri.toString(), size);

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
                                        reference.child("table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/pdf/" + id).setValue(clLinkPDF);
                                    }
                                });
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double process = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("FIle Uploaded..." + (int) process + "%");
                if (process == 100) {
                    Toast.makeText(ActivityShowEvent.this, "Up load file thành công", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    public String convertSize(double x) {
        ArrayList<String> fileSizeUnitList = new ArrayList<>();
        fileSizeUnitList.add("B");
        fileSizeUnitList.add("KB");
        fileSizeUnitList.add("MB");
        fileSizeUnitList.add("GB");
        int count = 0;
        while (x >= 1024) {
            x /= 1024;
            count++;
            if (count == 4) {
                break;
            }
        }
        x = (double) Math.round(x * 100) / 100;
        return x + fileSizeUnitList.get(count);
    }

    @Override
    public void onClickItemPDF(int position) {
        //Toast.makeText(this, ""+listPDF.get(position).getLinkPDF(), Toast.LENGTH_SHORT).show();
        clLinkPDF clLinkPDF = listPDF.get(position);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("application/*");
        intent.setData(Uri.parse(clLinkPDF.getLinkPDF()));
        startActivity(intent);
    }

    @Override
    public void onClickDeletePDF(int position) {
        clLinkPDF clLinkPDF = listPDF.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityShowEvent.this);
        builder.setTitle("Xóa bảng");
        builder.setMessage("Bạn có chắc xóa thẻ file: " + clLinkPDF.getNameLinkPDF());

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
                DatabaseReference reference = rootNode.getReference("users/" + User.getNameImage() + "/General/info/table/info/"
                        + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/pdf");


                // reference.child(cellImageList.get(i).getId()).setValue(null);

                // FirebaseStorage storage = FirebaseStorage.getInstance();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference desertRef = storageRef.child(User.getNameImage() + "/pdf/" + clLinkPDF.getNameLinkPDF());

                desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                        reference.child(clLinkPDF.getId()).setValue(null);
                        Toast.makeText(ActivityShowEvent.this, "Đã xóa file", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String DotFile(String uri) {

        /*
        * "application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"
        * */
        // variable: Một biến để kiểm tra.
        switch (uri) {
            case "application/msword":
                return ".doc";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return ".docx";
            case "application/vnd.ms-powerpoint":
                return ".ppt";
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                return ".pptx";
            case "application/vnd.ms-excel":
                return ".xls";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return ".xlsx";
            case "text/plain":
                return ".txt";
            case "application/pdf":
                return ".pdf";
            case "application/zip":
                return ".zip";
            default:
                return "";
            // Làm gì đó tại đây ...
        }
    }

    @Override
    public void onClickItemInternet(int position) {

        clLinkInternet clLinkInternet = listInternet.get(position);
        String url = clLinkInternet.getLinkInternet();

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        //Toast.makeText(this, "click internet " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickDeleteInternet(int position) {
        //Toast.makeText(this, "delete internet " + position, Toast.LENGTH_SHORT).show();
        Context context = ActivityShowEvent.this;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        alert.setTitle("Bạn có chắc muốn xóa link này");


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                clLinkInternet clLinkInternet = listInternet.get(position);
                String id = clLinkInternet.getId();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
                reference.child("table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/net/" + id).setValue(null);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

    @Override
    public void onClickRenameInternet(int position) {
        clLinkInternet clLinkInternet = listInternet.get(position);

        //Toast.makeText(this, "rename internet " + position, Toast.LENGTH_SHORT).show();

        Dialog dialog = new Dialog(ActivityShowEvent.this);
        dialog.setContentView(R.layout.dialog_link);
        dialog.show();

        EditText edtLink = dialog.findViewById(R.id.edtDialogPathLink);
        EditText edtName = dialog.findViewById(R.id.edtDialogNameLink);
        TextView txtCancel = dialog.findViewById(R.id.txtCancelLink);
        TextView txtOk = dialog.findViewById(R.id.txtOkLink);

        edtLink.setText(clLinkInternet.getLinkInternet());
        edtName.setText(clLinkInternet.getNameLinkInternet());

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = clLinkInternet.getId();
                if (edtLink.getText().toString().isEmpty() && edtName.getText().toString().isEmpty()) {
                    edtLink.setError("Hãy nhập link vào đây");

                } else if (!edtLink.getText().toString().isEmpty() && edtName.getText().toString().isEmpty()) {
                    clLinkInternet clLinkInternet = new clLinkInternet(id, edtLink.getText().toString(), edtLink.getText().toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
                    reference.child("table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/net/" + id).setValue(clLinkInternet);
                    dialog.dismiss();

                } else if (!edtLink.getText().toString().isEmpty() && !edtName.getText().toString().isEmpty()) {
                    clLinkInternet clLinkInternet = new clLinkInternet(id, edtName.getText().toString(), edtLink.getText().toString());

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
                    reference.child("table/info/" + User.getLink() + "/listcard/" + cellMain.getClCard().getId() + "/data/net/" + id).setValue(clLinkInternet);
                    dialog.dismiss();

                } else {
                    edtLink.setError("Hãy nhập link vào đây");
                }
            }
        });
    }


    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
            CharSequence name = "channel_1";
            String description = "channel notify";
            int important = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_1", name, important);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void setAlarmStart(List<String> timeDate, String Code_Start) {
        //List<String> timeDate = returnDateStart("Từ.25-6-2021 00:00\nĐến.25-6-2021 00:00");


        // Toast.makeText(this, "set Time", Toast.LENGTH_SHORT).show();

        int x = Integer.parseInt(cellMain.getClCard().getRequest());
        if (Code_Start.equals("0")) {
            //Toast.makeText(ActivityShowEvent.this, "start event", Toast.LENGTH_SHORT).show();

            Bundle mBundle = new Bundle();

            mBundle.putString("code_start", Code_Start);
            mBundle.putString("user", User.getNameImage());
            mBundle.putString("IdTable", User.getLink());
            mBundle.putString("IdCard", cellMain.getClCard().getId());
            mBundle.putString("nameTable", cacheTable.getName());
            mBundle.putString("nameCard", cacheCard.getNameEvent());

            mBundle.putInt("Id", x + 1);
            Intent intent = new Intent(ActivityShowEvent.this, Reminder.class);
            intent.putExtras(mBundle);


            int Day = Integer.parseInt(timeDate.get(0));
            int Month = Integer.parseInt(timeDate.get(1));
            int Year = Integer.parseInt(timeDate.get(2));
            int Hour = Integer.parseInt(timeDate.get(3));
            int Minute = Integer.parseInt(timeDate.get(4));
            Calendar myAlarmDate = Calendar.getInstance();
            myAlarmDate.setTimeInMillis(System.currentTimeMillis());

            myAlarmDate.set(Year, Month - 1, Day, Hour, Minute, 0);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            pendingIntent = PendingIntent.getBroadcast(ActivityShowEvent.this, x + 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), pendingIntent);

        } else if (Code_Start.equals("1")) {
            // Toast.makeText(ActivityShowEvent.this, "end event", Toast.LENGTH_SHORT).show();

            Bundle mBundle = new Bundle();

            mBundle.putString("code_start", Code_Start);
            mBundle.putString("user", User.getNameImage());
            mBundle.putString("IdTable", User.getLink());
            mBundle.putString("IdCard", cellMain.getClCard().getId());
            mBundle.putString("nameTable", cacheTable.getName());
            mBundle.putString("nameCard", cacheCard.getNameEvent());

            mBundle.putInt("Id", x + 2);
            Intent intent = new Intent(ActivityShowEvent.this, Reminder.class);
            intent.putExtras(mBundle);


            int Day = Integer.parseInt(timeDate.get(0));
            int Month = Integer.parseInt(timeDate.get(1));
            int Year = Integer.parseInt(timeDate.get(2));
            int Hour = Integer.parseInt(timeDate.get(3));
            int Minute = Integer.parseInt(timeDate.get(4));
            Calendar myAlarmDate = Calendar.getInstance();
            myAlarmDate.setTimeInMillis(System.currentTimeMillis());

            myAlarmDate.set(Year, Month - 1, Day, Hour, Minute, 0);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivityShowEvent.this, x + 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, myAlarmDate.getTimeInMillis(), pendingIntent);

        }

    }

    private void setAlarmEnd(int id) {
//        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
//        reference3.child("notify/general/start/" + cellMain.getClCard().getId()).setValue(null);

        int x = Integer.parseInt(cellMain.getClCard().getRequest());
        Intent intent = new Intent(ActivityShowEvent.this, Reminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivityShowEvent.this, x + 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);


//        DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info");
//        reference4.child("notify/general/start/" + cellMain.getClCard().getId()+"end").setValue(null);

        Intent intent2 = new Intent(ActivityShowEvent.this, Reminder.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(ActivityShowEvent.this, x + 2, intent2, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager2.cancel(pendingIntent2);
    }


    private List<String> returnDateStart(String time) {
        String x = "Từ.25-6-2021 00:00\nĐến.25-6-2021 00:00";
        List<String> rtDateEnd = new ArrayList<>();
        //List<EventDay> events = new ArrayList<>();
        String[] strDateEnd = time.split("(\n)");
        //Toast.makeText(getContext(), ""+strDateEnd[1], Toast.LENGTH_SHORT).show();
        String[] strSlitEndDate = strDateEnd[0].split("[.]");
        String[] strSplitFinal = strSlitEndDate[1].split(" ");
//
        String[] DayMonthYear = strSplitFinal[0].split("(-)");
        String[] Hour = strSplitFinal[1].split("(:)");

        rtDateEnd.add(DayMonthYear[0]);
        rtDateEnd.add(DayMonthYear[1]);
        rtDateEnd.add(DayMonthYear[2]);
        rtDateEnd.add(Hour[0]);
        rtDateEnd.add(Hour[1]);

        return rtDateEnd;
    }


    private List<String> returnDateEnd(String time) {
        String x = "Từ.25-6-2021 00:00\nĐến.25-6-2021 00:00";
        List<String> rtDateEnd = new ArrayList<>();
        //List<EventDay> events = new ArrayList<>();
        String[] strDateEnd = time.split("(\n)");
        //Toast.makeText(getContext(), ""+strDateEnd[1], Toast.LENGTH_SHORT).show();
        String[] strSlitEndDate = strDateEnd[1].split("[.]");
        String[] strSplitFinal = strSlitEndDate[1].split(" ");
//
        String[] DayMonthYear = strSplitFinal[0].split("(-)");
        String[] Hour = strSplitFinal[1].split("(:)");

        rtDateEnd.add(DayMonthYear[0]);
        rtDateEnd.add(DayMonthYear[1]);
        rtDateEnd.add(DayMonthYear[2]);
        rtDateEnd.add(Hour[0]);
        rtDateEnd.add(Hour[1]);

        return rtDateEnd;
    }


    private void LoadDataDefaultRes() {
        clRes = new clRequest("0");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users/" + User.getNameImage() + "/General/info/clRequest");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                clRequest re = snapshot.getValue(clRequest.class);

                if (re == null) {
                    reference.setValue(clRes);
                } else {
                    if (Integer.parseInt(re.getRes()) >= 1000000000) {
                        clRes.setRes("0");
                    } else {
                        clRes = snapshot.getValue(clRequest.class);
                    }
                }
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}
