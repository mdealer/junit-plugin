package io.jenkins.plugins.analysis.junit;

import java.util.Arrays;

import org.junit.Test;

import org.jenkinsci.test.acceptance.junit.AbstractJUnitTest;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.FreeStyleJob;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class PublisherTest extends AbstractJUnitTest {

    @Test
    public void successfulBuildWhenSkipMarkingBuildAsUnstableOnTestFailureChecked() {
        FreeStyleJob j = jenkins.jobs.create();
        j.configure();
        j.copyResource(resource("/failure/com.simple.project.AppTest.txt"));
        j.copyResource(resource("/failure/TEST-com.simple.project.AppTest.xml"));
        JUnitPublisher publisher = j.addPublisher(JUnitPublisher.class);
        publisher.testResults.set("*.xml");
        publisher.setSkipMarkingBuildAsUnstableOnTestFailure(true);
        j.save();

        Build build = j.startBuild();
        assertThat(build.getResult(), is("SUCCESS"));
    }

    @Test
    public void successfulBuildWhenEmptyTestResultsChecked() {
        FreeStyleJob j = jenkins.jobs.create();
        j.configure();
        JUnitPublisher publisher = j.addPublisher(JUnitPublisher.class);
        publisher.setAllowEmptyResults(true);
        j.save();

        j.startBuild().shouldSucceed();
    }

    @Test
    public void retainLongStandardOutputError() {
        FreeStyleJob j = jenkins.jobs.create();
        j.configure();
        j.copyResource(resource("/success/junit-with-long-output.xml"));
        JUnitPublisher publisher = j.addPublisher(JUnitPublisher.class);
        publisher.testResults.set("*.xml");
        publisher.setRetainLogStandardOutputError(true);

        j.save();
        Build build = j.startBuild().shouldSucceed();
        j.visit("/job/" + j.name + "/1/testReport/(root)/JUnit/testScore_0_/");

        JUnitTestDetail testDetail = new JUnitTestDetail(build);

        assertThat(testDetail.getStandardOutput(), not(containsString("truncated")));
    }

    // TODO: Check how to make it work
    @Test
    public void healthReportAmplificationFactor() {
//        FreeStyleJob j = jenkins.jobs.create();
//        j.configure();
//        j.copyResource(resource("/parameterized/testng.xml"));
//        JUnitPublisher publisher = j.addPublisher(JUnitPublisher.class);
//        publisher.testResults.set("*.xml");
//        publisher.setHealthScaleFactor("10.0");
//
//        j.save();
//        j.startBuild();
//        j.configure();
//        j.copyResource(resource("/parameterized/junit.xml"));
//        j.copyResource(resource("/parameterized/testng.xml"));
//        publisher.testResults.set("*.xml");
//        publisher.setHealthScaleFactor("10.0");
//
//        j.save();
//        j.startBuild();
    }


}