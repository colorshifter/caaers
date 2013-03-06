package gov.nih.nci.cabig.caaers.domain.repository;

import gov.nih.nci.cabig.caaers.dao.ExpeditedAdverseEventReportDao;


 
/*
* @author Ion C. Olaru
*
* */
/**
 * The Class ExpeditedAdverseEventReportRepository.
 */
public class ExpeditedAdverseEventReportRepository {

    /** The ae report dao. */
    private ExpeditedAdverseEventReportDao aeReportDao;

    /**
     * Gets the ae report dao.
     *
     * @return the ae report dao
     */
    public ExpeditedAdverseEventReportDao getAeReportDao() {
        return aeReportDao;
    }

    /**
     * Sets the ae report dao.
     *
     * @param aeReportDao the new ae report dao
     */
    public void setAeReportDao(ExpeditedAdverseEventReportDao aeReportDao) {
        this.aeReportDao = aeReportDao;
    }
}