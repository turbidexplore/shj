package com.turbid.explore.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity
@Table(name = "_daytask")
@Component
public class DayTask extends BaseEntity{

    //用户基础信息
    @ManyToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "userSecurity",referencedColumnName = "code")
    private UserSecurity userSecurity;

    private Integer taska;

    private Integer taskb;

    private Integer taskc;

    private Integer taskd;

    private Integer taske;

    private Integer taskf;

    private Integer taskg;

    private Integer taskh;

    private Integer taski;

    private Integer taskj;

    private Integer taskk;

    private Integer taskl;

    private String time;

    public Integer getTaska() {
        return taska;
    }

    public void setTaska() {
        this.taska =this.taska+1;
    }


    public void setTaskb() {
        this.taskb = this.taskb+1;
    }


    public void setTaskc() {
        this.taskc = this.taskc+1;
    }


    public void setTaskd() {
        this.taskd = this.taskd+1;
    }


    public void setTaske() {
        this.taske = this.taske+1;
    }


    public void setTaskf() {
        this.taskf = this.taskf+1;
    }

    public void setTaskg() {
        this.taskg = this.taskg+1;
    }


    public void setTaskh() {
        this.taskh = this.taskh+1;
    }


    public void setTaski() {
        this.taski = this.taski+1;
    }

    public void setTaskj() {
        this.taskj = this.taskj+1;
    }
    public void setTaskk() {
        this.taskk = this.taskk+1;
    }

    public void setTaskl() {
        this.taskl = this.taskl+1;
    }

    public DayTask() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        this.time=dateStr;
        this.taska=0;

        this.taskb=0;

        this.taskc=0;

        this.taskd=0;

        this.taske=0;

        this.taskf=0;

        this.taskg=0;

        this.taskh=0;

        this.taski=0;

        this.taskj=0;

        this.taskk=0;

        this.taskl=0;
    }
}

