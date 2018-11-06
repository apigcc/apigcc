package com.github.apiggs.example.diff;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystem {

    public static boolean open(Path path) {
        if(!Files.exists(path)){
            return false;
        }
        if (cmd(path.toString())) {
            return true;
        }
        return jdk(path.toFile());
    }

    private static boolean cmd(String file) {
        return cmd(currentOS().getCommand(), file);
    }


    private static boolean cmd(String command, String args) {
        try {
            Process p = Runtime.getRuntime().exec(new String[]{command,args});
            return p!=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean jdk(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public enum OS {
        mac("open"),
        win("explorer");

        private String command;

        OS(String command) {
            this.command = command;
        }

        public String getCommand(){
            return command;
        }
    }

    private static OS currentOS() {
        String s = System.getProperty("os.name").toLowerCase();
        if (s.contains("win")) {
            return OS.win;
        }
        return OS.mac;
    }
}

