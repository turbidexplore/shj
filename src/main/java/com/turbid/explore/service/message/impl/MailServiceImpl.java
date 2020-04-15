package com.turbid.explore.service.message.impl;

import com.sun.mail.util.MailSSLSocketFactory;
import com.turbid.explore.repository.MessageRepository;
import com.turbid.explore.service.message.MailService;
import com.turbid.explore.tools.CodeLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.account}")
    private String account;//登录用户名
    @Value("${spring.mail.pass}")
    private String pass;        //登录密码
    @Value("${spring.mail.from}")
    private String from;        //发件地址
    @Value("${spring.mail.host}")
    private String host;        //服务器地址
    @Value("${spring.mail.port}")
    private String port;        //端口
    @Value("${spring.mail.protocol}")
    private String protocol; //协议
    @Autowired
    private MessageRepository messageRepository;

    //用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication
    static class MyAuthenricator extends Authenticator {
        String u = null;
        String p = null;

        public MyAuthenricator(String u, String p) {
            this.u = u;
            this.p = p;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(u, p);
        }
    }

    public MimeMessage mimeMessage(){
        Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", protocol);
        //服务器
        prop.setProperty("mail.smtp.host", host);
        //端口
        prop.setProperty("mail.smtp.port", port);
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        //使用SSL，企业邮箱必需！
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account, pass));
        session.setDebug(true);
        return  new MimeMessage(session);
    }

    public void send(String mail) {

        MimeMessage mimeMessage=mimeMessage();
        try {
            mimeMessage.setFrom(new InternetAddress(from, "德玛西亚"));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
            mimeMessage.setSubject("德玛西亚万岁");
            mimeMessage.setSentDate(new Date());
            mimeMessage.setText("您在XXX使用了密码重置功能，请点击下面链接重置密码:\n"
                    + "http://localhost:/XXX/ResetPassword?id=123456789");
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void sendHtmlMail(String mail){
        try {
            String authcode= CodeLib.randomCode(6,1);
            MimeMessage mimeMessage=mimeMessage();
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(htmltext(authcode), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mimeMessage.setContent(mainPart);
            mimeMessage.setFrom(new InternetAddress(from, "浑浊探索"));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
            mimeMessage.setSubject("来自浑浊探索验证码 | 去！探索与众不同.");
            mimeMessage.setSentDate(new Date());
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
            messageRepository.save(new com.turbid.explore.pojo.Message(authcode,null,mail));
        }catch (Exception e){

        }
    }

    private String htmltext(String authcode){
       // border:1px solid #00FF00;border-radius:8px;
        return "<div style='background:#0b2145;padding:10px;'>" +
                    "<div style='color:#D0D0D0;margin-bottom:10px;font-weight:bold;'>" +
                        "<p style='font-size:26px;margin-bottom:3px;'>尊敬的用户：</p>" +
                        "<p style='font-size:14px;color:#F8F8F8'>以下是您操作账户时所需要的授权码: </p>" +
                        "<p style='color:#00FF00;font-size:24px;'> "+authcode+" <p>" +
                        "<div style='background-color:RGBA(0,0,0,.2);margin:10px;font-size:8px;color:#00FF33;padding:10px;'>" +
                            "<p style='color:#c6d4df'>此次授权码10分钟内有效。授权码是您身份标识，为了您的账户安全，请勿泄漏。浑浊世界，探索与众不同！</p><br>" +
                            "<p style='color:#c6d4df'>要完成登录，您将需要 Turbid 授权码。<span style='color:#ffffff'>无人可以在不访问这封电子邮件的前提下访问您的帐户。</span></p><br>" +
                            "<p style='color:#c6d4df'><span style='color:#ffffff'>如果您未曾试图登录</span>，请更改您的 Turbid 密码，并考虑更改您的电子邮件密码，确保您的帐户安全。</p><br>" +
                            "<p style='color:#61696d'>如果您无法访问您的帐户，那么可以<a style='color:#6d7880;font-size:12px;' href='https://turbidexplore.com'>使用该帐户专用救援链接</a>以获得救援或自行锁定您的帐户的协助。</p>" +
                        "</div>" +
                        "<p style='color:#6d7880;font-size:10px;'>TURBID 团队 <p>" +
                    "</div>" +
                "</div>" +
                "<div style='background:#ffffff;color:#909090;font-size:12px;text-align:center;padding-bottom:8px;'>Copyright ©2019 浑浊探索 All rights Reserved.</div>";
    }





}
