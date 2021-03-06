import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.ImageReader;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import org.apache.log4j.Logger;

import javax.xml.transform.Result;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

public final class BarcodeDecoder {

    private static final Logger LOG = Logger.getLogger(BarcodeDecoder.class);

    private static final Map<DecodeHintType, Object> HINTS;
    private static final Map<DecodeHintType, Object> HINTS_PURE;

    static {
        HINTS = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
        HINTS_PURE = new EnumMap<DecodeHintType, Object>(HINTS);
        HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
    }

    private BarcodeDecoder() {

    }

    public static String decode(String file) {

        BufferedImage image = null;
        try {
            image = ImageReader.readImage(new File(file));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        Reader reader = new MultiFormatReader();
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        Collection<Result> results = new ArrayList<Result>(1);

        try {

            multipleBarcode(reader, bitmap, results);

            pureBarcode(reader, bitmap, results);

            normalBarcode(reader, bitmap, results);

            withAnotherBinarizer(reader, source, results);

        } catch (RuntimeException e) {
            LOG.error(e.getMessage(), e);
        }

        for (Result result : results) {
            return result.getText();
        }
        return "";
    }

    private static void withAnotherBinarizer(Reader reader, LuminanceSource source, Collection<Result> results) {
        if (results.isEmpty()) {
            try {
                // Try again with other binarizer
                BinaryBitmap hybridBitmap = new BinaryBitmap(new HybridBinarizer(source));
                Result theResult = reader.decode(hybridBitmap, HINTS);
                if (theResult != null) {
                    results.add(theResult);
                }
            } catch (ReaderException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private static void multipleBarcode(Reader reader, BinaryBitmap bitmap, Collection<Result> results) {
        try {
            // Look for multiple barcodes
            MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(reader);
            Result[] theResults = multiReader.decodeMultiple(bitmap, HINTS);
            if (theResults != null) {
                results.addAll(Arrays.asList(theResults));
            }
        } catch (ReaderException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private static void pureBarcode(Reader reader, BinaryBitmap bitmap, Collection<Result> results) {
        if (results.isEmpty()) {
            try {
                // Look for pure barcode
                Result theResult = reader.decode(bitmap, HINTS_PURE);
                if (theResult != null) {
                    results.add(theResult);
                }
            } catch (ReaderException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    private static void normalBarcode(Reader reader, BinaryBitmap bitmap, Collection<Result> results) {
        if (results.isEmpty()) {
            try {
                // Look for normal barcode in photo
                Result theResult = reader.decode(bitmap, HINTS);
                if (theResult != null) {
                    results.add(theResult);
                }
            } catch (ReaderException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
