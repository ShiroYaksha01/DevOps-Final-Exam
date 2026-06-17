package net.orderzone.idcard.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import net.orderzone.idcard.model.BarcodeType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class CodeService {

    public String generateQRCodeBase64(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Could not generate QR code", e);
        }
    }

    public String generateBarcodeBase64(String text, BarcodeType type, int width, int height) {
        BarcodeFormat format = type == BarcodeType.EAN_13 ? BarcodeFormat.EAN_13 : BarcodeFormat.CODE_128;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, format, width, height);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Could not generate Barcode", e);
        }
    }
}
