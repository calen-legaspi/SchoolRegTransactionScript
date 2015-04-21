package com.orangeandbronze.schoolreg.service;

import java.util.*;

import com.orangeandbronze.schoolreg.dao.*;
import com.orangeandbronze.schoolreg.domain.*;

public class EnlistService {

	private StudentDao studentDao;
	private SectionDao sectionDao;
	private EnrollmentDao enrollmentDao;
	
	public Collection<Section> getAllSections() {
		return sectionDao.findAll();
	}

	/**
	 * @param studentNumber
	 *            Student number of the student that the sections will attempt
	 *            to be enlisted.
	 * @param sectionNumbers
	 *            Section numbers of the sections to be enlisted with the
	 *            student.
	 * @return EnlistmentResult contains which sections were successfully
	 *         enlisted, and which were not. Those that were not include the
	 *         reason why they failed to enlist.
	 */
	//@Transactional
	public EnlistmentResult enlistSections(Integer studentNumber, String... sectionNumbers) {
		// Fetch domain objects from DB
		Student student = studentDao.findById(studentNumber);
		Section[] sections = sectionNumbers != null ? new Section[sectionNumbers.length] : new Section[0];
		for (int i = 0; i < sections.length; i++) {
			sections[i] = sectionDao.findById(sectionNumbers[i]);
		}
		Enrollment enrollment = enrollmentDao.findLatestBy(student);
		
		// business logic here
		Set<Section> currentlyEnlistedSections = enrollment.getSections();
		Set<SectionInfo> successfullyEnlisted = new HashSet<>();
		Map<SectionInfo, String> failedToEnlist = new HashMap<>();
		
		for (Section newSection : sections) {
			boolean valid = true;
			// check for schedule conflict
			for (Section currentSection : currentlyEnlistedSections) {
				if (currentSection.getSchedule().equals(newSection.getSchedule())) {
					failedToEnlist.put(new SectionInfo(newSection), EnlistmentResult.CONFLICT_WITH_SECTIONS_ALREADY_ENLISTED);
					valid = false;
					break;
				} 
			}
			if (!valid) {
				continue;
			}
			// check for prerequisite conflict
			Collection<Subject> prerequistes = newSection.getSubject().getPrerequisites();
			//  get all enrollments previous to the current enrollment
			Collection<Enrollment> previousEnrollments = student.getEnrollments().headSet(enrollment);
			for (Enrollment previousEnrollment : previousEnrollments) {
				Collection<Section> previousSections = previousEnrollment.getSections();
				for (Subject prerequisite : prerequistes) {
					boolean found = false;
					for (Section previousSection : previousSections) {
						Subject subjectOfPreviousSection = previousSection.getSubject();
						if (subjectOfPreviousSection.equals(prerequisite)) {
							// prerequisite found!
							found = true;
							break;
						}
					}
					if (!found) {
						failedToEnlist.put(new SectionInfo(newSection), EnlistmentResult.MISSING_PREREQISITES);
						valid = false;
						break;
					}
				}				
			}
			
			if (!valid) {
				continue;
			}
			
			currentlyEnlistedSections.add(newSection);
			successfullyEnlisted.add(new SectionInfo(newSection));
		}
		
		return new EnlistmentResult(successfullyEnlisted, failedToEnlist);
	}

	public static class EnlistmentResult {
		
		public static final String CONFLICT_WITH_SECTIONS_ALREADY_ENLISTED = "Conflict with sections already enlisted.";
		public static final String MISSING_PREREQISITES = "Missing prerequisite/s.";

		private final Set<SectionInfo> successfullyEnlisted;
		private final Map<SectionInfo, String> failedToEnlist;

		EnlistmentResult(Set<SectionInfo> successfullyEnlisted,
				Map<SectionInfo, String> failedToEnlist) {
			this.successfullyEnlisted = successfullyEnlisted;
			this.failedToEnlist = failedToEnlist;
		}

		public Set<SectionInfo> getSuccessfullyEnlisted() {
			return successfullyEnlisted;
		}

		/**
		 * Returns a map with the sections that failed to enlist as keys, and
		 * the reason why it failed as values.
		 **/
		public Map<SectionInfo, String> getFailedToEnlist() {
			return failedToEnlist;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((failedToEnlist == null) ? 0 : failedToEnlist.hashCode());
			result = prime
					* result
					+ ((successfullyEnlisted == null) ? 0
							: successfullyEnlisted.hashCode());
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
			EnlistmentResult other = (EnlistmentResult) obj;
			if (failedToEnlist == null) {
				if (other.failedToEnlist != null)
					return false;
			} else if (!failedToEnlist.equals(other.failedToEnlist))
				return false;
			if (successfullyEnlisted == null) {
				if (other.successfullyEnlisted != null)
					return false;
			} else if (!successfullyEnlisted.equals(other.successfullyEnlisted))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "EnlistmentResult [successfullyEnlisted="
					+ successfullyEnlisted + ", failedToEnlist="
					+ failedToEnlist + "]";
		}

	}

	public void setStudentDao(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void setSectionDao(SectionDao sectionDao) {
		this.sectionDao = sectionDao;
	}

	public void setEnrollmentDao(EnrollmentDao enrollmentDao) {
		this.enrollmentDao = enrollmentDao;
	}
	
	

}
