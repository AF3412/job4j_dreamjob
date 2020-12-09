package ru.af3412.dream.store;

import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.model.City;
import ru.af3412.dream.model.Post;
import ru.af3412.dream.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {

    private static final MemStore INST = new MemStore();
    private static final AtomicInteger POST_ID = new AtomicInteger(4);
    private static final AtomicInteger CANDIDATE_ID = new AtomicInteger(4);
    private static final AtomicInteger USER_ID = new AtomicInteger(4);

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "desc 1", "2020 11 15"));
        posts.put(2, new Post(2, "Middle Java Job", "desc 2", "2020 11 15"));
        posts.put(3, new Post(3, "Senior Java Job", "desc 3", "2020 11 15"));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static MemStore instOf() {
        return INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public Post findPostById(int id) {
        return posts.get(id);
    }

    @Override
    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    @Override
    public User saveUser(User user) {
        if (user.getId() == 0) {
            user.setId(USER_ID.incrementAndGet());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(int id) {
        return users.get(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<City> findAllCities() {
        return List.of(new City(1, "Москва"), new City(2, "New York"));
    }

    @Override
    public City addCity(City newCity) {
        return newCity;
    }
}
