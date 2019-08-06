package com.xkx.util;

import com.xkx.Exception.DbhelperException;

import java.io.*;

public class FileUtil {


    public static String ReadFile(String Path) throws DbhelperException {
        BufferedReader reader = null;
        String laststr = "";
        try {
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream(Path);
            InputStreamReader inputStreamReader = null;
            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while((tempString = reader.readLine()) != null){
                laststr += tempString;
            }
            reader.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DbhelperException("文件不存在:"+Path);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr;
    }


    public static BufferedReader buildBufferedReader(String filePath) throws DbhelperException {
        BufferedReader reader = null;
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader;
            inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DbhelperException("未找到该文件:"+filePath);
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return reader;
    }


    public static int getTotalRowNumber(String filePath){
        BufferedReader reader = null;
        int lineNumber = 0;
        try{
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while((tempString = reader.readLine()) != null){
                lineNumber++;
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lineNumber;
    }
}
