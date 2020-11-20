package ru.af3412.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                List<Post> posts = new ArrayList<>();
                while (resultSet.next()) {
                    posts.add(
                            new Post(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name")
                            )
                    );
                }
                return posts;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet resultSet = ps.executeQuery()) {
                List<Candidate> candidates = new ArrayList<>();
                while (resultSet.next()) {
                    candidates.add(
                            new Candidate(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name")
                            )
                    );
                }
                return candidates;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void save(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("INSERT INTO post(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("INSERT INTO candidate(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Post findPostById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("created")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Candidate findCandidateById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return new Candidate(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
