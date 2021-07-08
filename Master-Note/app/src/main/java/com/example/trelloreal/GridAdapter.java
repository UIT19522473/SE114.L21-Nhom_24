package com.example.trelloreal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.trelloreal.DataBase.clCellImage;
import com.example.trelloreal.DataBase.clImage;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<clCellImage> imageList;

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

    public List<clCellImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<clCellImage> imageList) {
        this.imageList = imageList;
    }

    public GridAdapter(Context context, int layout, List<clCellImage> imageList) {
        this.context = context;
        this.layout = layout;
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // return null;
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imgHinh = (ImageView) view.findViewById(R.id.imgBgrCell);
            holder.txtTitle = (TextView) view.findViewById(R.id.txtTileCell);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        clCellImage cellImage = imageList.get(i);
        holder.txtTitle.setText(cellImage.getName());
        Glide.with(context).load(cellImage.getLink().trim()).into(holder.imgHinh);
        holder.imgHinh.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return view;
    }

    private class ViewHolder {
        ImageView imgHinh;
        TextView txtTitle;
    }

    /*
    *
    * public View getView(int position, View convertView, ViewGroup parent) {
        //return null;
        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.imgHinh = (ImageView)convertView.findViewById(R.id.imgBgrBackGround);
            holder.txtTitle = (TextView)convertView.findViewById(R.id.txtTileBackGround);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        clImage cellImage = imageList.get(position);
        holder.txtTitle.setText(cellImage.getNameImage());
        Glide.with(context).load(cellImage.getLink().trim()).into(holder.imgHinh);
        return convertView;
    }

    private class ViewHolder{
        ImageView imgHinh;
        TextView txtTitle;
    }
    * */
}
