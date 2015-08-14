package com.example.zaijie.gpslocation.bean;

import com.orm.SugarRecord;

/**
 * Created by zaijie on 15/8/13.
 */
public class History extends SugarRecord<History> {

     String lati;
     String lont;

    public String getLati() {
        return lati;
    }

    public String getLont() {
        return lont;
    }

    public String getLoc() {
        return loc;
    }

    String loc;


    public History(String lati, String lont, String loc) {

        this.lati = lati;
        this.lont = lont;
        this.loc = loc;
    }


    public History() {
    }
}
