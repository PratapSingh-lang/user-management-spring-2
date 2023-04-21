package com.bel.asp.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

@Configuration
@RestController
@RequestMapping("/api/v1/auth")
public class CaptchaController {

	@Autowired
    private DefaultKaptcha defaultKaptcha;

//    @Autowired
//    public CaptchaController(DefaultKaptcha defaultKaptcha) {
//        this.defaultKaptcha = defaultKaptcha;
//    }

//	@GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_JPEG_VALUE)
    public 
//    BufferedImage
    ResponseEntity<?>
    getCaptcha(HttpServletRequest request, HttpServletResponse response) 
    		throws IOException
    {
        // Set the response content type to image
        response.setContentType("image/jpeg");
        // Generate two random numbers
        Random rand = new Random();
        int num1 = rand.nextInt(8) + 1; // generate a random integer between 1 and 50
        int num2 = rand.nextInt(8) + 1; // generate a second random integer between 1 and 50
        // Save the addition of the two numbers as CAPTCHA text to the session
        int result = num1 + num2;
        String capText = num1 + " + " + num2 + " = ?";
        request.getSession().setAttribute("captcha", result);
        
        // Generate the CAPTCHA image
        BufferedImage bi = defaultKaptcha.createImage(capText);
//        response.setContentType(MediaType.IMAGE_JPEG);
//        saveImage(bi, "captcha");
        // Write the image to the response output stream
//        return new ResponseEntity<>(
//        		convertToByteArray(bi)
////        		ImageIO.write(bi, "jpeg", response.getOutputStream())
////        		bi
//        		, HttpStatus.OK);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(convertToByteArray(bi));
        
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<String> verifyCaptcha(HttpServletRequest request, @RequestParam("captcha") String captchaInput) {
        // Get the saved CAPTCHA result from the session
        int savedCaptcha = (int) request.getSession().getAttribute("captcha");
        // Convert the input value to integer
        int inputCaptcha = 0;
        try {
            inputCaptcha = Integer.parseInt(captchaInput);
        } catch (NumberFormatException ex) {
            // Handle invalid input
            return new ResponseEntity<>("Invalid input", HttpStatus.BAD_REQUEST);
        }
        // Check if the input value matches the saved CAPTCHA result
        if (savedCaptcha == inputCaptcha) {
            return new ResponseEntity<>("Captcha is valid", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Captcha is invalid", HttpStatus.BAD_REQUEST);
        }
    }

    @Bean
    public DefaultKaptcha captchaProducer() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.textproducer.font.color", "black");
        properties.setProperty("kaptcha.image.width", "200");
        properties.setProperty("kaptcha.image.height", "75");
        properties.setProperty("kaptcha.textproducer.font.size", "45");
        properties.setProperty("kaptcha.textproducer.char.space", "5");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    private byte[] convertToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
    public static void saveImage(BufferedImage image, String filename) {
        try {
//        	 File output = new File(
////        			 System.getProperty("user.home"), "images/"
//        "."	
//		+ filename);
//             ImageIO.write(image, "png", output);
            File output = new File(filename);
             ImageIO.write(image, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
