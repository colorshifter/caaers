package gov.nih.nci.cabig.caaers.web.participant;

import gov.nih.nci.cabig.caaers.domain.Organization;
import gov.nih.nci.cabig.caaers.domain.Participant;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.StudyParticipantAssignment;
import gov.nih.nci.cabig.caaers.domain.StudySite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Krikor Krumlian
 */

public class NewParticipantCommand {
	protected final Log log = LogFactory.getLog(getClass());

	private String[] studySiteArray;

	private String searchTypeText;

	private String searchType;

	private String searchText;

	// NEWLY ADDED
	private StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();

	private List<Study> studies = new ArrayList<Study>();

	private List<StudySite> studySites = new ArrayList<StudySite>();

	private Organization organization;

	private Participant participant;

	public NewParticipantCommand() {
		participant = new Participant();
	}

	public NewParticipantCommand(final Participant participant) {
		this.participant = participant;
	}

	// public static NewParticipantCommand createNewParticipantCommand(final Participant participant) {
	//
	// NewParticipantCommand newParticipantCommand = new NewParticipantCommand();
	// // newParticipantCommand.setInstitutionalPatientNumber(participant.getInstituitionalPatientNumber());
	//
	// List<StudyParticipantAssignment> assignments = participant.getAssignments();
	// for (StudyParticipantAssignment studyParticipantAssignment : assignments) {
	// newParticipantCommand.addStudySite(studyParticipantAssignment.getStudySite());
	// }
	//
	// newParticipantCommand.setOrganization(assignments.get(0).getStudySite().getOrganization());
	// // for (int i = 0; i < participant.getStudies()studySites.size(); i++) {
	// // newParticipantCommand.getAssignmts().add(new StudyParticipantAssignment(participant, studySites.get(i)));
	// // }
	// return newParticipantCommand;
	// }

	// private void addStudySite(final StudySite studySite) {
	// studySites.add(studySite);
	// }

	// public Participant createParticipant() {
	// Participant participant = new Participant();
	// participant.setInstitutionalPatientNumber(getInstituitionalPatientNumber());
	// participant.setInstitution(getInstitution());
	// participant.setFirstName(getFirstName());
	// participant.setMaidenName(getMaidenName());
	// participant.setMiddleName(getMiddleName());
	// participant.setLastName(getLastName());
	// participant.setDateOfBirth(getDateOfBirth());
	// participant.setGender(getGender());
	// participant.setRace(getRace());
	// participant.setEthnicity(getEthnicity());
	// participant.setIdentifiers(getIdentifiers());
	//
	// for (int i = 0; i < studySites.size(); i++) {
	// participant.getAssignments().add(new StudyParticipantAssignment(participant, studySites.get(i)));
	// }
	// return participant;
	// }

	public String[] getStudySiteArray() {
		return studySiteArray;
	}

	public void setStudySiteArray(final String[] studySiteArray) {
		this.studySiteArray = studySiteArray;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(final String searchType) {
		this.searchType = searchType;
	}

	public String getSearchTypeText() {
		return searchTypeText;
	}

	public void setSearchTypeText(final String searchTypeText) {
		this.searchTypeText = searchTypeText;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(final String searchText) {
		this.searchText = searchText;
	}

	public StudyParticipantAssignment getStudyParticipantAssignment() {
		return studyParticipantAssignment;
	}

	public void setStudyParticipantAssignment(final StudyParticipantAssignment studyParticipantAssignment) {
		this.studyParticipantAssignment = studyParticipantAssignment;
	}

	public List<Study> getStudies() {
		return studies;
	}

	public void setStudies(final List<Study> studies) {
		this.studies = studies;
	}

	public List<StudySite> getStudySites() {
		return studySites;
	}

	public void setStudySites(final List<StudySite> studySites) {
		this.studySites = studySites;

	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(final Organization organization) {
		this.organization = organization;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(final Participant participant) {
		this.participant = participant;
	}
}
