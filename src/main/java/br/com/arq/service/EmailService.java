package br.com.arq.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

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


    public void enviarGmail(String para) {

        try {

            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("devebsjunior@gmail.com");
            email.setTo(para);
            email.setSubject("Sua conta no Banco Invest foi criada");
            email.setText(
                    "Ola " + para + "\n\n" +
                            "Sua conta foi criada com sucesso no Banco Invest.\n\n" +
                            "Numero da conta:  \n\n" +
                            "Se nao foi voce, ignore este email.\n\n" +
                            "Atenciosamente,\nBanco Invest"
            );


            mailSender.send(email);

            System.out.println("EMAIL ENVIADO PARA: " + para);

        } catch (Exception e) {

            System.out.println("ERRO AO ENVIAR EMAIL: " + e.getMessage());
        }
    }
}

