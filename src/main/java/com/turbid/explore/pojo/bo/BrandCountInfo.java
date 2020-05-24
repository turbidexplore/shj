package com.turbid.explore.pojo.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class BrandCountInfo extends BaseEntity {

    private String name;

    private Integer count;

    public BrandCountInfo(String name,Long count) {
        this.name=name;
        this.count=Integer.parseInt(count.toString());
    }
}
