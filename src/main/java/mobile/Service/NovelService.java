package mobile.Service;

import mobile.model.Entity.Novel;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NovelService {
    List<Novel> getNovels();
    Novel findByName(String name);
    Novel findByUrl(String url);
    List<Novel> getNovels(Pageable pageable);
    List<Novel> findAllByStatus(String status,Pageable pageable);
    List<Novel> SearchByTentruyen(String value,Pageable pageable);
    List<Novel> SearchByTypeAndTentruyen(String type,String value,Pageable pageable);
    List<Novel> SearchByTacgia(String value,Pageable pageable);
    List<Novel> SearchByType(String theloai,Pageable pageable);
    List<Novel> SearchByNguoidangtruyen(ObjectId id, Pageable pageable);
    void SaveNovel(Novel newNovel);
    Optional<Novel> findById(ObjectId id);
    void DeleteNovel(Novel novel);
    List<Novel> findByTentruyenLike(String name);
}
