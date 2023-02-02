package com.sparta.be_finally.photo.repository;

import com.sparta.be_finally.photo.entity.Photo;
import com.sparta.be_finally.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByRoomId(Long roomId);

    @Query(value = "SELECT p.id " +
            "FROM photo AS p " +
            "WHERE p.room_id = ?1 AND p.photo_one IS NULL OR p.photo_two IS NULL OR p.photo_three IS NULL", nativeQuery = true)
    Photo findByRoomIdAndPhoto(@Param("roomId") Long roomId);

    Photo findByRoom(Room room);

    @Modifying
    @Query (value = "UPDATE photo p SET p.complete_photo = :completePhoto WHERE p.room_id = :roomId", nativeQuery = true)
    void updateCompletePhoto(@Param("completePhoto") String completePhoto, @Param("roomId") Long roomId);


    Optional<Photo> findByRoomIdAndCompletePhotoNull(Long roomId);

}





