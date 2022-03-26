package mobile.controller;

import mobile.Service.ChapterService;
import mobile.Service.CommentService;
import mobile.Service.NovelService;
import mobile.Service.ReadingService;
import mobile.Service.UserService;
import mobile.model.Entity.Chapter;
import mobile.model.Entity.Novel;
import mobile.model.Entity.User;
import mobile.security.JWT.JwtUtils;
import mobile.Handler.MethodArgumentNotValidException;
import mobile.Handler.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/novels")
@RequiredArgsConstructor
public class NovelResource {
	private static final Logger LOGGER = LogManager.getLogger(NovelResource.class);

	private final UserService userService;
	private final NovelService novelService;
	private final ChapterService chapterService;
	private final ReadingService readingService;

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
	public ResponseEntity<Novel> getNovelByName(@PathVariable String url) {

		Novel novel = novelService.findByUrl(url);
		if (novel == null) {
			throw new RecordNotFoundException("No Novel existing ");
		}
		return new ResponseEntity<Novel>(novel, HttpStatus.OK);
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
	public ResponseEntity<Chapter> getChapter(@PathVariable String url, @PathVariable int chapterNumber,HttpServletRequest request) {
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
				
				readingService.upsertReading(user, chapter, novel, url);
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
}
