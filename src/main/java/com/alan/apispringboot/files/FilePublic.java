package com.alan.apispringboot.files;

import lombok.Data;

import javax.persistence.*;

@Data

@Entity
@Table(name = "files_public")
public class FilePublic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "key")
    private String key;

}
