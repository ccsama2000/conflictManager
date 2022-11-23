package com.example.filemanager.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public List<String> readFile(File file) throws Exception {
        List<String> codes = new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while((line = reader.readLine()) != null){
            codes.add(line);
        }
        return codes;
    }
}
