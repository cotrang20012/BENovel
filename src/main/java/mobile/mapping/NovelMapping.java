package mobile.mapping;

import mobile.model.Entity.Novel;
import mobile.model.payload.request.novel.CreateNovelRequest;
import mobile.model.payload.request.novel.UpdateNovelRequest;

public class NovelMapping {
    public static Novel CreateRequestToNovel(CreateNovelRequest createNovelRequest){
        Novel newNovel = new Novel();
        newNovel.setHinhanh(createNovelRequest.getHinhanh());
        newNovel.setTentruyen(createNovelRequest.getTentruyen());
        newNovel.setTacgia(createNovelRequest.getTacgia());
        newNovel.setUrl(createNovelRequest.getUrl());
        newNovel.setDanhgia(0);
        newNovel.setNoidung(createNovelRequest.getNoidung());
        return newNovel;
    }

    public static void UpdateRequestToNovel(UpdateNovelRequest updateNovelRequest, Novel oldNovel){
        oldNovel.setHinhanh(updateNovelRequest.getHinhanh());
        oldNovel.setTentruyen(updateNovelRequest.getTentruyen());
        oldNovel.setTacgia(updateNovelRequest.getTacgia());
        oldNovel.setUrl(updateNovelRequest.getUrl());
        oldNovel.setNoidung(updateNovelRequest.getNoidung());
    }
}
