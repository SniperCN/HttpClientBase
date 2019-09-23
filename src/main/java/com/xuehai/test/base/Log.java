package com.xuehai.test.base;

import org.slf4j.LoggerFactory;

/**
 * @ClassName Log
 * @Description: slf4j结合log4j2
 * @Author Sniper
 * @Date 2019/3/13 10:53
 */
public class Log {

    public static void info(String className,String message) {
        LoggerFactory.getLogger(className).info(message);
    }

    public static void info(String className, String message, Object ... objects) {
        LoggerFactory.getLogger(className).info(message, objects);
    }

    public static void debug(String className, String message) {
        LoggerFactory.getLogger(className).debug(message);
    }

    public static void debug(String className, String message, Object ... objects) {
        LoggerFactory.getLogger(className).debug(message, objects);
    }

    public static void warn(String className, String message) {
        LoggerFactory.getLogger(className).warn(message);
    }

    public static void warn(String className, String message, Object ... objects) {
        LoggerFactory.getLogger(className).warn(message, objects);
    }

    public static void error(String className, String message) {
        LoggerFactory.getLogger(className).error(message);
    }

    public static void error(String className, String message, Object ... objects) {
        LoggerFactory.getLogger(className).error(message, objects);
    }

    public static void error(String className, String message, Throwable t) {
        LoggerFactory.getLogger(className).error(message, t);
    }

}
