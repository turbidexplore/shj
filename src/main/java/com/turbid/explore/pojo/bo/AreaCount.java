package com.turbid.explore.pojo.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.pojo.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class AreaCount extends BaseEntity {

    private String name;

    private Integer y;

    public AreaCount(String name, Long y) {
        if(null!=name&&""!=name&&!"".equals(name)){
            this.name = name;
        }else {
            this.name="未知区域";
        }
        this.y = Integer.parseInt(y.toString());
    }
}
