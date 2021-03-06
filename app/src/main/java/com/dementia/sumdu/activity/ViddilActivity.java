package com.dementia.sumdu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.dementia.sumdu.R;
import com.dementia.sumdu.adapter.ContactsAdapter;
import com.dementia.sumdu.adapter.ViddilsAdapter;
import com.dementia.sumdu.data.Contact;
import com.dementia.sumdu.data.Pidrozdil;
import com.dementia.sumdu.data.Viddil;
import com.dementia.sumdu.library.PinnedHeaderListView;
import com.dementia.sumdu.utils.DBManager;
import com.dementia.sumdu.utils.DataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class ViddilActivity extends AppCompatActivity {
    private LayoutInflater mInflater;
    private PinnedHeaderListView mListView;
    private ViddilsAdapter mAdapter;
    int id_pidrozdil;
    Toolbar toolbar;
    DataManager dataManager;
    ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viddil);
        toolbar = (Toolbar) findViewById(R.id.toolbarBack2);

        id_pidrozdil = getIntent().getIntExtra("pidrozdil_id",0);
        mInflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contacts = getContacts();
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                char lhsFirstLetter = TextUtils.isEmpty(lhs.displayName) ? ' ' : lhs.displayName.charAt(0);
                char rhsFirstLetter = TextUtils.isEmpty(rhs.displayName) ? ' ' : rhs.displayName.charAt(0);
                int firstLetterComparison = Character.toUpperCase(lhsFirstLetter) - Character.toUpperCase(rhsFirstLetter);
                if (firstLetterComparison == 0)
                    return lhs.displayName.compareTo(rhs.displayName);
                return firstLetterComparison;
            }
        });
        mListView = (PinnedHeaderListView) findViewById(R.id.list7);
        mAdapter = new ViddilsAdapter(contacts,this,id_pidrozdil);

        int pinnedHeaderBackgroundColor = getResources().getColor(getResIdFromAttribute(this, android.R.attr.colorBackground));
        mAdapter.setPinnedHeaderBackgroundColor(pinnedHeaderBackgroundColor);
        mAdapter.setPinnedHeaderTextColor(getResources().getColor(android.R.color.white));
        mListView.setPinnedHeaderView(mInflater.inflate(R.layout.pinned_header_listview_side_header, mListView, false));
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mAdapter);
        mListView.setEnableHeaderTransparencyChanges(false);
    }

    public ArrayList<Contact> getContacts(){
        dataManager = DataManager.getInstance();
        LinkedHashMap<Integer, Pidrozdil> alldata = dataManager.getlldata();
        LinkedHashMap<Integer,Viddil> viddils = alldata.get(id_pidrozdil).getViddils();
        ArrayList<Viddil> viddils1 = new ArrayList<>(viddils.values());
        ArrayList<Contact> array2 = new ArrayList();
        for (int i = 0; i < viddils1.size(); ++i) {
            Contact contact = new Contact();
            contact.contactId=viddils1.get(i).getId();
            contact.displayName = viddils1.get(i).getName();
            contact.underText = "";
            array2.add(contact);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(alldata.get(id_pidrozdil).getPartition());
        return array2;
    }

    public static int getResIdFromAttribute(final Activity activity, final int attr) {
        if (attr == 0)
            return 0;
        final TypedValue typedValue = new TypedValue();
        activity.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            DBManager dbManager = DBManager.getInstance(this);
            try {
                dbManager.open();
                dataManager.setAlldata(dbManager.getAllData());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                dbManager.close();
            }
            contacts = getContacts();
            mAdapter.updateData(contacts);
            setResult(RESULT_OK);
        }
    }
}
