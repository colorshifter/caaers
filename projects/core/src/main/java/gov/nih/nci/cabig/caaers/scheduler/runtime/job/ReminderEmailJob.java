package gov.nih.nci.cabig.caaers.scheduler.runtime.job;

import gov.nih.nci.cabig.caaers.domain.report.DeliveryStatus;
import gov.nih.nci.cabig.caaers.domain.report.ScheduledEmailNotification;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * This Job will send an email reminder notifaction,based on a Report.
 * @author Biju Joseph
 *
 */
public class ReminderEmailJob extends ScheduledNotificationJobTemplate {

	@Override
	public DeliveryStatus processNotification() {
		logger.debug("\n\r\n\r\nProceeding with emailing...[ \r\n\r\n" + String.valueOf(report) +"\r\n]\r\n\r\n");
		ScheduledEmailNotification sen = (ScheduledEmailNotification) notification;
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(sen.getToAddress());
		msg.setFrom(sen.getFromAddress());
		msg.setReplyTo(sen.getFromAddress());
		msg.setSentDate(sen.getScheduledOn());
		msg.setSubject(sen.getSubjectLine());
		msg.setText(new String(sen.getBody()));

		try{
			MailSender mailer = (MailSender)applicationContext.getBean("mailer");
			System.out.println("mailer :" + mailer);
            mailer.send(msg);
            return DeliveryStatus.DELIVERED;
        }catch(MailException ex) {
            logger.error(ex);
        }catch(RuntimeException ex){
        	logger.error(ex);
        }
        return DeliveryStatus.ERROR;
	}
}
