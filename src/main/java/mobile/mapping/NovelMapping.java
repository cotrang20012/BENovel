package mobile.mapping;

import mobile.model.Entity.Novel;
import mobile.model.payload.request.novel.CreateNovelRequest;
import mobile.model.payload.request.novel.UpdateNovelRequest;
import mobile.model.payload.response.NovelDetailResponse;
import mobile.model.payload.response.NovelResponse;

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

    public static NovelDetailResponse EntityToNovelDetailResponse(Novel novel, int sochap){
        NovelDetailResponse novelDetailResponse = new NovelDetailResponse();
        novelDetailResponse.setHinhanh(novel.getHinhanh());
        novelDetailResponse.setTentruyen(novel.getTentruyen());
        novelDetailResponse.setTacgia(novel.getTacgia());
        novelDetailResponse.setUrl(novel.getUrl());
        novelDetailResponse.setDanhgia(novel.getDanhgia());
        novelDetailResponse.setNoidung(novel.getNoidung());
        novelDetailResponse.setNguoidangtruyen(novel.getNguoidangtruyen().getTenhienthi());
        novelDetailResponse.setLuotdoc(novel.getLuotdoc());
        novelDetailResponse.setSoluongdanhgia(novel.getSoluongdanhgia());
        novelDetailResponse.setSochap(sochap);
        novelDetailResponse.setTheloai(novel.getTheloai());
        novelDetailResponse.setTrangthai(novel.getTrangthai());
        return novelDetailResponse;
    }

    public static void UpdateRequestToNovel(UpdateNovelRequest updateNovelRequest, Novel oldNovel){
        oldNovel.setHinhanh(updateNovelRequest.getHinhanh());
        oldNovel.setTentruyen(updateNovelRequest.getTentruyen());
        oldNovel.setTacgia(updateNovelRequest.getTacgia());
        oldNovel.setUrl(updateNovelRequest.getUrl());
        oldNovel.setNoidung(updateNovelRequest.getNoidung());
    }

    public static NovelResponse EntityToNovelResponse(Novel novel){
        NovelResponse novelResponse = new NovelResponse();
        novelResponse.setHinhanh(novel.getHinhanh());
        novelResponse.setTentruyen(novel.getTentruyen());
        novelResponse.setTacgia(novel.getTacgia());
        novelResponse.setUrl(novel.getUrl());
        novelResponse.setDanhgia(novel.getDanhgia());
        novelResponse.setNoidung(novel.getNoidung());
        novelResponse.setLuotdoc(novel.getLuotdoc());
        novelResponse.setSoluongdanhgia(novel.getSoluongdanhgia());
        novelResponse.setTheloai(novel.getTheloai());
        novelResponse.setTrangthai(novel.getTrangthai());
        return novelResponse;
    }
}
