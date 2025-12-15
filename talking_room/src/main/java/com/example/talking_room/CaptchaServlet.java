package com.example.talking_room;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "CaptchaServlet", value = "/captcha")
public class CaptchaServlet extends HttpServlet {
    // 验证码字符集
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    // 验证码长度
    private static final int CODE_LENGTH = 4;
    // 图片宽度
    private static final int WIDTH = 120;
    // 图片高度
    private static final int HEIGHT = 40;
    // 干扰线数量
    private static final int LINE_COUNT = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 创建图片缓冲区
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2. 获取画笔
        Graphics g = image.getGraphics();
        // 3. 设置背景色
        g.setColor(getRandomColor(200, 250));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // 4. 设置边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
        // 5. 绘制干扰线
        Random random = new Random();
        for (int i = 0; i < LINE_COUNT; i++) {
            g.setColor(getRandomColor(160, 200));
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }
        // 6. 生成随机验证码
        StringBuilder code = new StringBuilder();
        g.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < CODE_LENGTH; i++) {
            // 随机字符
            char c = CHAR_SET.charAt(random.nextInt(CHAR_SET.length()));
            code.append(c);
            // 随机颜色
            g.setColor(new Color(random.nextInt(80), random.nextInt(180), random.nextInt(255)));
            // 随机位置（带轻微偏移，防止重叠）
            g.drawString(String.valueOf(c), 25 * i + 10, 25 + random.nextInt(10));
        }
        // 7. 将验证码存入Session（后端验证用）
        HttpSession session = request.getSession();
        session.setAttribute("captchaCode", code.toString());
        // 8. 关闭画笔
        g.dispose();
        // 9. 输出图片（JPEG格式）
        response.setContentType("image/jpeg");
        javax.imageio.ImageIO.write(image, "jpeg", response.getOutputStream());
    }

    // 生成随机颜色（min~max之间）
    private Color getRandomColor(int min, int max) {
        Random random = new Random();
        int r = min + random.nextInt(max - min);
        int g = min + random.nextInt(max - min);
        int b = min + random.nextInt(max - min);
        return new Color(r, g, b);
    }
}