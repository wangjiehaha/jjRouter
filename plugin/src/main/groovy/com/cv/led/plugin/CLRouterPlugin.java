package com.cv.led.plugin;

import com.android.build.gradle.BaseExtension;
import com.cv.led.annotation.Const;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class CLRouterPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        CLRouterExtension extension = project.getExtensions()
                .create(Const.NAME, CLRouterExtension.class);
        project.getExtensions().findByType(BaseExtension.class)
                .registerTransform(new CLRouterTransform());
    }
}
