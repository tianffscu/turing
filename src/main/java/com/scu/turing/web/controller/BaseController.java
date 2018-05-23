package com.scu.turing.web.controller;

import com.scu.turing.comm.Const;
import com.scu.turing.entity.User;
import com.scu.turing.model.ExceptionMsg;
import com.scu.turing.model.Response;
import com.scu.turing.utils.Des3EncryptionUtil;
import com.scu.turing.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BaseController {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected Response success() {
        return new Response();
    }

    protected Response result(ExceptionMsg msg) {
        return new Response(msg);
    }

    protected <T> Response<T> result(T t) {
        return new Response<>(t);
    }

    protected Response failed() {
        return new Response(ExceptionMsg.FAILED);
    }

    protected Response failed(ExceptionMsg msg) {
        return new Response(msg);
    }

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected HttpSession getSession() {
        return getRequest().getSession();
    }

//    protected User getUser() {
//        return (User) getSession().getAttribute(Const.LOGIN_SESSION_KEY);
//    }
//
//    protected Long getUserId() {
//        Long id = 0L;
//        User user = getUser();
//        if (user != null) {
//            id = user.getId();
//        }
//        return id;
//    }
//
//    protected String getUserName() {
//        String userName = "default";
//        User user = getUser();
//        if (user != null) {
//            userName = user.getUserName();
//        }
//        return userName;
//    }
//
//    protected String getUserIp() {
//        String value = getRequest().getHeader("X-Real-IP");
//        if (StringUtils.isNotBlank(value) && !"unknown".equalsIgnoreCase(value)) {
//            return value;
//        } else {
//            return getRequest().getRemoteAddr();
//        }
//    }

    protected String getPwd(String password) {
        try {
            String pwd = MD5Util.encrypt(password + Const.PASSWORD_KEY);
            return pwd;
        } catch (Exception e) {
            logger.error("密码加密异常：", e);
        }
        return null;
    }

    protected String cookieSign(String value) {
        try {
            value = value + Const.PASSWORD_KEY;
            String sign = Des3EncryptionUtil.encode(Const.DES3_KEY, value);
            return sign;
        } catch (Exception e) {
            logger.error("cookie签名异常：", e);
        }
        return null;
    }
}
