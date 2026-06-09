package br.com.arq.service;

import com.resend.Resend;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;


    public void enviarResend(
              String para,
            String assunto,
            String mensagem
    ){
        Resend resend = new Resend("re_GnmaBz94_JAB84ep7f68Bu79WGYnXWUGZ");

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("onboarding@resend.dev")
                .to(para)
                .subject("Bem vindo ao Banco InvestTrader")
                .html("<p>Aproveite sua Jornada em nosso Banco, Obrigado pelo Preferência !!!</p>")
                .build();

        SendEmailResponse data = resend.emails().send(sendEmailRequest);
    }

    public void enviar(
            String para,
            String assunto,
            String mensagem
    ) {

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(para);
        email.setSubject(assunto);
        email.setText(mensagem);

        mailSender.send(email);
    }


}
