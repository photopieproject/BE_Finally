package com.sparta.be_finally.room.repository;

import com.sparta.be_finally.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomCode(String roomCode);

    boolean existsByIdAndUserId(Long id, Long userId);

    boolean existsByRoomCode(String roomCode);

    boolean existsBySessionId(String sessionId);

    List<Room> deleteAllByExpireDateAndId(LocalDateTime time, Long roomId);


    @Query
            (nativeQuery = true,
                    value = "select *from room b "
            )
    List<Room> findRooms();

}



