package com.healthycoderapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class BMICalculatorTest {

    private String environment = "dev";

    //run exactly once before all unit tests performed, eg. setup of 1 database instead of 1 DB instance each unit test
    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all unit tests");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all unit tests");
    }

    //when try to run below test, though we did not get red bar, it is also not green. Test did not pass, it was skipped
    //anything after the assumeTrue() statement will not run
    //will only run if Env is "prod"
    //assumeTrue() will never fail test, assertTrue() will
    //Assumptions only possible in JUnit5
    @Test
    @DisplayName("Sample method display name")
    //use @Disabled to skip test
    //@Disabled
    //use @Disabled to skip test from specified OS
    //@DisabledOnOs(OS.WINDOWS)
    void should_NotRunTest_When_EnvironmentNotDev() {
        assumeTrue(this.environment.equals("prod"));
        System.out.println("dev environment");
    }

    @Nested
    @DisplayName("Sample inner class display name")
    class IsDietRecommendedTests {

        //you can have @BeforeEach/@BeforeAll in Nested classes too
        @BeforeEach
        void nestedBeforeEachTest() {
            System.out.println("Print before each test in nested class");
        }

        @ParameterizedTest
        //@ValueSource easiest way to test multiple values for 1 variable in parameterized tests
        @ValueSource(doubles = {89.0, 95.0, 110.0})
        void should_ReturnTrue_When_DietRecommended(Double coderWeight) {
            //given
            assumeTrue(BMICalculatorTest.this.environment.equals("dev"));
            double weight = coderWeight;
            double height = 1.72;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        //numbers inside {} represent the variables based on their index, aka 0 for weight, 1 for height
        @ParameterizedTest(name = "weight = {0}, height = {1}")
        //CSV stands for Comma Separated values, used to test multiple values for multiple variables in parameterized tests
        @CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.0, 1.78"})
        void should_ReturnTrue_When_DietRecommended(Double coderWeight, Double coderHeight) {
            //given
            double weight = coderWeight;
            double height = coderHeight;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        @ParameterizedTest(name = "weight = {0}, height = {1}")
        //JUnit automatically searches by default in test/resources folder, so path just add on from there
        //skip line 1 as Excel file first line is title. Not mandatory, depending on use case
        @CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
        void should_ReturnTrue_When_DietRecommendedUsingCSVFile(Double coderWeight, Double coderHeight) {
            //given
            double weight = coderWeight;
            double height = coderHeight;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertTrue(recommended);
        }

        @Test
        void should_ReturnFalse_When_DietNotRecommended() {
            //given
            double weight = 50.0;
            double height = 1.92;

            //when
            boolean recommended = BMICalculator.isDietRecommended(weight, height);

            //then
            assertFalse(recommended);
        }

        @Test
        void should_ThrowArithmeticException_When_HeightZero() {
            //given
            double weight = 50.0;
            double height = 0.0;

            //when
            //right side needs a lambda expression for it to be an executable
            Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

            //then
            assertThrows(ArithmeticException.class, executable);
        }
    }

    @Nested
    class FindCoderWithWorstBMITests {
        @Test
        public void should_ReturnCoderWithWorstBMI_When_CoderListNotEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.80, 64.7));

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertAll(
                    () -> assertEquals(1.82, coderWorstBMI.getHeight()),
                    () -> assertEquals(98.0, coderWorstBMI.getWeight())
            );

        }

        @Test
        public void should_ReturnCoderWithWorstBMIIn1Ms_When_CoderListHas1000Elements() {
            //given
            List<Coder> coders = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                coders.add(new Coder(1.0 + i, 10.0 + i));
            }

            //when
            Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);

            //then
            //assertTimeout only measures how long executable took, not the entire test
            assertTimeout(Duration.ofMillis(500), executable);

        }

        @Test
        public void should_ReturnNullWorstBMICoder_When_CoderListEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();

            //when
            Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

            //then
            assertNull(coderWorstBMI);
        }
    }

    @Nested
    class GetBMIScoresTests {
        @Test
        public void should_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty() {
            //given
            List<Coder> coders = new ArrayList<>();
            coders.add(new Coder(1.80, 60.0));
            coders.add(new Coder(1.82, 98.0));
            coders.add(new Coder(1.80, 64.7));
            double[] expected = {18.52, 29.59, 19.97};

            //when
            double[] bmiScores = BMICalculator.getBMIScores(coders);

            //then
            //assertEquals(expected, bmiScores) won't work as this compares the object references, not values
            //so use assertArrayEquals()
            assertArrayEquals(expected, bmiScores);
        }
    }


}