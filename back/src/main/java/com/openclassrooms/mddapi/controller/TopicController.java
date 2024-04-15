package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.response.TopicDto;
import com.openclassrooms.mddapi.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topic")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    /**
     * @return List of topicDto as response
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        List<TopicDto> topicDtos = topicService.getAllTopic();
        return new ResponseEntity<>(topicDtos, HttpStatus.OK);
    }

    /**
     * @param topicId the id of the topic to be followed
     * @param login   the email or name of the subscriber
     * @return String message as response
     */
    @PostMapping("/subscribe/{login}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> subscribeTopic(@RequestParam(value = "topicId") long topicId, @PathVariable(value = "login") String login) {
        topicService.subscribeTopic(login, topicId);
        return new ResponseEntity<>("Subscription done successfully !", HttpStatus.OK);
    }

    /**
     * @param topicId the id of the topic to be unfollowed
     * @param login   the email or name of the subscriber
     * @return String message as response
     */
    @PostMapping("/subscribe/{login}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> unsubscribeTopic(@RequestParam(value = "topicId") long topicId, @PathVariable(value = "login") String login) {
        topicService.unsubscribeTopic(login, topicId);
        return new ResponseEntity<>("Unsubscription done successfully !", HttpStatus.OK);
    }
}
