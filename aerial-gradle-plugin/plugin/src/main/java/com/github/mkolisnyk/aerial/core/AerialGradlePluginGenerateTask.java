package com.github.mkolisnyk.aerial.core;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

public class AerialGradlePluginGenerateTask  extends DefaultTask {

    @TaskAction
    public void generate() throws Exception {
        AerialGradlePluginExtension extension = getProject().getExtensions()
                .findByType(AerialGradlePluginExtension.class);
        ArrayList<String> params = new ArrayList<String>();
        params.add(AerialParamKeys.INPUT_TYPE.toString());
        params.add(AerialSourceType.fromString(extension.getInputType()).toString());
        params.add(AerialParamKeys.SOURCE.toString());
        params.add(extension.getSource());
        params.add(AerialParamKeys.OUTPUT_TYPE.toString());
        params.add(AerialSourceType.fromString(extension.getOutputType()).toString());
        params.add(AerialParamKeys.DESTINATION.toString());
        params.add(extension.getDestination());
        if (extension.getConfigurationFile() != null) {
            params.add(AerialParamKeys.CONFIGURATION.toString());
            params.add(extension.getConfigurationFile());
        }
        if (extension.getNamedParams() != null) {
            for (Entry<String, String> entry : extension.getNamedParams().entrySet()) {
                params.add(entry.getKey() + "=" + entry.getValue());
            }
        }
        if (extension.getValueParams() != null) {
            for (String param : extension.getValueParams()) {
                param = param.replaceAll("=", "\\=");
                params.add(param);
            }
        }
        String[] paramsArray = new String[params.size()];
        paramsArray = params.toArray(paramsArray);
        AerialMain.main(paramsArray);
    }
}
