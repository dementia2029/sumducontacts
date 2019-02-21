package com.dementia.sumdu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dementia.sumdu.R;
import com.dementia.sumdu.data.ContactData;
import com.dementia.sumdu.utils.DBManager;
import com.dementia.sumdu.utils.DataManager;

import java.io.IOException;

public class ShowInfo extends AppCompatActivity {
    Toolbar toolbar;
    ContactData contactData;
    int contact_id, pidrozdil_id, viddil_id;
    DataManager dataManager;
    DBManager dbManager;

    String fav_old, fav_new;

    TextView name, position,number1,number2,place2,email2,text1,text2;

    RelativeLayout rr_place,rr_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contact_id = getIntent().getIntExtra("contact_id", 0);
        pidrozdil_id = getIntent().getIntExtra("pidrozdil_id", -1);
        viddil_id = getIntent().getIntExtra("viddil_id", -1);
        dbManager = DBManager.getInstance(this);
        if(pidrozdil_id!=-1){
            dataManager = DataManager.getInstance();
            contactData = dataManager.getlldata().get(pidrozdil_id).getViddils().get(viddil_id).getData().get(contact_id);
        }else {
            try {
                dbManager.open();
                contactData = dbManager.getContact(contact_id);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                dbManager.close();
            }

        }

        name = (TextView)findViewById(R.id.name);
        name.setText(contactData.getName());
        rr_place = (RelativeLayout)findViewById(R.id.rr_place);
        rr_email = (RelativeLayout)findViewById(R.id.rr_email);
        text2 = (TextView)findViewById(R.id.text2);
        position = (TextView)findViewById(R.id.position);
        if(!contactData.getPosition().equals("")){
            position.setText(contactData.getPosition());
        }
        number1 = (TextView)findViewById(R.id.number1);
        number1.setText(contactData.getCity_phone());

        number2 = (TextView)findViewById(R.id.number2);
        if(!contactData.getInternal_phone().equals("")){
            number2.setText(contactData.getInternal_phone());
        }else {
            text2.setVisibility(View.GONE);
            number2.setVisibility(View.GONE);
        }

        place2 = (TextView)findViewById(R.id.place2);
        if(!contactData.getCabinet().equals("")){
            place2.setText(contactData.getCabinet());
        }else {
            rr_place.setVisibility(View.GONE);
        }

        email2 = (TextView)findViewById(R.id.email2);
        if(!contactData.getEmail().equals("")){
            email2.setText(contactData.getEmail());
        }else {
            rr_email.setVisibility(View.GONE);
        }
        fav_old = contactData.getFav();
        if(fav_old==null){
            fav_old="0";
        }
        if(!fav_old.equals("1")){
            fav_old = "0";
        }
        fav_new=fav_old;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav, menu);
        boolean isFav_heart = fav_new.equals("1");
        menu.findItem(R.id.remove_fav).setVisible(isFav_heart).setEnabled(isFav_heart);
        menu.findItem(R.id.add_fav).setVisible(!isFav_heart).setEnabled(!isFav_heart);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_fav) {
            fav_new = "1";
            invalidateOptionsMenu();
            if(!fav_old.equals(fav_new)){
                try {
                    dbManager.open();
                    dbManager.saveFav(contact_id,fav_new);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    dbManager.close();
                }
                setResult(RESULT_OK);
            }else {
                setResult(RESULT_CANCELED);
            }
            return true;
        } if (id == R.id.remove_fav) {
            fav_new = "0";
            invalidateOptionsMenu();
            if(!fav_old.equals(fav_new)){
                try {
                    dbManager.open();
                    dbManager.saveFav(contact_id,fav_new);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    dbManager.close();
                }
                setResult(RESULT_OK);
            }else {
                setResult(RESULT_CANCELED);
            }
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
