package com.microservice.current.test;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.HashSet;

/**
 * Created by cmonkey on 17-6-30.
 */
public class NumberGeneratorTest {

    private String first;
    private String second;
    private NumberGenerator generator;

    @Test
    public void testNextNumber() throws InterruptedException {
        AnnotatedTestRunner runner = new AnnotatedTestRunner();
        HashSet methods = new HashSet();
        methods.add(NumberGenerator.class.getName() + ".nextNumber");
        runner.setMethodOption(MethodOption.LISTED_METHODS, methods);
        runner.setDebug(false);
        runner.runTests(this.getClass(), NumberGenerator.class);
    }

    @ThreadedBefore
    public void setup() {
        generator = new NumberGenerator();
    }

    @ThreadedMain
    public void main() {
        first = generator.nextNumber();
    }

    @ThreadedSecondary
    public void secondary() {
        second = generator.nextNumber();
    }

    @ThreadedAfter
    public void checkResults() {
        Assertions.assertThat(first).isNotEqualTo(second);
    }
}