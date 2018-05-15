package com.scu.turing.service;

import com.scu.turing.entity.Role;
import com.scu.turing.entity.User;
import com.scu.turing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User getUserById(Integer id) {

        return repository.findOne(id);
    }

    public User getUserByEmail(String email) {

        return repository.findByEmail(email);
    }

    public User getByUserName(String username){
        return repository.findByUserName(username);
    }

    //用户信息更新，通常用来进行密码修改
    public User update(User user) {
        return repository.save(user);
    }

    //保存注册用户
    public User saveUser(User user) {
        if (null == user.getRole()) {
            user.setRole(Role.getSysUser());
        }
        return repository.save(user);
    }

    //通过email查询用户
    public User findByEmail(String eamil) {
        return repository.findByEmail(eamil);
    }

}
