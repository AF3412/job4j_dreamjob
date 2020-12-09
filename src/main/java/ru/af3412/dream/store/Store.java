package ru.af3412.dream.store;

import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.model.City;
import ru.af3412.dream.model.Post;
import ru.af3412.dream.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Store {

    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void savePost(Post post);

    Candidate saveCandidate(Candidate candidate);

    Post findPostById(int id);

    Candidate findCandidateById(int id);

    User saveUser(User user);

    User findById(int id);

    Optional<User> findByEmail(String email);

    List<City> findAllCities();

    City addCity(City newCity);
}
