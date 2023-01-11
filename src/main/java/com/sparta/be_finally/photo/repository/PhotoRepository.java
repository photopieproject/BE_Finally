package com.sparta.be_finally.photo.repository;

import com.sparta.be_finally.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
