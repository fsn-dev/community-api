package xyz.yunzhongyan.www.domain.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Document(collection = "user")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String username;
    private String mailbox;
    private String password;
    private String headPortrait;//头像
    private String registerEmailCode;//注册邮件确认
//    private Date registerDate;//注册邮件确认

    private String passwordEmailCode;//找回密码
//    private Date passwordDate;//找回密码

    private Integer state;//0:已提交 1:已经邮件确认 2
    private Boolean active;

    private String githubId;//github登录id

    private Integer admin;//是否只管理员

}
