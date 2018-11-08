package io.crawlerbot.crawler.logger;


import org.slf4j.Logger;
import org.slf4j.Marker;

public class LoggerFactory implements Logger{
    private Logger logger;
    private String workerId;
    private String appName;

    public LoggerFactory(Class<?> clazz) {
        workerId = System.getProperty("workerId");
        appName = System.getProperty("appName");
        logger = org.slf4j.LoggerFactory.getLogger(clazz);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public void trace(String s) {
        logger.trace("workerId: {},appName: {}, {}",workerId, appName, s);
    }

    @Override
    public void trace(String s, Object o) {
        logger.trace("workerId: {},appName: {}, {}, {}",workerId, appName, s,o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        logger.trace("workerId: {},appName: {}, {}, {}, {}",workerId, appName, s, o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        logger.trace("workerId: {},appName: {} {}",workerId, appName, objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {

    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String s) {

    }

    @Override
    public void trace(Marker marker, String s, Object o) {

    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {

    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(String s) {
        logger.debug("workerId: {},appName: {}{}",workerId, appName, s);
    }

    @Override
    public void debug(String s, Object o) {
        logger.debug("workerId: {},appName: {},{}{}",workerId, appName, s,o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        logger.debug("workerId: {},appName: {}, {}, {}, {}",workerId, appName, s, o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        logger.debug("workerId: {},appName: {}, {}",workerId, appName, objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void debug(Marker marker, String s) {

    }

    @Override
    public void debug(Marker marker, String s, Object o) {

    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {

    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(String s) {
        logger.info("workerId: {},appName: {}, {}",workerId, appName, s);
    }

    @Override
    public void info(String s, Object o) {
        logger.info("workerId: {},appName: {}, {}, {}",workerId, appName, s,o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        logger.info("workerId: {},appName: {}, {}, {}, {}",workerId, appName, s, o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        logger.info("workerId: {},appName: {}, {}, {}",workerId, appName, objects);
    }

    @Override
    public void info(String s, Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String s) {

    }

    @Override
    public void info(Marker marker, String s, Object o) {

    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void info(Marker marker, String s, Object... objects) {

    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void warn(String s) {
        logger.warn("workerId: {},appName: {}, {}",workerId, appName, s);

    }

    @Override
    public void warn(String s, Object o) {
        logger.warn("workerId: {},appName: {},{}{}",workerId, appName, s,o);
    }

    @Override
    public void warn(String s, Object... objects) {
        logger.warn("workerId: {},appName: {}, {}, {}",workerId, appName, objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        logger.warn("workerId: {},appName: {},{},{}, {}",workerId, appName, s, o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {

    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String s) {

    }

    @Override
    public void warn(Marker marker, String s, Object o) {

    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {

    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void error(String s) {
        logger.error("workerId: {},appName: {}{}",workerId, appName, s);
    }

    @Override
    public void error(String s, Object o) {
        logger.error("workerId: {},appName: {},{}{}",workerId, appName, s,o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        logger.error("workerId: {},appName: {},{},{}{}",workerId, appName, s, o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        logger.error("workerId: {},appName: {},{}{}",workerId, appName, objects);
    }

    @Override
    public void error(String s, Throwable throwable) {

    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String s) {

    }

    @Override
    public void error(Marker marker, String s, Object o) {

    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void error(Marker marker, String s, Object... objects) {

    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {

    }

}
