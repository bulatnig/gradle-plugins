package org.bulatnig.gradle.jacoco

import org.gradle.testkit.runner.TaskOutcome

class MyPluginTest extends PluginTest {

    def setup() {
        buildFile << """
            plugins {
              id 'java-library'
              id 'org.bulatnig.gradle.jacoco'
            }
            repositories {
              mavenCentral()
            }
            dependencies {
              testImplementation 'junit:junit:4.13.2'
            }
        """
        new File(testProjectDir, 'src/main/java/org/bulatnig').mkdirs()
        new File(testProjectDir, 'src/main/java/org/bulatnig/Foo.java') << """
            package org.bulatnig;
            
            class Foo {
              final static public String FOO = "BAR";
              
              void bar() {
              }
            }
        """
        new File(testProjectDir, 'src/test/java/org/bulatnig').mkdirs()
    }

    def "secceedsd on enough test coverage"() {
        given:
        new File(testProjectDir, 'src/test/java/org/bulatnig/FooTest.java') << """
            package org.bulatnig;
            
            import org.junit.Test;
            
            public class FooTest {
              @Test
              public void bar() {
                new Foo().bar();
              }
            }
        """

        when:
        def result = runTask('jacocoTestCoverageVerification')

        then:
        result.task(":jacocoTestCoverageVerification").outcome == TaskOutcome.SUCCESS
    }

    def "fails on lack of test coverage"() {
        given:
        new File(testProjectDir, 'src/test/java/org/bulatnig/FooTest.java') << """
            package org.bulatnig;
            
            import org.junit.Test;
            
            public class FooTest {
              @Test
              public void bar() {
              }
            }
        """

        when:
        def result = runTaskWithFailure('jacocoTestCoverageVerification')

        then:
        result.task(":jacocoTestCoverageVerification").outcome == TaskOutcome.FAILED
        result.output.contains("Rule violated for bundle test: instructions covered ratio is 0.0, but expected minimum is 0.8")
    }

}
