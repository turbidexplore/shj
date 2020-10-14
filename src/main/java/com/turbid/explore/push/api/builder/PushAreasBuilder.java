package com.turbid.explore.push.api.builder;


import com.turbid.explore.push.api.model.PushAreas;
import com.turbid.explore.push.api.utils.ListUtil;

public class PushAreasBuilder {

    private final PushAreas pushAreas=new PushAreas();

    public PushAreas build(){
        return pushAreas;
    }

    public PushAreasBuilder buildCountries(PushAreas.PushCountry ... countries){
       pushAreas.setCountries(ListUtil.newList(countries));
       return this;
    }

}
