package gov.nih.nci.cabig.caaers.esb.client;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;


public class ESBMessageConsumerImpl implements ESBMessageConsumer {
	
	 	private MessageNotificationService messageNotificationService;
	    

	
	private Element getJobInfo(String message) {
		
		SAXBuilder saxBuilder=new SAXBuilder("org.apache.xerces.parsers.SAXParser");
		Reader stringReader=new StringReader(message);
		Element jobInfo = null;
		try {
			org.jdom.Document jdomDocument=saxBuilder.build(stringReader);
			org.jdom.Element root = jdomDocument.getRootElement();

			Element body = root.getChild("Body",root.getNamespace());
			Element response = body.getChild("submitAEDataXMLAsAttachmentResponse");
			Namespace n = ((Element)response.getChildren().get(0)).getNamespace();
			jobInfo = response.getChild("AEReportJobInfo",n);
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jobInfo;
	}
	public void processMessage(String message) {
		System.out.println("GOT MESSAGE ... ");
		System.out.println("BEGIN MESSAGE ... ");
		System.out.println(message );
		System.out.println("END MESSAGE ... ");
		
		Element jobInfo = getJobInfo(message);
		String caaersAeReportId = jobInfo.getChild("CAEERS_AEREPORT_ID").getValue();
		
		//buld error messages
		StringBuffer sb = new StringBuffer();
		sb.append("Report was successfully submitted to Adeers \n");
		sb.append("Report ID  : " + caaersAeReportId+"\n");
		sb.append("Job ID  : " +"\n");
	
		
		try {
			List<Element> exceptions = jobInfo.getChildren("jobExceptions");
			if (exceptions.size() > 0) {
				sb.append("		EXCEPTIONS		");
				sb.append("		----------		");
				sb.append("\n\n");
			}
			
			for (Element ex:exceptions){				
				sb.append(ex.getChild("code").getValue() + "  -  " + ex.getChild("description").getValue());
				sb.append("\n");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String errorMessages = sb.toString();
		
		//get caaers ID
		
		
		
		// Notify submitter
		System.out.println("calling msessageNotifyService 10..");
		
		try {
			messageNotificationService.sendNotificationToReporter(errorMessages, caaersAeReportId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public static void main ( String ars ) {
    	
    }
	public void setMessageNotificationService(MessageNotificationService messageNotificationService) {
		this.messageNotificationService = messageNotificationService;
	}

}
