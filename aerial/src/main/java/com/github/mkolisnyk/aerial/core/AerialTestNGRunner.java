package com.github.mkolisnyk.aerial.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.testng.IHookCallBack;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import com.github.mkolisnyk.aerial.annotations.AerialAfterSuite;
import com.github.mkolisnyk.aerial.annotations.AerialBeforeSuite;

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
        String[] args = AerialMain.toArgs(clazz);
        AerialMain.main(args);
        try {
            runPredefinedMethods(AerialBeforeSuite.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new TestNGCucumberRunner(clazz).runCukes();
        try {
            runPredefinedMethods(AerialAfterSuite.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Test(enabled = false)
    public void run_cukes() {
    }
}
