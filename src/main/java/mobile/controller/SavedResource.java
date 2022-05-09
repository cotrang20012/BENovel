package mobile.controller;


import lombok.RequiredArgsConstructor;
import mobile.Handler.HttpMessageNotReadableException;
import mobile.Handler.RecordNotFoundException;
import mobile.Service.NovelService;
import mobile.Service.SavedService;
import mobile.Service.UserService;
import mobile.mapping.SavedMapping;
import mobile.model.Entity.Novel;
import mobile.model.Entity.Saved;
import mobile.model.Entity.User;
import mobile.model.payload.request.saved.SavedRequest;
import mobile.model.payload.response.SavedResponse;
import mobile.model.payload.response.SuccessResponse;
import mobile.security.DTO.AppUserDetail;
import mobile.security.JWT.JwtUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/saved")
@RequiredArgsConstructor
public class SavedResource {
    private static final Logger LOGGER = LogManager.getLogger(NovelResource.class);

    private final UserService userService;
    private final NovelService novelService;
    private final SavedService savedService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<SuccessResponse> createSaved(@RequestBody @Valid SavedRequest savedRequest
                                                        , HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken) == true){
                throw new BadCredentialsException("access token is expired");
            }

            String username = jwtUtils.getUserNameFromJwtToken(accessToken);
            User user= userService.findByUsername(username);
            System.out.println(user.getId());
            if(user == null){
                throw new HttpMessageNotReadableException("user is not existed");
            }

            Novel novel = novelService.findByUrl(savedRequest.getUrl());
            if(novel == null){
                throw new RecordNotFoundException("Novel is not existed");
            }

            Saved saved = new Saved(user,novel);
            savedService.createSaved(saved);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Saved novel successful");
            response.setSuccess(true);
            response.getData().put("username",username);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }



    @GetMapping("/savedbyuser")
    @ResponseBody
    public ResponseEntity<List<SavedResponse>> getSavedsByUsername(HttpServletRequest request){
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

            List<Saved> savedList = savedService.getSavedByUserId(user.getId());
            if(savedList == null){
                throw new RecordNotFoundException("Saved is not existed");
            }
            List<SavedResponse> savedResponseList = SavedMapping.ListEntityToResponse(savedList);

            return new ResponseEntity<List<SavedResponse>>(savedResponseList,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }

    @GetMapping("/{url}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> checkSavedByUsername(@PathVariable String url,HttpServletRequest request){
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
            Novel novel = novelService.findByUrl(url);
            if(novel == null){
                throw new HttpMessageNotReadableException("Novel is not existed");
            }
            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setSuccess(true);

            Saved saved = savedService.getSaved(user.getId(),novel.getId());
            if(saved == null){
                response.getData().put("saved",false);
            }
            else
                response.getData().put("saved",true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @DeleteMapping("")
    @ResponseBody
    public ResponseEntity<SuccessResponse> deleteSaved(@RequestBody @Valid SavedRequest savedRequest
            , HttpServletRequest request){
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

            Novel novel = novelService.findByUrl(savedRequest.getUrl());
            if(novel == null){
                throw new RecordNotFoundException("Novel is not existed");
            }

            Saved deleteSaved = savedService.deleteSaved(user.getId(),novel.getId());
            if(deleteSaved == null){
                throw new RecordNotFoundException("Saved is not existed");
            }

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Unsaved novel successful");
            response.setSuccess(true);
            response.getData().put("username",deleteSaved);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }

}
