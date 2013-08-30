package com.googlecode.mad_schuelerturnier.business.picture;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.util.*;

public class BarcodeUtil {


    // No real reason to let people upload more than a 2MB image
    private static final long MAX_IMAGE_SIZE = 2000000L;
    // No real reason to deal with more than maybe 8.3 megapixels
    private static final int MAX_PIXELS = 1 << 23;
    private static final byte[] REMAINDER_BUFFER = new byte[8192];
    private static final Map<DecodeHintType, Object> HINTS;
    private static final Map<DecodeHintType, Object> HINTS_PURE;

    static {
        HINTS = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
        HINTS_PURE = new EnumMap<DecodeHintType, Object>(HINTS);
        HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
    }

    public static String decode(BufferedImage image) {

        Reader reader = new MultiFormatReader();
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        Collection<Result> results = new ArrayList<Result>(1);
        ReaderException savedException = null;

        try {

            try {
                // Look for multiple barcodes
                MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(reader);
                Result[] theResults = multiReader.decodeMultiple(bitmap, HINTS);
                if (theResults != null) {
                    results.addAll(Arrays.asList(theResults));
                }
            } catch (ReaderException re) {
                savedException = re;
            }

            if (results.isEmpty()) {
                try {
                    // Look for pure barcode
                    Result theResult = reader.decode(bitmap, HINTS_PURE);
                    if (theResult != null) {
                        results.add(theResult);
                    }
                } catch (ReaderException re) {
                    savedException = re;
                }
            }

            if (results.isEmpty()) {
                try {
                    // Look for normal barcode in photo
                    Result theResult = reader.decode(bitmap, HINTS);
                    if (theResult != null) {
                        results.add(theResult);
                    }
                } catch (ReaderException re) {
                    savedException = re;
                }
            }

            if (results.isEmpty()) {
                try {
                    // Try again with other binarizer
                    BinaryBitmap hybridBitmap = new BinaryBitmap(new HybridBinarizer(source));
                    Result theResult = reader.decode(hybridBitmap, HINTS);
                    if (theResult != null) {
                        results.add(theResult);
                    }
                } catch (ReaderException re) {
                    savedException = re;
                }
            }

        } catch (RuntimeException re) {
            re.printStackTrace();
        }


        for (Result result : results) {

            return result.getText();

        }


        return "";
    }


    public static BufferedImage encode(String text) {

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {


            Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1); /* default = 4 */

            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 256, 256, hints);
            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }

}

