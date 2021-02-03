package com.turbid.explore.pojo;
import com.turbid.explore.tools.CodeLib;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class BaseEntity implements Serializable {
    //用户编号
    @Id
    @Column(name = "code",  nullable = false, length = 32)
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String code;

    //添加时间
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time",length = 32)
    private Date create_time;

    @Column(name = "del")
    private Boolean del;

    private String addftime;

    public String getAddftime() {
        if(null!=this.create_time) {return CodeLib.getFriendlyTime(this.create_time,true);}else {
            return "";
        }
    }

    private String addtime;

    public String getAddtime() {
        if(null!=this.create_time) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = sdf.format(this.create_time.getTime());
            return dateStr;
        }else {
            return "";
        }
    }

    public Date getCreate_time() {
        if(null!=this.create_time) {
            return create_time;
        }else {
            return new Date();
        }

    }
}