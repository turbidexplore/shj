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
    }
}

