package com.example.withaop.repository;

import com.example.withaop.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long sequence = 0L;

    public UserRepository() {
        // 초기 데이터
        save(new User(null, "John Doe", "john@example.com"));
        save(new User(null, "Jane Smith", "jane@example.com"));
    }

    public User findById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + id);
        }
        return user;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++sequence);
        }
        users.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        if (!users.containsKey(id)) {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + id);
        }
        users.remove(id);
    }
}
