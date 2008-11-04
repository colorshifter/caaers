package gov.nih.nci.cabig.caaers.web.study;

import static gov.nih.nci.cabig.caaers.CaaersUseCase.CREATE_STUDY;
import org.easymock.EasyMock;
import org.springframework.validation.Errors;
import org.springframework.validation.BindException;
import org.springframework.validation.BeanPropertyBindingResult;
import gov.nih.nci.cabig.caaers.CaaersUseCases;
import gov.nih.nci.cabig.caaers.dao.*;
import gov.nih.nci.cabig.caaers.domain.Fixtures;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.Condition;
import gov.nih.nci.cabig.caaers.web.WebTestCase;

/**
 * @author Ion C. Olaru
 */

@CaaersUseCases( { CREATE_STUDY })
public class DCPDiseaseStudyTest extends WebTestCase {

    private CreateStudyController controller;
    private DiseaseTab diseaseTab;
    private StudyDao studyDao;
    private ConditionDao conditionDao;
    private Study study;
    private Errors errors;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        studyDao = registerDaoMockFor(StudyDao.class);
        conditionDao = registerDaoMockFor(ConditionDao.class);

        controller = new CreateStudyController();
        controller.setStudyDao(studyDao);
        controller.setConditionDao(conditionDao);

        study = Fixtures.createStudy("study");
        diseaseTab = new DiseaseTab();
        diseaseTab.setConditionDao(conditionDao);
    }

    public void testViewOnGet() throws Exception {

        Condition condition = new Condition();
        errors = new BindException(new BeanPropertyBindingResult(study, "command"));
        condition.setId(5);
        
        request.setMethod("POST");
        request.setParameter("condition", "5");
        request.setParameter("_action", "addOtherCondition");
        EasyMock.expect(conditionDao.getById(5)).andReturn(condition);
        replayMocks();
        
        diseaseTab.postProcess(request, study, errors);

        verifyMocks();
        assertEquals(new Integer(5), study.getStudyConditions().get(0).getId());
    }
}