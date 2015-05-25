/**
 * Copyright (c) 2013-2014, Rinc Liu (http://rincliu.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rincliu.library.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.os.Environment;
import android.util.Base64;
import android.util.Base64OutputStream;

public class RLFileUtil {

    private static RLFileUtil fileUtil;

    private String rootPath = Environment.getExternalStorageDirectory().getPath();

    /**
     * @return
     */
    public synchronized static RLFileUtil getInstance() {
        if (fileUtil == null) {
            fileUtil = new RLFileUtil();
        }
        return fileUtil;
    }

    /**
     * @return
     */
    public static boolean hasExternalStorage() {
        return hasSDCard() || !RLSysUtil.isExternalStorageRemovable();
    }

    /**
     * @return
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * @param name
     * @param content
     */
    public void saveFile(final String name, final String content) {
        new Thread() {
            @Override
            public void run() {
                FileOutputStream os = null;
                OutputStreamWriter osw = null;
                BufferedWriter bw = null;
                try {
                    os = new FileOutputStream(rootPath + name);
                    osw = new OutputStreamWriter(os);
                    bw = new BufferedWriter(osw);
                    bw.write(content);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (bw != null) {
                            bw.flush();
                        }
                        if (osw != null) {
                            osw.flush();
                        }
                        if (bw != null) {
                            bw.close();
                        }
                        if (osw != null) {
                            osw.close();
                        }
                        if (os != null) {
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    /**
     * @param name
     * @return
     */
    public String readFile(String name) {
        FileInputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        File file = new File(rootPath + name);
        if (!file.exists()) {
            return null;
        }
        StringBuffer res = new StringBuffer();
        try {
            is = new FileInputStream(rootPath + name);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String x = null;
            while ((x = br.readLine()) != null) {
                res.append(x);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res.toString();
    }

    /**
     * @param filename
     * @param content
     */
    public void write2Sd(String filename, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(content);
            bw.flush();
            osw.flush();
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filename
     * @return
     */
    public String readFromSd(String filename) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer res = new StringBuffer();
            String x = null;
            while ((x = br.readLine()) != null) {
                res.append(x);
            }
            br.close();
            isr.close();
            fis.close();
            return res.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param dir
     */
    public static void deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));
            }
        }
        dir.delete();
    }

    /**
     * @param f
     * @return
     */
    public static long getDirectorySize(File f) {
        if (f == null || !f.exists() || !f.isDirectory()) {
            return 0;
        }
        long size = 0;
        File[] flist = f.listFiles();
        if (flist != null) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getDirectorySize(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }
        }
        return size;
    }
    
    /**
     * 
     * @param file
     * @return
     */
    public static String fileToBase64(String file) {
        String result= null;
        try {
            FileInputStream fis = new FileInputStream(new File(file));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Base64OutputStream base64out = new Base64OutputStream(baos, Base64.NO_WRAP);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) >= 0) {
                base64out.write(buffer, 0, len);
            }
            base64out.flush();
            base64out.close();
            /*
             * Why should we close Base64OutputStream before processing the data:
             * http://stackoverflow.com/questions/24798745/android-file-to-base64-using-streaming-sometimes-missed-2-bytes
             */
            result = new String(baos.toByteArray(), "UTF-8");
            baos.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
