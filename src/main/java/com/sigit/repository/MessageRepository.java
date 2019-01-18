package com.sigit.repository;

import com.sigit.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Sigit Kurniawan on 1/18/2019.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByReceiver(String receiver);
    List<Message> findAllBySenderAndReceiver(String sender, String receiver);
}
