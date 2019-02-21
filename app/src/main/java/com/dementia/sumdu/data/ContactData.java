package com.dementia.sumdu.data;

public class ContactData {
    private int id, vid_id;
    private String name, position, internal_phone,city_phone,cabinet,email,note,fav;

    public ContactData(int id, String name, String position, String internal_phone,
                       String city_phone, String cabinet, String email, String note,
                       int vid_id,String fav){
        this.id=id;
        this.name=name;
        this.position=position;
        this.internal_phone=internal_phone;
        this.city_phone=city_phone;
        this.cabinet=cabinet;
        this.email=email;
        this.note=note;
        this.vid_id=vid_id;
        this.fav=fav;
    }
    public int getId(){
        return id;
    }
    public String getFav(){
        return fav;
    }
    public int getVid_id(){
        return vid_id;
    }
    public String getName(){
        return name;
    }
    public String getPosition(){
        return position;
    }
    public String getInternal_phone(){
        return internal_phone;
    }
    public String getCity_phone(){
        return city_phone;
    }
    public String getCabinet(){
        return cabinet;
    }
    public String getEmail(){
        return email;
    }
    public String getNote(){
        return note;
    }
}
