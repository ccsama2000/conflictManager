package com.example.filemanager.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id ;
    private String userName ;
    private String email;
    private String password;
    private String createTime;
    private String motto;
    private int del ;
    private String memo ;
    private int role;


}
