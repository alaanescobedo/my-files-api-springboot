package com.alan.apispringboot.files;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alan.apispringboot.users.entities.User;

@Repository
public interface FilePublicRepository extends JpaRepository<FilePublic, Long> {

  Set<FilePublic> findAllByOwner(User owner);

}
