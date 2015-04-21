package com.orangeandbronze.schoolreg.dao;

import com.orangeandbronze.schoolreg.domain.Enrollment;
import com.orangeandbronze.schoolreg.domain.Student;

public interface EnrollmentDao {

	
	public Enrollment findLatestBy(Student student) ;
	
	/** Should return a new Enrollment if none exists, or fetch an existing one. **/
	public Enrollment findBy(Student student, String term);


}
