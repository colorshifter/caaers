/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.validation.fields.validators;

import gov.nih.nci.cabig.caaers.validation.fields.validators.PastDateValidator;

import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class PastDateValidatorTest extends TestCase {

	public void testIsValid() {
		Date now = new Date();
		PastDateValidator pastDateValidator = new PastDateValidator();
		boolean valid = pastDateValidator.isValid(now);
		assertTrue(valid);
		
		Date startDate1 = new GregorianCalendar(2019, 07, 14, 14, 00).getTime();
		boolean notValid = pastDateValidator.isValid(startDate1);
		assertFalse(notValid);
		
	}

}
