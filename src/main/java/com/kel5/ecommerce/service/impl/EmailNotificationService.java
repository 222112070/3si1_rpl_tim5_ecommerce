package com.kel5.ecommerce.service.impl;

import com.kel5.ecommerce.entity.Order;
import com.kel5.ecommerce.entity.OrderItem;
import com.kel5.ecommerce.service.OrderObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements OrderObserver {

    @Autowired
    private JavaMailSender mailSender;

    private static final String SELLER_EMAIL = "jimbungindustrial@gmail.com";

    @Override
    public void onOrderCreated(Order order) {
        // Mengirim email ke pembeli
        StringBuilder message = new StringBuilder();
        message.append("*PESANAN BARU*\n")
                .append("----‐-------------------------------------\n")
                .append("*RINCIAN PEMESAN*\n")
                .append("\nNama Pemesan : ")
                .append(order.getUser().getName())
                .append("\nEmail Pemesan : ")
                .append(order.getUser().getEmail())
                .append("\nNomor WhatApp : ")
                .append(order.getWhatsapp())
                .append("\nTanggal Pemesanan : ")
                .append(order.getOrderDate())
                .append("\nPerkiraan Harga : Rp. ")
                .append(order.getTotalAmount())
                .append("\nAlamat Pesanan :  ")
                .append(order.getAddress())
                .append("\n\n")
                .append("*RINCIAN PESANAN*\n");

        int count = 1;
        for (OrderItem item : order.getOrderItems()) {
            message.append("")
                    .append("Nama Produk : ")
                    .append(item.getProduct().getName())
                    .append("\n Ukuran : ")
                    .append(item.getSize())
                    .append("\n Jumlah : ")
                    .append(item.getQuantity())
                    .append("\n\n");
        }

        message.append("\nCatatan Pemesan : ")
                .append(order.getNotes())
                .append("\n----‐-------------------------------------\n")
                .append("Terima kasih");
        
        sendEmail(order.getUser().getEmail(), "Order Confirmation",
                message.toString());
        // Mengirim email ke penjual
        sendEmail(SELLER_EMAIL, "New Order Received",
               message.toString());
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@example.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

