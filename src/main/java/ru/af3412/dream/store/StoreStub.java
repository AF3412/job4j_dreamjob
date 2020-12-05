package ru.af3412.dream.store;

import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.model.Post;
import ru.af3412.dream.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StoreStub implements Store {

    private final Map<Integer, Post> store = new HashMap<>();
    private int ids = 0;

    @Override
    public Collection<Post> findAllPosts() {
        return store.values();
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return null;
    }

    @Override
    public void savePost(Post post) {
        store.put(ids++, post);
    }

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        return null;
    }

    @Override
    public Post findPostById(int id) {
        return null;
    }

    @Override
    public Candidate findCandidateById(int id) {
        return null;
    }

    @Override
    public User saveUser(User user) {
        return null;
    }

    @Override
    public User findById(int id) {
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }
}
