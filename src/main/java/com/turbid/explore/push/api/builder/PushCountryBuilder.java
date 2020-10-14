package com.turbid.explore.push.api.builder;


import com.turbid.explore.push.api.model.PushAreas;
import com.turbid.explore.push.api.utils.ListUtil;

public class PushCountryBuilder {

    private final PushAreas.PushCountry pushCountry=new PushAreas.PushCountry();

    public PushAreas.PushCountry buid(){
        return pushCountry;
    }

    public PushCountryBuilder buildPushProvince(PushAreas.PushProvince ... pushProvince){
        pushCountry.setProvinces(ListUtil.newList(pushProvince));
        return this;
    }

    public PushCountryBuilder buildPushCountry(String country){
        pushCountry.setCountry(country);
        return this;
    }

    public PushCountryBuilder builExcludeProvinces(String ... province){
        pushCountry.setExcludeProvinces(ListUtil.newList(province));
        return this;
    }

}
