package xyz.yunzhongyan.www.service.impl;/**
 * 功能描述:
 *
 * @param: $
 * @return: $
 * @auther: $
 * @date: $ $
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.yunzhongyan.www.dao.mongo.UserDao;
import xyz.yunzhongyan.www.domain.po.AuthenticatedUser;
import xyz.yunzhongyan.www.domain.po.User;
import xyz.yunzhongyan.www.domain.vo.Authentication.*;
import xyz.yunzhongyan.www.domain.vo.Result;
import xyz.yunzhongyan.www.service.RedisTokenService;
import xyz.yunzhongyan.www.util.Constants;
import xyz.yunzhongyan.www.util.EmptyUtil;
import xyz.yunzhongyan.www.util.HttpClientUtils;
import xyz.yunzhongyan.www.util.HttpRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @program: api
 * @description:
 * @author: wander
 * @create: 2019-11-11 15:53
 **/
@Component
@Slf4j
public class AuthenticationServiceImpl implements xyz.yunzhongyan.www.service.AuthenticationService {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private RedisTokenService tokenService;
    @Autowired
    private EmailServiceImpl emailServiceImpl;
    @Autowired
    private UserDao userDao;

    @Override
    public Result deleteUser(String mailbox) {
        List<User> userByMailboxIs = userDao.findUserByMailboxIs(mailbox);
        for (User userByMailboxI : userByMailboxIs) {
            userDao.delete(userByMailboxI.getId());
        }
        return Result.success();
    }

    @Override
    public Result duplicateCheck(String value, Integer type) {
        if (type == 1) {
            List<User> users = userServiceImpl.findUserByUsernameIs(value);
            if (users != null && users.size() != 0) {
                return Result.fail("用户名重复");
            } else {
                return Result.success("用户名可以使用");
            }
        } else if (type == 2) {
            List<User> users = userServiceImpl.findUserByMailboxIs(value);
            if (users != null && users.size() != 0) {
                return Result.fail("该邮箱已经注册，请尝试找回密码");
            } else {
                return Result.success("邮箱可以注册");
            }
        }
        return Result.fail("请稍后再试");
    }

    @Override
    public Result forgotPassword(ForgotPasswordVo forgotPasswordVo) {
        if (forgotPasswordVo.getEmailCode() == null)
            return Result.fail("邮件已失效，请重新找回密码");
        List<User> users = userServiceImpl.findUserByPasswordEmailCodeIs(forgotPasswordVo.getEmailCode());
        if (users == null || users.size() == 0)
            return Result.fail("邮件已失效，请重新找回密码");
        User user = users.get(0);
//        user.setPasswordEmailCode(null);
        user.setPassword(forgotPasswordVo.getPassword());
        user.setActive(true);
        User save = userServiceImpl.saveUser(user);
        return Result.success().message("修改密码成功，请登录");
    }

    @Override
    public Result sendforGotPasswordEmail(MailboxVo mailboxVo) {
        List<User> users = userServiceImpl.findUserByMailboxIs(mailboxVo.getMailbox());
        if (users == null || users.size() == 0)
            return Result.fail("该邮箱未注册，请确认！");
        User user1 = users.get(0);
        user1.setPasswordEmailCode(UUID.randomUUID().toString() + new Date().getTime());
        //发送邮件
        String result = emailServiceImpl.sendPasswordEmail(user1.getUsername(), user1.getMailbox(), "https://fsn.dev/resetPassword?emailCode=" + user1.getPasswordEmailCode(), user1.getPasswordEmailCode());
        if (result.contains("success")) {
            User user = userServiceImpl.saveUser(user1);
            return Result.success(mailboxVo).message("邮件已发送，请去邮箱查看确认。");
        } else {
            return Result.success().message("邮件已发送失败，请请稍后再试");
        }
    }

    @Override
    public Result confirmEmailLogin(EmailCode emailCode) {
        if (emailCode.getEmailCode() == null)
            return Result.fail("邮件确认登录有误");
        log.info("确认邮件登录code", emailCode.getEmailCode());
        List<User> users = userServiceImpl.findUserByRegisterEmailCodeIs(emailCode.getEmailCode());
        if (users == null || users.size() == 0)
            return Result.fail("邮件确认有误");
        User user = users.get(0);
        String token = tokenService.createToken(user);
        return Result.success(new Authentication(token, user.getUsername(), user.getHeadPortrait()));
    }


    @Override
    public Result confirmEmai(EmailCode emailCode) {
        if (emailCode.getEmailCode() == null)
            return Result.fail("邮件确认有误");
        log.info("确认邮件code", emailCode.getEmailCode());
        List<User> users = userServiceImpl.findUserByRegisterEmailCodeIs(emailCode.getEmailCode());
        if (users == null || users.size() == 0)
            return Result.fail("邮件确认有误");
        User user = users.get(0);
        user.setState(1);
        user.setActive(true);
        User save = userServiceImpl.saveUser(user);
        return Result.success(emailCode).message("注册成功，请登录");
    }

    @Override
    public Result sendRegisterEmail(RegisterEmail registerEmail) {
        User one = userDao.findOne(registerEmail.getUserId());
        //发送邮件
        String result = emailServiceImpl.sendRegisterEmail(one.getUsername(), one.getMailbox(), "https://fsn.dev/validateSuccess?emailCode=" + one.getRegisterEmailCode(), one.getRegisterEmailCode());
        if (result.contains("success")) {
//            one.setRegisterEmailCode(null);
            User user = userServiceImpl.saveUser(one);
            return Result.success().message("邮件已发送，请去邮箱查看确认。");
        } else {
            return Result.success().message("邮件已发送失败，请请稍后再试");
        }
    }

    @Override
    public Result saveAuthentication(AuthenticationQuery authenticationQuery) {
        User register = new User();
        register.setHeadPortrait(getHeadPortrait(authenticationQuery.getUsername()));
        register.setState(0);
        register.setActive(false);
        register.setUsername(authenticationQuery.getUsername());
        register.setPassword(authenticationQuery.getPassword());
        register.setMailbox(authenticationQuery.getMailbox());
        //设置邮件确认码
        register.setRegisterEmailCode(UUID.randomUUID().toString() + new Date().getTime());
        authenticationQuery.setUsername(authenticationQuery.getUsername());
        authenticationQuery.setPassword(authenticationQuery.getPassword());
        //发送邮件
        String result = emailServiceImpl.sendRegisterEmail(authenticationQuery.getUsername(), authenticationQuery.getMailbox(), "https://fsn.dev/validateSuccess?emailCode=" + register.getRegisterEmailCode(), register.getRegisterEmailCode());
        if (result.contains("success")) {
            User user = userServiceImpl.saveUser(register);
            User user1 = new User();
            user1.setId(user.getId());
            return Result.success(user1);
        } else {
            return Result.success().message("邮件已发送失败，请请稍后再试");
        }
    }

    @Override
    public Result findAuthentication(HttpServletRequest request, String username, String password) {
        if ("unlogin".equals(username)) {
            return Result.fail("用户名或密码错误");
        }
        List<User> users = userServiceImpl.findByUsernameAndPasswordAndActive(username, password, true);
        if (users == null || users.size() == 0)
            return Result.fail("用户名或密码错误");
        User user = users.get(0);
        log.info("请求的referer={}", request.getHeader("referer"));
        String token = tokenService.createToken(user);
        return Result.success(new Authentication(token, user.getUsername(), user.getHeadPortrait()));
    }

    @Override
    public Result findAuthenticationGithub(HttpServletRequest request, String code) {
        log.info("githubCode" + code);
        Map<String, String> githubLoginInfo = getGithubLoginInfo(code);
        /*Map<String, String> githubLoginInfo =new HashMap<>();
        githubLoginInfo.put("id","35339316");
        githubLoginInfo.put("name","wander");*/
        for (String s : githubLoginInfo.keySet()) {
            log.info("githubCodeInfo==" + s + "==" + githubLoginInfo.get(s));
        }
        ;
        if (githubLoginInfo.get("id") == null) {
            log.info("githubCode异常" + code);
        }
        List<User> users = userServiceImpl.findUserByGithubIdIs(githubLoginInfo.get("id"), true);
        if (users == null || users.size() == 0) {
            Random random = new Random();
            User github = new User();
            github.setGithubId(githubLoginInfo.get("id"));
            //获取用户名
            String name = EmptyUtil.isEmpty(githubLoginInfo.get("name")) ? ("github" + random.nextInt(100000)) : githubLoginInfo.get("name");
            name = findName(name);
            log.info("github用户名" + name);
            github.setUsername(name);
            github.setActive(true);
            github.setState(1);
            github.setHeadPortrait(getHeadPortrait(code));
            User user1 = userServiceImpl.saveUser(github);
            List<User> save = userServiceImpl.findUserByGithubIdIs(user1.getGithubId(), true);
            if (save == null || save.size() == 0)
                return Result.fail("用户名或密码错误");
            User user = save.get(0);
            log.info("请求的referer={}", request.getHeader("referer"));
            String token = tokenService.createToken(user);
            return Result.success(new Authentication(token, user.getUsername(), user.getHeadPortrait()));
        } else {
            List<User> save = userServiceImpl.findUserByGithubIdIs(githubLoginInfo.get("id"), true);
            if (save == null || save.size() == 0)
                return Result.fail("用户名或密码错误");
            User user = save.get(0);
            log.info("请求的referer={}", request.getHeader("referer"));
            String token = tokenService.createToken(user);
            return Result.success(new Authentication(token, user.getUsername(), user.getHeadPortrait()));
        }
    }

    public String findName(String name) {
        Random random = new Random();
        List<User> users2 = userServiceImpl.findUserByUsernameIs(name);
        if (users2 != null && users2.size() != 0) {
            name = "github" + random.nextInt(100000);
            findName(name);
        }
        return name;
    }

    @Override
    public Result deleteAuthentication(AuthenticatedUser authenticatedUser) {
        tokenService.deleteToken(authenticatedUser.getToken());
        return Result.success();
    }

    private Map<String, String> getGithubLoginInfo(String code) {
        Map<String, String> responseMap = new HashMap<>();
        if (!StringUtils.isEmpty(code)) {
            try {
                //拿到我们的code,去请求token
                //发送一个请求到
                String token_url = Constants.TOKEN_URL.replace("CODE", code);
                //得到的responseStr是一个字符串需要将它解析放到map中
                String responseStr = null;
                responseStr = HttpRequestUtil.sendGet(token_url);
                // 调用方法从map中获得返回的--》 令牌
                String token = HttpClientUtils.getMap(responseStr).get("access_token");
                //根据token发送请求获取登录人的信息  ，通过令牌去获得用户信息
                String userinfo_url = Constants.USER_INFO_URL.replace("TOKEN", token);
                responseStr = HttpRequestUtil.sendGet(userinfo_url);//json
                log.info("github登录", responseStr);
                responseMap = HttpClientUtils.getMapByJson(responseStr);
                // 成功则登陆
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseMap;
    }

    private String getHeadPortrait(String code) {
        List<String> accessoriesType = Arrays.asList("Blank", "Kurt", "Prescription01", "Prescription02", "Round", "Sunglasses", "Wayfarers");
        List<String> clotheType = Arrays.asList("BlazerShirt", "BlazerSweater", "CollarSweater", "GraphicShirt", "Hoodie", "Overall", "ShirtCrewNeck", "ShirtScoopNeck", "ShirtVNeck");
        List<String> eyeType = Arrays.asList("Close", "Cry", "Default", "Dizzy", "EyeRoll", "Happy", "Hearts", "Side", "Squint", "Surprised", "Wink", "WinkWacky");
        List<String> facialHairType = Arrays.asList("Blank", "BeardMedium", "BeardLight", "BeardMagestic", "MoustacheFancy", "MoustacheMagnum");
        List<String> mouthType = Arrays.asList("Concerned", "Default", "Disbelief", "Eating", "Grimace", "Sad", "ScreamOpen", "Serious", "Smile", "Tongue", "Twinkle", "Vomit");
        List<String> skinColor = Arrays.asList("Tanned", "Yellow", "Pale", "Light", "Brown", "DarkBrown", "Black");
        List<String> topType = Arrays.asList("NoHair", "Eyepatch", "Hat", "Hijab", "Turban", "WinterHat1", "WinterHat2", "WinterHat3", "WinterHat4", "LongHairBigHair", "LongHairBob", "LongHairBun", "LongHairCurvy", "LongHairDreads", "LongHairFrida");
        String format = String.format("https://avataaars.io/?accessoriesType=%S&avatarStyle=Circle&clotheColor=Black&clotheType=%s&eyeType=%s&eyebrowType=Default&facialHairColor=BlondeGolden&facialHairType=%s&hairColor=PastelPink&mouthType=Sad&skinColor=Yellow&topType=Hat"
                , getRandomValue(accessoriesType), getRandomValue(clotheType), getRandomValue(eyeType), getRandomValue(facialHairType), getRandomValue(mouthType), getRandomValue(skinColor), getRandomValue(topType));
        return format;
    }

    private String getRandomValue(List<String> accessoriesType) {
        return accessoriesType.get(new Random().nextInt(accessoriesType.size()));
    }

    @Override
    public Result getUserInfo(AuthenticatedUser authenticatedUser) {
        User userByIdIs = userDao.findUserByIdIs(authenticatedUser.getUserId());
        User user = new User();
        user.setUsername(userByIdIs.getUsername());
        user.setHeadPortrait(userByIdIs.getHeadPortrait());
        return Result.success(user);
    }

    @Override
    public Result updateUserInfo(AuthenticatedUser authenticatedUser, User user) {
        User userByIdIs = userDao.findUserByIdIs(authenticatedUser.getUserId());
        if (EmptyUtil.isNotEmpty(user.getUsername())) {
            userByIdIs.setUsername(user.getUsername());
        }
        if (EmptyUtil.isNotEmpty(user.getHeadPortrait())) {
            userByIdIs.setHeadPortrait(user.getHeadPortrait());
        }
        User save = userDao.save(userByIdIs);
        return Result.success("已更新");
    }
}
