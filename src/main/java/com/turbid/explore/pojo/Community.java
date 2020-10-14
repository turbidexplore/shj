package com.turbid.explore.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@Entity
@Table(name = "_community")
@Component
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class Community extends BaseEntity {

    @Column(name = "local")
    private String local;

    @Column(name = "label")
    private String label;

    @Column(name = "content")
    private String content;

    @Column(name = "imgs")
    private String imgs;

    @Column(name = "video")
    private String video;

    @Column(name = "type")
    private Integer type;

    @Column(name = "star")
    private int star;

    @Column(name = "isstar")
    private boolean isstar;

    @Column(name = "commentcount")
    private int commentcount;

    //用户基础信息
    @ManyToOne(targetEntity = UserSecurity.class)
    @JoinColumn(name = "user_id",referencedColumnName = "code")
    private UserSecurity userSecurity;

    @Column(name = "ucontent")
    private String ucontent;

    public String getUcontent() {
        return Community.unicode2String(content);
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        int len = source.length();
        StringBuilder buf = new StringBuilder(source.length());
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                buf.append(codePoint);
            } else {
                buf.append(string2Unicode(String.valueOf(codePoint)));
            }
        }
        return buf.toString();

    }

    /***
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        if (TextUtils.isEmpty(unicode)) {
            return "";
        }
        StringBuffer string = new StringBuffer();
        int a = unicode.indexOf("\\u");
        if (a < 0) {
            return unicode;
        } else {
            if (a != 0) {
                string.append(unicode.substring(0, a));
            }
            String[] hex = unicode.split("\\\\u");
            try {
                for (int i = 1; i < hex.length; i++) {
                    if (hex[i].length() > 4) {
                        String q = hex[i].substring(0, 4);
                        // 转换出每一个代码点
                        int data = Integer.parseInt(q, 16);
                        // 追加成string
                        string.append((char) data);
                        String b = hex[i].substring(4);
                        string.append(b);
                    } else {
                        // 转换出每一个代码点
                        int data = Integer.parseInt(hex[i], 16);
                        // 追加成string
                        string.append((char) data);
                    }
                }
            } catch (Exception e) {
                string.append("??");
            }
            return string.toString();
        }
    }


    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }


    /**
     * 检测是否有emoji字符
     *
     * @param source
     * @return 一旦含有就抛出
     */
    private static boolean containsEmoji(String source) {
        if (isBlank(source)) {
            return false;
        }
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(String str) {
        int var1;
        if (str != null && (var1 = str.length()) != 0) {
            for (int var2 = 0; var2 < var1; ++var2) {
                if (!Character.isWhitespace(str.charAt(var2))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}
