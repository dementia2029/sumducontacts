package com.dementia.sumdu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dementia.sumdu.R;
import com.dementia.sumdu.activity.AllContactsActivity;
import com.dementia.sumdu.activity.ViddilActivity;
import com.dementia.sumdu.data.Contact;
import com.dementia.sumdu.library.SearchablePinnedHeaderListViewAdapter;
import com.dementia.sumdu.library.StringArrayAlphabetIndexer;
import com.dementia.sumdu.utils.CircularContactView;
import com.dementia.sumdu.utils.ContactImageUtil;
import com.dementia.sumdu.utils.ImageCache;
import com.dementia.sumdu.utils.async_task_thread_pool.AsyncTaskEx;
import com.dementia.sumdu.utils.async_task_thread_pool.AsyncTaskThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vadim on 18.12.2016.
 */

public class ViddilsAdapter extends SearchablePinnedHeaderListViewAdapter<Contact> {
    private ArrayList<Contact> mContacts;
    private final int CONTACT_PHOTO_IMAGE_SIZE;
    private final int[] PHOTO_TEXT_BACKGROUND_COLORS;
    private final AsyncTaskThreadPool mAsyncTaskThreadPool = new AsyncTaskThreadPool(1, 2, 10);
    private LayoutInflater mInflater;
    int pidrozdil_id;
    Context context2;

    @Override
    public CharSequence getSectionTitle(int sectionIndex) {
        return ((StringArrayAlphabetIndexer.AlphaBetSection) getSections()[sectionIndex]).getName();
    }

    public ViddilsAdapter(ArrayList<Contact> contacts, Context context,int pidrozdil_id) {
        this.pidrozdil_id=pidrozdil_id;
        setData(contacts);
        this.context2=context;
        mInflater = LayoutInflater.from(context2);
        PHOTO_TEXT_BACKGROUND_COLORS = context2.getResources().getIntArray(R.array.contacts_text_background_colors);
        CONTACT_PHOTO_IMAGE_SIZE = context2.getResources().getDimensionPixelSize(
                R.dimen.list_item__contact_imageview_size);
    }

    public void updateData(ArrayList<Contact> contacts){
        this.mContacts = contacts;
        notifyDataSetChanged();
    }

    public void setData(final ArrayList<Contact> contacts) {
        this.mContacts = contacts;
        final String[] generatedContactNames = generateContactNames(contacts);
        setSectionIndexer(new StringArrayAlphabetIndexer(generatedContactNames, true));
    }

    private String[] generateContactNames(final List<Contact> contacts) {
        final ArrayList<String> contactNames = new ArrayList<String>();
        if (contacts != null)
            for (final Contact contactEntity : contacts)
                contactNames.add(contactEntity.displayName);
        return contactNames.toArray(new String[contactNames.size()]);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        final View rootView;
        if (convertView == null) {
            holder = new ViewHolder();
            rootView = mInflater.inflate(R.layout.listview_item, parent, false);
            holder.friendProfileCircularContactView = (CircularContactView) rootView
                    .findViewById(R.id.listview_item__friendPhotoImageView);
            holder.friendProfileCircularContactView.getTextView().setTextColor(0xFFffffff);
            holder.friendName = (TextView) rootView
                    .findViewById(R.id.listview_item__friendNameTextView);
            holder.undertext = (TextView) rootView
                    .findViewById(R.id.underme);
            holder.headerView = (TextView) rootView.findViewById(R.id.header_text);
            rootView.setTag(holder);
        } else {
            rootView = convertView;
            holder = (ViewHolder) rootView.getTag();
        }
        final Contact contact = getItem(position);
        final String displayName = contact.displayName;
        final String undertext = contact.underText;
        holder.friendName.setText(displayName);
        if(undertext.equals("")){
            holder.undertext.setVisibility(View.GONE);
        }else {
            holder.undertext.setVisibility(View.VISIBLE);
            holder.undertext.setText(undertext);
        }
        boolean hasPhoto = !TextUtils.isEmpty(contact.photoId);
        if (holder.updateTask != null && !holder.updateTask.isCancelled())
            holder.updateTask.cancel(true);
        final Bitmap cachedBitmap = hasPhoto ? ImageCache.INSTANCE.getBitmapFromMemCache(contact.photoId) : null;
        if (cachedBitmap != null)
            holder.friendProfileCircularContactView.setImageBitmap(cachedBitmap);
        else {
            final int backgroundColorToUse = PHOTO_TEXT_BACKGROUND_COLORS[position
                    % PHOTO_TEXT_BACKGROUND_COLORS.length];
            if (TextUtils.isEmpty(displayName))
                holder.friendProfileCircularContactView.setImageResource(R.drawable.ic_person_white_120dp,
                        backgroundColorToUse);
            else {
                final String characterToShow = TextUtils.isEmpty(displayName) ? "" : displayName.substring(0, 1).toUpperCase(Locale.getDefault());
                holder.friendProfileCircularContactView.setTextAndBackgroundColor(characterToShow, backgroundColorToUse);
            }
            if (hasPhoto) {
                holder.updateTask = new AsyncTaskEx<Void, Void, Bitmap>() {

                    @Override
                    public Bitmap doInBackground(final Void... params) {
                        if (isCancelled())
                            return null;
                        final Bitmap b = ContactImageUtil.loadContactPhotoThumbnail(context2, contact.photoId, CONTACT_PHOTO_IMAGE_SIZE);
                        if (b != null)
                            return ThumbnailUtils.extractThumbnail(b, CONTACT_PHOTO_IMAGE_SIZE,
                                    CONTACT_PHOTO_IMAGE_SIZE);
                        return null;
                    }

                    @Override
                    public void onPostExecute(final Bitmap result) {
                        super.onPostExecute(result);
                        if (result == null)
                            return;
                        ImageCache.INSTANCE.addBitmapToCache(contact.photoId, result);
                        holder.friendProfileCircularContactView.setImageBitmap(result);
                    }
                };
                mAsyncTaskThreadPool.executeAsyncTask(holder.updateTask);
            }
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context2, AllContactsActivity.class);
                intent.putExtra("pidrozdil_id", pidrozdil_id);
                intent.putExtra("viddil_id",contact.contactId);
                ((Activity) context2).startActivityForResult(intent, 777);
                //Toast.makeText(context2,contact.displayName,Toast.LENGTH_SHORT).show();
            }
        });
        bindSectionHeader(holder.headerView, null, position);
        return rootView;
    }

    @Override
    public boolean doFilter(final Contact item, final CharSequence constraint) {
        if (TextUtils.isEmpty(constraint))
            return true;
        final String displayName = item.displayName;
        return !TextUtils.isEmpty(displayName) && displayName.toLowerCase(Locale.getDefault())
                .contains(constraint.toString().toLowerCase(Locale.getDefault()));
    }

    @Override
    public ArrayList<Contact> getOriginalList() {
        return mContacts;
    }

    public class ViewHolder {
        public CircularContactView friendProfileCircularContactView;
        public TextView friendName, headerView,undertext;
        public AsyncTaskEx<Void, Void, Bitmap> updateTask;
    }
}
