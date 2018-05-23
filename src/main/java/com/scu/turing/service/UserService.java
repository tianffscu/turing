package com.scu.turing.service;

import com.scu.turing.entity.Role;
import com.scu.turing.entity.User;
import com.scu.turing.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id) {
        return userRepository.findOne(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    //用户信息更新，通常用来进行密码修改
    public User changePassword(long userId, String password) {
        User user = userRepository.findOne(userId);
        user.setPassword(password);
        return userRepository.save(user);
    }

    //保存注册用户
    public User saveUser(User user) {
        if (null == user.getRole()) {
            user.setRole(Role.getSysUser());
        }
        return userRepository.save(user);
    }
}
