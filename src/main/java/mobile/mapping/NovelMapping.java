package mobile.mapping;

import mobile.model.Entity.Novel;
import mobile.model.payload.request.novel.CreateNovelRequest;

public class NovelMapping {
    public static Novel CreateRequestToNovel(CreateNovelRequest createNovelRequest){
        Novel newNovel = new Novel();
        newNovel.setHinhanh(createNovelRequest.getHinhanh());
        newNovel.setTentruyen(createNovelRequest.getTentruyen());
        newNovel.setTacgia(createNovelRequest.getTacgia());
        newNovel.setUrl(createNovelRequest.getUrl());
        newNovel.setDanhgia(0);
        //newNovel.setNoidung();
        return newNovel;
    }
}
