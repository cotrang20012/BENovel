package mobile.Service.Impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mobile.Service.ReadingService;
import mobile.model.Entity.Chapter;
import mobile.model.Entity.Novel;
import mobile.model.Entity.Reading;
import mobile.model.Entity.User;
import mobile.repository.NovelRepository;
import mobile.repository.ReadingRepository;


@Service @RequiredArgsConstructor @Transactional @Slf4j
public class ReadingServiceImpl implements ReadingService{
	final ReadingRepository readingRepository;
	
	@Override
	public void upsertReading(User user, Chapter chapter, Novel novel, String url) {
		Optional<Reading> reading = readingRepository.findByUserIdAndDautruyenId(user.getId(), novel.getId());
		if(reading.isEmpty()) {
			Reading newReading = new Reading(novel.getId(),chapter.getId(),user.getId(),chapter.getChapnumber(),url);
			readingRepository.save(newReading);
		} else {
			Reading oldReading = reading.get();
			oldReading.setChapnumber(chapter.getChapnumber());
			oldReading.setChapterId(chapter.getId());
			readingRepository.save(oldReading);
		}
	}
	
}
