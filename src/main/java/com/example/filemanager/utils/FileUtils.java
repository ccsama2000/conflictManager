package com.example.filemanager.utils;

import java.io.*;
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

    public void write(String path, byte[] bytes) throws Exception {

        File file = new File(path);
        if (file.exists()) {
            if (!file.delete()) {
                throw new Exception("file failed to be deleted");
            }
        }

        FileOutputStream fos = new FileOutputStream(file);
        if (bytes != null) {
            fos.write(bytes, 0, bytes.length);
        }
        fos.flush();
        fos.close();
    }
}
