package com.example.hyc.movieshow.datas.sources;

import io.realm.RealmObject;

/**
 * Created by hyc on 2016/11/6.
 */

public class RealmInt extends RealmObject {
    private int val;
    public RealmInt(int val){
        this.val=val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
    public RealmInt(){

    }
}
