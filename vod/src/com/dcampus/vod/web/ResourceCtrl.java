package com.dcampus.vod.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dcampus.common.config.Global;
import com.dcampus.common.generic.GenericService;
import com.dcampus.common.util.FileServerUtil;
import com.dcampus.common.web.BaseController;
import com.dcampus.vod.entity.Resource;
import com.dcampus.vod.service.ResourceService;
import com.dcampus.vod.service.WeblibService;
import com.dcampus.vod.util.DownloadUtil;
import com.dcampus.vod.util.JsonUtil;

/**
 * 资源下载管理，根据ID请求资源
 * 在本系统中无法查找到相应资源时
 * 向weblib系统请求该资源后进行返回
 * @author patrick
 *
 */
@Controller
@RequestMapping(value = "/resource")
public class ResourceCtrl extends BaseController {
	
	private static final Logger logger = Logger.getLogger(ResourceCtrl.class);
	private JsonUtil jsonUtil = new JsonUtil();
	@Autowired
	private GenericService genericService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private WeblibService weblibService;
	
	/**
	 * 文件下载接口， 根据weblibId进行查找，
	 * 如果在本地查找不到,向weblib资源库请求下载到本地，再返回给lms
	 * @param id 资源库中对应的id
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "download")
	@ResponseBody
	public String download(String id, Model model, RedirectAttributes redirectAttributes,
	HttpServletRequest request, HttpServletResponse response){
		Long weblibId = Long.parseLong(id);
		JsonUtil ju = new JsonUtil();
		
		Resource resource = resourceService.getResourceByWeblibId(weblibId);
		if(resource == null) {
			weblibService.loginWeblib();
			String filename = weblibService.downloadFromWeblib(weblibId);
			if(filename != null && filename != ""){
				String[] temp = filename.split("\\.");
				String name = temp[0];
				String fileExt = temp[1];
				Resource re = new Resource();
				re.setFileExt(fileExt);
				re.setName(name);
				re.setWeblibId(weblibId);
				try{
					resourceService.saveOrUpdateResource(re);
				}catch(Exception e) {
					ju.setResult("failed");
					ju.setMessage("保存资源失败" + e.toString());
					return jsonUtil.bean2json(ju);
				}
			} else {
				ju.setResult("failed");
				ju.setMessage("weblib中未找到该资源！");
				return jsonUtil.bean2json(ju);
			}
		}
		_download(weblibId, request, response);
		ju.setResult("success");
		ju.setMessage("");
		return jsonUtil.bean2json(ju);
	}

	
	/**
	 * 提供从VOD下载文件到客户端的方法
	 * @param weblibId
	 * @param request
	 * @param response
	 */
	public void _download(Long weblibId, HttpServletRequest request, HttpServletResponse response) {
		Resource resource = resourceService.getResourceByWeblibId(weblibId);
		String fileExt = resource.getFileExt();
		String name = resource.getName();
		String filename = new String(name + "." + fileExt);
		
		try {
		    File file = new File(new URI(DownloadUtil.getFileFullPath(resource)));   
			if(file != null && file.exists()) {					
				filename = filename.replaceAll(" ", "");				
				FileServerUtil.output(file, filename, request, response);
			}
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
