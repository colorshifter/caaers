package gov.nih.nci.cabig.caaers.web.ae;

import gov.nih.nci.cabig.caaers.api.AdeersReportGenerator;
import gov.nih.nci.cabig.caaers.api.AdverseEventReportSerializer;
import gov.nih.nci.cabig.caaers.dao.ExpeditedAdverseEventReportDao;
import gov.nih.nci.cabig.caaers.domain.ExpeditedAdverseEventReport;
import gov.nih.nci.cabig.caaers.rules.common.RuleUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

public class GenerateExpeditedPdfController extends AbstractCommandController {
	
	public GenerateExpeditedPdfController() {
		setCommandClass(GenerateExpeditedPdfCommand.class);
    }
	
	

	@Override
	protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object arg2, BindException arg3) throws Exception {

		
		String tempDir = System.getProperty("java.io.tmpdir");
		String reportId = request.getParameter("aeReport");
    	try {
   		try {
    			ExpeditedAdverseEventReportDao expeditedAdverseEventReportDao = (ExpeditedAdverseEventReportDao)getApplicationContext().getBean("expeditedAdverseEventReportDao");
    			ExpeditedAdverseEventReport aeReport = expeditedAdverseEventReportDao.getById(Integer.parseInt(reportId));
    			AdverseEventReportSerializer ser = new AdverseEventReportSerializer();
    			String xml = ser.serialize(aeReport);

    			String pdfOutFile = tempDir+"/expeditedAdverseEventReport-"+reportId+".pdf";
    			
    			AdeersReportGenerator gen = new AdeersReportGenerator();
    			gen.genatePdf(xml,pdfOutFile);
    			
    			
				File file = new File(pdfOutFile);
				FileInputStream fileIn = new FileInputStream(file);
				OutputStream out = response.getOutputStream();
				response.setContentType( "application/x-download" );
				response.setHeader( "Content-Disposition", "attachment; filename=expeditedAdverseEventReport-"+reportId+".pdf" );
				 
				byte[] buffer = new byte[2048];
				int bytesRead = fileIn.read(buffer);
				while (bytesRead >= 0) {
				  if (bytesRead > 0)
				    out.write(buffer, 0, bytesRead);
				    bytesRead = fileIn.read(buffer);
				}
				out.flush();
				out.close();
				fileIn.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RemoteException ("Error generating PDF ",e);
			}
			
			
			
			//input = new BufferedReader( new FileReader(xmlFile) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
   
}