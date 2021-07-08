package com.example.trelloreal.Card;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trelloreal.DataBase.clLinkInternet;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.trelloreal.R.color.DarkSlideMenu;

public class Adapter_Internet extends RecyclerView.Adapter<Adapter_Internet.ViewHolder> {

    private Context mContext;
    private List<clLinkInternet> mListItem;
    private int layout;
    Interface_clickInternet clickInternet;
    private saveShareprefrences pref;

    public Adapter_Internet(Context mContext, List<clLinkInternet> mListItem, int layout, Interface_clickInternet click,saveShareprefrences pref) {
        this.mContext = mContext;
        this.mListItem = mListItem;
        this.layout = layout;
        this.clickInternet = click;
        this.pref = pref;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<clLinkInternet> getmListItem() {
        return mListItem;
    }

    public void setmListItem(List<clLinkInternet> mListItem) {
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
    public Adapter_Internet.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(layout, parent, false);

        return new Adapter_Internet.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Adapter_Internet.ViewHolder holder, int position) {
        clLinkInternet item_card = mListItem.get(position);
        holder.txtTitle.setText(item_card.getNameLinkInternet());

        if (pref.getState() == true) {
            holder.lnLink.setBackgroundColor(mContext.getResources().getColor(DarkSlideMenu));
            holder.txtTitle.setTextColor(Color.WHITE);
            holder.imgAttachItemLink.setImageResource(R.drawable.ic_baseline_link_light);
            holder.imgHinh.setImageResource(R.drawable.ic_baseline_list_24);

        } else {
            holder.lnLink.setBackgroundColor(mContext.getResources().getColor(R.color.LightSlideMenu));
            holder.txtTitle.setTextColor(Color.BLACK);
            holder.imgAttachItemLink.setImageResource(R.drawable.ic_baseline_link);
            holder.imgHinh.setImageResource(R.drawable.ic_baseline_list_black);
        }
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView txtTitle;
        ImageView imgHinh;

        LinearLayout lnLink;
        ImageView imgAttachItemLink;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtInternet);
            imgHinh = (ImageView) itemView.findViewById(R.id.imgCloseInternet);

            lnLink = itemView.findViewById(R.id.lnLink);
            imgAttachItemLink = itemView.findViewById(R.id.imgAttachItemLink);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickInternet.onClickItemInternet(getAdapterPosition());
                }
            });
            imgHinh.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            showPopupMenu(view);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.popup_edit:
                    clickInternet.onClickRenameInternet(getAdapterPosition());
                    return true;

                case R.id.popup_delete:
                    clickInternet.onClickDeleteInternet(getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }
}
