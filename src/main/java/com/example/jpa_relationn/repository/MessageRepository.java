package com.example.jpa_relationn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.jpa_relationn.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

        @Query("SELECT m FROM Message m WHERE " +
                        "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
                        "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
                        "ORDER BY m.messageId ASC")
        List<Message> findMessagesBetweenUsers(@Param("userId1") Integer userId1,
                        @Param("userId2") Integer userId2);

}
