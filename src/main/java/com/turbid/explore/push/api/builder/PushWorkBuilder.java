package com.turbid.explore.push.api.builder;



import com.turbid.explore.push.api.model.*;
import com.turbid.explore.push.api.utils.SetUtil;

import java.util.*;


public class PushWorkBuilder {

    public static final int TARGET_ALL = 1;
    public static final int TARGET_ALIAS = 2;
    public static final int TARGET_TAGS = 3;
    public static final int TARGET_RIDS = 4;
    public static final int TARGET_AREAS = 9;

    private final Push push = new Push();

    public Push build() {
        return push;
    }

    public void fillParams(String workNo, String title, String content,String key,String value,String url){
        if (push.getPushTarget() == null) {
            push.setPushTarget(new PushTarget());
        }
        push.setWorkno(workNo);
        push.getPushNotify().setTitle(title);
        push.getPushNotify().setContent(content);
        PushForward pushForward=new PushForward();
        pushForward.setNextType(2);
        pushForward.setScheme(url);
        List<PushMap> schemeDataList = new ArrayList<>();
                schemeDataList.add(new PushMap(key,value));
                pushForward.setSchemeDataList(schemeDataList);
        push.setPushForward(pushForward);

    }

    public void fillParams1(String workNo, String title, String content,String key,String value,String url){
        if (push.getPushTarget() == null) {
            push.setPushTarget(new PushTarget());
        }
        push.setWorkno(UUID.randomUUID().toString().replace("-",""));
       // push.getPushNotify().setTitle(title);
        push.getPushNotify().setContent(content);
        push.getPushNotify().setType(2);
//        PushForward pushForward=new PushForward();
//        pushForward.setNextType(2);
//        pushForward.setScheme(url);
//        List<PushMap> schemeDataList = new ArrayList<>();
//        schemeDataList.add(new PushMap(key,value));
//        pushForward.setSchemeDataList(schemeDataList);
//        push.setPushForward(pushForward);
//
    }


    public PushWorkBuilder setTargetAll(String workNo, String title, String content,String key,String value,String url) {
        this.fillParams(workNo,title,content,key,value,url);
        push.getPushTarget().setTarget(TARGET_ALL);
        return this;
    }

    public PushWorkBuilder setTargetByAlias(String workNo, String title, String content,String key,String value,String url, String ... alias) {
        this.fillParams1(workNo,title,content,key,value,url);
        push.getPushTarget().setTarget(TARGET_ALIAS);
        push.getPushTarget().setAlias(SetUtil.newSet(alias));
        return this;
    }

    public PushWorkBuilder setTargetTags(String workNo, String title, String content,String key,String value,String url, String ... tags) {
        this.fillParams(workNo,title,content,key,value,url);
        push.getPushTarget().setTarget(TARGET_TAGS);
        push.getPushTarget().setTags(SetUtil.newSet(tags));
        return this;
    }

    public PushWorkBuilder setTargetRids(String workNo, String title, String content,String key,String value,String url, String ... rids) {
        this.fillParams(workNo,title,content,key,value,url);
        push.getPushTarget().setTarget(TARGET_RIDS);
        push.getPushTarget().setRids(SetUtil.newSet(rids));
        return this;
    }

    public PushWorkBuilder setTargetByAreas(String workNo, String title, String content, PushAreas pushAreas,String key,String value,String url) {
        this.fillParams(workNo,title,content,key,value,url);
        push.getPushTarget().setTarget(TARGET_AREAS);
        push.getPushTarget().setPushAreas(pushAreas);
        return this;
    }

    public PushWorkBuilder setNotifyExtraParams(String key, String value) {
        PushMap pushMap=new PushMap();
        pushMap.setKey(key);
        pushMap.setValue(value);
        push.getPushNotify().getExtrasMapList().add(pushMap);
        return this;
    }

    public PushWorkBuilder setNotifyExtraMap(Map<String,String> extraMap) {
        push.getPushNotify().setExtrasMapList(getExtraParamsList(extraMap));
        return this;
    }


    public PushWorkBuilder setForwardExtraParams(String key, String value) {
        PushMap pushMap=new PushMap();
        pushMap.setKey(key);
        pushMap.setValue(value);
        push.getPushForward().getSchemeDataList().add(pushMap);
        return this;
    }

    public PushWorkBuilder setForwardExtraMap(Map<String,String> extraMap) {
        push.getPushForward().setSchemeDataList(getExtraParamsList(extraMap));
        return this;
    }

    public List getExtraParamsList(Map<String,String> extraMap){
        List<PushMap> pushMapList=new ArrayList<>();
        Iterator<Map.Entry<String, String>> entries = extraMap.entrySet().iterator();
        PushMap pushMap;
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            pushMap=new PushMap();
            pushMap.setKey(entry.getKey());
            pushMap.setValue(entry.getValue());
            pushMapList.add(pushMap);
        }
        return pushMapList;
    }
}
