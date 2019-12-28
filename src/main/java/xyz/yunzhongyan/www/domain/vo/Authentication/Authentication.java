package xyz.yunzhongyan.www.domain.vo.Authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Authentication {
    private String token;
    private String username;
    private String headPortrait;
}