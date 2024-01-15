package my.novik.telegrambotgpt.repository;

import my.novik.telegrambotgpt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
