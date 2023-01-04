package com.example.filemanager.services.impl;

import com.example.filemanager.dao.SolveMapper;
import com.example.filemanager.dao.fileInfoMapper;
import com.example.filemanager.pojo.MergeScenario;
import com.example.filemanager.pojo.MergeTuple;
import com.example.filemanager.pojo.fileInfoWithBLOBs;
import com.example.filemanager.pojo.solved;
import com.example.filemanager.services.ConflictServices;
import com.example.filemanager.services.fileServices;
import com.example.filemanager.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ConflictServices conflictServices;
    @Autowired
    SolveMapper solveMapper;
    @Autowired
    fileInfoMapper infoMapper;
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
    public void write2ConflictFile(String content, String path,String fileName) {
        PrintStream stream;
        FileUtils fileUtils=new FileUtils();
        try {
            stream=new PrintStream(path);//写入的文件path
            stream.print(content);//写入的字符串
            fileInfoWithBLOBs infoWithBLOBs=infoMapper.selectByPrimaryKey(fileName);
            infoWithBLOBs.setIssolve(0);
            infoMapper.updateByPrimaryKeySelective(infoWithBLOBs);
            //在这之后应将冲突块写入数据库中待查找用，待修改
            MergeScenario mergeScenario=conflictServices.getSpecifiedFile(fileName);
            List<String> conflict=mergeScenario.conflict;
            File res=new File(path);
            List<String> resolve=fileUtils.readFile(res);
            List<MergeTuple> tuples=conflictServices.extractTuple(conflict,resolve,1);
            for (MergeTuple tuple:tuples) {
                StringBuilder r = new StringBuilder();
                StringBuilder c = new StringBuilder();
                for (String line:tuple.resolve) {
                    r.append(line).append("\n");
                }
                for (String line:tuple.ours) {
                    c.append(line).append("\n");
                }
                for (String line:tuple.base) {
                    c.append(line).append("\n");
                }
                for (String line:tuple.theirs) {
                    c.append(line).append("\n");
                }
                if(tuple.resolve.size()>0) {
                    solved solve = new solved(c.toString(), r.toString());
                    solveMapper.insert(solve);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
