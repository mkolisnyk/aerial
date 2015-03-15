package com.github.mkolisnyk.aerial.core;

import org.gradle.api.Project;
import org.gradle.api.Plugin;

public class AerialGradlePlugin  implements Plugin<Project> {

    public void apply(Project target) {
        target.getExtensions().create("aerial", AerialGradlePluginExtension.class);
        target.getTasks().create("generate", AerialGradlePluginGenerateTask.class);
    }
}
