package xyz.yunzhongyan.www.service;

import xyz.yunzhongyan.www.domain.po.User;

import java.util.List;

public interface UserService {
    List<User> findByUsernameAndPasswordAndActive(String username, String password, Boolean active);

    List<User> findUserByGithubIdIs(String githubId, boolean active);

    User saveUser(User user);

    List<User> findUserByRegisterEmailCodeIs(String emailCode);

    List<User> findUserByPasswordEmailCodeIs(String emailCode);

    List<User> findUserByMailboxIs(String emailCode);

    List<User> findUserByUsernameIs(String name);
}
