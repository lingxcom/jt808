package com.lingx.jt808.server.netty.websocket.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
@Component
public class WebSocketApi3001 extends AbstractApi {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public WebSocketApi3001(){
		this.setCmd("3001");
	}
	@Override
	public Map<String, Object> execute(Map<String, Object> param, ChannelHandlerContext ctx) {
		String account=param.get("account").toString();
		String password=param.get("password").toString();
		Map<String,Object> ret=null;
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select id,name,id token from tlingx_user where account=?",account);
		if(list.size()==1) {
			ret=this.getRetMap(1, "登录成功！",list.get(0));
		}else {
			ret=this.getRetMap(-1, "登录失败，账号或密码有误！");
		}
		return ret;
	}


}
