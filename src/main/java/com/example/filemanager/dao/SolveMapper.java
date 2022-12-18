package com.example.filemanager.dao;

import com.example.filemanager.pojo.fileInfoWithBLOBs;
import com.example.filemanager.pojo.solved;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SolveMapper {

    List<solved> searchResolved(String tuple);
    int insert(solved solve);
}
