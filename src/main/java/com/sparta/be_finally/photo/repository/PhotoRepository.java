package com.sparta.be_finally.photo.repository;

import com.sparta.be_finally.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByRoomId(Long roomId);

    boolean existsByRoomId(Long roomId);
}





