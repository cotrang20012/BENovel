package mobile.Service.Impl;

import mobile.Handler.RecordNotFoundException;
import mobile.Service.UserService;

import mobile.mapping.UserMapping;
import mobile.model.Entity.Role;
import mobile.model.Entity.User;
import mobile.model.payload.request.user.InfoUserRequest;
import mobile.repository.RoleRepository;
import mobile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    @Override
    public User saveUser(User user,String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        log.info("Saving user {} to database",user.getEmail());
        if(user.getRoles() == null){
            Set<Role> RoleSet = new HashSet<>();
            RoleSet.add(role.get());
            user.setRoles(RoleSet);
        }
        else{
            user.getRoles().add(role.get());
        }
        return userRepository.save(user);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving User {} to database",user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving Role {} to database",role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        log.info("Adding Role {} to user {}", roleName, email);
        Optional<Role> role = roleRepository.findByName(roleName);
        Optional<User> user = userRepository.findByEmail(email);

        if(user.get().getRoles() == null){
            Set<Role> RoleSet = new HashSet<>();
            RoleSet.add(role.get());
            user.get().setRoles(RoleSet);
        }
        else{
            user.get().getRoles().add(role.get());
        }
        userRepository.save(user.get());
    }

    public void updateRoleToUser(User user, List<String> strList) {
        Set<Role> roleList = new HashSet<>();
        for (String str: strList ) {
            Optional<Role> role = roleRepository.findByName(str);
            if(!role.isEmpty()){
                roleList.add(role.get());
            }
        }
        if(roleList.size()==0)
            throw new RecordNotFoundException("Not found any role");

        user.setRoles(roleList);
        userRepository.save(user);

    }

    @Override
    public User getUser(String email) {
        log.info("Fetching user {}",email);
        return userRepository.findByEmail(email).get();
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users ");
        return userRepository.findAll();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User findByUsername(String username) {
         return userRepository.findByUsername(username).get();
    }

    @Override
    public User updateUserInfo(User user, InfoUserRequest userInfo) {
        user = UserMapping.UpdateUserInfoByUser(user,userInfo);
        return userRepository.save(user);
    }

    @Override
    public User updateUserPassword(User user, String password) {
        user = UserMapping.UpdatePasswordByUser(user,password);
        return userRepository.save(user);
    }

    @Override
    public User deleteUser(String username) {
        return userRepository.deleteByUsername(username);
    }

    @Override
    public User updateActive(User user) {
        user.setActive(!user.getActive());
        return userRepository.save(user);
    }
}
