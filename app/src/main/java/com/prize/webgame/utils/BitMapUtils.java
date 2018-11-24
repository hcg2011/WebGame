package com.prize.webgame.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by prize on 2018/11/22.
 */

public class BitMapUtils
{


    /** 保存方法 */
    public static String saveBitmap(Bitmap bm, String h5Url)
    {

        String fileDir = Environment.getExternalStorageDirectory() + "/prize/web_game/img/";

        File dirFile = new File(fileDir);
        if(!dirFile.exists())
            dirFile.mkdirs();

        String fileName = MD5Util.md5(h5Url);
        File f = new File(fileDir, fileName);
        if (f.exists())
        {
            f.delete();
        }
        try
        {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

            return f.getAbsolutePath();

        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
