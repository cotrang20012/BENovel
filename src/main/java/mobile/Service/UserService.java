package mobile.Service;

import mobile.model.Entity.Role;
import mobile.model.Entity.User;
import mobile.model.payload.request.user.InfoUserRequest;


import java.util.List;


public interface UserService {
    User saveUser(User user, String roleName);
    Role saveRole(Role role);
    void addRoleToUser(String email, String roleName);
    User getUser(String email);
    List<User> getUsers();
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    User findByUsername(String username);
    User updateUserInfo(User user, InfoUserRequest userInfo);
    User updateUserPassword(User user, String password);
    User deleteUser(String username);
    User updateActive(User user);
}
