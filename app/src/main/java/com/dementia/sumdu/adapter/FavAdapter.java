package com.dementia.sumdu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dementia.sumdu.R;
import com.dementia.sumdu.activity.ShowInfo;
import com.dementia.sumdu.data.ContactData;

import java.util.ArrayList;
import java.util.Locale;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    private final int[] PHOTO_TEXT_BACKGROUND_COLORS;
    private ArrayList<ContactData> contactDatas;
    private Context mContext;

    public FavAdapter(ArrayList<ContactData> contactDatas, Context context) {
        PHOTO_TEXT_BACKGROUND_COLORS = context.getResources().getIntArray(R.array.contacts_text_background_colors);
        this.contactDatas = contactDatas;
        mContext = context;
    }

    public void setNewReciepts(ArrayList<ContactData> contactDatas){
        this.contactDatas = contactDatas;
        notifyDataSetChanged();
    }

    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_fav, viewGroup, false);

        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(FavAdapter.ViewHolder viewHolder, int index) {
        ContactData contactData = contactDatas.get(index);
        final int backgroundColorToUse = PHOTO_TEXT_BACKGROUND_COLORS[index
                % PHOTO_TEXT_BACKGROUND_COLORS.length];
        viewHolder.layoutItemRecipes.setBackgroundColor(backgroundColorToUse);
        viewHolder.textView.setText(contactData.getName());
        viewHolder.layoutItemRecipes.setClickable(true);
        viewHolder.layoutItemRecipes.setOnClickListener(new ItemClickListener(mContext,contactData.getId()));

    }

    class ItemClickListener implements View.OnClickListener {
        private Context mContext;
        private int mId;

        public ItemClickListener(Context context,int id) {
            mContext = context;
            mId = id;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, ShowInfo.class);
            intent.putExtra("contact_id",mId);
            ((Activity) mContext).startActivityForResult(intent, 777);
        }
    }

    @Override
    public int getItemCount() {
        return contactDatas != null ? contactDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View layoutItemRecipes;
        public TextView textView;


        public ViewHolder(View itemView) {
            super(itemView);
            layoutItemRecipes = itemView.findViewById(R.id.view_m);
            textView = (TextView) itemView.findViewById(R.id.fav_contact);
        }
    }
}