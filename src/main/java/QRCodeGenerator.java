import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * @author Volodymyr Burtsev at 13.09.2018 1:50
 */
public class QRCodeGenerator {

    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            noArgs(args);
        }

        createQR(args, true);


    }

    private static void createQR(String[] args, boolean showInfo) {
        // set text to encode
        String text = args[0];
        if (showInfo) System.out.println("The string to encode : " + text);

        // set size of image
        int size = 300;
        try {
            size = Integer.valueOf(args[1]);
        } catch (Exception e) {}
        if (showInfo) System.out.println("Size : " + size + "px");

        // set margins of image
        int margin = 20;
        try {
            margin = Integer.valueOf(args[2]);
        } catch (Exception e) {}
        if (showInfo) System.out.println("Margin : " + margin + "px");

        // set error-level correction
        int errLvl = 0;
        try {
            errLvl = " lhq".indexOf(args[3].toLowerCase());
        } catch (Exception e) {}
        if (errLvl < 0) {
            errLvl = 0;
        }
        if (showInfo) System.out.println("Error-level is : " + errLvl);

        String fileName = text + ".png";
        try {
            fileName = args[4] + ".png";
            if (showInfo) System.out.println("New filename : " + fileName);
        } catch (Exception e) {
            if (showInfo) System.out.println("Used default file naming. The name is : " + fileName);
        }

        try {
            generateQRCodeImage(text, size, size, "./" + fileName);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }

    private static void noArgs(String[] args) {
        System.out.println("There is nothing to create...");
        System.out.println("Use the following arguments mask with separator of spaces:");
        System.out.println(" - string to encode (will be used as filename)");
        System.out.println(" - image size");
        System.out.println(" - margins for image");
        System.out.println(" - Error-level correction : {L|M|Q|H} [not implemented yet]");
        System.out.println(" - specified filename");
        System.out.println();
        System.out.println("Now you can create your QR manually or type 'Q' to exit.");

        loopCreate();
    }

    private static void loopCreate() {
        Scanner line = new Scanner(System.in);
        System.out.print("Enter text: ");
        while (line.hasNext()) {
            String s = line.nextLine();
            if (s.equalsIgnoreCase("q") || s.isEmpty()) System.exit(0);
            for (String text: s.split(" ")) {
                createQR(new String[]{text}, false);
            }
            System.out.print("Enter text: ");
        }
    }
}