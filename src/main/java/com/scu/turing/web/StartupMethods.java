package com.scu.turing.web;

import com.scu.turing.entity.Role;
import com.scu.turing.service.repository.RoleRepository;
import com.scu.turing.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.TimeZone;

@Component
public class StartupMethods implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupMethods.class);

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public StartupMethods(@Autowired UserRepository userRepository,
                          @Autowired RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            onFirstStartUp();
            LOGGER.info("Changing TimeZone to CN GMT+8....");
//            changeTimeZone();

            // TODO: 2018/5/12 Other start up methods
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void changeTimeZone() {
//        System.setProperty("user.timezone", "GMT +08");
        System.setProperty("user.timezone", "Asia/Shanghai");
        LOGGER.warn("Current system.user.timezone: " + System.getProperty("user.timezone"));
        LOGGER.warn("Current Timezone.getTimeZone: " + TimeZone.getDefault());
        LOGGER.warn("Current time: " + new Date());
    }

    private void onFirstStartUp() {
        //creating all roles
        long size = roleRepository.count();
        if (size == 0) {
            LOGGER.info("First startup, Auto-creating all system Role!");
            Role u = roleRepository.save(Role.getAdmin());
            Role.getAdmin().setId(u.getId());
            LOGGER.info("successfully saved : " + Role.getAdmin());
            Role r = roleRepository.save(Role.getSysUser());
            Role.getSysUser().setId(r.getId());
            LOGGER.info("successfully saved : " + Role.getSysUser());
        }
        // TODO: 2018/5/13
    }
}