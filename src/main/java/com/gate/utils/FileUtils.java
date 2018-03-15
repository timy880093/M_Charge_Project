package com.gate.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Title：FileUtils.java
 * </p>
 * <p>
 * Description：文件操作工具類
 * </p>
 * <p>
 * Copyright：Copyright (c) 2004 TECHMORE,Inc
 * </p>
 * <p>
 * Company：TECHMORE,Inc
 * </p>
 * 
 * @author Jason 2004-8-11
 * @version 1.0
 */
@Component
public class FileUtils {

	/**
	 * 複製文件
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile,File targetFile)throws IOException{
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff=new BufferedInputStream(input);
		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff=new BufferedOutputStream(output);
		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len =inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();
		//关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	public static void copyFile(String sourceFilePath,String targetFilePath)throws IOException{
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(new File(sourceFilePath));
		BufferedInputStream inBuff=new BufferedInputStream(input);
		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(new File(targetFilePath));
		BufferedOutputStream outBuff=new BufferedOutputStream(output);
		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len =inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();
		//关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}

	/**
	 * 取得文件類型
	 * <p>
	 * <code>getFileType</code>
	 * </p>
	 * 
	 * @param fileName
	 * @return
	 * @author Jason 2004-9-5
	 * @since 1.0
	 */
	public static String getFileType(String fileName) {
		if (fileName == null || fileName.indexOf(".") < 0
				|| fileName.lastIndexOf(".") == fileName.length() - 1) {
			return null;
		}

		return fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length()).toLowerCase();
	}

	/**
	 * 取得文件名
	 * <p>
	 * <code>getFileName</code>
	 * </p>
	 * 
	 * @param filePath
	 * @return
	 * @author Jason 2004-9-19
	 * @since 1.0
	 */
	public static String getFileName(String filePath) {
		if (filePath == null || filePath.indexOf("\\") < 0
				|| filePath.lastIndexOf("\\") == filePath.length() - 1)
			return null;

		return filePath.substring(filePath.lastIndexOf("\\") + 1, filePath
				.length());
	}

	/**
	 * 取得文件內容
	 * <p>
	 * <code>getFileContent</code>
	 * </p>
	 * 
	 * @param inputStream
	 * @param encoding
	 * @return
	 * @author Jason 2005-4-4
	 * @since 1.0
	 */
	public static String getFileContent(InputStream inputStream, String encoding) {

		if (encoding == null) {
			encoding = "UTF-8";
		}

		BufferedReader reader = null;
		InputStreamReader streamReader = null;

		try {
			streamReader = new InputStreamReader(inputStream, encoding);
			reader = new BufferedReader(streamReader);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		String line;
		String content = "";

		try {
			while ((line = reader.readLine()) != null) {
				content += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				reader.close();
				streamReader.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return content;
	}

	/**
	 * 取得文件內容
	 * <p>
	 * <code>getFileContent</code>
	 * </p>
	 * 
	 * @param path
	 * @param encoding
	 * @return
	 * @author Jason 2005-4-4
	 * @since 1.0
	 */
	public static String getFileContent(String path, String encoding) {

		if (encoding == null) {
			encoding = "UTF-8";
		}

		BufferedReader reader = null;
		InputStreamReader streamReader = null;
		try {
			streamReader = new InputStreamReader(new FileInputStream(path),
					encoding);
			reader = new BufferedReader(streamReader);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		String line;
		String content = "";

		try {
			while ((line = reader.readLine()) != null) {
				content += line + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				reader.close();
				streamReader.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return content;
	}

	/**
	 * 取得html中<body>與</body>之間的內容
	 * <p>
	 * <code>getHtmlContent</code>
	 * </p>
	 * 
	 * @param stringFile
	 * @return
	 * @author ziven 2005-4-4
	 * @since 1.0
	 */
	public static String getHtmlContent(String stringFile) {

		String htmlContent = "";
		String body = "";

		htmlContent = getFileContent(stringFile, "UTF-8");
		String pattern = "(<[.[^<>]]*body[.[^<>]]*>([.[^$]]*)</[.[^<>]]*body[.[^<>]]*>)";

		Pattern pBody = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher mBody = pBody.matcher(htmlContent);
		while (mBody.find()) {
			body = mBody.group(2);
		}

		return body;
	}

	/**
	 * 取得html中<body>與</body>之間的內容
	 * <p>
	 * <code>getHtmlContent</code>
	 * </p>
	 * 
	 * @param inputStream
	 * @return
	 * @author ziven 2005-4-4
	 * @since 1.0
	 */
	public static String getHtmlContent(InputStream inputStream) {

		String htmlContent = "";
		String body = "";

		htmlContent = getFileContent(inputStream, "UTF-8");
		String pattern = "(<[.[^<>]]*body[.[^<>]]*>([.[^$]]*)</[.[^<>]]*body[.[^<>]]*>)";

		Pattern pBody = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher mBody = pBody.matcher(htmlContent);
		while (mBody.find()) {
			body = mBody.group(2);
		}

		return body;
	}

	/**
	 * 取得文件名的後綴
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getSuffixName(String fileName) {

		int index = fileName.lastIndexOf(".");
		if (index < 0) {
			return fileName;
		} else {
			return fileName.substring(index);
		}
	}

	private static String[] imageType = { "jpg", "bmp", "gif", "png", "ico" };

	public static boolean isImageFile(String fileName) {
		boolean f = false;
		try {
			String suffix = getSuffixName(fileName);
			suffix = suffix.toLowerCase();
			for (int i = 0; i < imageType.length; i++) {
				if (suffix.indexOf(imageType[i])>0) {
					f = true;
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return f;
	}

    public String readFromJARFile(String filename)
            throws IOException
    {
        InputStream is = this.getClass().getResourceAsStream(filename);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null)
        {
            sb.append(line);
        }
        br.close();
        isr.close();
        is.close();
        return sb.toString();
    }

    public void readFromJARFileXML(String filename)
            throws IOException
    {
        SAXReader saxReader = new SAXReader();
        Document document;
        try {
            document = saxReader.read(this.getClass().getResourceAsStream("test.txt"));
            Element incomingForm = document.getRootElement();
            String ss = document.asXML().toString();
            System.out.println("ss="+ss);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

	public void writeTextFile(File outputFile,List<String> textList) throws IOException {
		if(textList.size()>0){
			FileOutputStream fos = new FileOutputStream(outputFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for(String text : textList){
				bw.write(text);
				bw.newLine();
			}
			bw.close();
			fos.close();
		}
	}
}
