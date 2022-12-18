package com.example.filemanager.controller;

import com.example.filemanager.dao.fileInfoMapper;
import com.example.filemanager.dao.SolveMapper;
import com.example.filemanager.pojo.fileInfoWithBLOBs;
import com.example.filemanager.pojo.solved;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    private fileInfoMapper fileInfoMapper;
    @Autowired
    private SolveMapper mapper;

    @GetMapping(value = "/getFile")
    public fileInfoWithBLOBs getUser(){
        return fileInfoMapper.selectByPrimaryKey("anotherFile.java");
    }

    @GetMapping(value = "/search")
    public List<solved> search(){
        return mapper.searchResolved("public static class RejectAllFilter extends Filter");
    }
}
