package com.mci.defecttracker.utils;




import javax.ws.rs.core.Application;

import com.mci.defecttracker.service.ProjectService;

import java.util.HashSet;
import java.util.Set;

public class AppConfig extends Application{
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

//        DefaultStorageProvider.setInstance(new MemoryStorageProvider());
        classes.add(ProjectService.class);
        return classes;
    }
}
