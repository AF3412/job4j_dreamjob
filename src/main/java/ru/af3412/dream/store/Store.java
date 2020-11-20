package ru.af3412.dream.store;

import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.model.Post;

import java.util.Collection;

public interface Store {

    Collection<Post> findAllPosts();
    Collection<Candidate> findAllCandidates();
    void save(Post post);
    void save(Candidate candidate);
    Post findPostById(int id);
    Candidate findCandidateById(int id);

}
