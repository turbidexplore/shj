package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "_joincount")
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Joincount extends BaseEntity {

    @Column(name = "type")
    private String type;

}
