package com.alan.apispringboot.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alan.apispringboot.users.entities.UserAvatar;

public interface UserAvatarRepository extends JpaRepository<UserAvatar, Long> {
  
}
