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
import mobile.model.payload.request.chapter.DeleteChapterRequest;
import mobile.model.payload.request.chapter.UpdateChapterRequest;
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
    public ResponseEntity<List<NovelResponse>> getNovels(@RequestParam(defaultValue = "None") String status,
                                                 @RequestParam(defaultValue = "tentruyen") String sort, @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "3") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<Novel> novelList = null;
        List<NovelResponse> novelResponseList=new ArrayList<>();
        if (status.equals("None"))
            novelList = novelService.getNovels(pageable);
        else
            novelList = novelService.findAllByStatus(status, pageable);

        if (novelList == null) {
            throw new RecordNotFoundException("Không tìm thấy truyện");
        }
        for (Novel novel: novelList
        ) {
            novelResponseList.add(NovelMapping.EntityToNovelResponse(novel));
        }
        return new ResponseEntity<List<NovelResponse>>(novelResponseList, HttpStatus.OK);
    }

    @GetMapping("/theloai/{theloai}")
    @ResponseBody
    public ResponseEntity<List<Novel>> getNovelsByType(@PathVariable String theloai,
                                                       @RequestParam(defaultValue = "tentruyen") String sort, @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<Novel> novelList = null;
        //List<NovelResponse> novelResponseList=new ArrayList<>();
        novelList = novelService.SearchByType(theloai, pageable);

        if (novelList == null) {
            throw new RecordNotFoundException("Không tìm thấy truyện");
        }
//        for (Novel novel: novelList
//             ) {
//            novelResponseList.add(NovelMapping.EntityToNovelResponse(novel));
//        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }
/*
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
            throw new RecordNotFoundException("Không tìm thấy truyện");
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }*/
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Novel>> searchNovelByTenTruyenLike(@RequestParam(defaultValue = "") String theloai,
                                                   @RequestParam(defaultValue = "") String tentruyen, @RequestParam(defaultValue = "tentruyen") String sort,
                                                   @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        List<Novel> novelList = null;
        if (theloai.equals("")) {
            novelList = novelService.SearchByTentruyen(tentruyen, pageable);
        } else {
            novelList = novelService.findByTentruyenLike(tentruyen);
        }

        if (novelList == null) {
            throw new RecordNotFoundException("Không tìm thấy truyện");
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
            throw new RecordNotFoundException("Không tìm thấy truyện");
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
            throw new RecordNotFoundException("Không tìm thấy truyện: " + url);
        }

        List<Chapter> chapterList = chapterService.findByDauTruyen(novel.getId(), pageable);
        if (chapterList == null) {
            throw new RecordNotFoundException("Không có chương nào được đăng");
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
            throw new RecordNotFoundException("Không tìm thấy truyện: " + url);
        }

        List<Object> chapterList = chapterService.getNameAndChapnumber(novel.getId(), pageable);
        if (chapterList == null) {
            throw new RecordNotFoundException("Không có chương nào được đăng");
        }
        return new ResponseEntity<List<Object>>(chapterList, HttpStatus.OK);
    }

    @GetMapping("/novel/{url}/mucluc/total")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> getTotalChapter(@PathVariable String url) {

        Novel novel = novelService.findByUrl(url);
        if (novel == null) {
            throw new RecordNotFoundException("Không tìm thấy truyện: " + url);
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
            throw new RecordNotFoundException("Không có chương được yêu cầu");
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
            throw new RecordNotFoundException("Không tìm thấy truyện");
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
            throw new RecordNotFoundException("Không tìm thấy truyện nào được đăng");
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }

    @GetMapping("/readings") //lấy danh sách truyện mà người dùng đã đọc tạo theo username
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
                throw new BadCredentialsException("access token đã hết hạn");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

            if (user == null)
                throw new RecordNotFoundException("Không tìm thấy người dùng");

            List<Reading> readingList = readingService.getReadings(user);
            if (readingList == null) {
                throw new RecordNotFoundException("Người dùng chưa đọc truyện nào");
            }
            List<ReadingResponse> readingResponseList = new ArrayList<>();
            for (Reading reading : readingList) {
                try {
                    int sochap = chapterService.countByDauTruyen(reading.getNovel().getId());
                    readingResponseList.add(ReadingMapping.EntityToResponese(reading, sochap));
                }
                catch (Exception ex){
                    
                }
            }

            return new ResponseEntity<List<ReadingResponse>>(readingResponseList, HttpStatus.OK);
        } else {
            throw new BadCredentialsException("Không tìm thấy access token");
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
                throw new RecordNotFoundException("Không có tìm thấy truyện");
            }
            List<ReadingResponse> readingResponseList = new ArrayList<>();
            for (Novel novel : novelList) {
                try{
                    int sochap = chapterService.countByDauTruyen(novel.getId());
                    readingResponseList.add(ReadingMapping.NovelToResponese(novel, sochap));
                }catch (Exception ex){

                }

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
                throw new BadCredentialsException("access token đã hết hạn");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("Không tìm thấy người dùng");

            Novel newNovel = NovelMapping.CreateRequestToNovel(createNovelRequest);
            newNovel.setNguoidangtruyen(user);
            novelService.SaveNovel(newNovel);


            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Đăng truyện mới thành công");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("Không tìm thấy access token");
        }
    }
    @PutMapping("novel/edit")//Update đầu truyện
    @ResponseBody
    public ResponseEntity<SuccessResponse> editNovel(@RequestBody UpdateNovelRequest updateNovelRequest,HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token đã hết hạn");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("Không tìm thấy người dùng");

            ObjectId truyenId = new ObjectId(updateNovelRequest.getId());
            Optional<Novel> findNovel = novelService.findById(truyenId);
            if(!findNovel.isPresent()){
                throw new RecordNotFoundException("Không tìm thấy truyện");
            }

            Novel oldNovel = findNovel.get();
            if(oldNovel.getNguoidangtruyen().getUsername().equals(user.getUsername())){
                NovelMapping.UpdateRequestToNovel(updateNovelRequest,oldNovel);
                novelService.SaveNovel(oldNovel);
            }
            else{
                throw new BadCredentialsException("Không thể chỉnh sửa truyện của người khác");
            }

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Cập nhật truyện thành công");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("Không tìm thấy access token");
        }
    }
    @DeleteMapping("/{url}")//Delete đầu truyện, sẽ delete chapter, comment, reading liên kết cùng
    @ResponseBody
    public ResponseEntity<SuccessResponse> deleteNovel(@PathVariable String url,HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token đã hết hạn");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("Không tìm thấy người dùng");

            Novel findNovel = novelService.findByUrl(url);
            if(findNovel == null ){
                throw new RecordNotFoundException("Không tìm thấy truyện");
            }

            if(findNovel.getNguoidangtruyen().getUsername().equals(user.getUsername())){
                commentService.DeleteCommentByNovelUrl(findNovel.getUrl());
                readingService.deleteAllReadingByNovel(findNovel);
                chapterService.DeleteAllChapterByNovel(findNovel);
                novelService.DeleteNovel(findNovel);
            }
            else{
                throw new BadCredentialsException("Không thể chỉnh sửa truyện của người khác");
            }

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Xóa truyện thành công");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("Không có access token");
        }
    }

    @GetMapping("/novel/newupdate")
    @ResponseBody
    public ResponseEntity<List<ChapterNewUpdateResponse>> getNewestUpdate(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<Chapter> chapters = chapterService.getChaptersNewUpdate(pageable);
        if (chapters == null) {
            throw new RecordNotFoundException("Truyện không có chương nào !!!");
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
                throw new BadCredentialsException("access token đã hết hạn");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("Không tìm thấy người dùng");

            if(createChapterRequest.getContent().length()<10){
                throw new BadCredentialsException("Nội dung phải dài hơn 10 ký tự");
            }
            Novel novel = novelService.findByUrl(createChapterRequest.getUrl());
            if(novel == null){
                throw new RecordNotFoundException("Không tìm thấy truyện");
            }

            if(novel.getNguoidangtruyen().getUsername().equals(user.getUsername())){
                int chapnumber = chapterService.countByDauTruyen(novel.getId()) + 1;
                String tenchap = "Chương "+chapnumber+": " +createChapterRequest.getTenchap();
                Chapter newChapter = new Chapter();
                newChapter.setDautruyenId(novel);
                newChapter.setContent(createChapterRequest.getContent());
                newChapter.setChapnumber(chapnumber);
                newChapter.setTenchap(tenchap);
                chapterService.SaveChapter(newChapter);
            }
            else{
                throw new BadCredentialsException("Không thể chỉnh sửa truyện của người khác");
            }

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Đăng chương mới thành công");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("Không tìm thấy access token");
        }
    }
    @PutMapping("/novel/chuong/edit")
    @ResponseBody
    public ResponseEntity<SuccessResponse> UpdateChapter(@RequestBody UpdateChapterRequest updateChapterRequest, HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token đã hết hạn");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("Không tìm thấy người dùng");

            if(updateChapterRequest.getContent().length()<10){
                throw new BadCredentialsException("Nội dung phải dài hơn 10 ký tự");
            }
            Novel novel = novelService.findByUrl(updateChapterRequest.getUrl());
            if(novel == null){
                throw new RecordNotFoundException("Không tìm thấy truyện");
            }
            Chapter chapter = chapterService.findByDauTruyenAndChapterNumber(novel.getId(), updateChapterRequest.getChapnumber());
            if(chapter == null){
                throw new RecordNotFoundException("Không tìm thấy chương cần chỉnh sửa");
            }
            if(novel.getNguoidangtruyen().getUsername().equals(user.getUsername())){
                String tenchap = updateChapterRequest.getTenchap();
                chapter.setTenchap(tenchap);
                chapter.setContent(updateChapterRequest.getContent());
                chapterService.SaveChapter(chapter);
            }
            else{
                throw new BadCredentialsException("Không thể chỉnh sửa truyện của người khác");
            }

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Cập nhật chương thành công");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("Không tìm thấy access token");
        }
    }

    @DeleteMapping("/novel/chuong")
    @ResponseBody
    public ResponseEntity<SuccessResponse> DeleteChapter(@RequestBody DeleteChapterRequest deleteChapterRequest, HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring("Bearer ".length());

            if (jwtUtils.validateExpiredToken(accessToken) == true) {
                throw new BadCredentialsException("access token đã hết hạn");
            }

            User user = userService.findByUsername(jwtUtils.getUserNameFromJwtToken(accessToken));

            if (user == null)
                throw new RecordNotFoundException("Không tìm thấy người dùng");

            Novel novel = novelService.findByUrl(deleteChapterRequest.getUrl());
            if(novel == null){
                throw new RecordNotFoundException("Không tìm thấy truyện");
            }
            Chapter chapter = chapterService.findByDauTruyenAndChapterNumber(novel.getId(), deleteChapterRequest.getChapnumber());
            if(chapter == null){
                throw new RecordNotFoundException("Không tìm thấy chương cần chỉnh sửa");
            }

            if(novel.getNguoidangtruyen().getUsername().equals(user.getUsername())){
                chapterService.DeleteChapter(chapter);
            }
            else{
                throw new BadCredentialsException("Không thể chỉnh sửa truyện của người khác");
            }

            SuccessResponse response = new SuccessResponse();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Xóa chương thành công");
            response.setSuccess(true);
            return new ResponseEntity<SuccessResponse>(response,HttpStatus.OK);
        } else {
            throw new BadCredentialsException("Không tìm thấy access token");
        }
    }
}
