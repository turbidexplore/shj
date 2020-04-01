package com.turbid.explore.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 用户认证信息
 */
@Data
@Entity
@Table(name = "user_auth")
public class UserAuth extends BaseEntity {

    //企业名称
    @Column(name = "companyname", unique = true, nullable = false, length = 32)
    private String companyname;

    //统一信用代码
    @Column(name = "unifiedcreditcode", unique = true, nullable = false, length = 32)
    private String unifiedcreditcode;

    //注册地址
    @Column(name = "companyaddress", unique = true, nullable = false, length = 32)
    private String companyaddress;

    //经营范围
    @Column(name = "businessscope",unique = true,nullable = false,length = 2555)
    private String businessscope;

    //营业执照
    @Column(name = "businesslicense",unique = true,nullable = false,length = 2555)
    private String businesslicense;

    //联系人项目
    @Column(name = "contactname",unique = true,nullable = false,length = 255)
    private String contactname;

    //联系人邮箱
    @Column(name = "contactemail",unique = true,nullable = false,length = 255)
    private String contactemail;

    //联系人邮箱
    @Column(name = "contactphone",unique = true,nullable = false,length = 255)
    private String contactphone;

    //身份证号码
    @Column(name = "idcard",unique = true,nullable = false,length = 255)
    private String idcard;

    //身份证正面
    @Column(name = "idcardpositive",unique = true,nullable = false,length = 255)
    private String idcardpositive;

    //身份证正面
    @Column(name = "idcardreverse",unique = true,nullable = false,length = 255)
    private String idcardreverse;

    //联系人地址
    @Column(name = "contactaddress",unique = true,nullable = false,length = 255)
    private String contactaddress;

    //审核状态
    @Column(name = "status",unique = true,nullable = false,length = 1)
    private Integer status;




}
