package com.example.sweater.service;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService() {
    }

    public boolean addUser(User user){
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) return false;
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        sendActivateLinkToUserMail(user);

        return true;
    }

    private void sendActivateLinkToUserMail(User user) {
        if(StringUtils.hasText(user.getEmail())){
            String message = String.format(
                    """
                            Hello, %s!
                            Welcome to Sweeater. Please, visit next link: http://localhost:8080/activate/%s
                            """, user.getUsername(), user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Sweeter activation code", message);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if(user == null) return false;
        user.setActivationCode(null);// признак того тчо пользователь подтвердил почту
        userRepo.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        //очищаем роли пользователей
        user.getRoles().clear();

        //добавляем роли
        for (String key : form.keySet()) {
            if(roles.contains(key)) user.getRoles().add(Role.valueOf(key));
        }

        user.setUsername(username);
        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
       String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if(isEmailChanged) {
            user.setEmail(email);
            if(StringUtils.hasText(user.getEmail())) user.setActivationCode(UUID.randomUUID().toString());
        }

        if(StringUtils.hasText(password)) user.setPassword(password);

        userRepo.save(user);

        if(isEmailChanged) sendActivateLinkToUserMail(user);
    }
}
