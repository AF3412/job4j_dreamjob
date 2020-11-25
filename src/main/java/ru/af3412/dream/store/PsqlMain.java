package ru.af3412.dream.store;

import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.savePost(new Post(0, "Java Job"));
        for (var val : store.findAllPosts()) {
            System.out.println(val.getId() + " " + val.getName());
        }
        store.saveCandidate(new Candidate(0, "Candidate"));
        for (var val : store.findAllCandidates()) {
            System.out.println(val.getId() + " " + val.getName());
        }
        System.out.println(store.findPostById(1));
        System.out.println(store.findCandidateById(1));
    }
}
