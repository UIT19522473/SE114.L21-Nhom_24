package com.example.trelloreal.Card;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.trelloreal.AllCard_Show.Adapter_Table_Card;
import com.example.trelloreal.DataBase.clCellImage;
import com.example.trelloreal.GridAdapter;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.example.trelloreal.R.color.DarkSlideMenu;

public class Adapter_Item extends /*BaseAdapter*/ RecyclerView.Adapter<Adapter_Item.ViewHolder> implements Filterable {

    private Context mContext;
    private List<clDataCard> mListItem;
    private int layout;
    private RecycleClickInterface clickInterface;
    private List<clDataCard> mListItemAll = new ArrayList<>();
    private saveShareprefrences pref;
    // private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public Adapter_Item(Context mContext, List<clDataCard> mListItem, int layout, RecycleClickInterface clickInterface,saveShareprefrences pref) {
        this.mContext = mContext;
        this.mListItem = mListItem;
        this.layout = layout;
        this.clickInterface = clickInterface;
        this.mListItemAll = mListItem;

        this.pref = pref;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<clDataCard> getmListItem() {
        return mListItem;
    }

    public void setmListItem(List<clDataCard> mListItem) {
        this.mListItem = mListItem;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        clDataCard item_card = mListItem.get(position);
        holder.txtTitle.setText(item_card.getClCard().getNameEvent());
        if (!item_card.getClCard().getStartEvent().isEmpty() || !item_card.getClCard().getEndEvent().isEmpty()) {
            //holder.imgHinh.setImageResource(R.drawable.ic_baseline_timer_24);
            holder.txtTime.setText(item_card.getClCard().getStartEvent());
            if (pref.getState() == true) {
                holder.imgHinh.setImageResource(R.drawable.ic_baseline_timer_light);
            } else {
                holder.imgHinh.setImageResource(R.drawable.ic_baseline_timer_24);
            }
            if(setColorDate(item_card.getClCard().getStartEvent().toString())){
                //holder.lnTime.setBackgroundColor(mContext.getResources().getColor(R.color.colorSoon));
                holder.txtTime.setBackgroundColor(mContext.getResources().getColor(R.color.colorSoon));
            }else {
                //holder.lnTime.setBackgroundColor(mContext.getResources().getColor(R.color.colorLate));
                holder.txtTime.setBackgroundColor(mContext.getResources().getColor(R.color.colorLate));
            }


        } else {
            holder.imgHinh.setImageResource(0);
            holder.txtTime.setText("");
        }

        if (item_card.getClCard().getCheck().equals("0")) {
            holder.checkBox.setChecked(false);
        } else {
            holder.checkBox.setChecked(true);
        }

        if (pref.getState() == true) {
            holder.lnItem.setBackgroundColor(mContext.getResources().getColor(DarkSlideMenu));
            holder.txtTitle.setTextColor(Color.WHITE);
            holder.txtTime.setTextColor(Color.WHITE);
        } else {
            holder.lnItem.setBackgroundColor(mContext.getResources().getColor(R.color.LightSlideMenu));
            holder.txtTitle.setTextColor(Color.BLACK);
            holder.txtTime.setTextColor(Color.BLACK);
        }

    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();

            List<clDataCard> filteredList = new ArrayList<>();
            if (charString.isEmpty()) {
                filteredList.addAll(mListItemAll);
            } else {
                for (clDataCard data : mListItemAll) {

                    if (data.getClCard().getNameEvent().toString().toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(data);
                    }
                }
            }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mListItem.clear();
            mListItem.addAll((Collection<? extends clDataCard>) results.values);
            notifyDataSetChanged();
        }
    };

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtTitle;
            ImageView imgHinh;
            TextView txtTime;
            CheckBox checkBox;
            //LinearLayout lnItem,lnTime;
            ConstraintLayout lnItem,lnTime;

            public ViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);

                imgHinh = (ImageView) itemView.findViewById(R.id.imgItemNotify);
                txtTitle = (TextView) itemView.findViewById(R.id.txtItemTitle);
                txtTime = (TextView) itemView.findViewById(R.id.txtItemTime);
                checkBox = (CheckBox) itemView.findViewById(R.id.ckbItem);
                lnItem = itemView.findViewById(R.id.lnItem);
                //lnTime = itemView.findViewById(R.id.lnTime);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickInterface.onItemClick(getAdapterPosition());
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        clickInterface.onLongItemClick(getAdapterPosition());
                        return true;
                    }
                });

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        clickInterface.checkedBox(getAdapterPosition(), isChecked);
                    }
                });
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
