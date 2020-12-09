package ru.af3412.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.af3412.dream.model.Candidate;
import ru.af3412.dream.model.City;
import ru.af3412.dream.model.Post;
import ru.af3412.dream.model.User;

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
             PreparedStatement ps = cn.prepareStatement("""
                     SELECT c.*, COALESCE(p.id, 0), COALESCE(ct.id, 0) photo_id FROM candidate c 
                     LEFT JOIN photo p ON p.id = c.photo_id
                     LEFT JOIN city ct ON ct.id = c.city_id"""
             )) {
            try (ResultSet resultSet = ps.executeQuery()) {
                List<Candidate> candidates = new ArrayList<>();
                while (resultSet.next()) {
                    candidates.add(
                            new Candidate(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name"),
                                    resultSet.getInt("photo_id"),
                                    resultSet.getInt("city_id")
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
    public void savePost(Post post) {
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
    public Candidate saveCandidate(Candidate candidate) {
        try {
            if (candidate.getId() == 0) {
                createCandidate(candidate);
            } else {
                updateCandidate(candidate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidate;
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
             PreparedStatement ps = cn.prepareStatement("""
                     SELECT c.*, COALESCE(p.id, 0) photo_id FROM candidate c 
                     LEFT JOIN photo p ON p.id = c.photo_id AND c.id = ?
                     LEFT JOIN city ct ON ct.id = c.city_id"""
             )) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return new Candidate(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("photo_id"),
                            resultSet.getInt("city_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User saveUser(User user) {
        try {
            if (user.getId() == 0) {
                createUser(user);
            } else {
                updateUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT u.* FROM users u WHERE u.id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT u.* FROM users u WHERE u.email = ?")
        ) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<City> findAllCities() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM city ORDER BY id"
             )) {
            try (ResultSet resultSet = ps.executeQuery()) {
                List<City> cities = new ArrayList<>();
                while (resultSet.next()) {
                    cities.add(
                            new City(
                                    resultSet.getInt("id"),
                                    resultSet.getString("name")
                            )
                    );
                }
                return cities;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public City addCity(City newCity) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO city (name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, newCity.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    return new City(id.getInt(1), newCity.getName());
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return newCity;
    }

    private User createUser(User user) throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("INSERT INTO users(name, email, password) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        }
        return user;
    }

    private User updateUser(User user) throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE users SET name = (?), email = (?), password = (?) WHERE id = ?")
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.execute();
        }
        return user;
    }

    private Candidate createCandidate(Candidate candidate) throws SQLException {
        candidate.setPhotoId(createPhotoId());
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("INSERT INTO candidate(name, photo_id, city_id) VALUES (?, ?, ?)",
                             PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhotoId());
            ps.setInt(3, candidate.getCityId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        }
        return candidate;
    }

    private Candidate updateCandidate(Candidate candidate) throws SQLException {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET name = (?), city_id = (?) WHERE id = ?")
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCityId());
            ps.setInt(3, candidate.getId());
            ps.execute();
        }
        return candidate;
    }

    private int createPhotoId() {
        int result = 0;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("INSERT INTO photo(id) VALUES (DEFAULT)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    result = id.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
