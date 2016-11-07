package com.example.hyc.movieshow.utils;

import android.support.annotation.WorkerThread;

import org.reactivestreams.Subscriber;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.realm.Realm;

/**
 * Created by hyc on 16-11-7.
 */

public class RealmHelper
{

    // @formatter:off
    /**
     * 在当前线程创建一个Realm实例
     * eg:
     *
     * @return Realm 实例
     * @WorkerThread
     * public void test(){
     *    //此时 realm是在 主线程创建的   迷！！
     *     Realm realm=Realm.getDefaultInstance()
     *   }
     * 使用Rx 链式创建 避免线程问题
     */
    // @formatter:on
    public static Flowable<Realm> getCurrentThreadRealm()
    {
        return Flowable.create(new FlowableOnSubscribe<Realm>()
        {
            @Override
            public void subscribe(FlowableEmitter<Realm> e) throws Exception
            {
                Realm realm = null;
                try
                {

                    realm = Realm.getDefaultInstance();
                } catch (Exception e1)
                {
                    e.onError(new UnsupportedOperationException("Realm build error = " + e1.getMessage()));

                }
                Subscriber subscriber;


                e.onNext(realm);
            }
        }, BackpressureStrategy.BUFFER);
    }
}
