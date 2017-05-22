package com.flash.dir_size;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by zhangj52 on 5/20/2017.
 */
public class DirSize {

    public static long directorySize_v1(final String dirPath) {
        File dir = new File(dirPath);
        File[] childFiles = dir.listFiles();
        long size = 0;
        for (File f : childFiles) {
            if (f.isDirectory()) {
                size += directorySize_v1(f.getAbsolutePath());
            } else {
                size += f.length();
            }
        }
        return size;
    }

    public static void timimg(Function<String, Long> function, String path){
        long start = System.currentTimeMillis();
        System.out.println("Function Result: " + function.apply(path));
        long duration = System.currentTimeMillis() - start;
        System.out.println(duration/1000+ " secs .");
    }

    public static void main(String[] args) throws IOException {
        String dirPath = "C:\\Users\\zhangj52";
        dirPath = "C:\\Users\\zhangj52\\Documents\\GitHub\\akka\\akka";
        long MB = 1024 * 1024;
//        System.out.println(directorySize_v1(dirPath)/MB);
        timimg(DirSize::directorySize_v1, dirPath);
    }
}
