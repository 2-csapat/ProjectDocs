package hu.elte.bankapp.controllers;

import hu.elte.bankapp.configuration.Utility;
import hu.elte.bankapp.entities.User;
import hu.elte.bankapp.exception.UserNotFoundException;
import hu.elte.bankapp.service.UserService;
import hu.elte.bankapp.webdomain.ForgotView;
import hu.elte.bankapp.webdomain.RegisterView;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(){
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (UserNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email.");
        }
        return "forgot_password_form";
    }

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("villaskulcs234@gmail.com", "Support");
        helper.setTo(recipientEmail);

        String subject = "Here is the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content,true);
        mailSender.send(message);

    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        model.addAttribute("customer", new ForgotView());

        if(user == null) {
            model.addAttribute("message", "Invalid Token");
            return "reset_password_form";
        }
        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(Model model,
                                       @Valid @ModelAttribute("customer") ForgotView customer,
                                       BindingResult bindingResult,
                                       HttpServletRequest request
                                       ) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");


        if (bindingResult.hasErrors()) {
            model.addAttribute("message","Passwords do not match.");
            model.addAttribute("token", token);
            return "reset_password_form";
        }

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid token");
            return "reset_password_form";
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password.");
        }
        return "redirect:/login";
    }

}
