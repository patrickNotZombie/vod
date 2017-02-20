package com.dcampus.common.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.dcampus.common.config.Global;
import com.google.gson.Gson;

public class KindEditorUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static String baseDir;// CKEditor的根目录   
    private static boolean enabled = false;// 是否开启CKEditor上传   
    private static Map<String, String> allowedExtensions;// 允许的上传文件扩展名   
    private static Map<String, String> deniedExtensions;// 阻止的上传文件扩展名   
    private static SimpleDateFormat dirFormatter;// 目录命名格式:yyyyMM   
    private static SimpleDateFormat fileFormatter;// 文件命名格式:yyyyMMddHHmmssSSS   

	public KindEditorUploadServlet() {
        super();
    }
	
	/**  
     * Servlet初始化方法  
     */  
	public void init() throws ServletException {
		// 格式化目录和文件命名方式
		dirFormatter = new SimpleDateFormat("yyyyMM");
		fileFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		// 获取根目录名称
		baseDir = Global.getConfig("KindEditor_baseDir");
		if (baseDir == null) {
			baseDir = "/KindEditorFile/";
		}
		String realBaseDir = getServletContext().getRealPath(baseDir);
		File baseFile = new File(realBaseDir);
		if (!baseFile.exists()) {
			baseFile.mkdirs();
		}
		// 实例化允许的扩展名和阻止的扩展名
		allowedExtensions = new HashMap<String, String>();
		deniedExtensions = new HashMap<String, String>();
		
		// 从web.xml中读取配置信息
		allowedExtensions.put("file", Global.getConfig("AllowedExtensionsFile"));
		allowedExtensions.put("image", Global.getConfig("AllowedExtensionsImage"));
		allowedExtensions.put("flash", Global.getConfig("AllowedExtensionsFlash"));
		allowedExtensions.put("media", Global.getConfig("AllowedExtensionsMedia"));
		
		deniedExtensions.put("file", Global.getConfig("DeniedExtensionsFile"));
		deniedExtensions.put("image", Global.getConfig("DeniedExtensionsImage"));
		deniedExtensions.put("flash", Global.getConfig("DeniedExtensionsFlash"));
		deniedExtensions.put("media", Global.getConfig("DeniedExtensionsMedia"));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		upload(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		upload(request, response);
	}

	private void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * KindEditor JSP
		 * 
		 * 本JSP程序是演示程序，建议不要直接在实际项目中使用。
		 * 如果您确定直接使用本程序，使用之前请仔细确认相关安全设置。
		 * 
		 */
		
		//文件保存目录路径
		String savePath = getServletContext().getRealPath("/") + baseDir;

		//文件保存目录URL
		String saveUrl  = request.getContextPath() + baseDir;

		//最大文件大小 Bytes
		long maxSize = Global.getLabTableAttachApply();
		
		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();

		if (!ServletFileUpload.isMultipartContent(request)) {
			out.println(getError("请选择文件。"));
			return;
		}
		//检查目录
		File uploadDir = new File(savePath);
		if (!uploadDir.isDirectory()) {
			out.println(getError("上传目录不存在。"));
			return;
		}
		//检查目录写权限
		if (!uploadDir.canWrite()) {
			out.println(getError("上传目录没有写权限。"));
			return;
		}

		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if (!allowedExtensions.containsKey(dirName)) {
			out.println(getError("目录名不正确。"));
			return;
		}
		//创建文件夹
		savePath += dirName + "/";
		saveUrl += dirName + "/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List items = null;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			out.println(getError("上传文件失败。"));
			return;
		}
		Iterator itr = items.iterator();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			String fileName = item.getName();
			long fileSize = item.getSize();
			if (!item.isFormField()) {
				//检查文件大小
				if(item.getSize() > maxSize){
					out.println(getError("上传文件大小超过限制。"));
					return;
				}
				//检查扩展名
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				if(!Arrays.<String>asList(allowedExtensions.get(dirName).split(",")).contains(fileExt)){
					out.println(getError("上传文件扩展名是不允许的扩展名。\n只允许" + allowedExtensions.get(dirName) + "格式。"));
					return;
				}

				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
				try{
					File uploadedFile = new File(savePath, newFileName);
					item.write(uploadedFile);
				}catch(Exception e){
					out.println(getError("上传文件失败。"));
					return;
				}


				Map<String, Object> jsonMap = new HashMap<String, Object>();
				jsonMap.put("error", 0);
				jsonMap.put("url", saveUrl + newFileName);
				out.println(new Gson().toJson(jsonMap));
			}
		}
	}

	private String getError(String message) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("error", 1);
		jsonMap.put("message", message);
		return new Gson().toJson(jsonMap);
	}
}
