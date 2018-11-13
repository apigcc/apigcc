package com.apigcc.core.common.loging;

public class LoggerFactory {

    private static Class<? extends Logger> loggerClass = SimpleLogger.class;

    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name){
        Logger logger = newInstance();
        logger.setName(name);
        return logger;
    }

    private synchronized static Logger newInstance(){
        try {
            return loggerClass.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(),e);
        }
    }

    public static void setLoggerClass(Class<? extends Logger> loggerClass){
        LoggerFactory.loggerClass = loggerClass;
    }

}
