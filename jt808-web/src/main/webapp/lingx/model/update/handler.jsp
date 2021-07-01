<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="java.net.*,java.io.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %><%!

public void findFile(File file,List<String> list,String filename){
	{
		boolean b=file.getName().equals(filename);
		if(b&&list.size()<10){
			list.add(file.getAbsolutePath());
		}
	}

	if(file.isDirectory()){
		File files[]=file.listFiles();
		if(list.size()<10)
		for(File f:files){
			findFile(f,list,filename);
		}
	}
}
%><%
org.springframework.context.ApplicationContext spring = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
ILingxService lingx=spring.getBean(ILingxService.class);
if(!lingx.isSuperman(request))return;

ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
UserBean userBean=(UserBean)session.getAttribute(Constants.SESSION_USER);

Map<String,Object>success=new HashMap<String,Object>();
success.put("message","操作成功");
Map<String,Object>failure=new HashMap<String,Object>();
failure.put("message","操作失败");
String cmd=request.getParameter("c");
if("findAndAddFile".equals(cmd)){
	String filename=request.getParameter("filename");
	List<String> list=new ArrayList<String>();
	List<String> list1=new ArrayList<String>();
	String path=request.getRealPath("/");
	findFile(new File(path),list,filename);
	//path=path.replaceAll("\\\\", "/");
	for(String s:list){
		s=s.replace(path, "");//s.replaceAll("\\\\", "/")
		list1.add(s);
	}
	success.put("list", list1);
	out.println(JSON.toJSONString(success));
}else if("packAndDownload".equals(cmd)){
	String entityids=request.getParameter("entityids");
	String files=request.getParameter("files");
	String sqltext=URLDecoder.decode(request.getParameter("sqltext"),"UTF-8"); //request.getParameter("sqltext");
	String remark=URLDecoder.decode(request.getParameter("remark"),"UTF-8"); //request.getParameter("remark");
	//type,reboot,isdata;
	String type=request.getParameter("type") ;
	String reboot=request.getParameter("reboot") ;
	String isdata=request.getParameter("isdata") ;
	//设置打包信息
	com.lingx.core.model.bean.PackBean bean=new com.lingx.core.model.bean.PackBean();
	bean.setAppid(request.getParameter("appid"));
	bean.setAuthor(userBean.getName());
	bean.setContent(remark);
	String[] filesArray=files.split(",");
	List<String> fileList=new ArrayList<String>();
	for(String temp:filesArray){
		fileList.add(temp.replaceAll("\\\\", "/"));
	}
	bean.setFileList(fileList);
	
	List<String> modelList=new ArrayList<String>();
	filesArray=entityids.split(",");
	for(String temp:filesArray){
		modelList.add(temp);
	}
	bean.setModelList(modelList);
	//URLDecoder.decode(request.getParameter("msg"),"UTF-8") 
	bean.setName(URLDecoder.decode(request.getParameter("name"),"UTF-8") );
	bean.setOption("1".equals(request.getParameter("isOption")));
	bean.setReboot("1".equals(reboot));
	bean.setSecret(URLDecoder.decode(request.getParameter("secret"),"UTF-8") );
	bean.setType(Integer.parseInt(type));
	bean.setSql(sqltext);
	//System.out.println(JSON.toJSONString(bean));
	IPackageService pack=applicationContext.getBean(IPackageService.class);
	 response.reset();
	   response.addHeader("Content-Disposition", "attachment;filename=lingx-"+Utils.getTime()+".zip");
	  // response.addHeader("Content-Length", "" + file.length());
	   response.setContentType("application/octet-stream");
	   OutputStream ous = new BufferedOutputStream(response.getOutputStream());
	pack.packAndDownload(bean, request.getServletContext().getRealPath("/"), ous);
	//out.println(JSON.toJSONString(success));
}else if("updatePakeage".equals(cmd)){
	String file=request.getParameter("file");
	String ts=request.getParameter("ts");
	if(ts==null){
		ts="20121221153030";
	}
	IUpdateService update=applicationContext.getBean(IUpdateService.class);
	
	if(file.charAt(0)=='['){
		List<Map<String,Object>> json=(List<Map<String,Object>>)JSON.parse(file);
		file=json.get(0).get("value").toString();
	}
	
	if(file.startsWith("http:")){
		String basePath=request.getServletContext().getRealPath("/");
		
		boolean b=update.update(new URL(file), basePath,ts);
		if(b){
			success.put("message", "更新成功");
		}else{
			success.put("message", "更新失败");
			
		}
	}else{
		String basePath=request.getServletContext().getRealPath("/");
		boolean b=update.update(new File(basePath+file), basePath,ts);
		if(b){
			success.put("message", "更新成功");
		}else{
			success.put("message", "更新失败");
			
		}
	}
	
	out.println(JSON.toJSONString(success));
}else if("download".equals(cmd)){
	String path=request.getParameter("path");
	out.println(JSON.toJSONString(success));
}else if("upload".equals(cmd)){
	String appid=request.getParameter("appid");
	String entityids=request.getParameter("entityids");
	String files=request.getParameter("files");
	String sqltext=URLDecoder.decode(request.getParameter("sqltext"),"UTF-8"); //request.getParameter("sqltext");
	String remark=URLDecoder.decode(request.getParameter("remark"),"UTF-8"); //request.getParameter("remark");
	//type,reboot,isdata;
	String type=request.getParameter("type") ;
	String option=request.getParameter("option") ;
	String gncd=request.getParameter("gncd") ;
	String sjmx=request.getParameter("sjmx") ;
	String reboot=request.getParameter("reboot") ;
	String isdata=request.getParameter("isdata") ;
	//设置打包信息
	com.lingx.core.model.bean.PackBean bean=new com.lingx.core.model.bean.PackBean();
	bean.setAppid(request.getParameter("appid"));
	bean.setAuthor(userBean.getName());
	bean.setContent(remark);
	String[] filesArray=files.split(",");
	List<String> fileList=new ArrayList<String>();
	for(String temp:filesArray){
		fileList.add(temp.replaceAll("\\\\", "/"));
	}
	bean.setFileList(fileList);
	
	/* List<String> modelList=new ArrayList<String>();
	List<Map<String,Object>> listEntity=jdbc.queryForList("select code from tlingx_entity where app_id=?",appid);
	for(Map<String,Object> temp:listEntity){
		modelList.add(temp.get("code").toString());
	}
	bean.setModelList(modelList); */
	List<String> modelList=new ArrayList<String>();
	filesArray=entityids.split(",");
	for(String temp:filesArray){
		modelList.add(temp);
	}
	bean.setModelList(modelList);
	
	//URLDecoder.decode(request.getParameter("msg"),"UTF-8") 
	bean.setName(URLDecoder.decode(request.getParameter("name"),"UTF-8") );
	bean.setOption("1".equals(request.getParameter("isOption")));
	bean.setReboot("1".equals(reboot));
	bean.setGncd("1".equals(gncd));
	bean.setSjmx("1".equals(sjmx));
	bean.setSecret(URLDecoder.decode(request.getParameter("secret"),"UTF-8") );
	bean.setType(Integer.parseInt(type));
	bean.setSql(sqltext);
	
	IPackageService pack=applicationContext.getBean(IPackageService.class);
	String ret="";
	//if("335ec1fc-1011-11e5-b7ab-74d02b6b5f61".equals(request.getParameter("appid"))){
    //
	//	ret=pack.uploadPackLingx(bean, request.getServletContext().getRealPath("/"), bean.getSecret());
	//}else{

		ret=pack.packAndUpload(bean, request.getServletContext().getRealPath("/"), bean.getSecret());
	//}
	out.println(ret);
}else{//show create function func_name
	System.out.println("NO CMD:"+request.getParameter("c"));
	/*
	CREATE TABLE gbsmilie(id int NOT NULL AUTO_INCREMENT,primary key(id));
	*/
	out.println(JSON.toJSONString(failure));
}
%>