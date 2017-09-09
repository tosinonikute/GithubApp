package com.githubapp.ui.list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.githubapp.R;
import com.githubapp.data.model.Item;
import com.githubapp.data.model.Users;
import com.githubapp.ui.detail.DetailActivity;
import com.githubapp.ui.webview.WebviewActivity;
import com.githubapp.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Tosin Onikute.
 */

public class ListAdapter
        extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<Item> mItem;
    private Context mContext;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final TextView nameText;
        public final TextView urlText;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            imageView = (ImageView) view.findViewById(R.id.github_image);
            nameText = (TextView) view.findViewById(R.id.name);
            urlText = (TextView) view.findViewById(R.id.url);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public String getValueAt(int position) {
        return String.valueOf(mItem.get(position).getId().toString());
    }

    public ListAdapter(Context context, List<Item> mItem) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        this.mContext = context;
        mBackground = mTypedValue.resourceId;
        this.mItem = mItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_users, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        /* Set your values */
        final Item model = (Item) mItem.get(position);

        String name = "";
        String url = "";
        String imageUrl = "";
        if(model.getLogin() != null) name = model.getLogin();
        if(model.getHtmlUrl() != null) url = model.getHtmlUrl();
        if(model.getAvatarUrl() != null) imageUrl = model.getAvatarUrl();

        holder.nameText.setText(mContext.getString(R.string.names) + " " + name);
        holder.urlText.setText(mContext.getString(R.string.url) + " " +url);
        ImageUtil.displayImage(holder.imageView.getContext(), imageUrl, holder.imageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name", model.getLogin());
                context.startActivity(intent);
            }
        });

        holder.urlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("EXTRA_URL", model.getHtmlUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mItem ? mItem.size() : 0);
    }

    public void addAll(List<Item> data){
        notifyDataSetChanged();
    }

    public void add(Item data){
        notifyDataSetChanged();
        mItem.add(data);
    }


    public Item getItemPos(int pos){
        return mItem.get(pos);
    }

    public void clear(){
        mItem.clear();
    }

}

