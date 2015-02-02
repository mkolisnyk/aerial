package com.github.mkolisnyk.aerial.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.testng.IHookCallBack;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.github.mkolisnyk.aerial.annotations.Aerial;
import com.github.mkolisnyk.aerial.annotations.AerialAfterSuite;
import com.github.mkolisnyk.aerial.annotations.AerialBeforeSuite;
import com.github.mkolisnyk.aerial.core.params.AerialParamKeys;
import com.github.mkolisnyk.aerial.core.params.AerialSourceType;

import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.testng.TestNGCucumberRunner;


public class AerialTestNGRunner extends AbstractTestNGCucumberTests {
    private Class<?> clazz;

    /* (non-Javadoc)
     * @see cucumber.api.testng.AbstractTestNGCucumberTests#run(org.testng.IHookCallBack, org.testng.ITestResult)
     */
    @Override
    public void run(IHookCallBack iHookCallBack,
            ITestResult iTestResult) {
        super.run(iHookCallBack, iTestResult);
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

    /* (non-Javadoc)
     * @see cucumber.api.testng.AbstractTestNGCucumberTests#run_cukes()
     */
    @Test(groups = "cucumber", description = "Runs Cucumber Features")
    public void runCukes() throws Exception {
        clazz = this.getClass();
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
        try {
            runPredefinedMethods(AerialBeforeSuite.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new TestNGCucumberRunner(getClass()).runCukes();
        try {
            runPredefinedMethods(AerialAfterSuite.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
