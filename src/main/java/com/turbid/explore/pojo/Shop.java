package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "shop")
@ApiModel(description= "Shop")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Shop extends BaseEntity{

    public Shop() {
    }

    //企业名称
    @Column(name = "name",  nullable = false, length = 32)
    private String name;

    //企业名称
    @Column(name = "companyname",  nullable = false, length = 32)
    private String companyname;

    //统一信用代码
    @Column(name = "unifiedcreditcode",  nullable = false, length = 32)
    private String unifiedcreditcode;

    //注册地址
    @Column(name = "companyaddress",  nullable = false, length = 255)
    private String companyaddress;

    //经营范围
    @Column(name = "businessscope",nullable = false,length = 2555)
    private String businessscope;

    //营业执照
    @Column(name = "businesslicense",nullable = false,length = 2555)
    private String businesslicense;

    //联系人项目
    @Column(name = "contactname",nullable = false,length = 255)
    private String contactname;

    //联系人邮箱
    @Column(name = "contactemail",nullable = false,length = 255)
    private String contactemail;

    //联系人邮箱
    @Column(name = "contactphone",nullable = false,length = 255)
    private String contactphone;

    //身份证号码
    @Column(name = "idcard",nullable = false,length = 255)
    private String idcard;

    //身份证正面
    @Column(name = "idcardpositive",nullable = false,length = 255)
    private String idcardpositive;

    //身份证正面
    @Column(name = "idcardreverse",nullable = false,length = 255)
    private String idcardreverse;

    //联系人地址
    @Column(name = "contactaddress",nullable = false,length = 255)
    private String contactaddress;

    @ApiModelProperty(value = "公司logo")
    @Column(name = "logo",length = 255)
    private String logo;


    @ApiModelProperty(value = "是否严选")
    @Column(name = "ischoose",length = 255)
    private Integer ischoose;

    @ApiModelProperty(value = "所属城市")
    @Column(name = "city",length = 255)
    private String city;

    @ApiModelProperty(value = "公司标签信息")
    @Column(name = "label",length = 255)
    private String label;

    @ApiModelProperty(value = "公司所属品牌馆")
    @Column(name = "brandgroup",length = 255)
    private String brandgroup;

    @ApiModelProperty(value = "banner")
    @Column(name = "banner",length = 255)
    private String banner;

    @ApiModelProperty(value = "公司展示图片")
    @Column(name = "company_show",length = 255)
    private String company_show;

    @ApiModelProperty(value = "公司介绍")
    @Column(name = "introduce",length = 255)
    private String introduce;

    //用户基础信息
    @OneToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    @ApiModelProperty(value = "公司状态")
    @Column(name = "status")
    private Integer status;

    @ApiModelProperty(value = "访问量")
    @Column(name = "seecount")
    private Integer seecount;

    @ApiModelProperty(value = "粉丝数")
    @Column(name = "fanscount")
    private Integer fanscount;

    @ApiModelProperty(value = "活跃度")
    @Column(name = "hat")
    private Integer hat;

    @ApiModelProperty(value = "vr地址")
    @Column(name = "vrweb")
    private String vrweb;

    @ApiModelProperty(value = "bzj")
    @Column(name = "bzj",length = 255)
    private String bzj;

    //保证金
    @Column(name = "margin",length = 10)
    private Integer margin;

    //vip有效期
    @Column(name = "vipday",length = 32)
    private String vipday;


    public Shop(String code,String companyname,String logo) {
        super();
        this.setCode(code);
        this.companyname=companyname;
        this.logo=logo;
    }
}
