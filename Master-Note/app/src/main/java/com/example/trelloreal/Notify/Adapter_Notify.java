package com.example.trelloreal.Notify;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trelloreal.AllCard_Show.Adapter_Table_Card;
import com.example.trelloreal.Card.Adapter_PDF;
import com.example.trelloreal.Card.Interface_ClickItemPDF;
import com.example.trelloreal.DataBase.clLinkPDF;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.trelloreal.R.color.DarkSlideMenu;

public class Adapter_Notify  extends RecyclerView.Adapter<Adapter_Notify.ViewHolder>{
    private Context mContext;
    private List<clNotify> mListItem;
    private int layout;
    private Interface_ClickNotify clickItemNotify;
    private saveShareprefrences pref;

    public Adapter_Notify(Context mContext, List<clNotify> mListItem, int layout, Interface_ClickNotify clickItemNotify,saveShareprefrences pref) {
        this.mContext = mContext;
        this.mListItem = mListItem;
        this.layout = layout;
        this.clickItemNotify = clickItemNotify;
        this.pref = pref;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<clNotify> getmListItem() {
        return mListItem;
    }

    public void setmListItem(List<clNotify> mListItem) {
        this.mListItem = mListItem;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public Interface_ClickNotify getClickItemNotify() {
        return clickItemNotify;
    }

    public void setClickItemNotify(Interface_ClickNotify clickItemNotify) {
        this.clickItemNotify = clickItemNotify;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(layout, parent, false);

        return new Adapter_Notify.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        clNotify item_Notify = mListItem.get(position);
        holder.txtMess.setText(item_Notify.getMess());
        holder.txtTime.setText(item_Notify.getTime());

        if (pref.getState() == true) {
            holder.txtTime.setTextColor(Color.WHITE);
            holder.txtMess.setTextColor(Color.WHITE);
            holder.lnItemNotify.setBackgroundColor(mContext.getResources().getColor(R.color.DarkSlideMenu));
            holder.imgNotify.setImageResource(R.drawable.ic_baseline_timer_light);
        } else {
            holder.txtTime.setTextColor(Color.BLACK);
            holder.txtMess.setTextColor(Color.BLACK);
            holder.lnItemNotify.setBackgroundColor(mContext.getResources().getColor(R.color.LightSlideMenu));
            holder.imgNotify.setImageResource(R.drawable.ic_baseline_timer_24);
        }
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMess;
        TextView txtTime;
        ImageView imgNotify;
        ConstraintLayout lnItemNotify;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgNotify = itemView.findViewById(R.id.imgNotify);
            lnItemNotify = itemView.findViewById(R.id.lnItemNotify);
            txtMess = (TextView) itemView.findViewById(R.id.txtMessNotify);
            txtTime = (TextView) itemView.findViewById(R.id.txtTimeNotify);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemNotify.onClickItemNotify(getAdapterPosition());
                }
            });

        }
    }
}
