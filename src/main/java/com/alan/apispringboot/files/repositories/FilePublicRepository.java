package com.alan.apispringboot.files.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alan.apispringboot.files.entities.FilePublic;
import com.alan.apispringboot.users.entities.User;

@Repository
public interface FilePublicRepository extends JpaRepository<FilePublic, Long> {

  List<FilePublic> findAllByOwner(User owner);

}
