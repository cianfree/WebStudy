package edu.zhku.cn.common.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * ��ά�����ɹ�����
 *
 * @author ���
 * @version 0.1
 * @time 2016��1��19�� ����2:07:58
 * @since 0.1
 */
public class BxtpwQRCodeUtil {

    /**
     * ��ɫ
     */
    private static final int WHITE = 0xffffffff;

    /**
     * ���ɶ�ά��,����true��ʾ���ɳɹ�
     *
     * @param bxtpwQRCode
     * @return
     * @author ���
     * @time 2016��1��26�� ����11:01:55
     * @version 0.1
     * @since 0.1
     */
    public boolean encodeQRCode(BxtpwQRCode bxtpwQRCode) {
        if (bxtpwQRCode == null) {
            throw new RuntimeException("��ά�������Ϊ��");
        }
        if (bxtpwQRCode.getContent() == null) {
            throw new RuntimeException("��ά�����ݲ���Ϊ��!");
        }

        // ��ά�뱣��·����֤
        String savePath = bxtpwQRCode.getSavePath();
        if (StringUtils.isBlank(savePath)) {
            throw new RuntimeException("��ά�뱣��·������Ϊ��!");
        } else {
            File saveFile = new File(savePath);
            if (saveFile.exists()) {
                throw new RuntimeException("��ά�뱣��·���Ѵ���!");
            }
            if (bxtpwQRCode.getFormatType() == null) {
                throw new RuntimeException("��ά�뱣��·����ʽ����ȷ");
            }
        }

        // ��ά��logo·����֤
        String logoPath = bxtpwQRCode.getLogoPath();
        if (StringUtils.isNotBlank(logoPath)) {
            File logoFile = new File(logoPath);
            if (!logoFile.isFile() || !logoFile.exists()) {
                throw new RuntimeException("��ά��logo·���쳣!");
            }
        }
        try {
            // ���ö�ά������ݺ��Զ����ȸ߶�
            BitMatrix bitMatrix = new MultiFormatWriter().encode(bxtpwQRCode.getContent(), BarcodeFormat.QR_CODE, bxtpwQRCode.getSize(), bxtpwQRCode.getSize(),
                    getEncodeHintType());

            // ���ñ߿��С��ͼƬ��С����һ���̶ȵ���С
            Integer borderSize = bxtpwQRCode.getBorderSize();
            if (borderSize != null && borderSize > 0) {
                bitMatrix = updateBorderSize(bitMatrix, bxtpwQRCode.getBorderSize());
            }
            Path path = FileSystems.getDefault().getPath(savePath);

            // ����ָ�����͵Ķ�ά��ͼ��
            writeToPath(bitMatrix, bxtpwQRCode, path);
            return decodeQRCode(path.toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("���ɶ�ά���쳣!�Ѵ��ڶ�ά��ľ�����!");
        }
    }

    /**
     * ����logo�����ɶ�ά��
     *
     * @param bitMatrix
     * @param bxtpwQRCode
     * @param path        ���ɶ�ά��ͼƬ��·��
     * @throws IOException
     * @author ���
     * @time 2016��1��21�� ����2:47:21
     * @version 0.1
     * @since 0.1
     */
    private void writeToPath(BitMatrix bitMatrix, BxtpwQRCode bxtpwQRCode, Path path) throws IOException {
        if (bxtpwQRCode == null) {
            throw new RuntimeException("bxtpwQRCode����ʵ�������Ϊ��!");
        }

        // ����������ɫ
        BufferedImage bufferedImage = toBufferedImage(bitMatrix, bxtpwQRCode.getHexColorType());
        Graphics2D gs = bufferedImage.createGraphics();

        // logo��Ϊ��
        if (StringUtils.isNotBlank(bxtpwQRCode.getLogoPath())) {
            BufferedImage logo = ImageIO.read(new File(bxtpwQRCode.getLogoPath()));

            // logoͼ��������
            LogoConfig logoConfig;
            if (bxtpwQRCode.getLogoConfig() != null) {
                logoConfig = bxtpwQRCode.getLogoConfig();
            } else {
                logoConfig = new LogoConfig();
            }

            int widthLogo = bufferedImage.getWidth() / logoConfig.getLogoPart();
            int heightLogo = bufferedImage.getHeight() / logoConfig.getLogoPart();

            int x;
            int y;
            // ����Logoλ��
            if (BxtpwQRCode.LogoPosition.RIGHT_CORNER.equals(bxtpwQRCode.getLogoPosition())) {
                int borderSize = bxtpwQRCode.getBorderSize();
                x = bufferedImage.getWidth() - widthLogo - borderSize;
                y = bufferedImage.getHeight() - heightLogo - borderSize;
            } else {
                x = (bufferedImage.getWidth() - widthLogo) / 2;
                y = (bufferedImage.getHeight() - heightLogo) / 2;
            }

            gs.drawImage(logo, x, y, widthLogo, heightLogo, null);
            gs.drawRoundRect(x, y, widthLogo, heightLogo, 15, 15);
            gs.setStroke(new BasicStroke(logoConfig.getBorder()));
            gs.setColor(logoConfig.getBorderColor());
            gs.drawRect(x, y, widthLogo, heightLogo);
        }

        gs.dispose();

        String format = bxtpwQRCode.getFormatType().getValue();
        if (!ImageIO.write(bufferedImage, format, path.toFile())) {
            throw new IOException("·��" + path.toString() + "����д��" + format + "���͵�ͼƬ!");
        }
    }

    /**
     * ��BitMatrixת��ΪBufferedImage,�����ö�ά��������ɫ
     *
     * @param matrix
     * @param mainColor ������ɫ
     * @return
     * @author ���
     * @time 2016��1��21�� ����2:49:17
     * @version 0.1
     * @since 0.1
     */
    private BufferedImage toBufferedImage(BitMatrix matrix, BxtpwQRCode.HexColorType mainColor) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? mainColor.getValue() : WHITE);
            }
        }
        return image;
    }

    /**
     * ���ɶ�ά��ʱ�ĸ�ʽ����
     *
     * @return
     */
    private Map<EncodeHintType, Object> getEncodeHintType() {
        // ��������QR��ά�����
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // ����QR��ά��ľ�����HΪ��߼��𣩾��弶����Ϣ
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // ���ñ��뷽ʽ
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        return hints;
    }

    /**
     * �޸ı߿���
     *
     * @param matrix ԭͼ��
     * @param margin �ױ߱߿��С
     * @return
     * @author ���
     * @time 2016��1��20�� ����3:09:13
     * @version 0.1
     * @since 0.1
     */
    private BitMatrix updateBorderSize(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle(); // ��ȡ��ά��ͼ��������
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // �����Զ���߿������µ�BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) { // ѭ��������ά��ͼ�����Ƶ��µ�bitMatrix��
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    /**
     * ������ά��
     *
     * @param filePath
     * @return
     * @throws IOException
     * @throws NotFoundException
     * @author ���
     * @time 2016��1��22�� ����4:00:54
     * @version 0.1
     * @since 0.1
     */
    public boolean decodeQRCode(String filePath) throws IOException, NotFoundException {
        if (StringUtils.isNotBlank(filePath)) {
            BufferedImage image;
            image = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            return StringUtils.isNotBlank(result.getText()) ? true : false;
        }
        return false;
    }

    /**
     * ��ȡͼƬ��ʽ
     *
     * @param imgPath
     * @return
     * @author ���
     * @time 2016��1��26�� ����10:30:26
     * @version 0.1
     * @since 0.1
     */
    public static String getImgFormatType(String imgPath) {
        if (StringUtils.isNotBlank(imgPath)) {
            String[] pathArr = StringUtils.split(imgPath, ".");
            if (pathArr.length > 1) {
                return pathArr[pathArr.length - 1];
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * �����õ�������
     *
     * @param args
     * @author ���
     * @time 2016��1��26�� ����2:21:13
     * @version 0.1
     * @since 0.1
     */
    public static void main(String[] args) {

        BxtpwQRCode bxtpwQRCode = new BxtpwQRCode();
        bxtpwQRCode.setContent("��λ�ֵ������Ϻ�");
        bxtpwQRCode.setLogoPath("C:\\Users\\Arvin\\Desktop\\QQ��ͼ20160225145931.png");
        bxtpwQRCode.setSize(300);
        bxtpwQRCode.setBorderSize(10);
        bxtpwQRCode.setHexColorType(BxtpwQRCode.HexColorType.BLACK);
        bxtpwQRCode.setLogoPosition(BxtpwQRCode.LogoPosition.CENTER);
        bxtpwQRCode.setSavePath("C:\\Users\\Arvin\\Desktop\\export.png");
        // logoͼ������
        LogoConfig logoConfig = new LogoConfig(Color.PINK, 5);
        bxtpwQRCode.setLogoConfig(logoConfig);

        BxtpwQRCodeUtil qcu = new BxtpwQRCodeUtil();
        boolean result = qcu.encodeQRCode(bxtpwQRCode);
        System.err.println("result:" + result);
    }
}
