package com.uboxol.cloud.pandorasBox;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import java.io.File;

/**
 * 发送邮件工具
 *
 * @author liyunde
 * @since 2019-05-24 14:17
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MailUtils {

    private static final String SERVER = "smtp.uboxol.com";
    private static final int SERVER_PORT = 465;
    private static final String MAIL_FROM = "友宝结算中心告警";
    private static final String MAIL_USER = "monit2@uboxol.com";
    private static final String MAIL_SECRET = "ubox2012";

    private static final String COMMON_SUBJECT = "结算中心告警信息！";

    /**
     * 发送简单邮件
     *
     * @param emails 接收人
     * @param content 内容
     */
    public static void send(String title, String content, String... emails) throws EmailException {

        logger.info("发送邮件,TO:" + StringUtils.join(emails, ","));
        logger.info("发送邮件Tit:{},Body:{}", title, content);

        if (emails.length == 0) {
            logger.warn("没有邮件地址");
            return;
        }

        Email m = new SimpleEmail();

        init(m);

        if (title == null || title.isEmpty()) {
            title = COMMON_SUBJECT;
        }

        m.setSubject(title);

        m.setMsg(content);

        m.addTo(emails);

//        m.addCc(); //抄送
//
//        m.addBcc();//密送

        String id = m.send();

        logger.info("邮件 {} 已发送,messageID:{}", emails, id);
    }

    public static void sendWithAttachFile(String title, String content, File file, String... emails) throws EmailException {

        logger.info("发送邮件,TO:" + StringUtils.join(emails, ","));
        logger.info("发送邮件Tit:{},Body:{}", title, content);

        if (emails.length == 0) {
            logger.warn("没有邮件地址");
            return;
        }

        MultiPartEmail m = new MultiPartEmail();

        init(m);

        if (title == null || title.isEmpty()) {
            title = COMMON_SUBJECT;
        }

        m.setSubject(title);

        m.setMsg(content);

        m.addTo(emails);

        m.attach(file);

//        m.addCc(); //抄送
//
//        m.addBcc();//密送

        String id = m.send();

        logger.info("邮件 {} 已发送,messageID:{}", emails, id);
    }

    /**
     * 设置服务器信息
     *
     * @param m Email
     *
     * @throws EmailException 异常
     */
    private static void init(Email m) throws EmailException {

        m.setHostName(SERVER);

        m.setSmtpPort(SERVER_PORT);

        m.setSSLOnConnect(true);

        m.setStartTLSEnabled(false);

        m.setSSLCheckServerIdentity(false);

        m.setAuthentication(MAIL_USER, MAIL_SECRET);

        m.setFrom(MAIL_USER, MAIL_FROM);
    }

}
