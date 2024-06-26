package com.goott.trip.concho.mapper;

import com.goott.trip.concho.model.HotelCrawledImg;
import com.goott.trip.concho.model.HotelCrawledInfo;
import com.goott.trip.concho.model.HotelCrawledRoom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface HotelCrawledMapper {
    void insertHotelCrawledInfo(HotelCrawledInfo hotelCrawledInfo);
    List<HotelCrawledInfo> findHotelCrawledInfoByHotelId(String crHotelId);

    void insertHotelCrawledImg(HotelCrawledImg hotelCrawledImg);
    List<HotelCrawledImg> findHotelCrawledImgByHotelId(String crHotelId);

    void insertHotelCrawledRoom(HotelCrawledRoom hotelCrawledRoom);
    List<HotelCrawledRoom> findHotelCrawledRoomByHotelId(String crHotelId);
    // 이미 크롤링 한 데이터인지 확인
    boolean existsHotelInAllTables(String crHotelId);
    // 이미지 하나 얻기
    Optional<HotelCrawledImg> findOneHotelCrawledImgByHotelId(String crHotelId);
}
