package com.example.filemanager.utils;

import java.io.File;

public class PathUtils {
    public String getFileWithPathSegment(String... segments){
        return getSystemCompatiblePath(String.join(File.separator,segments));
    }
    public String getSystemCompatiblePath(String path) {
        String compatiblePath;
        if (File.separator.equals("/")) {
            compatiblePath = path.replace('\\', '/');
        } else {
            compatiblePath = path.replace('/', '\\');
        }
        return compatiblePath;
    }
}
