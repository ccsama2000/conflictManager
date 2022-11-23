package com.example.filemanager.pojo;

public class fileInfoWithBLOBs {
    private String filename;

    private String path;

    private Integer issolve;

    private byte[] ours;

    private byte[] theirs;

    private byte[] base;

    public fileInfoWithBLOBs(String file,int issolve) {
        this.filename=file;
        this.issolve=issolve;
    }
    public fileInfoWithBLOBs() {}

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public Integer getIssolve() {
        return issolve;
    }

    public void setIssolve(Integer issolve) {
        this.issolve = issolve;
    }

    public byte[] getOurs() {
        return ours;
    }

    public void setOurs(byte[] ours) {
        this.ours = ours;
    }

    public byte[] getTheirs() {
        return theirs;
    }

    public void setTheirs(byte[] theirs) {
        this.theirs = theirs;
    }

    public byte[] getBase() {
        return base;
    }

    public void setBase(byte[] base) {
        this.base = base;
    }
    public String getFilename() {
        return filename;
    }
}