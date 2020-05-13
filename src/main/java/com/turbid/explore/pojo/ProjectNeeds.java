package com.turbid.explore.pojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turbid.explore.tools.CodeLib;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "projectneeds")
@ApiModel(description= "产品需求实体")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class ProjectNeeds extends BaseEntity{

    @ApiModelProperty(value = "订单号")
    @Column(name = "orderno")
    private String orderno;

    @ApiModelProperty(value = "产品品类")
    @Column(name = "category")
    private String category;

    @ApiModelProperty(value = "产品风格")
    @Column(name = "style")
    private String style;

    @ApiModelProperty(value = "需求标题")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "需求类型0是产品1是设计")
    @Column(name = "type")
    private String type;

    @ApiModelProperty(value = "需求内容")
    @Column(name = "context")
    private String context;

    @ApiModelProperty(value = "所限城市")
    @Column(name = "city")
    private String city;

    @ApiModelProperty(value = "附件")
    @Column(name = "url",length = 5000)
    private String url;

    @ApiModelProperty(value = "结束时间")
    @Column(name = "overtime")
    private String overtime;

    @ApiModelProperty(value = "实效")
    @Column(name = "timeout")
    private Integer timeout;

    @ApiModelProperty(value = "需求状态")
    @Column(name = "status")
    private Integer status;

    @ApiModelProperty(value = "是否加急0不加急1加急")
    @Column(name = "isurgent")
    private Integer isurgent;

    @ApiModelProperty(value = "浏览量")
    @Column(name = "browsecount")
    private Integer browsecount;

    @ApiModelProperty(value = "聊天量")
    @Column(name = "chatcount")
    private Integer chatcount;

    @ApiModelProperty(value = "发布者")
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="usercode")
    private UserSecurity userSecurity;


    public ProjectNeeds() {
    }


}
