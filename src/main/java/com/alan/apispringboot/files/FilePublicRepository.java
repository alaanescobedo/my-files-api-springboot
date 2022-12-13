package com.alan.apispringboot.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilePublicRepository extends JpaRepository<FilePublic, Long> {

}
