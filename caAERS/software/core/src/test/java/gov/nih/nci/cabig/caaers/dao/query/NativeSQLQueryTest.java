package gov.nih.nci.cabig.caaers.dao.query;

import gov.nih.nci.cabig.caaers.AbstractTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.hibernate.Hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * NativeSQLQuery Tester.
 *
 * @author Biju Joseph
 * @since <pre>11/07/2010</pre>
 * 
 */
public class NativeSQLQueryTest extends AbstractTestCase {
    NativeSQLQuery query;

    public void setUp() throws Exception {
        super.setUp();
        query = new  NativeSQLQuery("select id from emp");
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetScalar() throws Exception {
       query.setScalar("id", Hibernate.INTEGER);
       assertSame(Hibernate.INTEGER, query.getScalarType("id"));
    }

    public void testGetScalarNames() throws Exception {
        query.setScalar("x", Hibernate.INTEGER);
        query.setScalar("y", Hibernate.INTEGER);

        Set<String> s = new HashSet<String>();
        s.add("x");
        s.add("y");


       assertEquals(s, query.getScalarNames());
    }

    public void testGetScalarType() throws Exception {
         query.setScalar("id", Hibernate.INTEGER);
       assertSame(Hibernate.INTEGER, query.getScalarType("id"));
       assertNull(query.getScalarType("junk"));
    }

}