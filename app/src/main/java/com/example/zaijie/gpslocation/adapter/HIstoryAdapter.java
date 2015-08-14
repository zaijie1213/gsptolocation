package com.example.zaijie.gpslocation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zaijie.gpslocation.R;
import com.example.zaijie.gpslocation.bean.History;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zaijie on 15/8/13.
 *
 */
public class HIstoryAdapter extends RecyclerView.Adapter<HIstoryAdapter.ViewHolder> {

    List<History> histories;

    public HIstoryAdapter(List<History> histories) {
        this.histories = histories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mLat.setText(histories.get(position).getLati());
        holder.mLont.setText(histories.get(position).getLont());
        holder.mLoc.setText(histories.get(position).getLoc());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'history_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.lat)
        TextView mLat;
        @Bind(R.id.lont)
        TextView mLont;
        @Bind(R.id.loc)
        TextView mLoc;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
