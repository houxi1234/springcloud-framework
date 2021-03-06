package cn.ce.st.framework.security.utils;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * 获取当前登录的用户名
 *
 * @author jie
 * @date 2019-01-17
 */
public class SecurityContextHolder {

    public static UserDetails getUserDetails() {
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new RuntimeException("登录状态过期");
        }
        return userDetails;
    }
}
