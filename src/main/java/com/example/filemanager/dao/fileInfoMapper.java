package com.example.filemanager.dao;

import com.example.filemanager.pojo.fileInfoWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface fileInfoMapper {
    int deleteByPrimaryKey(String filename);

    int insert(fileInfoWithBLOBs record);

    int insertSelective(fileInfoWithBLOBs record);

    fileInfoWithBLOBs selectByPrimaryKey(String filename);

    int updateByPrimaryKeySelective(fileInfoWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(fileInfoWithBLOBs record);

}