package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "studyhis")
@ApiModel(description= "studyhis")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class StudyHis extends BaseEntity {

    @Column(name = "usercode",   length = 32)
    private String usercode;

    @Column(name = "studycode",   length = 32)
    private String studyCode;

    @Column(name = "status",   length = 32)
    private String studygroupCode;
}
