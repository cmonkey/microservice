package com.microservice.dao;

import com.microservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by cmonkey on 12/6/16.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
