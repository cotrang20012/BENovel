package mobile.controller;

import mobile.Service.RoleService;
import mobile.Service.UserService;
import mobile.mapping.UserMapping;
import mobile.model.Entity.User;
import mobile.model.payload.request.user.DeleteUserRequest;
import mobile.model.payload.request.user.RegisterAdminRequest;
import mobile.model.payload.request.user.RoleToUserRequest;
import mobile.Handler.HttpMessageNotReadableException;
import mobile.Handler.MethodArgumentNotValidException;
import mobile.Handler.RecordNotFoundException;
import mobile.model.payload.request.user.UpdateRoleToUserRequest;
import mobile.model.payload.response.ErrorResponseMap;
import mobile.model.payload.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
public class AdminResource {
    private static final Logger LOGGER = LogManager.getLogger(AdminResource.class);

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    AuthenticationManager authenticationManager;


    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<User>> getUsers() {
        List<User> userList = userService.getUsers();
        if (userList == null) {
            throw new RecordNotFoundException("No User existing ");
        }
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @PostMapping("user/save")
    @ResponseBody
    public ResponseEntity<SuccessResponse> saveUser(@RequestBody @Valid RegisterAdminRequest user, BindingResult errors) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        if (user == null) {
            LOGGER.info("Inside addIssuer, adding: " + user.toString());
            throw new HttpMessageNotReadableException("Missing field");
        } else {
            LOGGER.info("Inside addIssuer...");
        }

        if (userService.existsByEmail(user.getEmail())) {
            return SendErrorValid("email", user.getEmail());
        }

        if (userService.existsByUsername(user.getUsername())) {
            return SendErrorValid("username", user.getUsername());
        }

        try {

            User newUser = UserMapping.registerToEntity(user);
            newUser.setActive(true);
            userService.saveUser(newUser, user.getRoles());
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("add user successful");
            response.setSuccess(true);
            response.getData().put("email", user.getEmail());
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);

        } catch (Exception ex) {
            throw new Exception("Can't create your account");
        }
    }

    @PutMapping("user/active")
    @ResponseBody
    public ResponseEntity<SuccessResponse> activeUser(@RequestBody Map<String, String> json, BindingResult errors) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        if (json == null) {
            LOGGER.info("Inside addIssuer, adding: " + json.toString());
            throw new HttpMessageNotReadableException("Missing field");
        } else {
            LOGGER.info("Inside addIssuer...");
        }
        User user = userService.findByUsername(json.get("username"));

        if (user == null) {
            throw new RecordNotFoundException("User not exist");
        }
        user.setActive(true);
        try {
            userService.saveUser(user);
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Active user successful");
            response.setSuccess(true);
            response.getData().put("username", user.getUsername());
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);

        } catch (Exception ex) {
            throw new Exception("Can't active account");
        }
    }
    @PutMapping("user/inactive")
    @ResponseBody
    public ResponseEntity<SuccessResponse> inactiveUser(@RequestBody Map<String, String> json, BindingResult errors) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        if (json == null) {
            LOGGER.info("Inside addIssuer, adding: " + json.toString());
            throw new HttpMessageNotReadableException("Missing field");
        } else {
            LOGGER.info("Inside addIssuer...");
        }
        User user = userService.findByUsername(json.get("username"));

        if (user == null) {
            throw new RecordNotFoundException("User not exist");
        }
        user.setActive(false);
        try {
            userService.saveUser(user);
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Inactive user successful");
            response.setSuccess(true);
            response.getData().put("username", user.getUsername());
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);

        } catch (Exception ex) {
            throw new Exception("Can't inactive account");
        }
    }

    @PostMapping("role/addtouser")
    @ResponseBody
    public ResponseEntity<SuccessResponse> addRoleToUser(@RequestBody @Valid RoleToUserRequest roleForm, BindingResult errors) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }

        if (roleForm == null) {
            LOGGER.info("Inside addIssuer, adding: " + roleForm.toString());
            throw new HttpMessageNotReadableException("Missing field");
        } else {
            LOGGER.info("Inside addIssuer...");
        }

        if (!userService.existsByEmail(roleForm.getEmail())) {
            throw new HttpMessageNotReadableException("User is not exist");
        }

        if (roleService.existsByRoleName(roleForm.getRoleName())) {
            throw new HttpMessageNotReadableException("Role is not exist");
        }
        try {
            userService.addRoleToUser(roleForm.getEmail(), roleForm.getRoleName());

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("add user successful");
            response.setSuccess(true);
            response.getData().put("email", roleForm.getEmail());
            response.getData().put("role", roleForm.getRoleName());
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);

        } catch (Exception ex) {
            throw new Exception("Can't add role to account");
        }
    }

    @PutMapping("role/updatetouser")
    @ResponseBody
    public ResponseEntity<SuccessResponse> updateRoleToUser(@RequestBody @Valid UpdateRoleToUserRequest roleListForm, BindingResult errors) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }

        if (roleListForm == null) {
            LOGGER.info("Inside addIssuer, adding: " + roleListForm.toString());
            throw new HttpMessageNotReadableException("Missing field");
        } else {
            LOGGER.info("Inside addIssuer...");
        }
        User user = userService.findByUsername(roleListForm.getUsername());
        if (user == null) {
            throw new HttpMessageNotReadableException("User is not exist");
        }

        try {
            userService.updateRoleToUser(user, roleListForm.getRoles());
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("add user successful");
            response.setSuccess(true);
            response.getData().put("username", roleListForm.getUsername());
            return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);

        } catch (Exception ex) {
            throw new Exception("Can't add role to account");
        }
    }

    @DeleteMapping("user")
    @ResponseBody
    public ResponseEntity<SuccessResponse> deleteAccount(@RequestBody @Valid DeleteUserRequest deleteUserRequest) throws Exception{
        if(deleteUserRequest.getUsername()==null){
            throw new HttpMessageNotReadableException("Missing field");
        }
           try{
               User user = userService.findByUsername(deleteUserRequest.getUsername());
               if(user==null){
                   throw new RecordNotFoundException("Không tìm thấy tài khoản");
               }
               User userDel = userService.deleteUser(deleteUserRequest.getUsername());
               SuccessResponse response = new SuccessResponse();
               response.setStatus(HttpStatus.OK.value());
               response.setMessage("Delete user successful");
               response.setSuccess(true);
               response.getData().put("username", deleteUserRequest.getUsername());
               return new ResponseEntity<SuccessResponse>(response, HttpStatus.OK);
           }catch (Exception ex){
               throw new Exception("Xoá tài khoản thất bại");
           }

    }


    private ResponseEntity SendErrorValid(String field, String message) {
        ErrorResponseMap errorResponseMap = new ErrorResponseMap();
        Map<String, String> temp = new HashMap<>();
        errorResponseMap.setMessage("Field already taken");
        temp.put(field, message + " has already used");
        errorResponseMap.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseMap.setDetails(temp);
        return ResponseEntity
                .badRequest()
                .body(errorResponseMap);
    }
}