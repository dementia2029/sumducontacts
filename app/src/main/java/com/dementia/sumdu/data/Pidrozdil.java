package com.dementia.sumdu.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Pidrozdil {
    int id;
    String partition,category;
    ArrayList<Viddil> viddilArrayList;
    LinkedHashMap<Integer, Viddil> groupViddil;

    public Pidrozdil(int id, String partition, String category, Viddil viddil){
        this.id=id;
        this.partition=partition;
        this.category=category;
        groupViddil = new LinkedHashMap<>();
        groupViddil.put(viddil.getId(),viddil);
        //this.viddilArrayList = new ArrayList<>();
        //this.viddilArrayList.add(viddil);
    }
    public int getId(){
        return id;
    }
    public String getPartition(){
        return partition;
    }
    public String getCategory(){
        return category;
    }
    public  LinkedHashMap<Integer, Viddil> getViddils(){return  groupViddil;}
    public void addData(Viddil viddil){
        groupViddil.put(viddil.getId(),viddil);
    }
}
