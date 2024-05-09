package com.equipment.shop.models;

import com.liqpay.LiqPay;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class OrderGenerator {
    private int num;

    private final String PUBLIC_KEY;
    private final String PRIVATE_KEY;

    {
        try {
            Properties properties = new Properties();
            InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
            properties.load(resourceStream);
            PUBLIC_KEY = properties.getProperty("PUBLIC_KEY");
            PRIVATE_KEY = properties.getProperty("PRIVATE_KEY");
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\vlads\\Desktop\\shop\\src\\main\\resources\\id.txt"));
            num = Integer.valueOf(reader.readLine());
            reader.close();
            resourceStream.close();
        } catch (NullPointerException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    private int getId() {
        try {
            int result = num++;
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\vlads\\Desktop\\shop\\src\\main\\resources\\id.txt", false));
            writer.write(String.valueOf(num));
            writer.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String createPaymentFormHtml(double uah) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "pay");
        params.put("amount", String.valueOf(uah));
        params.put("currency", "UAH");
        params.put("description", "Оплата обраних товарів");
        params.put("order_id", String.valueOf(getId()));
        params.put("version", "3");
        params.put("sandbox", "1");
        params.put("result_url", "http://localhost:8080/goods");
        params.put("server_url", "http://localhost:8080/order/payment");
        LiqPay liqpay = new LiqPay(PUBLIC_KEY, PRIVATE_KEY);
        return liqpay.cnb_form(params);
    }
}
