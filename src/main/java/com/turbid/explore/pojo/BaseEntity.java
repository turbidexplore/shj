package com.turbid.explore.pojo;

import com.turbid.explore.utils.CodeLib;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class BaseEntity implements Serializable {
    //用户编号
    @Id
    @Column(name = "code", unique = true, nullable = false, length = 32)
    @GenericGenerator(name = "jpa-uuid", strategy = "uuid")
    @GeneratedValue(generator = "jpa-uuid")
    private String code;

    //是否删除
    @Column(name = "isdel",length = 1)
    private boolean isdel=false;

    //添加时间
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time",length = 32)
    private Calendar create_time;

}