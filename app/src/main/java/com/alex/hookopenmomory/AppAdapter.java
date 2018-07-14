package com.alex.hookopenmomory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppAdapter extends RecyclerView.Adapter {
    private List<Appinfo> appinfos;
    private int selected = -1;
    private Context context;

    private OnItemClickLitener mOnItemClickLitener;

    public AppAdapter(List<Appinfo> appinfos, Context context) {
        this.appinfos = appinfos;
        this.context = context;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setSelected(int position){
        this.selected = position;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SingleViewHolder) {
            final SingleViewHolder viewHolder = (SingleViewHolder) holder;
            String appname = appinfos.get(position).getAppName();
            String apppackage = appinfos.get(position).getAppPackage();
            Drawable appicon = appinfos.get(position).getIcon();
            viewHolder.iv_icon.setImageDrawable(appicon);
            viewHolder.tv_appname.setText(appname);
            viewHolder.tv_apppackage.setText(apppackage);
            if (selected == position) {
                viewHolder.cb_select.setChecked(true);
            } else {
                viewHolder.cb_select.setChecked(false);
            }

            if (mOnItemClickLitener != null) {
                viewHolder.cb_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(viewHolder.itemView, viewHolder.getAdapterPosition());
                        if(viewHolder.cb_select.isChecked()){
//                            Config.addOne(viewHolder.tv_apppackage.getText().toString());
                            PreferenceUtils.write2Config(context, viewHolder.tv_apppackage.getText().toString());
//                            PreferenceUtils.write2Config2(viewHolder.tv_apppackage.getText().toString());
                            KillProcessById.restartAppFromPackagename(viewHolder.tv_apppackage.getText().toString(), context);
                        }
                    }
                });
            }
        }
    }



    @Override
    public int getItemCount() {
        return appinfos.size();
    }

    public void setSelection(int selection) {
        this.selected = selection;
        notifyDataSetChanged();
    }

    class SingleViewHolder extends RecyclerView.ViewHolder{
        TextView tv_appname;
        TextView tv_apppackage;
        ImageView iv_icon;
        CheckBox cb_select;

        public SingleViewHolder(View itemView) {
            super(itemView);
            tv_appname = (TextView) itemView.findViewById(R.id.tv_appname);
            tv_apppackage = (TextView) itemView.findViewById(R.id.tv_apppackage);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            cb_select = (CheckBox) itemView.findViewById(R.id.cb_select);
        }
    }
}