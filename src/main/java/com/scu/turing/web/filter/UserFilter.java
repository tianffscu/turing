package com.scu.turing.web.filter;

import com.scu.turing.comm.Const;
import com.scu.turing.model.Role;
import com.scu.turing.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 不过滤的uri
        String[] notFilter = new String[]{"login", "index", "register", "css", "img", "js", "media", "vendor","webjar"};

        // 请求的url
        String url = request.getRequestURL().toString();

        boolean doFilter = true;
        for (String s : notFilter) {
            if (url.contains(s)) {
                // 如果uri中包含不过滤的uri，则不进行过滤
                doFilter = false;
                break;
            }
        }

        if (doFilter) {
            User user = (User) request.getSession().getAttribute(Const.LOGIN_SESSION_KEY);
//            response.setContentType("text/html; charset=utf-8");
            if (user == null) {
//                response.setHeader("","");
                response.sendRedirect("/login");
            } else {
                //权限验证
                if (url.contains("admin")) {//页面需要权限？
                    if (!user.getRole().equals(Role.getAdmin())) {//登录用户不是管理员?
                        LOGGER.warn("Non admin user request a admin page: " + user + url);
                        response.sendRedirect("/authorize");
                    } else {
                        filterChain.doFilter(request, response);
                    }
                } else {//页面不需要权限
                    filterChain.doFilter(request, response);
                }
            }
        } else {
            // 如果不执行过滤，则继续
            filterChain.doFilter(request, response);
        }
    }
}
