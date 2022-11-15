package net.javaguides.springboot.service;

import net.javaguides.springboot.model.Customer;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface CustomerService {

    void generateOneTimePassword(Customer customer) throws UnsupportedEncodingException, MessagingException;
    void sendOTPEmail(Customer customer, String OTP) throws UnsupportedEncodingException, MessagingException;
    void clearOTP(Customer customer);
}
