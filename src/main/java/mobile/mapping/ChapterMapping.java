package mobile.mapping;

import mobile.model.Entity.Chapter;
import mobile.model.payload.response.ChapterNewUpdateResponse;

import java.util.ArrayList;
import java.util.List;

public class ChapterMapping {

    public static ChapterNewUpdateResponse getChapterNewUpdateResponse(Chapter chapter){
        ChapterNewUpdateResponse c =new ChapterNewUpdateResponse();
        c.setTheloai(chapter.getDautruyenId().getTheloai());
        c.setTentruyen(chapter.getDautruyenId().getTentruyen());
        c.setTacgia(chapter.getDautruyenId().getTacgia());
        c.setUpdateAt(chapter.getUpdateAt());
        c.setChapnumber(chapter.getChapnumber());
        c.setNguoidangtruyen(chapter.getDautruyenId().getNguoidangtruyen().getTenhienthi());
        c.setTenchap(chapter.getTenchap());
        c.setUrl(chapter.getDautruyenId().getUrl());
        return c;
    }
    public static List<ChapterNewUpdateResponse> getListChapterNewUpdateResponse(List<Chapter> chapterList){
        List<ChapterNewUpdateResponse> list = new ArrayList<>();
        for (Chapter chapter:chapterList ) {
            list.add(getChapterNewUpdateResponse(chapter));
        }
        return  list;
    }

}
