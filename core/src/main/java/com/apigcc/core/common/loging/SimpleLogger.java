package com.apigcc.core.common.loging;

import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleLogger implements Logger {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    PrintStream out = System.out;
    protected String name;
    private Level level = Level.INFO;


    public void setOut(PrintStream out) {
        this.out = out;
    }

    @Override
    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isDebug() {
        return Level.COMPARATOR.compare(Level.DEBUG,this.level) >= 0;
    }

    @Override
    public void out(Level level, String message, Object... args) {
        if(Level.COMPARATOR.compare(level,this.level) < 0){
            return;
        }
        out.println(format(message,args));
    }

    private String format(String template, Object ... args){
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        int cursor = 0;
        Matcher m = Pattern.compile("\\{}").matcher(template);
        while(m.find()){
            stringBuilder.append(template, cursor, m.start());
            stringBuilder.append(args[index]);
            cursor = m.end();
            index++;
        }
        stringBuilder.append(template, cursor, template.length());
        return stringBuilder.toString();
    }

}
