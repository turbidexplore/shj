package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "brand")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Brand extends BaseEntity{
    public Brand() {
    }

    @ApiModelProperty(value = "品牌logo")
    @Column(name = "logo",length = 255)
    private String logo;

    @ApiModelProperty(value = "品牌名称")
    @Column(name = "name",length = 255)
    private String name;

    @ApiModelProperty(value = "品牌标签")
    @Column(name = "label",length = 255)
    private String label;

    @ApiModelProperty(value = "品牌内容")
    @Column(name = "content",length = 5000)
    private String content;

    @ApiModelProperty(value = "品牌介绍")
    @Column(name = "introduce",length = 500)
    private String introduce;

    @ApiModelProperty(value = "招商信息")
    @Column(name = "business",length = 500)
    private String business;

    @ApiModelProperty(value = "所属品牌馆")
    @Column(name = "brandgroup",length = 255)
    private String brandgroup;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)
    @JoinColumn(name="company_code")
    private Shop company;

    //评论信息
    @OneToMany(cascade= CascadeType.ALL,fetch=FetchType.EAGER)
    private List<Comment> comments;

    public Brand(String code,String name,String logo) {
        super();
        this.setCode(code);
        this.name=name;
        this.logo=logo;
    }

    public String getContent() {
        return content.replace("<img","<br><img");
    }
}
