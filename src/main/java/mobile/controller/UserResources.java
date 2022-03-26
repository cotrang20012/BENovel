package mobile.controller;

import mobile.Service.UserService;
import mobile.model.Entity.User;
import mobile.model.payload.request.user.ChangePassRequest;
import mobile.model.payload.request.user.InfoUserRequest;
import mobile.Handler.HttpMessageNotReadableException;
import mobile.Handler.MethodArgumentNotValidException;
import mobile.model.payload.response.ErrorResponseMap;
import mobile.model.payload.response.SuccessResponse;
import mobile.security.JWT.JwtUtils;
import lombok.RequiredArgsConstructor;
import mobile.model.payload.request.authenticate.LoginRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserResources {
    private static final Logger LOGGER = LogManager.getLogger(AdminResource.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  getInfo(HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken) == true){
                throw new BadCredentialsException("access token is  expired");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Info user");
            response.setSuccess(true);
            response.getData().put("userInfo",user);

            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @PutMapping("/info")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  updateInfo(@RequestBody @Valid InfoUserRequest userInfo, BindingResult errors, HttpServletRequest request) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken) == true){
                throw new BadCredentialsException("access token is  expired");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));
            user = userService.updateUserInfo(user,userInfo);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Update info successful");
            response.setSuccess(true);
            response.getData().put("userInfo",user);

            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @PutMapping("/info/password")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  updatePassword(@RequestBody @Valid ChangePassRequest pass, BindingResult errors, HttpServletRequest request) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken) == true){
                throw new BadCredentialsException("access token is expired");
            }

            String username = jwtUtils.getUserNameFromJwtToken(accessToken);
            User user= userService.findByUsername(username);


            if(user == null){
                throw new HttpMessageNotReadableException("user is not existed");
            }

            if(pass == null){
                throw new HttpMessageNotReadableException("password is not existed");
            }

            if(!(passwordEncoder.matches(pass.getPassword(),user.getPassword()))){
                throw new BadCredentialsException("username or password is not matched");
            }

            userService.updateUserPassword(user,pass.getNewPassword());

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("upadate password successful");
            response.setSuccess(true);
            response.getData().put("username",username);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @DeleteMapping("")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  deleteUser(@RequestBody @Valid LoginRequest user, BindingResult errors, HttpServletRequest request) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
       String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken) == true){
                throw new BadCredentialsException("access token is expired");
            }

            String username = jwtUtils.getUserNameFromJwtToken(accessToken);
            User userDeleted = userService.findByUsername(username);


            if(userDeleted == null){
                throw new HttpMessageNotReadableException("user is not existed");
            }

            if(user == null){
                throw new HttpMessageNotReadableException("user is not existed");
            }

            if(!(passwordEncoder.matches(user.getPassword(),userDeleted.getPassword())&& username.equals(user.getUsername()))){
                throw new BadCredentialsException("username or password is not matched");
            }

            userService.deleteUser(username);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("delete successful");
            response.setSuccess(true);
            response.getData().put("username",username);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    private ResponseEntity SendErrorValid(String field, String message){
        ErrorResponseMap errorResponseMap = new ErrorResponseMap();
        Map<String,String> temp =new HashMap<>();
        errorResponseMap.setMessage("Field already taken");
        temp.put(field,message+" has already used");
        errorResponseMap.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponseMap.setDetails(temp);
        return ResponseEntity
                .badRequest()
                .body(errorResponseMap);
    }
}