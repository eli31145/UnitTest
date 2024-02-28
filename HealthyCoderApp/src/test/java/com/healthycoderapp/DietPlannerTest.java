package com.healthycoderapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DietPlannerTest {

    //unit tests should be independent of each other so should not initialize it here aka new DietPlanner();
    private DietPlanner dietPlanner;

    @BeforeEach
    public void setup() {
        this.dietPlanner = new DietPlanner(20, 30, 50);
    }

    @AfterEach
    public void afterEach() {
        System.out.println("Unit test finished");
    }

    //@Test
        //use @RepeatedTest to run same text certain number of times
    //@RepeatedTest(5)
        //not mandatory, but you can also specify name of the repeated test so easier to see
    @RepeatedTest(value = 10, name = RepeatedTest.LONG_DISPLAY_NAME)
    void should_ReturnCorrectDietPlan_When_CorrectCoder() {
        //given
        Coder coder = new Coder(1.82, 75.0, 26, Gender.MALE);
        DietPlan expected = new DietPlan(2202, 110, 73, 275);

        //when
        DietPlan actual = dietPlanner.calculateDiet(coder);

        //then
            //once again cannot use assertEquals(expected, actual) as both expected and actual are separate objects
            //despite having same values
        assertAll(
                () -> assertEquals(expected.getCalories(), actual.getCalories()),
                () -> assertEquals(expected.getProtein(), actual.getProtein()),
                () -> assertEquals(expected.getFat(), actual.getFat()),
                () -> assertEquals(expected.getCarbohydrate(), actual.getCarbohydrate())
        );
    }

}