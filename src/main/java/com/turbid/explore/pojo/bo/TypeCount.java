package com.turbid.explore.pojo.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class TypeCount extends BaseEntity {

    private Long count;

    private Integer type;

    public TypeCount(Long count,Integer type) {
        this.count=count;
        this.type=type;
    }
}
