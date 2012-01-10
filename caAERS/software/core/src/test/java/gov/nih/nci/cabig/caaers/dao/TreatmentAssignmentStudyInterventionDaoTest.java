package gov.nih.nci.cabig.caaers.dao;

import gov.nih.nci.cabig.caaers.DaoTestCase;
import gov.nih.nci.cabig.caaers.domain.*;
import org.dbunit.operation.DatabaseOperation;

import java.util.List;

/**
 * @author Ion C. Olaru
 */
public class TreatmentAssignmentStudyInterventionDaoTest extends DaoTestCase<TreatmentAssignmentStudyInterventionDao> {

    private TreatmentAssignmentDao tadao = getApplicationContext().getBean("treatmentAssignmentDao", TreatmentAssignmentDao.class);
    private CtcTermDao ctcTermDao = getApplicationContext().getBean("ctcTermDao", CtcTermDao.class);
    private StudyAgentDao studyAgentDao = getApplicationContext().getBean("studyAgentDao", StudyAgentDao.class);

    public void testLoadTreatmentAssignment() {
        TreatmentAssignment ta = tadao.getById(-11);
        System.out.println(">>> TA:" + ta);
        System.out.println(ta.getCode());
        System.out.println(ta.getId());
    }

    public void testAddTreatmentAssignmentAgent() {
        TreatmentAssignmentAgent taa = new TreatmentAssignmentAgent();
        TreatmentAssignment ta = tadao.getById(-11);
        taa.setTreatmentAssignment(ta);
        getDao().save(taa);
        assertEquals(1, getDao().getAll().size());
        assertEquals(1, tadao.getTreatmentAssignmentAgents().size());
    }

    public void testAddDifferentTreatmentAssignments() {
        TreatmentAssignment ta = tadao.getById(-11);

        TreatmentAssignmentAgent taa = new TreatmentAssignmentAgent();
        taa.setTreatmentAssignment(ta);
        getDao().save(taa);

        TreatmentAssignmentDevice tad1 = new TreatmentAssignmentDevice();
        tad1.setTreatmentAssignment(ta);
        getDao().save(tad1);

        TreatmentAssignmentDevice tad2 = new TreatmentAssignmentDevice();
        tad2.setTreatmentAssignment(ta);
        getDao().save(tad2);

        TreatmentAssignmentOtherIntervention tao = new TreatmentAssignmentOtherIntervention();
        tao.setTreatmentAssignment(ta);
        getDao().save(tao);

        assertEquals(4, getDao().getAll().size());

        List<TreatmentAssignmentStudyIntervention> loaded = getDao().loadAllByClass(TreatmentAssignmentDevice.class);
        assertEquals(2, loaded.size());

        assertEquals(TreatmentAssignmentDevice.class, loaded.get(0).getClass());
        assertEquals(TreatmentAssignmentDevice.class, loaded.get(1).getClass());

        assertEquals(4, ta.getTreatmentAssignmentStudyInterventions().size());

        assertEquals(1, tadao.getTreatmentAssignmentAgents().size());
        assertEquals(2, tadao.getTreatmentAssignmentDevices().size());
        assertEquals(1, tadao.getTreatmentAssignmentOthers().size());

    }

    public void testSaveStudyInterventionExpectedTerms() {
        CtcTerm t = ctcTermDao.getById(3012);
        TreatmentAssignment ta = tadao.getById(-11);
        StudyAgent studyAgent = studyAgentDao.getById(-1000);

        TreatmentAssignmentAgent taa = new TreatmentAssignmentAgent();
        taa.setTreatmentAssignment(ta);
        taa.setStudyAgent(studyAgent);

        StudyInterventionExpectedCtcTerm term = new StudyInterventionExpectedCtcTerm();
        term.setTerm(t);
        term.setExpectednessFrequency(0.1);
        term.setExpectednessFrequency(0.9);
        term.setGrade1Frequency(0.1);
        term.setGrade2Frequency(0.2);
        term.setGrade3Frequency(0.3);
        term.setGrade4Frequency(0.4);
        term.setGrade5Frequency(0.5);
        term.setOtherMeddraTerm(null);
        taa.addAbstractStudyInterventionExpectedAEs(term);
        getDao().save(taa);

    }

    @Override
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.REFRESH;
    }

/*
    @Override
    protected DatabaseOperation getSetUpOperation() throws Exception {
        return DatabaseOperation.NONE;
    }
*/
}
