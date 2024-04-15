package com.openclassrooms.mddapi.service.impl;

import com.openclassrooms.mddapi.dto.response.TopicDto;
import com.openclassrooms.mddapi.dto.response.UserDto;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.GlobalException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.TopicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<TopicDto> getAllTopic() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream().map(this::constructTopic).toList();
    }

    @Override
    public void subscribeTopic(String login, long topicId) {

        User user = userRepository.findByEmailOrName(login, login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with email or name: %s", login)));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Topic not found with id %s", topicId)));

        Set<User> currentSubscribers = topic.getUsers();
        if (!currentSubscribers.isEmpty()) {
            currentSubscribers.forEach(currentUser -> {
                if (currentUser.getName().contentEquals(login) || currentUser.getEmail().contentEquals(login)) {
                    throw new GlobalException(HttpStatus.BAD_REQUEST, String.format("User with login %s has already subscribed topic %s", login, topic.getTitle()));
                }
            });
        }
        currentSubscribers.add(user);
        topic.setUsers(currentSubscribers);
        topicRepository.save(topic);
    }

    @Override
    public void unsubscribeTopic(String login, long topicId) {

        User user = userRepository.findByEmailOrName(login, login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with email or name: %s", login)));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Topic not found with id %s", topicId)));

        Set<User> users = topic.getUsers();
        if (!users.isEmpty()) {
            users.remove(user);
        }
        topic.setUsers(users);
        topicRepository.save(topic);
    }

    private TopicDto constructTopic(Topic topic) {
        TopicDto topicDto = modelMapper.map(topic, TopicDto.class);
        Set<User> users = topic.getUsers();
        if (!users.isEmpty()) {
            Set<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(toSet());
            topicDto.setUsers(userDtos);
        }
        return topicDto;
    }
}
