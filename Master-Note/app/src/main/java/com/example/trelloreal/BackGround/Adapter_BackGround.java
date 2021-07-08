package com.example.trelloreal.BackGround;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.trelloreal.DataBase.clBackGround;
import com.example.trelloreal.DataBase.clImage;
import com.example.trelloreal.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter_BackGround extends BaseAdapter {

    private Context context;
    private int layout;
    private List<clImage> imageList;

    public Adapter_BackGround(Context context, int layout, List<clImage> imageList) {
        this.context = context;
        this.layout = layout;
        this.imageList = imageList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public List<clImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<clImage> imageList) {
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return null;
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.imgHinh = (ImageView) convertView.findViewById(R.id.imgBgrBackGround);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTileBackGround);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        clImage cellImage = imageList.get(position);
        holder.txtTitle.setText(cellImage.getNameImage());
        Glide.with(context).load(cellImage.getLink().trim()).into(holder.imgHinh);
        holder.imgHinh.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return convertView;
    }

    private class ViewHolder {
        ImageView imgHinh;
        TextView txtTitle;
    }

}
