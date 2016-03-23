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
 * 二维码生成工具类
 *
 * @author 李成
 * @version 0.1
 * @time 2016年1月19日 下午2:07:58
 * @since 0.1
 */
public class BxtpwQRCodeUtil {

    /**
     * 白色
     */
    private static final int WHITE = 0xffffffff;

    /**
     * 生成二维码,返回true表示生成成功
     *
     * @param bxtpwQRCode
     * @return
     * @author 李成
     * @time 2016年1月26日 上午11:01:55
     * @version 0.1
     * @since 0.1
     */
    public boolean encodeQRCode(BxtpwQRCode bxtpwQRCode) {
        if (bxtpwQRCode == null) {
            throw new RuntimeException("二维码对象不能为空");
        }
        if (bxtpwQRCode.getContent() == null) {
            throw new RuntimeException("二维码内容不能为空!");
        }

        // 二维码保存路径验证
        String savePath = bxtpwQRCode.getSavePath();
        if (StringUtils.isBlank(savePath)) {
            throw new RuntimeException("二维码保存路径不能为空!");
        } else {
            File saveFile = new File(savePath);
            if (saveFile.exists()) {
                throw new RuntimeException("二维码保存路径已存在!");
            }
            if (bxtpwQRCode.getFormatType() == null) {
                throw new RuntimeException("二维码保存路径格式不正确");
            }
        }

        // 二维码logo路径验证
        String logoPath = bxtpwQRCode.getLogoPath();
        if (StringUtils.isNotBlank(logoPath)) {
            File logoFile = new File(logoPath);
            if (!logoFile.isFile() || !logoFile.exists()) {
                throw new RuntimeException("二维码logo路径异常!");
            }
        }
        try {
            // 设置二维码的内容和自定义宽度高度
            BitMatrix bitMatrix = new MultiFormatWriter().encode(bxtpwQRCode.getContent(), BarcodeFormat.QR_CODE, bxtpwQRCode.getSize(), bxtpwQRCode.getSize(),
                    getEncodeHintType());

            // 设置边框大小后图片大小将会一定程度的缩小
            Integer borderSize = bxtpwQRCode.getBorderSize();
            if (borderSize != null && borderSize > 0) {
                bitMatrix = updateBorderSize(bitMatrix, bxtpwQRCode.getBorderSize());
            }
            Path path = FileSystems.getDefault().getPath(savePath);

            // 生成指定类型的二维码图像
            writeToPath(bitMatrix, bxtpwQRCode, path);
            return decodeQRCode(path.toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成二维码异常!已大于二维码的纠错级别!");
        }
    }

    /**
     * 加入logo后生成二维码
     *
     * @param bitMatrix
     * @param bxtpwQRCode
     * @param path        生成二维码图片的路径
     * @throws IOException
     * @author 李成
     * @time 2016年1月21日 下午2:47:21
     * @version 0.1
     * @since 0.1
     */
    private void writeToPath(BitMatrix bitMatrix, BxtpwQRCode bxtpwQRCode, Path path) throws IOException {
        if (bxtpwQRCode == null) {
            throw new RuntimeException("bxtpwQRCode参数实体对象不能为空!");
        }

        // 设置主体颜色
        BufferedImage bufferedImage = toBufferedImage(bitMatrix, bxtpwQRCode.getHexColorType());
        Graphics2D gs = bufferedImage.createGraphics();

        // logo不为空
        if (StringUtils.isNotBlank(bxtpwQRCode.getLogoPath())) {
            BufferedImage logo = ImageIO.read(new File(bxtpwQRCode.getLogoPath()));

            // logo图标配置类
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
            // 设置Logo位置
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
            throw new IOException("路径" + path.toString() + "不能写入" + format + "类型的图片!");
        }
    }

    /**
     * 将BitMatrix转化为BufferedImage,并设置二维码主体颜色
     *
     * @param matrix
     * @param mainColor 主体颜色
     * @return
     * @author 李成
     * @time 2016年1月21日 下午2:49:17
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
     * 生成二维码时的格式参数
     *
     * @return
     */
    private Map<EncodeHintType, Object> getEncodeHintType() {
        // 用于设置QR二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别（H为最高级别）具体级别信息
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        return hints;
    }

    /**
     * 修改边框宽度
     *
     * @param matrix 原图像
     * @param margin 白边边框大小
     * @return
     * @author 李成
     * @time 2016年1月20日 下午3:09:13
     * @version 0.1
     * @since 0.1
     */
    private BitMatrix updateBorderSize(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle(); // 获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) { // 循环，将二维码图案绘制到新的bitMatrix中
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    /**
     * 解析二维码
     *
     * @param filePath
     * @return
     * @throws IOException
     * @throws NotFoundException
     * @author 李成
     * @time 2016年1月22日 下午4:00:54
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
     * 获取图片格式
     *
     * @param imgPath
     * @return
     * @author 李成
     * @time 2016年1月26日 上午10:30:26
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
     * 测试用的主函数
     *
     * @param args
     * @author 李成
     * @time 2016年1月26日 下午2:21:13
     * @version 0.1
     * @since 0.1
     */
    public static void main(String[] args) {

        BxtpwQRCode bxtpwQRCode = new BxtpwQRCode();
        bxtpwQRCode.setContent("各位兄弟们早上好");
        bxtpwQRCode.setLogoPath("C:\\Users\\Arvin\\Desktop\\QQ截图20160225145931.png");
        bxtpwQRCode.setSize(300);
        bxtpwQRCode.setBorderSize(10);
        bxtpwQRCode.setHexColorType(BxtpwQRCode.HexColorType.BLACK);
        bxtpwQRCode.setLogoPosition(BxtpwQRCode.LogoPosition.CENTER);
        bxtpwQRCode.setSavePath("C:\\Users\\Arvin\\Desktop\\export.png");
        // logo图标配置
        LogoConfig logoConfig = new LogoConfig(Color.PINK, 5);
        bxtpwQRCode.setLogoConfig(logoConfig);

        BxtpwQRCodeUtil qcu = new BxtpwQRCodeUtil();
        boolean result = qcu.encodeQRCode(bxtpwQRCode);
        System.err.println("result:" + result);
    }
}
