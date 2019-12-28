package xyz.yunzhongyan.www.dao.mongo;

import xyz.yunzhongyan.www.domain.po.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserDao extends MongoRepository<User, String> {
    List<User> findByUsernameAndPasswordAndActive(String username, String password, Boolean active);
    List<User> findUserByRegisterEmailCodeIs(String emailCode);
    List<User> findUserByPasswordEmailCodeIs(String emailCode);
    List<User> findUserByMailboxIs(String email);
    List<User> findUserByUsernameIs(String name);
    List<User> findUserByGithubIdIs(String githubId,boolean active);
    User findUserByIdIs(String id);
}
