package com.example.trelloreal.AllCard_Show;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

import static com.example.trelloreal.R.color.DarkBackGround;
import static com.example.trelloreal.R.color.DarkSlideMenu;

public class Adapter_Table_Card extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<clTable_Card> tableCardList;
    private Interface_ClickAllCard clickAllCard;
    private saveShareprefrences pref;

    public Adapter_Table_Card(Context context, List<clTable_Card> tableCardList,Interface_ClickAllCard clickAllCard,saveShareprefrences pref) {
        this.context = context;
        this.tableCardList = tableCardList;
        this.clickAllCard = clickAllCard;
        this.pref = pref;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       if(viewType == 0){
           return new TableHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title_table,parent,false));
       }else{
           return new CardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false));
       }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 0){
            clTable_Card clTable_card = tableCardList.get(position);

            ((TableHolder)holder).txtTable.setText(clTable_card.getTable().getName());
            Glide.with(context).load(clTable_card.getTable().getLink().trim()).into(((TableHolder)holder).imgTable);
            ((TableHolder)holder).imgTable.setScaleType(ImageView.ScaleType.FIT_XY);



            if (pref.getState() == true) {
                ((TableHolder)holder).itemView.setBackgroundColor(context.getResources().getColor(DarkSlideMenu));
                ((TableHolder)holder).txtTable.setTextColor(Color.WHITE);
            } else {
                ((TableHolder)holder).itemView.setBackgroundColor(context.getResources().getColor(R.color.LightSlideMenu));
                ((TableHolder)holder).txtTable.setTextColor(Color.BLACK);
            }

        }else {
            clTable_Card clTable_card = tableCardList.get(position);

            ((CardHolder)holder).txtTitleCard.setText(clTable_card.getCard().getNameEvent());

            if (!clTable_card.getCard().getStartEvent().isEmpty() || !clTable_card.getCard().getEndEvent().isEmpty()) {

                ((CardHolder)holder).txtTimeCard.setText(clTable_card.getCard().getStartEvent());

                if (pref.getState() == true) {
                    ((CardHolder)holder).imgCard.setImageResource(R.drawable.ic_baseline_timer_light);
                } else {
                    ((CardHolder)holder).imgCard.setImageResource(R.drawable.ic_baseline_timer_24);
                }

                if(setColorDate(clTable_card.getCard().getStartEvent().toString())){
                    //((CardHolder)holder).lnTime.setBackgroundColor(context.getResources().getColor(R.color.colorSoon));
                    ((CardHolder)holder).txtTimeCard.setBackgroundColor(context.getResources().getColor(R.color.colorSoon));
                    //((CardHolder)holder).lyTime.setBackgroundColor(context.getResources().getColor(R.color.colorSoon));
                }else {
                    //((CardHolder)holder).lnTime.setBackgroundColor(context.getResources().getColor(R.color.colorLate));
                   ((CardHolder)holder).txtTimeCard.setBackgroundColor(context.getResources().getColor(R.color.colorLate));
                    //((CardHolder)holder).lyTime.setBackgroundColor(context.getResources().getColor(R.color.colorLate));
                }

            } else {
                ((CardHolder)holder).imgCard.setImageResource(0);
                ((CardHolder)holder).txtTimeCard.setText("");
            }

            if (clTable_card.getCard().getCheck().equals("0")) {
                ((CardHolder)holder).ckbCard.setChecked(false);
            } else {
                ((CardHolder)holder).ckbCard.setChecked(true);
            }

            if (pref.getState() == true) {
                ((CardHolder)holder).lnItem.setBackgroundColor(context.getResources().getColor(DarkSlideMenu));
                ((CardHolder)holder).txtTimeCard.setTextColor(Color.WHITE);
                ((CardHolder)holder).txtTitleCard.setTextColor(Color.WHITE);
            } else {
                ((CardHolder)holder).lnItem.setBackgroundColor(context.getResources().getColor(R.color.LightSlideMenu));
                ((CardHolder)holder).txtTimeCard.setTextColor(Color.BLACK);
                ((CardHolder)holder).txtTitleCard.setTextColor(Color.BLACK);
            }


            ((CardHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAllCard.onClickItemAllCard(position);
                }
            });


            ((CardHolder)holder).ckbCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   clickAllCard.checkedBox(position, isChecked);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tableCardList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return tableCardList.get(position).getType();
    }

    static class TableHolder extends RecyclerView.ViewHolder{

        private ImageView imgTable;
        private TextView txtTable;

        public TableHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imgTable = itemView.findViewById(R.id.imgItem_Table_All_Card);
            txtTable = itemView.findViewById(R.id.txtNameItem_Table_All_Card);
        }

    }

    static class CardHolder extends RecyclerView.ViewHolder{

        private ImageView imgCard;
        private CheckBox ckbCard;
        private TextView txtTitleCard,txtTimeCard;
       // private LinearLayout lnItem,lnTime;
       ConstraintLayout lnItem;
       CardView lyTime;


        public CardHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imgCard = itemView.findViewById(R.id.imgItemNotify);
            txtTitleCard = itemView.findViewById(R.id.txtItemTitle);
            ckbCard = itemView.findViewById(R.id.ckbItem);
            txtTimeCard = itemView.findViewById(R.id.txtItemTime);

            lnItem = itemView.findViewById(R.id.lnItem);
            //lyTime = itemView.findViewById(R.id.lyTime);
            //lnTime= itemView.findViewById(R.id.lnTime);

        }

    }

    private boolean setColorDate(String Time){

        String x = "Từ.25-6-2021 00:00\nĐến.25-6-2021 00:00";
        //List<EventDay> events = new ArrayList<>();
        String[] strDateEnd = Time.split("(\n)");

        String[] strSlitEndDate = strDateEnd[1].split("[.]");
        String[] strSplitFinal = strSlitEndDate[1].split(" ");

        String[] DayMonthYear = strSplitFinal[0].split("(-)");


        Calendar calendar = Calendar.getInstance();
        Calendar calndr1 = (Calendar)Calendar.getInstance();
        calndr1.set(Integer.parseInt(DayMonthYear[2]),Integer.parseInt(DayMonthYear[1])-1,Integer.parseInt(DayMonthYear[0]));
        long t1 = calendar.getTimeInMillis();
        long t2 = calndr1.getTimeInMillis();
        if(t1<t2){
            return true;
        }
        return false;
    }

}
