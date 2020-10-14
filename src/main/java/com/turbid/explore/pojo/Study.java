package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "study")
@ApiModel(description= "Study")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Study extends BaseEntity {

    @Column(name = "title",   length = 32)
    private String title;

    @Column(name = "price",   length = 32)
    private String price;

    @Column(name = "balance",   length = 32)
    private String balance;

    @Column(name = "shb",   length = 32)
    private String shb;

    @Column(name = "status",   length = 32)
    private Integer status;

    @Column(name = "pricetype",   length = 32)
    private String pricetype;

    @Column(name = "type",   length = 32)
    private String type;

    @Column(name = "indeximgurl",   length = 2000)
    private String indeximgurl;

    @Column(name = "videourl",   length = 2000)
    private String videourl;


    @Column(name = "seecount",  length = 32)
    private Integer seecount;

    @Column(name = "commentcount",  length = 32)
    private Integer commentcount;

    @Column(name = "content",  length = 2000)
    private String content;

    @Column(name = "istry")
    private Boolean istry=true;

    @ManyToOne(targetEntity = StudyGroup.class)
    @JoinColumn(name = "group_id",referencedColumnName = "code")
    private StudyGroup studyGroup;

    public String getBalance() {
        return price;
    }





    public Study(String code, Date create_time, String indeximgurl, Integer seecount, String price, String pricetype, String shb, String title, String type, StudyGroup studyGroup,String videourl) {
        this.setCode(code);
        this.setCreate_time(create_time);
        this.indeximgurl=indeximgurl;
        this.seecount=seecount;
        this.price=price;
        this.balance=price;
        this.pricetype=pricetype;
        this.shb=shb;
        this.title=title;
        this.type=type;
        this.studyGroup=studyGroup;
        this.videourl=videourl;
        this.audiourl=getAudiourl();
    }

    public Study(String videourl,String code, Date create_time, String indeximgurl, Integer seecount, String price, String pricetype, String shb, String title, String type) {
        this.videourl=videourl;
        this.setCode(code);
        this.setCreate_time(create_time);
        this.indeximgurl=indeximgurl;
        this.seecount=seecount;
        this.price=price;
        this.pricetype=pricetype;
        this.shb=shb;
        this.title=title;
        this.type=type;
    }

    public Study() {
    }

    private String audiourl;

    private Integer urltype;
    public String getAudiourl() {

        if(null!=this.videourl){
            if(this.videourl.contains("mp4")){
                this.urltype=0;
                this.audiourl= this.videourl.replace("mp4","mp3");
            }else {
                this.urltype=1;
                this.audiourl= this.videourl;
            }
        }else {
            this.audiourl=null;
        }
        return this.audiourl;
    }

}
