package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "studygroup")
@ApiModel(description= "studygroup")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class StudyGroup extends BaseEntity {

    @Column(name = "title",   length = 32)
    private String title;

    @Column(name = "status",   length = 32)
    private Integer status;

    @Column(name = "type",   length = 32)
    private String type;

    @Column(name = "indeximgurl",   length = 2000)
    private String indeximgurl;

    @Column(name = "content",  length = 2000)
    private String content;

    @Column(name = "teachername")
    private String teachername;

    @Column(name = "teacherheadurl")
    private String teacherheadurl;

    @Column(name = "teacherdesc",  length = 2000)
    private String teacherdesc;

    public StudyGroup() {

    }


}
