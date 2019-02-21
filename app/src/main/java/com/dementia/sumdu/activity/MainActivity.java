package com.dementia.sumdu.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.dementia.sumdu.R;
import com.dementia.sumdu.adapter.AllContactsAdapter;
import com.dementia.sumdu.adapter.ContactsAdapter;
import com.dementia.sumdu.adapter.SectionsPagerAdapter;
import com.dementia.sumdu.data.Contact;
import com.dementia.sumdu.data.ContactData;
import com.dementia.sumdu.data.Pidrozdil;
import com.dementia.sumdu.library.PinnedHeaderListView;
import com.dementia.sumdu.utils.DBManager;
import com.dementia.sumdu.utils.DataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    LinkedHashMap<Integer, Pidrozdil> pidrozdilLinkedHashMap;
    SectionsPagerAdapter mSectionsPagerAdapter;
    DataManager dataManager;
    SearchView searchView;
    DBManager dbManager;

    private LayoutInflater mInflater;
    private PinnedHeaderListView mListView;
    private ContactsAdapter mAdapter;
    AllContactsAdapter allContactsAdapter;

    ArrayList<ContactData> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        dataManager = DataManager.getInstance();

    }
    int stringLength;

    public void initViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbarBack);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
        dbManager = DBManager.getInstance(this);
        mListView = (PinnedHeaderListView)findViewById(R.id.list_search);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        searchView = (SearchView)findViewById(R.id.search_icon2);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                stringLength = newText.length();
                if (stringLength >= 2) {
                    tabLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    GoToSeatch goToSeatch = new GoToSeatch(newText);
                    goToSeatch.execute();
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                //newSearchInProducts(query);
                return true;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //spinner.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //mTextToolbarTitle.setVisibility(View.VISIBLE);
                //newSearchInProducts("");
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                //newSearchInProducts("");
                return false;
            }
        });
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
            mSectionsPagerAdapter.update();
            /*contacts = getContacts();
            mAdapter.updateData(contacts);
            setResult(RESULT_OK);
            */
        }
    }

    class GoToSeatch extends AsyncTask<Void, Void,  Void> {

        String query;

        public GoToSeatch(String query){
            this.query=query;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                dbManager.open();
                arrayList= dbManager.getSearch(query);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                dbManager.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(arrayList.size()>0){
                ArrayList<Contact> array2 = new ArrayList();
                for (int i = 0; i < arrayList.size(); ++i) {
                    Contact contact = new Contact();
                    contact.contactId = arrayList.get(i).getId();
                    contact.displayName = arrayList.get(i).getName();
                    contact.underText = "";
                    array2.add(contact);
                }
                if(allContactsAdapter==null){
                    allContactsAdapter = new AllContactsAdapter(array2,MainActivity.this,-1,-1);
                    mListView.setAdapter(allContactsAdapter);
                }else {
                    allContactsAdapter.updateData(array2);
                }
            }
        }
    }
}
