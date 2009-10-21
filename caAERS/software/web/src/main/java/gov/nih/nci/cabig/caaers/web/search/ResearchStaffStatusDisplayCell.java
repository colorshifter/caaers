package gov.nih.nci.cabig.caaers.web.search;

import gov.nih.nci.cabig.caaers.domain.ResearchStaff;
import gov.nih.nci.cabig.caaers.domain.SiteResearchStaff;

import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.cell.AbstractCell;
import org.extremecomponents.table.core.TableModel;

public class ResearchStaffStatusDisplayCell extends AbstractCell {

    @Override
    protected String getCellValue(final TableModel model, final Column column) {

        ResearchStaff researchStaff = ((SiteResearchStaff)model.getCurrentRowBean()).getResearchStaff();
        SiteResearchStaff siteResearchStaff = (SiteResearchStaff)model.getCurrentRowBean();
        String cellValue = column.getValueAsString();

        //if (researchStaff.isActive()) return "Active"; else return "Inactive";
        if (siteResearchStaff.isActive()) return "Active"; else return "Inactive";
    }
}
