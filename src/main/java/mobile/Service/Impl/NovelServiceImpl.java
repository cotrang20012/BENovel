package mobile.Service.Impl;

import mobile.Service.NovelService;

import mobile.model.Entity.Novel;
import mobile.repository.NovelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class NovelServiceImpl implements NovelService {
    final NovelRepository novelRepository;

    @Override
    public List<Novel> getNovels(){
        log.info("Fetching all Novels ");
        return novelRepository.findAll();
    }

    @Override
    public List<Novel> getNovels(Pageable pageable) {
        log.info("Fetching all Novels page: "+pageable.getPageNumber()+" page size: "+pageable.getPageSize());
        return novelRepository.findAllBy_idNotNull(pageable);
    }



    @Override
    public Novel findByName(String name) {
        log.info("Fetching  Novel: "+name);
        return novelRepository.findByTentruyen(name).get();
    }

    @Override
    public Novel findByUrl(String url) {
        log.info("Fetching  Novel: "+url);
        return novelRepository.findByUrl(url).get();
    }

    @Override
    public List<Novel> findAllByStatus(String status, Pageable pageable) {
        log.info("Fetching  Novel status: "+status);
        return novelRepository.findAllByStatus(status,pageable);
    }

    @Override
    public List<Novel> SearchByTentruyen(String value,Pageable pageable) {
        log.info("Searching  Novel value: "+value);
        return novelRepository.findAllByTentruyenContainsAllIgnoreCase(value, pageable);
    }

    @Override
    public List<Novel> SearchByTypeAndTentruyen(String type, String value, Pageable pageable) {
        log.info("Searching  Novel type: "+type+" value: "+value);
        return novelRepository.findAllByTheloaiContainsAndTentruyenContainsAllIgnoreCase(type,value, pageable);
    }

    @Override
    public List<Novel> SearchByTacgia(String value, Pageable pageable) {
        log.info("Searching Novel value: "+value);
        return novelRepository.findAllByTacgiaContainsAllIgnoreCase(value, pageable);
    }

    @Override
    public List<Novel> SearchByType(String theloai, Pageable pageable) {
        log.info("Searching Novel by theloai: "+theloai);
        return novelRepository.findAllByTheloaiContainsAllIgnoreCase(theloai, pageable);
    }
}
