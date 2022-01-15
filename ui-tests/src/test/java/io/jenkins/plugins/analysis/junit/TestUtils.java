package io.jenkins.plugins.analysis.junit;

import java.util.Arrays;
import java.util.List;

import org.checkerframework.checker.units.qual.C;
import org.junit.Test;

import org.jenkinsci.test.acceptance.junit.AbstractJUnitTest;
import org.jenkinsci.test.acceptance.junit.WithPlugins;
import org.jenkinsci.test.acceptance.po.Build;
import org.jenkinsci.test.acceptance.po.FreeStyleJob;
import org.jenkinsci.test.acceptance.po.JUnitPublisher;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class TestUtils {

    public static Build createFreeStyleJobWithResources(AbstractJUnitTest abstractJUnitTestBaseClass, List<String> resourcePaths, String expectedBuildResult) {
        FreeStyleJob j = abstractJUnitTestBaseClass.jenkins.jobs.create();
        FixedCopyJobDecorator fixedCopyJob = new FixedCopyJobDecorator(j);
        fixedCopyJob.getJob().configure();
        for (String resourcePath : resourcePaths) {
            fixedCopyJob.copyResource(abstractJUnitTestBaseClass.resource(resourcePath));
        }
        fixedCopyJob.getJob().addPublisher(JUnitPublisher.class).testResults.set("*.xml");
        fixedCopyJob.getJob().save();

        Build build = fixedCopyJob.getJob().startBuild();
        assertThat(build.getResult(), is(expectedBuildResult));
        build.open();
        return build;
    }
}