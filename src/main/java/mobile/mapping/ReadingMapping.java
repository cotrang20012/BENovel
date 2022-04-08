package mobile.mapping;

import lombok.RequiredArgsConstructor;
import mobile.Service.ChapterService;
import mobile.model.Entity.Novel;
import mobile.model.Entity.Reading;
import mobile.model.payload.response.ReadingResponse;
@RequiredArgsConstructor
public class ReadingMapping {
    private final ChapterService chapterService;
    public static ReadingResponse EntityToResponese(Reading reading, int sochap){
        ReadingResponse readingResponse= new ReadingResponse();
        readingResponse.setTentruyen(reading.getNovel().getTentruyen());
        readingResponse.setHinhanh(reading.getNovel().getHinhanh());
        readingResponse.setChapnumber(reading.getChapnumber());
        readingResponse.setUrl(reading.getNovel().getUrl());
        readingResponse.setSochap(sochap);
        return readingResponse;
    }

    public static ReadingResponse NovelToResponese(Novel novel, int sochap){
        ReadingResponse readingResponse= new ReadingResponse();
        readingResponse.setTentruyen(novel.getTentruyen());
        readingResponse.setHinhanh(novel.getHinhanh());
        readingResponse.setChapnumber(1);
        readingResponse.setUrl(novel.getUrl());
        readingResponse.setSochap(sochap);
        return readingResponse;
    }
}
