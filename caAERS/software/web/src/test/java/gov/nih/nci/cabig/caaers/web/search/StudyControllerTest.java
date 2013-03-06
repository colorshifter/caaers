package gov.nih.nci.cabig.caaers.web.search;

import gov.nih.nci.cabig.caaers.web.ListValues;
import gov.nih.nci.cabig.caaers.web.WebTestCase;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Krikor Krumlian
 */
public class StudyControllerTest extends WebTestCase {
    private StudyController controller = new StudyController();

    protected void setUp() throws Exception {
        super.setUp();
        controller.setListValues(new ListValues());
    }

    public void testViewOnGoodSubmit() throws Exception {

        ModelAndView mv = controller.handleRequest(request, response);
        assertEquals("search/study_search", mv.getViewName());
    }
}