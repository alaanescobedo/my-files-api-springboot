package com.alan.apispringboot.files.entities;

import com.alan.apispringboot.users.entities.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String key;

    @Column(name = "public_name")
    private String publicName;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
}
