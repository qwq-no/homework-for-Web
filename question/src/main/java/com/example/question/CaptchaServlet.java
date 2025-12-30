package com.example.question;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

@WebServlet("/captcha")
public class CaptchaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 4;
    private static final int IMAGE_WIDTH = 120;
    private static final int IMAGE_HEIGHT = 40;
    private static final int FONT_SIZE = 20;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/png");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, FONT_SIZE));
        Random random = new Random();
        g.setColor(new Color(100, 100, 100));
        for (int i = 0; i < 10; i++) {
            int x1 = random.nextInt(IMAGE_WIDTH);
            int y1 = random.nextInt(IMAGE_HEIGHT);
            int x2 = random.nextInt(IMAGE_WIDTH);
            int y2 = random.nextInt(IMAGE_HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }
        StringBuilder captchaCode = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            char c = CHARS.charAt(random.nextInt(CHARS.length()));
            captchaCode.append(c);
            g.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));
            g.drawString(String.valueOf(c), i * (IMAGE_WIDTH / CODE_LENGTH) + 5, IMAGE_HEIGHT / 2 + FONT_SIZE / 2 - 5);
        }
        request.getSession().setAttribute("captcha", captchaCode.toString());
        try (OutputStream out = response.getOutputStream()) {
            ImageIO.write(image, "png", out);
        } finally {
            g.dispose(); // 释放资源
        }
    }
}