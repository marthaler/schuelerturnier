/**
 * Apache License 2.0
 */
package ch.emad.business.schuetu.picture;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Kodieren und dekodieren von QR Codes
 *
 * @author marthaler.worb@gmail.com
 * @since 1.2.7
 */
public final class BarcodeUtil {

    private static final Logger LOG = Logger.getLogger(BarcodeUtil.class);

    private static final Map<DecodeHintType, Object> HINTS;
    private static final Map<DecodeHintType, Object> HINTS_PURE;

    static {
        HINTS = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
        HINTS_PURE = new EnumMap<DecodeHintType, Object>(HINTS);
        HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
    }

    private BarcodeUtil() {

    }

    public static String decode(BufferedImage image) {

        Reader reader = new MultiFormatReader();
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        Collection<Result> results = new ArrayList<Result>(1);

        try {
            // Look for multiple barcodes
            MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(reader);
            Result[] theResults = multiReader.decodeMultiple(bitmap, HINTS);
            if (theResults != null) {
                results.addAll(Arrays.asList(theResults));
            }

            if (results.isEmpty()) {
                // Look for pure barcode
                Result theResult = reader.decode(bitmap, HINTS_PURE);
                if (theResult != null) {
                    results.add(theResult);
                }
            }

            if (results.isEmpty()) {
                // Look for normal barcode in photo
                Result theResult = reader.decode(bitmap, HINTS);
                if (theResult != null) {
                    results.add(theResult);
                }
            }

            if (results.isEmpty()) {
                // Try again with other binarizer
                BinaryBitmap hybridBitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result theResult = reader.decode(hybridBitmap, HINTS);
                if (theResult != null) {
                    results.add(theResult);
                }
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        for (Result result : results) {
            return result.getText();
        }

        return "";
    }


    public static BufferedImage encode(String text) {

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix;
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);

            matrix = writer.encode(text, BarcodeFormat.QR_CODE, 256, 256, hints);
            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (WriterException e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }

}

