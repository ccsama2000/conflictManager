package com.example.filemanager.services;

import java.util.List;

public interface fileServices {
    public List<Object> getAllDirection(String filePath);


    public  List<Object> getAll(String directoryPath);


    public  List<Object> getAllFiles(String directoryPath);

    public void write2ConflictFile(String content,String path,String fileName);
}
