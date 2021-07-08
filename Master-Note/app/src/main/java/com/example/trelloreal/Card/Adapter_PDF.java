package com.example.trelloreal.Card;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trelloreal.DataBase.clLinkPDF;
import com.example.trelloreal.R;
import com.example.trelloreal.saveShareprefrences;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.trelloreal.R.color.DarkSlideMenu;

public class Adapter_PDF extends RecyclerView.Adapter<Adapter_PDF.ViewHolder> {

    private Context mContext;
    private List<clLinkPDF> mListItem;
    private int layout;
    private Interface_ClickItemPDF clickItemPDF;
    private saveShareprefrences pref;

    public Adapter_PDF(Context mContext, List<clLinkPDF> mListItem, int layout, Interface_ClickItemPDF click,saveShareprefrences pref) {
        this.mContext = mContext;
        this.mListItem = mListItem;
        this.layout = layout;
        this.clickItemPDF = click;
        this.pref = pref;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<clLinkPDF> getmListItem() {
        return mListItem;
    }

    public void setmListItem(List<clLinkPDF> mListItem) {
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

        return new Adapter_PDF.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        clLinkPDF item_card = mListItem.get(position);
        holder.txtTitle.setText(item_card.getNameLinkPDF());
        holder.txtSize.setText(item_card.getSize());


        if (pref.getState() == true) {
            holder.layoutPDF.setBackgroundColor(mContext.getResources().getColor(DarkSlideMenu));
            holder.txtTitle.setTextColor(Color.WHITE);
            holder.txtSize.setTextColor(Color.WHITE);
            holder.imageView.setImageResource(R.drawable.ic_baseline_attach_file_light);
            holder.imgPDF.setImageResource(R.drawable.ic_baseline_close_24);

        } else {
            holder.layoutPDF.setBackgroundColor(mContext.getResources().getColor(R.color.LightSlideMenu));
            holder.txtTitle.setTextColor(Color.BLACK);
            holder.txtSize.setTextColor(Color.BLACK);
            holder.imageView.setImageResource(R.drawable.ic_baseline_attach_file_24);
            holder.imgPDF.setImageResource(R.drawable.ic_baseline_close_black);
        }
    }

    @Override
    public int getItemCount() {
        return mListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layoutPDF;
        ImageView imageView,imgPDF;
        TextView txtTitle;
        TextView txtSize;
        ImageView imgHinh;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            layoutPDF = itemView.findViewById(R.id.lnFile);
            imageView = itemView.findViewById(R.id.imageView);
            imgPDF = itemView.findViewById(R.id.imgPDF);
            txtTitle = (TextView) itemView.findViewById(R.id.txtPDF);
            txtSize = (TextView) itemView.findViewById(R.id.txtSizeFile);
            imgHinh = (ImageView) itemView.findViewById(R.id.imgPDF);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemPDF.onClickItemPDF(getAdapterPosition());
                }
            });

            imgHinh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemPDF.onClickDeletePDF(getAdapterPosition());
                }
            });


        }
    }
}
