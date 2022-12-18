package com.example.filemanager.utils;

import difflib.DiffUtils;
import difflib.Patch;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    public List<String> readFile(File file) throws Exception {
        List<String> codes = new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(isr);

        String line;
        while ((line = reader.readLine()) != null) {
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

    public int[] alignLines(List<String> conflict, List<String> resolve) {
        int n = conflict.size();
        int m = resolve.size();
        int[] rec = new int[n];
        Arrays.fill(rec, -1);

        Patch<String> patch = DiffUtils.diff(conflict, resolve);
        List<String> diff = DiffUtils.generateUnifiedDiff("", "", conflict, patch, Math.max(n, m));

        for (int i = 0, j = 0, k = 3; k < diff.size(); ++k) {
            char c = diff.get(k).charAt(0);
            if (c == '-')
                i++;
            else if (c == '+')
                j++;
            else {
                rec[i] = j;
                i++;
                j++;
            }
        }
        return rec;
    }

    public List<String> getCodeSnippets(List<String> code, int start, int end){
        if(start >= end)
            return new ArrayList<>();
        return code.subList(start + 1, end);
    }
}

