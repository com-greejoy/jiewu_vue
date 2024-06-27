package com.ruoyi.common.constant;

/**
 * 缓存的key 常量
 *
 * @author ruoyi
 */
public class CacheConstants {
    /**
     * 项目redis前缀
     */
    public static final String PROJECT_KEY_PREFIX = "poke:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = PROJECT_KEY_PREFIX + "login_tokens:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = PROJECT_KEY_PREFIX + "captcha_codes:";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = PROJECT_KEY_PREFIX + "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = PROJECT_KEY_PREFIX + "sys_dict:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = PROJECT_KEY_PREFIX + "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = PROJECT_KEY_PREFIX + "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = PROJECT_KEY_PREFIX + "pwd_err_cnt:";
}
