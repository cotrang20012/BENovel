package mobile.Service.Impl;

import mobile.Service.ChapterService;
import mobile.model.Entity.Chapter;
import mobile.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class ChapterServiceImpl implements ChapterService {
     final ChapterRepository chapterRepository;

    @Override
    public List<Chapter> findByDauTruyen(ObjectId id) {
        log.info("Fetching all chapter  Novel id: "+id.toHexString());
        return chapterRepository.findByDautruyenId(id);
    }

    @Override
    public List<Chapter> findByDauTruyen(ObjectId id, Pageable pageable) {
        log.info("Fetching all chapter  Novel id: "+id.toHexString()+" Page: "+pageable.getPageNumber()+" Size "+pageable.getPageSize());
        return chapterRepository.findAllByDautruyenId(id, pageable);
    }

    @Override
    public Chapter findByDauTruyenAndChapterNumber(ObjectId id, int number) {
        log.info("Fetching all chapter  Novel id: "+id.toHexString()+" chpater: "+number);
        return chapterRepository.findByDautruyenIdAndChapnumber(id,number).get();
    }

    @Override
    public int countByDauTruyen(ObjectId id) {
        log.info("count all chapter  Novel id: "+id.toHexString());
        return chapterRepository.countAllByDautruyenId(id);
    }

    @Override
    public List<Object> getNameAndChapnumber(ObjectId id,Pageable pageable) {
        log.info("fetch all chapter  Novel id: "+id.toHexString());
        return chapterRepository.getTenchapAndChapNumberByDautruyenId(id,pageable);
    }
}
