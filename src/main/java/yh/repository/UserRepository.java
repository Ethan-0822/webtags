package yh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yh.domain.User;

/**
 * 2  * @Description :
 * 4
 */

public interface UserRepository extends JpaRepository<User,Long> {
    User findByOpenId(String openid);
}