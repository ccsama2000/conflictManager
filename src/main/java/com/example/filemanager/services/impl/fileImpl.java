package com.example.filemanager.services.impl;

import com.example.filemanager.services.fileServices;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class fileImpl implements fileServices {

/**
 * 遍历路径下的所有文件夹
 * @Param filePath 需要遍历的文件夹路径
 * @Return
 */
    @Override
    public List<Object> getAllDirection(String dirPath) {
        List<Object> directionList = new ArrayList<>();
        File baseFile=new File(dirPath);
        if (!baseFile.isFile()) {
            baseFile.exists();
        }
        File[] files=baseFile.listFiles();
        for (File file:files) {
            if(file.isDirectory()){
                directionList.add(file.getAbsolutePath());
            }
        }
        return directionList;
    }
/**
 * 遍历该路径下的所有文件夹内的文件，包括子文件夹内的文件
 * @Param directoryPath 需要遍历的文件夹路径
 * @Return
 */
    @Override
    public List<Object> getAll(String directoryPath) {
        List<Object> list = new ArrayList<>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                list.addAll(getAll(file.getAbsolutePath()));
            }
            if(file.isFile()){
                Map<String, String> map = new HashMap<>();
                map.put("fileName", file.getName());
                map.put("filePath", file.getAbsolutePath());
                list.add(map);
            }
        }
        return list;
    }



    @Override
    public List<Object> getAllFiles(String directoryPath) {
        List<Object> list = new ArrayList<Object>();
        File baseFile = new File(directoryPath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return list;
        }
        File[] files = baseFile.listFiles();
        for (File file : files) {
            Map<String, String> map = new HashMap<>();
            if (file.isFile()) {
            map.put("fileName", file.getName());
            map.put("filePath", file.getAbsolutePath());
//            map.put("parent", file.getParent());
            list.add(map);
            }
        }
        return list;
    }

    @Override
    public void write2ConflictFile(String content, String path) {
        PrintStream stream;
        try {
            stream=new PrintStream(path);//写入的文件path
            stream.print(content);//写入的字符串
            //在这之后应将冲突块写入数据库中待查找用，待修改
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
