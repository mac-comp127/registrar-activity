package registrar;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess")
class RegistrarTest {
    // ------ Setup ------

    private TestObjectFactory factory = new TestObjectFactory();
    private Course comp127, math6, basketWeaving101;
    private Student sally, fred, zongo;

    @BeforeEach
    public void createStudents() {
        sally = factory.makeStudent("Sally");
        fred = factory.makeStudent("Fred");
        zongo = factory.makeStudent("Zongo Jr.");
    }

    @BeforeEach
    public void createCourses() {
        comp127 = factory.makeCourse("COMP 127", "Software Fun Fun");
        comp127.setEnrollmentLimit(16);

        math6 = factory.makeCourse("Math 6", "All About the Number Six");
        basketWeaving101 = factory.makeCourse("Underwater Basket Weaving 101", "Senior spring semester!");
    }

    // ------ Enrolling ------

    @Test
    void studentStartsInNoCourses() {
        assertEquals(List.of(), sally.getCourses());
    }

    @Test
    void studentCanEnroll() {
        sally.enrollIn(comp127);
        assertEquals(List.of(comp127), sally.getCourses());
        assertEquals(List.of(sally), comp127.getRoster());
    }

    // ------ Enrollment limits ------

    @Test
    void coursesHaveEnrollmentLimits() {
        comp127.setEnrollmentLimit(16);
        assertEquals(16, comp127.getEnrollmentLimit());
    }

    @Test
    void enrollmentLimitDefaultsToUnlimited() {
        factory.enrollMultipleStudents(math6, 1000);
        assertEquals(1000, math6.getRoster().size());
    }

    @Test
    void enrollingUpToLimitAllowed() {
        factory.enrollMultipleStudents(comp127, 15);
        assertTrue(sally.enrollIn(comp127));
        assertTrue(comp127.getRoster().contains(sally));
    }

    @Test
    void cannotEnrollPastLimit() {
        factory.enrollMultipleStudents(comp127, 16);
        assertFalse(sally.enrollIn(comp127));
        assertFalse(comp127.getRoster().contains(sally));
    }

    // ------ Post-test invariant check ------
    //
    // This is a bit persnickety for day-to-day testing, but these kinds of checks are appropriate
    // for security sensitive or otherwise mission-critical code. Some people even add them as
    // runtime checks in the code, instead of writing them as tests.

    @AfterEach
    public void checkInvariants() {
        for (Student s : factory.allStudents())
            checkStudentInvariants(s);
        for (Course c : factory.allCourses())
            checkCourseInvariants(c);
    }

    private void checkStudentInvariants(Student s) {
        for (Course c : s.getCourses())
            assertTrue(
                c.getRoster().contains(s),
                s + " thinks they are enrolled in " + c
                    + ", but " + c + " does not have them in the list of students");
    }

    private void checkCourseInvariants(Course c) {
        for (Student s : c.getRoster()) {
            assertTrue(
                s.getCourses().contains(c),
                c + " thinks " + s + " is enrolled, but " + s + " doesn't think they're in the class");
        }

        assertTrue(
            c.getRoster().size() <= c.getEnrollmentLimit(),
            c + " has an enrollment limit of " + c.getEnrollmentLimit()
                + ", but has " + c.getRoster().size() + " students");
    }
}
