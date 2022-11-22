package com.example.filemanager.controller;

import com.example.filemanager.dao.fileInfoMapper;
import com.example.filemanager.pojo.fileInfoWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private fileInfoMapper fileInfoMapper;

    @GetMapping(value = "/getFile")
    public fileInfoWithBLOBs getUser(){
        return fileInfoMapper.selectByPrimaryKey("file1");
    }


}
