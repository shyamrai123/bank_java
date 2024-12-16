package spring.example.bankService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import spring.example.bankService.dto.EmailDetails;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
     @Value("${spring.mail.username}")
    private String fromEmail;



    public void sendEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }


    public  void  sendLoanApproval(EmailDetails emailDetails){
        try {

            SimpleMailMessage message =new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailDetails.getRecipient());
            message.setText(emailDetails.getMessageBody());
            message.setSubject(emailDetails.getSubject());

        }
        catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
