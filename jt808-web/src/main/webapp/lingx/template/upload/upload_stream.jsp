<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%@ page import="java.io.*,java.net.URLDecoder,java.text.MessageFormat,com.lingx.core.utils.MD5Utils,com.lingx.core.action.IRequestAware,com.lingx.core.engine.*,com.lingx.core.utils.Utils,com.lingx.core.model.bean.UserBean,com.lingx.core.service.*,com.lingx.core.Constants,com.lingx.core.service.*,com.lingx.core.model.*,java.util.*,com.alibaba.fastjson.JSON,org.springframework.context.ApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils,org.springframework.jdbc.core.JdbcTemplate" %>
<%!

public static String getBasePath(HttpServletRequest request){
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
	return basePath;
}
public static void saveToDatabase(String id,String url,String name,long length,JdbcTemplate jdbc,File file,String xdlj){
	if(jdbc.queryForInt("select count(*) from tlingx_upload where id=?",id)==0){
		String time=getTime();
		jdbc.update("insert into tlingx_upload(id,name,length,file,url,upload_time,download_count,last_download_time) values(?,?,?,?,?,?,?,?)"
				,id,name,length,xdlj,url,time,0,time);
	}else{
		file.delete();
	}
}
public static String getDate() {
	String dateTime = MessageFormat.format("{0,date,yyyy/MM/dd/}",
			new Object[] { new java.sql.Date(System.currentTimeMillis()) });
	return dateTime;

}
public static String getTime() {
	String dateTime = MessageFormat.format("{0,date,yyyyMMddHHmmss}",
			new Object[] { new java.sql.Date(System.currentTimeMillis()) });
	return dateTime;

}
    %><%
    ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	JdbcTemplate jdbc=applicationContext.getBean("jdbcTemplate",JdbcTemplate.class);
	Map<String,Object> ret=new HashMap<String,Object>();
	String prefix=String.valueOf(System.currentTimeMillis() );
	String filename=request.getHeader("filename");
	filename=URLDecoder.decode(filename,"UTF-8");
	//String contentLength=request.getHeader("Content-Length");
	String t_ext = filename.substring(filename.lastIndexOf(".") + 1);
	InputStream in=request.getInputStream();
	BufferedInputStream bis=new BufferedInputStream(in);
	File dir=new File(request.getServletContext().getRealPath("/") + "upload/"+getDate());
	if(!dir.exists()){
		dir.mkdirs();
	}
	String xdlj="upload/"+getDate()+ prefix + "." + t_ext;;
	String path= request.getServletContext().getRealPath("/") +xdlj;
	FileOutputStream fos=new FileOutputStream(path);
	
	BufferedOutputStream bos=new BufferedOutputStream(fos);
	int fileLength=0;
	byte buffer[]=new byte[4096];
	int len=0;
	while((len=bis.read(buffer))!=-1){
		bos.write(buffer, 0, len);
		fileLength+=len;
	}bis.close();
	in.close();
	bos.flush();
	bos.close();
	fos.close();
	//ret.put("message","文件上传成功. 已保存为: " + prefix + "." + t_ext
	//		+ " ；文件大小: " + fileLength/1024 + "KB");
	String id=t_ext+fileLength+MD5Utils.getFileMD5String(new File(path));
	String url=getBasePath(request)+ xdlj;
	ret.put("code","1");
	ret.put("message","SUCCESS");
	ret.put("length", fileLength);
	ret.put("name", filename);
	ret.put("path",getBasePath(request)+"download?id="+id);

	saveToDatabase(id, url, filename, fileLength, jdbc, new File(path),xdlj);
	response.setContentType("text/html;charset=UTF-8");  
	response.setStatus(HttpServletResponse.SC_OK);
	System.out.println(JSON.toJSONString(ret));
	response.getWriter().print( JSON.toJSONString(ret));
    %>