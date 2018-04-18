package com.scu.turing.service;

import com.scu.turing.repository.UserRepository;
import com.scu.turing.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User getUserById(Integer id){

        return repository.findOne(id);
    }

    public User getUserByEmail(String email){

        return repository.findByEmail(email);
    }

    //用户信息更新，通常用来进行密码修改
    public User update(User user){
        return repository.save(user);
    }
    //保存注册用户
    public User saveUser(User user){
        return repository.save(user);
    }
    //通过email查询用户
    public User findByEmail(String eamil){
        return repository.findByEmail(eamil);
    }

}
