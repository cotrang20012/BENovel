package mobile.Service.Impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
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
	public void upsertReading(Reading reading) {
		Optional<Reading> readingDB = readingRepository.findWithParam(reading.getUser().getId(), reading.getNovel().getId());
		if(readingDB.isEmpty()) {
			Reading newReading = new Reading(reading.getUser(),reading.getChapnumber(),reading.getNovel());
			readingRepository.save(newReading);
		} else {
			Reading oldReading = readingDB.get();
			oldReading.setChapnumber(reading.getChapnumber());
			readingRepository.save(oldReading);
		}
	}

	@Override
	public List<Reading> getReadings(User user) {
		List<Reading> list = readingRepository.findByUserId(user.getId());
		return list;
	}

	@Override
	public void deleteAllReadingByNovel(Novel novel) {
		readingRepository.deleteAllByNovel(novel);
	}
}
