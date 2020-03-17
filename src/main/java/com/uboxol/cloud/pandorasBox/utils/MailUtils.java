package com.uboxol.cloud.pandorasBox.utils;

import com.uboxol.cloud.pandorasBox.cfg.MailConfig;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Component
public class MailUtils {
    private static final String HOST = MailConfig.host;
    private static final Integer PORT = MailConfig.port;
    private static final String USERNAME = MailConfig.userName;
    private static final String PASSWORD = MailConfig.passWord;
    private static final String EMAILFROM = MailConfig.emailForm;
    private static final String TIMEOUT = MailConfig.timeout;
    private static final String PERSONAL = MailConfig.personal;
    private static JavaMailSenderImpl mailSender = createMailsender();


    /**
     * 配置好的工具
     *
     * @return
     */
    private static JavaMailSenderImpl createMailsender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(HOST);
        sender.setPort(PORT);
        sender.setUsername(USERNAME);
        sender.setPassword(PASSWORD);
        sender.setDefaultEncoding("UTF-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", TIMEOUT);
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.ssl.enable", "true");//加这句ssl的话，对应端口465，不加这句的话端口对应25，不能共存
        sender.setJavaMailProperties(p);
        return sender;
    }

    /**
     * 发送邮件
     *
     * @param to 接收人
     * @param subject 主题
     * @param html 发送内容
     *
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static void sendMail(String to, String subject, String html) throws MessagingException, UnsupportedEncodingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //设置编码
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom(EMAILFROM, PERSONAL);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(html, true);
        mailSender.send(mimeMessage);

    }

    /**
     * 发送带附件的邮件
     *
     * @param to 接受人
     * @param subject 主题
     * @param html 发送内容
     * @param filePath 附件路径
     *
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendAttachmentMail(String to, String subject, String html, String filePath) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(EMAILFROM, PERSONAL);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        messageHelper.addAttachment(fileName, file);
        mailSender.send(mimeMessage);
    }

    public static void main(String[] args) {
        try {
            //这里的邮箱是接受者的邮箱，和配置的邮箱不一样
            // 发送普通文本邮件
            //sendMail("huyifan@ubox.cn", "邮件发送测试", "<a href='https://www.baidu.com' >百度一下</a>");
            // 发送带附件的邮件
            sendAttachmentMail("huyifan@ubox.cn", "邮件发送测试", "<a href='https://www.baidu.com' >百度一下</a>", "D:\\excel\\示例文件.xlsx");
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
