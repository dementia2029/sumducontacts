package com.dementia.sumdu.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Viddil {
    LinkedHashMap<Integer, ContactData> arrayList;
    int id, pidrozdil_id;
    String department;

    public Viddil(int id, int pidrozdil_id, String department, ContactData contactData) {
        this.id = id;
        this.pidrozdil_id = pidrozdil_id;
        this.department = department;
        this.arrayList = new LinkedHashMap<Integer, ContactData>();
        this.arrayList.put(contactData.getId(),contactData);
    }
    public void addData(ContactData contactData){
        arrayList.put(contactData.getId(),contactData);
    }
    public  LinkedHashMap<Integer, ContactData> getData(){
        return arrayList;
    }
    public int getId(){
        return id;
    }
    public int getPidrozdil_id(){
        return pidrozdil_id;
    }
    public String getName(){
        return department;
    }
}
