package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "city_cn")
@JsonIgnoreProperties(value = { "hibernateEAGERInitializer"})
public class City_CN {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "state_code")
    private String state_code;

    @Column(name = "city_code")
    private String city_code;

    @Column(name = "country_code")
    private String country_code;
}
