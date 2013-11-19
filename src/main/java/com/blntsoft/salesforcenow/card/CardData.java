package com.blntsoft.salesforcenow.card;

import android.content.Context;

import com.orm.SugarRecord;

/**
 * Created by baolongnt on 11/19/13.
 */
public class CardData extends SugarRecord<CardData> {

    public String name;
    public String value;

    public CardData(Context ctx){
        super(ctx);
    }

    public CardData(Context ctx, String name, String value){
        super(ctx);
        this.name = name;
        this.value = value;
    }

}
