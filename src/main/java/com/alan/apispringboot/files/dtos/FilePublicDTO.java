package com.alan.apispringboot.files.dtos;

import com.alan.apispringboot.users.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class FilePublicDTO {

  private Long id;
  private String url;
  @JsonIgnore
  private String key;
  private String publicName;

  @JsonIgnore
  private User owner;

}
