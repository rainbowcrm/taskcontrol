package com.primus.generic;


import com.primus.common.context.IRadsContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class BusinessContext implements IRadsContext, Serializable {

    String user;
    Locale locale = Locale.US;
    String currentEntity ;

    public String getCurrentEntity() {
        return currentEntity;
    }

    public void setCurrentEntity(String currentEntity) {
        this.currentEntity = currentEntity;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String s) {
        this.user =s;

    }

    @Override
    public Map getProperties() {
        return null;
    }

    @Override
    public void setProperties(Map map) {

    }

    @Override
    public void addProperty(String s, String s1) {

    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public boolean isAuthorized() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean b) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public void setLocale(Locale locale) {

    }

    public static BusinessContext createContext(SecurityContext securityContext)
    {
        BusinessContext context = new BusinessContext();
        context.setUser((String)securityContext.getAuthentication().getPrincipal());
        return context;

    }
    public static BusinessContext createSchedulerContext() {
        BusinessContext context = new BusinessContext();
        context.setUser("scheduler");
        return context;

    }

    public static BusinessContext createContext()
    {
        BusinessContext context = new BusinessContext();
        context.setUser((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return context;

    }




    @Override
    public void setDateFormat(String s) {

    }

    @Override
    public String getDateFormat() {
        return "yyyy-MM-dd";
    }

    @Override
    public void setDateTimeFormat(String s) {

    }

    @Override
    public String getDateTimeFormat() {
        return "yyyy-MM-dd HH:mm";
    }
}
