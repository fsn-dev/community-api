package xyz.yunzhongyan.www.domain.vo.Authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationQuery {
    private String username;
    private String mailbox;
    private String password;
}