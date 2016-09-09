package com.example.administrator.lepaiyizhu;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/8/27 0027.
 */
public class LruCacheUtils {
    private static LruCacheUtils lruCacheUtils;
    private DiskLruCache diskLruCache;
    private LruCache<String, Bitmap> lruCache;
    private Context context;

    private LruCacheUtils() {
    }

    //通过该方法的到对象
    public static LruCacheUtils getLruCacheUtils() {
        if (lruCacheUtils == null) {
            lruCacheUtils = new LruCacheUtils();
        }
        return lruCacheUtils;
    }

    //通过他打开DiskLruCache
    public void open(Context context, String disk_cache_subdir, int disk_cache_size) {
        try {
            this.context = context;
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int memoryClass = manager.getMemoryClass();
            lruCache = new LruCache<>(memoryClass / 8 * 1024 * 1024);
            diskLruCache = DiskLruCache.open(getCacheDir(disk_cache_subdir), 1, 1, disk_cache_size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //得到缓存路径
    public File getCacheDir(String name) {
        String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED || !Environment.isExternalStorageRemovable() ? context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
        name = cachePath + File.separator + name;
        System.out.println(name);
        return new File(name);

    }

    //通过他计算MD5图片地址
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(key.getBytes());
            cacheKey = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    //计算MD5方法
    public String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //压缩图片的方法
    public static Bitmap decodeSampledBitmap(byte[] bytes, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    //计算压缩比例
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    //添加到一级缓存中去
    public void addBitmapToCache(String url, Bitmap bitmap) {
        String key = hashKeyForDisk(url);
        if (getBitmapFromCache(key) == null) {
            System.out.println("key====" + key);
            System.out.println("bitmap====" + bitmap);
            lruCache.put(key, bitmap);
        }
    }

    //从一级缓存中得到图片
    public Bitmap getBitmapFromCache(String url) {
        String key = hashKeyForDisk(url);
        return lruCache.get(key);
    }

    // 从网上下载图片
    public boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //添加到磁盘缓存中去
    public Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight)
            throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if (diskLruCache == null) {
            return null;
        }
        String key = hashKeyForDisk(url);
        DiskLruCache.Editor editor = diskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(0);
            if (downloadUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            diskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    // 从磁盘缓存处获取图片
    public Bitmap loadBitmapFromDiskCache(String url, int reqWidth,
                                          int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.d("hyh", "load bitmap from UI Thread, it's not recommended!");
        }
        if (diskLruCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyForDisk(url);
        DiskLruCache.Snapshot snapShot = diskLruCache.get(key);
        if (snapShot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(0);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = decodeSampledBitmapFromFileDescriptor(fileDescriptor,
                    reqWidth, reqHeight);
            if (bitmap != null) {
                addBitmapToCache(key, bitmap);
            }
        }
        return bitmap;
    }

    //压缩图片到一级缓存中去
    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize1(options, reqWidth,
                reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    //压缩比例
    public int calculateInSampleSize1(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
