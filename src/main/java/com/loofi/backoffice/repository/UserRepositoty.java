package com.loofi.backoffice.repository;

import com.loofi.backoffice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepositoty extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE username=:username")
	User getRequestedUser(@Param("username") String username);

	Boolean existsByGmail(String gmail);

	Boolean existsByUsername(String gmail);

	Boolean existsById(int id);

	User findByGmail(String gmail);

	User findByUsername(String username);
}
