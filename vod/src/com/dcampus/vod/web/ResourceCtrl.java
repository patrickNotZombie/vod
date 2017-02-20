package com.dcampus.vod.web;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dcampus.common.generic.GenericService;
import com.dcampus.common.util.FileServerUtil;
import com.dcampus.common.web.BaseController;
import com.dcampus.vod.service.ResourceService;

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
	@Autowired
	private GenericService genericService;
	@Autowired
	private ResourceService resourceService;
	
	/**
	 * 统一的本地文件下载接口， done json
	 * @param id
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @return
	 */
/*	@RequestMapping(value = "download")
	public String download(String id, Model model, RedirectAttributes redirectAttributes,
	HttpServletRequest request, HttpServletResponse response){
		String filename = id;
		JsonUtil ju = new JsonUtil();

		if(filename == null || filename.trim().equals("")){
			ju.setResult("failed");
			ju.setMessage("对不起，没有找到该课程资料文档。");
			return jsonUtil.bean2json(ju);
		}

		try {
			File file = FileUtils.getFile(Global.getFileRootPath(), filename);
			if(file != null){					
				filename = filename.replaceAll(" ", "");				
				FileServerUtil.output(file, filename, request, response);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ju.setResult("success");
		ju.setMessage("");
		return jsonUtil.bean2json(ju);
	}*/

}
