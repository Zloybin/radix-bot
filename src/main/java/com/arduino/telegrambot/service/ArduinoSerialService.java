package com.arduino.telegrambot.service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@Slf4j
public class ArduinoSerialService {

    @Value("${arduino.port}")
    private String portName;

    @Value("${arduino.baud-rate}")
    private int baudRate;

    private SerialPort serialPort;
    private Consumer<String> messageListener;

    @PostConstruct
    public void init() {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(baudRate);

        if (serialPort.openPort()) {
            log.info("✅ Порт {} успешно открыт", portName);

            serialPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    byte[] newData = event.getReceivedData();
                    String message = new String(newData).trim();

                    if (!message.isEmpty()) {
                        log.info("📥 От Arduino: {}", message);
                        if (messageListener != null) {
                            messageListener.accept(message);
                        }
                    }
                }
            });
        } else {
            log.error("❌ Не удалось открыть порт {}. Проверьте настройки.", portName);
        }
    }

    public void sendCommand(String command) {
        if (serialPort != null && serialPort.isOpen()) {
            String msg = command + "\n";
            serialPort.writeBytes(msg.getBytes(), msg.length());
            log.info("📤 Отправлено в Arduino: {}", command);
        } else {
            log.warn("⚠️ Порт закрыт, команда не отправлена: {}", command);
        }
    }

    public void setMessageListener(Consumer<String> listener) {
        this.messageListener = listener;
    }

    @PreDestroy
    public void close() {
        if (serialPort != null) {
            serialPort.closePort();
            log.info("🔌 Порт закрыт");
        }
    }
}