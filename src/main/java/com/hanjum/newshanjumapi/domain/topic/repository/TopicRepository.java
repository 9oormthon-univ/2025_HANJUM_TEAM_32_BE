package com.hanjum.newshanjumapi.domain.topic.repository;

import com.hanjum.newshanjumapi.domain.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TopicRepository extends JpaRepository<Topic, Long> {

}
