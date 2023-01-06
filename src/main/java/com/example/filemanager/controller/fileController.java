package com.example.filemanager.controller;

import com.example.filemanager.services.fileServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class fileController {
    @Autowired
    fileServices fileService;
    /**
     * @Param path:指定目录
     * @Return data:目录下所有文件
     * dirList：所有文件名以及绝对路径
     */
    @GetMapping(value ="/files")
    public Object getFiles(@RequestParam("path") String path){
        Map<String, Object> dataMap = new HashMap<>();
        try {
            List<Object> dirList = fileService.getAllFiles(path);
            dataMap.put("data", dirList);
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
     * @Param path:指定代码仓库目录
     * @Return data:代码仓库目录下所有文件
     * dirList：所有文件名以及绝对路径
     */
    @GetMapping(value = "/allFiles")
    public Object getAllFiles(@RequestParam("path") String path){
        Map<String,Object> dataMap=new HashMap<>();
        try{
            List<Object> dirList=fileService.getAll(path);
            dataMap.put("data",dirList);
            dataMap.put("code",200);

        }catch (Exception e){
            e.printStackTrace();
            dataMap.put("data","");
            dataMap.put("code",500);
        }
        return dataMap;
    }
/**
 * @Param path:代码仓库根目录
 * @Return data:根目录下所有代码仓库目录
 * dirList：所有仓库路径
 */
    @GetMapping(value = "/directions")
    public Object getAllDir(@RequestParam("path") String path){
        Map<String,Object> dataMap=new HashMap<>();
        try{
            List<Object> dirList=fileService.getAllDirection(path);
            dataMap.put("data",dirList);
            dataMap.put("code",200);

        }catch (Exception e){
            e.printStackTrace();
            dataMap.put("data","");
            dataMap.put("code",500);
        }
        return dataMap;
    }
/**
 *2023-1-6
 * 写入文件，同时根据冲突文件提取解决方案，存入数据库中待将来解决新冲突时查询
 */
    @PutMapping(value="/write2File")
    public Object updateUser(@RequestParam("path") String path,
                             @RequestParam("content") String content,
                             @RequestParam("fileName") String fileName){
        Map<String, Object> dataMap = new HashMap<>();
        try {
            fileService.write2ConflictFile(content,path,fileName);
            dataMap.put("code", 200);
            dataMap.put("msg", "成功写入冲突文件");
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("data", "");
            dataMap.put("code", 500);
            dataMap.put("root", "");
            dataMap.put("msg", "写入失败");
        }
        return dataMap;
    }


}
