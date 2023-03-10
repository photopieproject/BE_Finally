package com.sparta.be_finally.photo.repository;

import com.sparta.be_finally.photo.dto.FrameTop5Interface;
import com.sparta.be_finally.photo.entity.Photo;
import com.sparta.be_finally.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    // 룸 객체를 찾아 photo를 반환
    Photo findByRoom(Room room);



    // 방 번호 확인후 완성된 사진 추가
    @Modifying
    @Query (value = "UPDATE photo p SET p.complete_photo = :completePhoto WHERE p.room_id = :roomId", nativeQuery = true)
    void updateCompletePhoto(@Param("completePhoto") String completePhoto, @Param("roomId") Long roomId);

    // 방 번호 확인후 완성된 사진이 없으면 photo optional 객체 반환
    Optional<Photo> findByRoomIdAndCompletePhotoNull(Long roomId);

    // 방번호에 맞는 이미지 url 반환
    @Query(value = "SELECT p.complete_photo FROM photo p WHERE p.room_id = :roomId", nativeQuery = true)
    String createQrPhotoUrl(@Param("roomId") Long roomId);

    // QR코드 생성
    @Modifying
    @Query(value = "UPDATE photo p SET p.qr_code = :qrCode where p.room_id = :roomId", nativeQuery = true)
    void saveQrCode(@Param("qrCode") String qrCode, @Param("roomId") Long roomId);

    // QR코드 반환
    @Query(value = "SELECT p.qr_code FROM photo p WHERE p.room_id = :roomId", nativeQuery = true)
    String findByRoomIdAndQrCode(@Param("roomId") Long roomId);

    boolean existsByRoomIdAndCompletePhotoIsNull(Long roomId);

    @Query(value = "SELECT p.complete_photo FROM photo p WHERE p.room_id = :roomId", nativeQuery = true)
    String findByRoomIdAndCompletePhoto(@Param("roomId") Long roomId);

    // 프레임 인기 top5
    @Query(value = "SELECT row_number() over (ORDER BY count(frame) DESC) AS 'ranking',frame AS 'frameNum', frame_url AS 'frameUrl' " +
                                                                                    "FROM room WHERE frame != 0 GROUP BY frame " +
                                                                                    "ORDER BY count(frame) DESC limit 5",
                                                                                    nativeQuery = true)
    List<FrameTop5Interface> getTop5();
}





