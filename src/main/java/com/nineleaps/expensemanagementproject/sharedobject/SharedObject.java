package com.nineleaps.expensemanagementproject.sharedobject;

public class SharedObject {
    private static SharedObject instance;
    private String fileName1;

    private SharedObject() {
        // Private constructor to prevent instantiation
    }

    public static SharedObject getInstance() {
        if (instance == null) {
            instance = new SharedObject();
        }
        return instance;
    }

    public String getFileName1() {
        return fileName1;
    }

    public void setFileName1(String fileName1) {
        this.fileName1 = fileName1;
    }
}