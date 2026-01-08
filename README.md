# Registrar, Part 1

This is an in-class activity. [Directions are here](https://comp127.macalester.digital/latest/activities/registrar_1).

## Contract of the `Student` and `Course` API

- Students know their registered courses, and courses know the list of students enrolled in them:
    > For all students and courses, `student.getCourses().contains(course)` if and only if `course.getRoster().contains(student)`.

- Courses can have a max enrollment limit on the number of students:
    > For all courses, `course.getRoster().size()` ≤ `course.getEnrollmentLimit()`.

- The enrollment limit cannot change if any students are already registered for the course.

We have implemented tests that verify that the existing code satisfies this contract. Run `RegistrarTest`. All the tests should pass.
