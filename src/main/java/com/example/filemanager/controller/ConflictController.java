package com.example.filemanager.controller;

import com.example.filemanager.pojo.MergeScenario;
import com.example.filemanager.services.ConflictServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ConflictController {
    @Autowired
    ConflictServices conflictServices;
    /**
     * 2022-11-12
     不使用，仅示范用
     * @Param path:本地仓库路径
     * @Return data:
     * mergeScenarios base,ours,theirs以string方式存储的文件内容，
     * conflict 以list<String>方式按行存储的冲突文件内容，
     * fileName 冲突文件名 ，
     * absPath 绝对路径，本地访问路径
     *
     */
    @GetMapping(value="/conflicts")
    public Object getConflicts(@RequestParam("path") String path){
        Map<String, Object> dataMap = new HashMap<>();
        try {
            List<MergeScenario> mergeScenarios=conflictServices.getAllConflictInfo(path);
            dataMap.put("data", mergeScenarios);
            dataMap.put("code", 200);
            dataMap.put("msg", "恭喜，查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("data", "");
            dataMap.put("code", 500);
            dataMap.put("root", "");
            dataMap.put("msg", "抱歉，查询失败");
        }
        return dataMap;
    }

    /**
     * 2022-11-19
     * @Param path:本地仓库路径
     * @Param branch1:主分支名
     * @Param branch2:需要合并的分支名
     * @Return data:指定分支合并后得到的冲突文件内容
     * mergeScenarios base,ours,theirs:以string方式存储的文件内容，
     * conflict:以list<String>方式按行存储的冲突文件内容，
     * fileName:冲突文件名 ，
     * absPath:绝对路径，本地访问路径
     *
     */
    @GetMapping(value="/specifiedConflicts")
    public Object getSpecifiedConflicts(@RequestParam("path") String path,
                                        @RequestParam("branch1") String branch1,
                                        @RequestParam("branch2") String branch2){
        Map<String, Object> dataMap = new HashMap<>();
        try {
            List<MergeScenario> mergeScenarios=conflictServices.getSpecifiedConflictInfo(path,branch1,branch2);
            dataMap.put("data", mergeScenarios);
            dataMap.put("code", 200);
            dataMap.put("msg", "成功查询指定分支的冲突");
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("data", "");
            dataMap.put("code", 500);
            dataMap.put("root", "");
            dataMap.put("msg", "查询失败");
        }
        return dataMap;
    }
}
