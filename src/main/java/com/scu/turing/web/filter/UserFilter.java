//package com.scu.turing.web.filter;
//
//import com.scu.turing.model.Role;
//import com.scu.turing.model.User;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class UserFilter extends OncePerRequestFilter {
//
//    public static final String SESSION_USER_KEY = "auser";
//    private static final Logger LOGGER = LoggerFactory.getLogger(UserFilter.class);
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        // 不过滤的uri
//        String[] notFilter = new String[]{"login", "index", "register"};
//
//        // 请求的uri
//        String uri = request.getRequestURI();
//
//        boolean doFilter = true;
//        for (String s : notFilter) {
//            if (uri.contains(s)) {
//                // 如果uri中包含不过滤的uri，则不进行过滤
//                doFilter = false;
//                break;
//            }
//        }
//
//        if (doFilter) {
//            User user = (User) request.getSession().getAttribute(SESSION_USER_KEY);
////            response.setContentType("text/html; charset=utf-8");
//            if (user == null) {
////                response.setHeader("","");
//                response.sendRedirect("/user/login");
//            } else {
//                //权限验证
//                if (uri.contains("admin")) {//页面需要权限？
//                    if (!user.getRole().equals(Role.getAdmin())) {//登录用户不是管理员?
//                        LOGGER.warn("Non admin user request a admin page: " + user + uri);
//                        response.sendRedirect("/401");
//                    } else {
//                        filterChain.doFilter(request, response);
//                    }
//                } else {//页面不需要权限
//                    filterChain.doFilter(request, response);
//                }
//            }
//        } else {
//            // 如果不执行过滤，则继续
//            filterChain.doFilter(request, response);
//        }
//    }
//}
