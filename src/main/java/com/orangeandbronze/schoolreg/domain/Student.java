package com.orangeandbronze.schoolreg.domain;

import java.util.*;

public class Student extends Entity {

	private final Integer studentNumber;

	private final SortedSet<Enrollment> enrollments = new TreeSet<>();

	public Student(Integer studentNumber) {
		this.studentNumber = studentNumber;
	}

	public Student(Integer studentNumber, Collection<Enrollment> enrollments) {
		this.studentNumber = studentNumber;
		this.enrollments.addAll(enrollments);
	}

	public Integer getStudentNumber() {
		return studentNumber;
	}
	
	public SortedSet<Enrollment> getEnrollments() {
		return enrollments;	// risk of corruption
	}

}
