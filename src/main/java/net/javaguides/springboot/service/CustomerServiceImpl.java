package net.javaguides.springboot.service;

import net.bytebuddy.utility.RandomString;
import net.javaguides.springboot.model.Customer;
import net.javaguides.springboot.repository.CustomerRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class CustomerServiceImpl implements CustomerService {


    CustomerRepository customerRepository;

    JavaMailSender mailSender;

    PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerRepository customerRepository, JavaMailSender mailSender, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void generateOneTimePassword(Customer customer)
            throws UnsupportedEncodingException, MessagingException {
        String OTP = RandomString.make(6);
        String encodedOTP = passwordEncoder.encode(OTP);

        customer.setOneTimePassword(encodedOTP);
        customer.setOtpRequestedTime(new Date());

        customerRepository.save(customer);

        sendOTPEmail(customer, OTP);
    }

    @Override
    public void sendOTPEmail(Customer customer, String OTP)
            throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("Test@gmail.com", "Test spring mail");
        helper.setTo(customer.getEmailId());

        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";

        String content = "<p>Hello " + customer.getFirstName() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to login:</p>"
                + "<p><b>" + OTP + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 5 minutes.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public void clearOTP(Customer customer) {
        customer.setOneTimePassword(null);
        customer.setOtpRequestedTime(null);
        customerRepository.save(customer);
    }
}
