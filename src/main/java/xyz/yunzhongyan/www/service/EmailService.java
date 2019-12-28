package xyz.yunzhongyan.www.service;

public interface EmailService {
    /**
     * 发送注册邮件
     * @param username
     * @param mailbox
     * @param url
     * @return
     */
    String sendRegisterEmail(String username, String mailbox, String url, String code);

    String sendPasswordEmail(String username, String mailbox, String url, String code);
}
