package com.zhangchong.toolsapplication.Partner.Camera.QRDecode;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.zhangchong.toolsapplication.Partner.Camera.QRDecode.ZxingCode.DecodeFormatManager;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * Created by Zhangchong on 2015/4/9.
 */
public class DecodeThread extends  Thread{
    public static final String BARCODE_BITMAP = "barcode_bitmap";
    public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";
    private Looper mLooper;
    private DecodeHandle mDecodeHandle;
    private IDecodeCallback mDecoder;

    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType,Object> mHints;

    public DecodeThread(IDecodeCallback decoder){
        mDecoder = decoder;
        //TODO preference
//        if (decodeFormats == null || decodeFormats.isEmpty()) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
//            decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
//            if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_1D, false)) {
//                decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
//            }
//            if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_QR, false)) {
//                decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
//            }
//            if (prefs.getBoolean(PreferencesActivity.KEY_DECODE_DATA_MATRIX, false)) {
//                decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
//            }
//        }

        decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);

        mHints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        mHints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        mHints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, decoder);
    }

    public Handler getHandler() {
        return mDecodeHandle;
    }


    @Override
    public void run() {
        Looper.prepare();
        mDecodeHandle = new DecodeHandle(mDecoder, mHints);
        Looper.loop();
        mLooper = Looper.myLooper();
    }

    public void onDestroy(){
        if(mLooper != null) {
            mLooper.quit();
            mLooper = null;
        }
    }

    public static class DecodeHandle extends Handler{
        public static final int DECODE = 0x001;
        public static final int QUIT = 0x002;
        private volatile boolean running = true;
        private MultiFormatReader multiFormatReader;

        private WeakReference<IDecodeCallback> reference;

        public DecodeHandle(IDecodeCallback callback, Map<DecodeHintType,Object> hints){
            reference = new WeakReference<IDecodeCallback>(callback);
            multiFormatReader = new MultiFormatReader();
            multiFormatReader.setHints(hints);
        }

        @Override
        public void handleMessage(Message msg) {
            if (!running) {
                return;
            }
            if(reference.get() == null)
                return;
            IDecodeCallback callback = reference.get();

            switch (msg.what){
                case DECODE:
                    decode((byte[])msg.obj, msg.arg1, msg.arg2, callback);
                    break;
                case QUIT:
                    running = false;
                    break;
            }
        }

        private void decode(byte[] data, int width, int height, IDecodeCallback callback) {
            if(data == null)
                return;
            long start = System.currentTimeMillis();
            Result rawResult = null;


            Rect scanRect =((QrDecodeFragment)(((DecodeManager)callback).getFragment())).getMaskView().getScanFrame();
            Rect previewRect =((QrDecodeFragment)(((DecodeManager)callback).getFragment())).getMaskView().getPreviewFrame();
            Rect qrRect = QRUtils.calculateQrRect(previewRect, scanRect, new Rect(0,0,width, height));
            PlanarYUVLuminanceSource source = buildLuminanceSource(data, qrRect, width, height);
            if (source != null) {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    rawResult = multiFormatReader.decodeWithState(bitmap);
                } catch (ReaderException re) {
                } finally {
                    multiFormatReader.reset();
                }
            }

            if (rawResult != null) {
                long end = System.currentTimeMillis();
                DecodeResult decodeResult = new DecodeResult();
                decodeResult.rawResult = rawResult;
                bundleThumbnail(source, decodeResult);
                callback.CallDecodeStatus(IDecodeCallback.STATUS_OK, decodeResult);
            } else {
                callback.CallDecodeStatus(IDecodeCallback.STATUS_ERROR, null);
            }
        }

        public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, Rect previewRect, int width, int height) {
            if (previewRect == null) {
                return null;
            }
            // Go ahead and assume it's YUV rather than die.
            return new PlanarYUVLuminanceSource(data, width, height, previewRect.left, previewRect.top,
                    previewRect.width(), previewRect.height(), false);
        }

        //解析扫描的图片
        private static void bundleThumbnail(PlanarYUVLuminanceSource source, DecodeResult decodeResult) {
            int[] pixels = source.renderThumbnail();
            int width = source.getThumbnailWidth();
            int height = source.getThumbnailHeight();
            Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            decodeResult.bytes = out.toByteArray();
            decodeResult.factor = (float) width / source.getWidth();
        }
    }
}
