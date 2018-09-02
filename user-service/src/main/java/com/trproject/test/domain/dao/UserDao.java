package com.trproject.test.domain.dao;

import com.trproject.test.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    List<User> findByRoleId(long roleId);

    Optional<User> findByName(String name);

}
