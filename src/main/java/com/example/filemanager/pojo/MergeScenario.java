package com.example.filemanager.pojo;
import com.example.filemanager.utils.PathUtils;

import java.util.List;

public class MergeScenario {
    public String base;
    public String ours;
    public String theirs;
    public List<String> conflict;
    public String fileName;
    public String absPath;
    public MergeScenario(String fileName){
        this.fileName=fileName;
    }

}
