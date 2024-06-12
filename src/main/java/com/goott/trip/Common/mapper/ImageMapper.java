package com.goott.trip.Common.mapper;

import com.goott.trip.Common.model.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImageMapper {
    int insertImage(Image image); // 사진 삽입
    int updateImage(Image image); // 사진 수정
    int deleteImage(@Param("imgKey") String imgKey); // 사진 삭제
    Image selectImage(@Param("imgKey") String imgKey); // 사진 가져옴
}
