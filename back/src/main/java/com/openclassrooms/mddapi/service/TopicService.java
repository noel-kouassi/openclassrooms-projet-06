package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.response.TopicDto;

import java.util.List;

public interface TopicService {

    List<TopicDto> getAllTopic();

    void subscribeTopic(String login, long topicId);

    void unsubscribeTopic(String login, long topicId);
}
