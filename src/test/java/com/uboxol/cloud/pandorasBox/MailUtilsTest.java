package com.uboxol.cloud.pandorasBox;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.junit.Test;

import java.io.File;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/12/6 16:52
 */
@Slf4j
public class MailUtilsTest {
    @Test
    public void test() {

        File file = new File("target/示例文件.xlsx");

        try {
            MailUtils.sendWithAttachFile("测试用地", "有附件的邮件", file, "liyunde@ubox.cn");
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
