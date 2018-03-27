package com.gate.utils;


import com.gate.config.MailConfig;
import com.gate.config.SystemConfig;
import com.gate.config.SystemConfigLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.commons.mail.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: good504
 * Date: 2014/9/26
 * Time: 下午 5:36
 * To change this template use File | Settings | File Templates.
 */
public class SendEmailFileUtils extends MultiPartEmail {


    private static Logger logger = LogManager.getLogger(SendEmailFileUtils.class);

    private static MultiPartEmail mailSender;
    private static SendEmailFileUtils instance = null;


    public static MultiPartEmail getInstance() throws EmailException {
        if (mailSender == null)
            synchronized (SendEmailFileUtils.class) {
                mailSender = getMailSender();
            }
        return mailSender;
    }

    public String send() {
        return null;
    }

    public static MultiPartEmail getMailSender() throws EmailException {
        MailConfig config = SystemConfig.getInstance().getMailConfig();
        HtmlEmail email = new HtmlEmail();
        if (config == null)
            return null;

        email.setHostName(config.getHost());
        email.setSmtpPort(Integer.parseInt(config.getPort()));
        email.setAuthenticator(new DefaultAuthenticator(config.getUserName(), config.getPassword()));
        email.setCharset(config.getEncoding());
        //email.setFrom(config.getFrom());
        email.setFrom(config.getFrom(), "關網資訊");
        return email;
    }

    public static EmailAttachment getAttachment(String path,String fileName) throws UnsupportedEncodingException {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(path);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription("error excel");
        attachment.setName(MimeUtility.encodeText(fileName));
        return attachment;
    }

    public static void sendEmail(String[] to, String subject, String content,String path,String fileName) throws EmailException, UnsupportedEncodingException {
        MultiPartEmail email = SendEmailFileUtils.getMailSender();
        email.addTo(to);
        email.setSubject(subject);
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");
        sb.append(content);
        sb.append("</body>");
        sb.append("</html>");
        email.setMsg(sb.toString());
        email.attach(getAttachment(path,fileName));
        email.send();
        logger.info("send to: " + to.toString());
    }

    public static void sendEmail(String to, String toName, String subject, String content) throws EmailException, UnsupportedEncodingException {
        MultiPartEmail email = SendEmailFileUtils.getMailSender();
        email.addTo(to, toName);
        email.setSubject(subject);
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");
        sb.append(content);
        sb.append("</body>");
        sb.append("</html>");
        email.setMsg(sb.toString());
        email.send();
        logger.info("send to: " + to.toString());
    }

    public static void sendEmail(String[] to, String subject, String content) throws EmailException, UnsupportedEncodingException {
        MultiPartEmail email = SendEmailFileUtils.getMailSender();
        email.addTo(to);
        email.setSubject(subject);
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body>");
        sb.append(content);
        sb.append("</body>");
        sb.append("</html>");
        email.setMsg(sb.toString());
        email.send();
        logger.info("send to: " + to.toString());
    }

    public static void main(String... args) {
        try {
            SystemConfigLoader.instance().load();
            StringBuffer sb = new StringBuffer();
            sb.append("您好:\n<br>");
            sb.append("今天的發票傳送清單\n").append("<br>");
//            SendEmailUtils.sendEmail(new String[]{"fugenyuzen@gmail.com"}, "發票傳送清單", sb.toString(),"");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
