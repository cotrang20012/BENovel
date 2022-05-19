package mobile.controller;

import mobile.Service.CommentService;
import mobile.Service.UserService;
import mobile.mapping.CommentMapping;
import mobile.model.Entity.Comment;
import mobile.model.Entity.User;
import mobile.Handler.RecordNotFoundException;
import mobile.model.payload.request.comment.CommentRequest;
import mobile.model.payload.request.comment.UpdateCommentRequest;
import mobile.model.payload.request.user.ChangePassRequest;
import mobile.model.payload.response.CommentResponse;
import mobile.Handler.HttpMessageNotReadableException;
import mobile.Handler.MethodArgumentNotValidException;
import mobile.model.payload.response.SuccessResponse;
import mobile.model.payload.response.SuccessResponseList;
import mobile.security.JWT.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/comment")
@RequiredArgsConstructor
public class CommentResources {
    private static final Logger LOGGER = LogManager.getLogger(AuthentiactionController.class);

    private final UserService userService;
    private final CommentService commentService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  addComent(@RequestBody @Valid CommentRequest commentRequest, BindingResult errors, HttpServletRequest request) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken)){
                throw new BadCredentialsException("access token is expired");
            }

            String username = jwtUtils.getUserNameFromJwtToken(accessToken);
            User user= userService.findByUsername(username);


            if(user == null){
                throw new HttpMessageNotReadableException("user is not existed");
            }
            Comment comment = CommentMapping.CommentRequestToEntity(commentRequest,user);
            commentService.AddComment(comment);
            CommentResponse commentResponse = CommentMapping.EntityToResponse(comment);


            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("add comment successful");
            response.setSuccess(true);
            response.getData().put("comment",commentResponse);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }

    @GetMapping("/{url}")
    @ResponseBody
    public ResponseEntity<SuccessResponseList>  getCommentUrl(@PathVariable String url,
                                                          @RequestParam(defaultValue = "") String parentId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("chapnumber"));
        List<Comment> commentList = null;
        if(parentId.equals(""))
            commentList = commentService.findByUrl(url,pageable);
        else
            commentList = commentService.findByIdParent(parentId,pageable);

        if(commentList == null) {
            throw new RecordNotFoundException("Not found novel: "+url);
        }

        List<CommentResponse> responseList = CommentMapping.ListEntityToResponse(commentList);

        SuccessResponseList response = new SuccessResponseList();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Update info successful");
        response.setSuccess(true);
        response.getData().addAll(responseList);

        return new ResponseEntity<SuccessResponseList>(response,HttpStatus.OK);
    }

    @PutMapping("/{idComment}")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  updateCommentUrl(
            @PathVariable String idComment,
            @RequestBody String newContent, BindingResult errors, HttpServletRequest request) throws Exception {
        if (errors.hasErrors()) {
            throw new MethodArgumentNotValidException(errors);
        }
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken)){
                throw new BadCredentialsException("access token is expired");
            }

            String username = jwtUtils.getUserNameFromJwtToken(accessToken);
            User user= userService.findByUsername(username);
            Comment comment = commentService.findById(idComment);

            if(!user.getUsername().equals(comment.getUser().getUsername())){
                throw new BadCredentialsException("You can't update orther people's comment");
            }

            comment = commentService.UpdateComment(comment,newContent);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("comment updated successful");
            response.setSuccess(true);
            response.getData().put("comment",comment);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @DeleteMapping("/{idComment}")
    @ResponseBody
    public ResponseEntity<SuccessResponse>  deleteCommentUrl(@PathVariable String idComment,
                                                             HttpServletRequest request
    ) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if(jwtUtils.validateExpiredToken(accessToken)){
                throw new BadCredentialsException("access token is expired");
            }

            String username = jwtUtils.getUserNameFromJwtToken(accessToken);
            User user= userService.findByUsername(username);
            Comment comment = commentService.findById(idComment);
            if(comment == null){
                throw  new RecordNotFoundException("dont found comment with id"+ idComment);
            }
            if(!user.getUsername().equals(comment.getUser().getUsername())){
                throw new BadCredentialsException("You can't delete orther people's comment");
            }

            commentService.deleteComment(comment);

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("comment deleted successful");
            response.setSuccess(true);
            response.getData().put("comment",comment);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        }
        else
        {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @GetMapping("count/{url}")
    @ResponseBody
    public ResponseEntity<SuccessResponse> countCommentUrl(@PathVariable String url,
                                                           @RequestParam(defaultValue = "") String parentId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "3") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("chapnumber"));
        List<Comment> commentList = null;
        if(parentId.equals(""))
            commentList = commentService.findByUrl(url,pageable);
        else
            commentList = commentService.findByIdParent(parentId,pageable);

        if(commentList == null) {
            throw new RecordNotFoundException("Not found novel: "+url);
        }
        List<CommentResponse> responseList = CommentMapping.ListEntityToResponse(commentList);

        SuccessResponse response = new SuccessResponse();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("count comments successful");
        response.setSuccess(true);
        response.getData().put("countCommentByUrl",responseList.stream().count());
        return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
    }
}
