package com.example.hyc.movieshow.utils;

import android.support.annotation.NonNull;

import com.example.hyc.movieshow.datas.sources.RealmInt;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by hyc on 16-11-8.
 */

public class GsonHelper
{

    public static Gson buildForRealm()
    {


        Type token = new TypeToken<RealmList<RealmInt>>()
        {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy()
        {
            @Override
            public boolean shouldSkipField(FieldAttributes f)
            {
                return f.getDeclaredClass().equals(RealmObject.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz)
            {
                return false;
            }
        }).registerTypeAdapter(token, new TypeAdapter<RealmList<RealmInt>>()
        {

            @Override
            public void write(JsonWriter out, RealmList<RealmInt> value) throws IOException
            {
                // Ignore
            }

            @Override
            public RealmList<RealmInt> read(JsonReader in) throws IOException
            {
                RealmList<RealmInt> list = new RealmList<RealmInt>();
                in.beginArray();
                while (in.hasNext())
                {
                    list.add(new RealmInt(in.nextInt()));
                }
                in.endArray();
                return list;
            }
        });

        return gsonBuilder.create();

    }
}
