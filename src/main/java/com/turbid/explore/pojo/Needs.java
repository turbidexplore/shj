package com.turbid.explore.pojo;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "needs")
public class Needs extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "context")
    private String context;


}
