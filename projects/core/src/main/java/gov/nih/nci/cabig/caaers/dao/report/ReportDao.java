package gov.nih.nci.cabig.caaers.dao.report;


import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import edu.nwu.bioinformatics.commons.CollectionUtils;

import gov.nih.nci.cabig.caaers.dao.GridIdentifiableDao;
import gov.nih.nci.cabig.caaers.domain.report.Report;
/**
 * 
 * 
 * @author <a href="mailto:biju.joseph@semanticbits.com">Biju Joseph</a>
 * Created-on : May 13, 2007
 * @version     %I%, %G%
 * @since       1.0
 */
@Transactional(readOnly=true)
public class ReportDao extends GridIdentifiableDao<Report>{

	/* (non-Javadoc)
	 * @see gov.nih.nci.cabig.caaers.dao.CaaersDao#domainClass()
	 */
	@Override
	public Class<Report> domainClass() {
		return Report.class;
	}

    @Transactional(readOnly=false)
	public void save(Report rs){
		getHibernateTemplate().saveOrUpdate(rs);
	}
	
	public Session getHibernateSession(){
		return super.getSession();
	}
	
	@SuppressWarnings("unchecked")
	public List<Report> getAll(){
		return getHibernateTemplate().find("from Report");
	}
	
	@SuppressWarnings("unchecked")
	public Report getByName(String name){
		return CollectionUtils.firstElement(
				(List<Report>) getHibernateTemplate().find(
						"from Report s where s.name=?", new String[]{name}
						)
				);
	}
	
	public List<Report> getAllByDueDate(Date dueDate){
		return getByDate(2, dueDate);
	}
	
	public List<Report> getAllByCreatedDate(Date dueDate){
		return getByDate(3, dueDate);
	}
	
	public List<Report> getAllBySubmittedDate(Date dueDate){
		return getByDate(1, dueDate);
	}
	
	@SuppressWarnings("unchecked")
	private List<Report> getByDate(int dateType, Date d){
		String column = (dateType == 1)? "submittedOn" : ((dateType == 2)? "dueOn" : "createdOn");
		String hsql = "from Report s where s." + column + "=?";
		return getHibernateTemplate().find( hsql, new Object[]{d});
	}
	
	public boolean deleteById(int id){
		int count = getHibernateTemplate().bulkUpdate("delete Report s where s.id=?", new Object[]{id});
		return count >= 1;
	}
	
	public void delete(Report rs){
		getHibernateTemplate().delete(rs);
	}
	
	public void delete(Collection<Report> c){
		getHibernateTemplate().deleteAll(c);
	}
	
}
