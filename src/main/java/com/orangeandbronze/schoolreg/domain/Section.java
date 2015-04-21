package com.orangeandbronze.schoolreg.domain;

import java.util.*;

public class Section extends Entity {

	private final String sectionNumber;
	private final Subject subject;
	private final String term;
	private Faculty instructor;
	private Schedule schedule;

	public Section(String sectionNumber, Subject subject, String term, Schedule schedule) {
		this.sectionNumber = sectionNumber;
		this.subject = subject;
		this.term = term;
		this.schedule = schedule;
		this.instructor = Faculty.TBA;
	}
	
	public Section(String sectionNumber, Subject subject, String term, Schedule schedule, Faculty instructor) {
		this(sectionNumber, subject, term, schedule);
		this.instructor = instructor;
	}

	public String getSectionNumber() {
		return sectionNumber;
	}

	public Subject getSubject() {
		return subject;
	}

	public Faculty getInstructor() {
		return instructor;
	}

	void setInstructor(Faculty instructor) {
		this.instructor = instructor;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	
	public String getTerm() {
		return term;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sectionNumber == null) ? 0 : sectionNumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Section other = (Section) obj;
		if (sectionNumber == null) {
			if (other.sectionNumber != null)
				return false;
		} else if (!sectionNumber.equals(other.sectionNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return sectionNumber + " {" + subject + " " + schedule + "} ";
	}
	
	/** Null Object pattern **/
	public final static Section NONE = new Section("Does Not Exist", null, null, null);

}
