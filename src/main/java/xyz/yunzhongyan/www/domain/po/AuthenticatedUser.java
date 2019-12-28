package xyz.yunzhongyan.www.domain.po;


import xyz.yunzhongyan.www.domain.RedisEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthenticatedUser implements RedisEntity {
    private String userId;
    private String token;
    private String username;
    private String password;
    private Boolean active;


    public AuthenticatedUser(User user, String token) {
        userId = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        active = user.getActive();
        this.token = token;
    }

    @Override
    public String generateRedisKey() {
        return String.format("authenticated_user:%s:token", token);
    }

    @Override
    public String generateRedisKey(Object key) {
        return String.format("authenticated_user:%s:token", key);
    }
}
