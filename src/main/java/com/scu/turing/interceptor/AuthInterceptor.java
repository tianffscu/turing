//package com.scu.turing.interceptor;
//
//import com.scu.turing.comm.Const;
//import com.scu.turing.entity.User;
//import com.scu.turing.service.repository.UserRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Arrays;
//
//@Component
//public class AuthInterceptor extends HandlerInterceptorAdapter {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(AuthInterceptor.class);
//
//    @Value("${base.auth.mockenable}")
//    private boolean mockEnable;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        try {
//            return innerHandle(request, response);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage(), e);
//            return false;
//        }
//    }
//
//    private boolean innerHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        // TODO: 2018/5/8
//        User user = (User) request.getSession().getAttribute(Const.LOGIN_SESSION_KEY);
//        if (user == null) {
//            if (mockEnable) {
//                User mock = userRepository.findByEmail("123@mail.com");
//                request.getSession().setAttribute(Const.LOGIN_SESSION_KEY, mock);
//            } else if (needLogin(request.getRequestURI())) {
//                {
//                    response.sendRedirect("/login");
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    private boolean needLogin(String uri) {
//        String[] excludes = {"login", "register","webjarslocator"};
//
//        return Arrays.stream(excludes)
//                .noneMatch(uri::contains);
//    }
//}
