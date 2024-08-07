package com.goott.trip.common.service;

import com.goott.trip.common.mapper.ImageMapper;
import com.goott.trip.common.model.Image;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
@Service
public class ImageService {

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private ImageMapper imageMapper;

    public Optional<Image> insertImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.out.println("이미지 URL이 비었습니다.");
            return Optional.empty();
        }

        String randomKey = "trip/" + UUID.randomUUID(); // UUID를 이용한 고유 키 생성

        try {
            // URL을 MultipartFile로 변환
            MultipartFile multipartFile = urlToMultipartFile(imageUrl);
            // 기존 insertFile 메서드 재사용
            return insertFile(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private MultipartFile urlToMultipartFile(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        try (InputStream inputStream = url.openStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            String fileName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
            return new MockMultipartFile(fileName, fileName, "image/jpeg", bytes);
        }
    }

    /*
     * 이미지 파일 저장 후 저장한 Optional<Image> 반환
     * 파일이 비거나 null 혹은 오류시 Optional.empty() 객체 반환
     * if( 결과.isEmpty() ) 혹은 if( 결과.isPresent()) 를 이용하면 예외 처리 가능
     */
    public Optional<Image> insertFile(MultipartFile multipartFile) {
        // 파일 빔
        if(multipartFile == null || multipartFile.isEmpty()) {
            System.out.println("파일이 비었습니다.");
            return Optional.empty();
        }

        String randomKey = "trip/" + UUID.randomUUID();    // UUID 를 이용한 고유 키 생성

        try {
            String url = awsS3Service.uploadMultipartFile(multipartFile,randomKey);
            Timestamp now = Timestamp.valueOf(LocalDateTime.now()); // 현재시간
            Image image = new Image(randomKey,now,url);
            int dbresult = imageMapper.insertImage(image);

            if(dbresult>0) {
                return Optional.of(image); // DB 저장 성공
            }else{
                awsS3Service.deleteFile(image.getImgKey());
                return Optional.empty(); // DB 저장 실패
            }

        } catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /*
     * 기존 key 에 새로운 이미지 파일 업데이트 후 Optional<Image> 객체 반환
     * key, 파일이 비거나 null 혹은 오류시 Optional.empty() 객체 반환
     * if( 결과.isEmpty() ) 혹은 if( 결과.isPresent()) 를 이용하면 예외 처리 가능
     */
    public Optional<Image> updateFile(MultipartFile multipartFile, String key) {
        Optional<Image> resultImage = Optional.empty();
        if(key == null || key.isEmpty()){
            System.out.println("키가 비었습니다.");
            return resultImage;
        }
        if(multipartFile == null || multipartFile.isEmpty()) {
            System.out.println("파일이 비었습니다.");
            return resultImage;
        }
        try{
            awsS3Service.deleteFile(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            String url = awsS3Service.getUrl(key);
            Image image = new Image(key,Timestamp.valueOf(LocalDateTime.now()),url);
            imageMapper.updateImage(image);
            resultImage = Optional.of(image);
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultImage;
    }

    /*
     * key 로 이미지 삭제
     * key 가 null 값이거나 오류시 false 반환
     */
    public boolean deleteFile(String key) {
        if(key == null || key.isEmpty()){
            System.out.println("키가 비었습니다.");
            return false;
        }
        try{
            awsS3Service.deleteFile(key);
            imageMapper.deleteImage(key);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*
     * key 로 이미지 객체 찾기 Optional<Image> 반환
     * key 가 null 이거나 오류시 Optional.empty() 객체 반환
     * if( 결과.isEmpty() ) 혹은 if( 결과.isPresent()) 를 이용하면 예외 처리 가능
     */
    public Optional<Image> findImageByKey(String key) {
        Optional<Image> resultImage = Optional.empty();
        if(key == null || key.isEmpty()){
            System.out.println("키가 비어있습니다.");
            return resultImage;
        }
        try {
            Image image = imageMapper.selectImage(key);
            LocalDateTime createdAt = image.getCreatedAt().toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();

            // 초단위 차의 절대값이 3000초 초과면 url 새로 발급
            if (Math.abs(Duration.between(createdAt, now).getSeconds()) > 3000) {
                String newUrl = awsS3Service.getUrl(key);
                image.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
                image.setUrl(newUrl);
                // db 갱신
                imageMapper.updateImage(image);
            }
            resultImage = Optional.of(image);
        }catch (Exception e){
            System.out.println(e.getMessage());
            resultImage = Optional.empty();
        }
        return resultImage;
    }
}
