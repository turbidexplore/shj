package com.turbid.explore.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "complaint")
@ApiModel(description= "Complaint")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Complaint extends  BaseEntity {

    @Column(name = "content",length = 500)
    private String content;

    @Column(name = "urls",length = 5000)
    private String urls;

    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "complaint_user_id",referencedColumnName = "code")
    private UserSecurity complaintUserSecurity;

    @Column(name = "status",length = 10)
    private Integer status;


}
