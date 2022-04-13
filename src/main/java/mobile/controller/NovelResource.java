package mobile.controller;

import mobile.Handler.HttpMessageNotReadableException;
import mobile.Service.ChapterService;
import mobile.Service.CommentService;
import mobile.Service.NovelService;
import mobile.Service.ReadingService;
import mobile.Service.UserService;
import mobile.mapping.ChapterMapping;
import mobile.mapping.CommentMapping;
import mobile.mapping.NovelMapping;
import mobile.mapping.ReadingMapping;
import mobile.model.Entity.*;
import mobile.model.payload.request.chapter.CreateChapterRequest;
import mobile.model.payload.request.novel.CreateNovelRequest;
import mobile.model.payload.request.novel.UpdateNovelRequest;
import mobile.model.payload.request.reading.ReadingRequest;
import mobile.model.payload.response.*;
import mobile.security.JWT.JwtUtils;
import mobile.Handler.MethodArgumentNotValidException;
import mobile.Handler.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("api/novels")
@RequiredArgsConstructor
public class NovelResource {
    private static final Logger LOGGER = LogManager.getLogger(NovelResource.class);

    private final UserService userService;
    private final NovelService novelService;
    private final ChapterService chapterService;
    private final ReadingService readingService;
    private final CommentService commentService;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Novel>> getNovels(@RequestParam(defaultValue = "None") String status,
                                                 @RequestParam(defaultValue = "tentruyen") String sort, @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "3") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<Novel> novelList = null;
        if (status.equals("None"))
            novelList = novelService.getNovels(pageable);
        else
            novelList = novelService.findAllByStatus(status, pageable);

        if (novelList == null) {
            throw new RecordNotFoundException("No Novel existing ");
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }

    @GetMapping("/theloai/{theloai}")
    @ResponseBody
    public ResponseEntity<List<Novel>> getNovelsByType(@PathVariable String theloai,
                                                       @RequestParam(defaultValue = "tentruyen") String sort, @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<Novel> novelList = null;
        novelList = novelService.SearchByType(theloai, pageable);

        if (novelList == null) {
            throw new RecordNotFoundException("No Novel existing ");
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Novel>> searchNovel(@RequestParam(defaultValue = "") String theloai,
                                                   @RequestParam(defaultValue = "") String value, @RequestParam(defaultValue = "tentruyen") String sort,
                                                   @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<Novel> novelList = null;
        if (theloai.equals("")) {
            novelList = novelService.SearchByTentruyen(value, pageable);
        } else {
            novelList = novelService.SearchByTypeAndTentruyen(theloai, value, pageable);
        }

        if (novelList == null) {
            throw new RecordNotFoundException("No Novel existing ");
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }

    @GetMapping("/novel/{url}")
    @ResponseBody
    public ResponseEntity<NovelDetailResponse> getNovelByName(@PathVariable String url) {

        Novel novel = novelService.findByUrl(url);
        int sochap = chapterService.countByDauTruyen(novel.getId());
        NovelDetailResponse novelDetailResponse = NovelMapping.EntityToNovelDetailResponse(novel,sochap);
        if (novelDetailResponse == null) {
            throw new RecordNotFoundException("No Novel existing ");
        }
        return new ResponseEntity<NovelDetailResponse>(novelDetailResponse, HttpStatus.OK);
    }

    @GetMapping("/novel/{url}/chuong")
    @ResponseBody
    public ResponseEntity<List<Chapter>> getChapterpagination(@PathVariable String url,
                                                              @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("chapnumber"));

        Novel novel = novelService.findByUrl(url);
        if (novel == null) {
            throw new RecordNotFoundException("Not found novel: " + url);
        }

        List<Chapter> chapterList = chapterService.findByDauTruyen(novel.getId(), pageable);
        if (chapterList == null) {
            throw new RecordNotFoundException("No Chapter existing");
        }
        return new ResponseEntity<List<Chapter>>(chapterList, HttpStatus.OK);
    }

    @GetMapping("/novel/{url}/mucluc")
    @ResponseBody
    public ResponseEntity<List<Object>> getMuclucpagination(@PathVariable String url,
                                                            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("chapnumber"));

        Novel novel = novelService.findByUrl(url);
        if (novel == null) {
            throw new RecordNotFoundException("Not found novel: " + url);
        }

        List<Object> chapterList = chapterService.getNameAndChapnumber(novel.getId(), pageable);
        if (chapterList == null) {
            throw new RecordNotFoundException("No Chapter existing");
        }
        return new ResponseEntity<List<Object>>(chapterList, HttpStatus.OK);
    }

    @GetMapping("/novel/{url}/mucluc/total")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> getTotalChapter(@PathVariable String url) {

        Novel novel = novelService.findByUrl(url);
        if (novel == null) {
            throw new RecordNotFoundException("Not found novel: " + url);
        }

        int chaptolal = chapterService.countByDauTruyen(novel.getId());
        Map<String, Integer> map = new HashMap<>();
        map.put("total", chaptolal);

        return new ResponseEntity<Map<String, Integer>>(map, HttpStatus.OK);
    }

    @GetMapping("/novel/{url}/chuong/{chapterNumber}")
    @ResponseBody
    public ResponseEntity<Chapter> getChapter(@PathVariable String url, @PathVariable int chapterNumber, HttpServletRequest request) {
        Novel novel = novelService.findByUrl(url);
        Chapter chapter = chapterService.findByDauTruyenAndChapterNumber(novel.getId(), chapterNumber);
        if (chapter == null) {
            throw new RecordNotFoundException("No Chapter existing ");
        }

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) != true) {
                String username = jwtUtils.getUserNameFromJwtToken(accessToken);
                User user = userService.findByUsername(username);

                Reading reading = new Reading(user, chapterNumber, novel);
                readingService.upsertReading(reading);
            }
        }


        return new ResponseEntity<Chapter>(chapter, HttpStatus.OK);
    }

    @GetMapping("/tacgia/{tacgia}")
    @ResponseBody
    public ResponseEntity<List<Novel>> searchNovelByTacgia(@PathVariable String tacgia,
                                                           @RequestParam(defaultValue = "tentruyen") String sort, @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<Novel> novelList = novelService.SearchByTacgia(tacgia, pageable);

        if (novelList == null) {
            throw new RecordNotFoundException("No Novel existing ");
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }

    @GetMapping("/created") //lấy danh sách truyện được tạo theo username
    @ResponseBody
    public ResponseEntity<List<Novel>> getNovelsByUsername(@RequestParam(defaultValue = "None") String status,
                                                           @RequestParam(defaultValue = "tentruyen") String sort,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "20") int size,
                                                           @RequestParam String username,
                                                           HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        User user = userService.findByUsername(username);
        System.out.println(user.getId().toHexString());
        List<Novel> novelList = novelService.SearchByNguoidangtruyen(user.getId(), pageable);
        if (novelList == null) {
            throw new RecordNotFoundException("No Novel existing ");
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }

    @GetMapping("/readings") //lấy danh sách truyện được tạo theo username
    @ResponseBody
    public ResponseEntity<List<ReadingResponse>> getReadingsByUsername(@RequestParam(defaultValue = "None") String status,
                                                                       @RequestParam(defaultValue = "tentruyen") String sort,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "20") int size,
														   HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

            if (user == null)
                throw new RecordNotFoundException("User not found");

            List<Reading> readingList = readingService.getReadings(user);
            if (readingList == null) {
                throw new RecordNotFoundException("No Reading existing ");
            }
            List<ReadingResponse> readingResponseList = new ArrayList<>();
            for (Reading reading : readingList) {
                int sochap = chapterService.countByDauTruyen(reading.getNovel().getId());
                readingResponseList.add(ReadingMapping.EntityToResponese(reading, sochap));
            }

            return new ResponseEntity<List<ReadingResponse>>(readingResponseList, HttpStatus.OK);
        } else {
            throw new BadCredentialsException("access token is missing");
        }
    }

    @GetMapping("/readingsdefault") //lấy danh sách truyện được tạo theo username
    @ResponseBody
    public ResponseEntity<List<ReadingResponse>> getReadingsDefault(@RequestParam(defaultValue = "None") String status,
                                                                       @RequestParam(defaultValue = "tentruyen") String sort,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "20") int size,
                                                                       HttpServletRequest request) {

            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

            List<Novel> novelList = novelService.getNovels(pageable);
            if (novelList == null) {
                throw new RecordNotFoundException("No Reading existing ");
            }
            List<ReadingResponse> readingResponseList = new ArrayList<>();
            for (Novel novel : novelList) {
                int sochap = chapterService.countByDauTruyen(novel.getId());
                readingResponseList.add(ReadingMapping.NovelToResponese(novel, sochap));
            }

            return new ResponseEntity<List<ReadingResponse>>(readingResponseList, HttpStatus.OK);

    }

    @PostMapping("novel/create") //Tạo đầu truyện
    @ResponseBody
    public ResponseEntity<SuccessResponse> createNovel(@RequestBody CreateNovelRequest createNovelRequest, HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("User not found");

            Novel newNovel = NovelMapping.CreateRequestToNovel(createNovelRequest);
            newNovel.setNguoidangtruyen(user);
            novelService.SaveNovel(newNovel);


            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Create novel success!!");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @PutMapping("novel/edit")//Update đầu truyện
    @ResponseBody
    public ResponseEntity<SuccessResponse> editNovel(@RequestBody UpdateNovelRequest updateNovelRequest,HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("User not found");

            ObjectId truyenId = new ObjectId(updateNovelRequest.getId());
            Optional<Novel> findNovel = novelService.findById(truyenId);
            if(!findNovel.isPresent()){
                throw new RecordNotFoundException("Novel not found");
            }

            Novel oldNovel = findNovel.get();
            if(oldNovel.getNguoidangtruyen().getUsername().equals(user.getUsername())){
                NovelMapping.UpdateRequestToNovel(updateNovelRequest,oldNovel);
                novelService.SaveNovel(oldNovel);
            }
            else{
                throw new BadCredentialsException("Can't edit other user novel!!!!");
            }

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Update novel success!!");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("access token is missing");
        }
    }
    @DeleteMapping("/{url}")//Delete đầu truyện, sẽ delete chapter, comment, reading liên kết cùng
    @ResponseBody
    public ResponseEntity<SuccessResponse> deleteNovel(@PathVariable String url,HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("User not found");

            Novel findNovel = novelService.findByUrl(url);
            if(findNovel == null ){
                throw new RecordNotFoundException("Novel not found");
            }

            if(findNovel.getNguoidangtruyen().getUsername().equals(user.getUsername())){
                //commentService.DeleteCommentByNovelUrl(findNovel.getUrl());
                //readingService.deleteAllReadingByNovel(findNovel);
                chapterService.DeleteAllChapterByNovel(findNovel);
                novelService.DeleteNovel(findNovel);
            }
            else{
                throw new BadCredentialsException("Can't edit other user novel!!!!");
            }

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Delete novel success!!");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("access token is missing");
        }
    }

    @GetMapping("/novel/newupdate")
    @ResponseBody
    public ResponseEntity<List<ChapterNewUpdateResponse>> getNewestUpdate(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<Chapter> chapters = chapterService.getChaptersNewUpdate(pageable);
        if (chapters == null) {
            throw new RecordNotFoundException("No Chapter existing" );
        }
        List<ChapterNewUpdateResponse> list = ChapterMapping.getListChapterNewUpdateResponse(chapters);
        return new ResponseEntity<List<ChapterNewUpdateResponse>>(list, HttpStatus.OK);
    }

    @PostMapping("/novel/chuong/create")
    @ResponseBody
    public ResponseEntity<SuccessResponse> CreateChapter(@RequestBody CreateChapterRequest createChapterRequest, HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token is  expired");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("User not found");

            if(createChapterRequest.getContent().length()<10){
                throw new BadCredentialsException("Nội dung phải dài hơn 10 ký tự");
            }
            Novel novel = novelService.findByUrl(createChapterRequest.getUrl());
            if(novel == null){
                throw new RecordNotFoundException("Novel not found");
            }

            if(novel.getNguoidangtruyen().getUsername().equals(user.getUsername())){
                int chapnumber = chapterService.countByDauTruyen(novel.getId());
                String tenchap = "Chương "+chapnumber+": " +createChapterRequest.getTenchap();
                Chapter newChapter = new Chapter();
                newChapter.setDautruyenId(novel);
                newChapter.setContent(createChapterRequest.getContent());
                newChapter.setChapnumber(chapnumber);
                newChapter.setTenchap(tenchap);
                chapterService.SaveChapter(newChapter);
            }
            else{
                throw new BadCredentialsException("Can't edit other user novel!!!!");
            }

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Create chapter success!!");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("access token is missing");
        }
    }
}
