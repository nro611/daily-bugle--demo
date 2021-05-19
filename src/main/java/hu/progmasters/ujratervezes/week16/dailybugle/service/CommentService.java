package hu.progmasters.ujratervezes.week16.dailybugle.service;

import hu.progmasters.ujratervezes.week16.dailybugle.dto.CommentCreateUpdateData;
import hu.progmasters.ujratervezes.week16.dailybugle.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@Transactional
public class CommentService {

    private CommentRepository commentRepository;
    private Clock clock;

    @Autowired
    public CommentService(CommentRepository commentRepository, Clock clock) {
        this.commentRepository = commentRepository;
        this.clock = clock;
    }


    public boolean saveComment(CommentCreateUpdateData data, int articleId) {
        return commentRepository.saveComment(data, articleId, LocalDateTime.now(clock));
    }
}
