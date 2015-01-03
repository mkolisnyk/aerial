package com.github.mkolisnyk.aerial.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import com.github.mkolisnyk.aerial.annotations.Aerial;
import com.github.mkolisnyk.aerial.annotations.AerialAfterSuite;
import com.github.mkolisnyk.aerial.annotations.AerialBeforeSuite;
import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

import cucumber.api.junit.Cucumber;

public class AerialRunner extends Runner {

    private Class<?> clazz;
    private Cucumber cucumber;

    public AerialRunner(Class<?> clazzValue) throws Exception {
        clazz = clazzValue;
        Aerial annotation = clazz.getAnnotation(Aerial.class);
        String[] args = (String[]) ArrayUtils.addAll(new String[] {
                AerialParamKeys.INPUT_TYPE.toString(),
                annotation.inputType().toString(),
                AerialParamKeys.SOURCE.toString(),
                annotation.source(),
                AerialParamKeys.OUTPUT_TYPE.toString(),
                AerialSourceType.FILE.toString(),
                AerialParamKeys.DESTINATION.toString(),
                annotation.destination()}, annotation.additionalParams());
        AerialMain.main(args);
        cucumber = new Cucumber(clazz);
    }

    @Override
    public Description getDescription() {
        return cucumber.getDescription();
    }

    private void runPredefinedMethods(Class<?> annotation) throws Exception {
        if (!annotation.isAnnotation()) {
            return;
        }
        Method[] methodList = this.clazz.getMethods();
        for (Method method : methodList) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation item : annotations) {
                if (item.annotationType().equals(annotation)) {
                    method.invoke(null);
                    break;
                }
            }
        }
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            runPredefinedMethods(AerialBeforeSuite.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cucumber.run(notifier);
        try {
            runPredefinedMethods(AerialAfterSuite.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
