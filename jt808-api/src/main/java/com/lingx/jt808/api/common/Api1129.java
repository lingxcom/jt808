package com.lingx.jt808.api.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lingx.service.LingxService;
import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.IJT808Command;
import com.lingx.jt808.core.bean.Cmd8103Bean;
import com.lingx.jt808.core.cmd.Cmd8103;
import com.lingx.jt808.core.service.JT808CommandService;
import com.lingx.jt808.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
@Component
public class Api1129 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommandService commandService;
	@Autowired
	private LingxService lingxService;
	@Override
	public int getApiCode() {
		return 1129;
	}
	@Override
	public String getApiName() {
		return "设置设备参数信息";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		if(!"true".equals(this.lingxService.getConfigValue("lingx.system.isedit", "true")))  {
			ret.put("message", "操作失败，系统禁止操作!");
			return ret;
		}
		if(this.checkIsNull(params, "tid",ret))return ret; 
		if(this.checkIsNull(params, "id",ret))return ret; 
		if(this.checkIsNull(params, "param",ret))return ret; 
		if(this.checkIsNull(params, "type",ret))return ret; 
		String carid=this.getParamString(params, "tid", "");
		String id=this.getParamString(params, "id", "");
		String param=this.getParamString(params, "param", "");
		String type=this.getParamString(params, "type", "");
		String tid=this.commandService.getTidByCarid(carid);
		String token=IApi.getParamString(params, "lingxtoken", "");
		String userid=TokenUtils.getTokenDataUserId(token);
		IJT808Command cmd=null;
		if("117".equals(type)) {
			Map<String,Object> map=(Map<String,Object>)JSON.parse(param);
			param=f117(map);
			type="HEXSTRING";
			map.clear();
		}else if("62308".equals(type)) {
			Map<String,Object> map=(Map<String,Object>)JSON.parse(param);
			param=f62308(map);
			type="HEXSTRING";
			map.clear();
			//System.out.println("下发参数");
			//System.out.println(id);
			//System.out.println(param);
		}else if("62309".equals(type)) {
			Map<String,Object> map=(Map<String,Object>)JSON.parse(param);
			param=f62309(map);
			type="HEXSTRING";
			map.clear();
		}else if("62310".equals(type)) {
			Map<String,Object> map=(Map<String,Object>)JSON.parse(param);
			param=f62310(map);
			type="HEXSTRING";
			map.clear();
		}else if("62311".equals(type)) {
			Map<String,Object> map=(Map<String,Object>)JSON.parse(param);
			param=f62311(map);
			type="HEXSTRING";
			map.clear();
		}else if("62320".equals(type)) {
			Map<String,Object> map=(Map<String,Object>)JSON.parse(param);
			param=f62320(map);
			type="HEXSTRING";
			map.clear();
		}
		
		Cmd8103Bean bean=new Cmd8103Bean(Integer.parseInt(id),type,param);
		cmd=new Cmd8103(tid,new Cmd8103Bean[] {bean} );
		ret=commandService.sendCommand(cmd,carid,userid,true);
		return ret;

	}
	public String f62308(Map<String,Object> data) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",1);
		ByteBuf buff=Unpooled.buffer();
		//Map<String,Object> data=(Map<String,Object>)params.get("param");
		int index=0;//MyByteBuf mbb=new MyByteBuf(buff);
		String temp=null;
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeByte(Integer.parseInt(temp));
			}else if("WORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeShort(Integer.parseInt(temp));
			}else if("DWORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeInt(Integer.parseInt(temp));
			}else if("BYTE[2]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}
			
			index++;
		}
		
		byte bytes[]=new byte[buff.readableBytes()];
		buff.readBytes(bytes);
		buff.release();
		return Utils.bytesToHex(bytes);
	}

	public String f62309(Map<String,Object> data) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",2);
		ByteBuf buff=Unpooled.buffer();
		//Map<String,Object> data=(Map<String,Object>)params.get("param");
		int index=0;//MyByteBuf mbb=new MyByteBuf(buff);
		String temp=null;
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeByte(Integer.parseInt(temp));
			}else if("WORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeShort(Integer.parseInt(temp));
			}else if("DWORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeInt(Integer.parseInt(temp));
			}else if("BYTE[2]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}
			
			index++;
		}
		
		byte bytes[]=new byte[buff.readableBytes()];
		buff.readBytes(bytes);
		buff.release();
		return Utils.bytesToHex(bytes);
	}

	public String f62310(Map<String,Object> data) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",3);
		ByteBuf buff=Unpooled.buffer();
		int index=0;//MyByteBuf mbb=new MyByteBuf(buff);
		String temp=null;
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeByte(Integer.parseInt(temp));
			}else if("WORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeShort(Integer.parseInt(temp));
			}else if("DWORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeInt(Integer.parseInt(temp));
			}else if("BYTE[2]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}
			
			index++;
		}
		
		byte bytes[]=new byte[buff.readableBytes()];
		buff.readBytes(bytes);
		buff.release();
		return Utils.bytesToHex(bytes);
	}

	public String f62311(Map<String,Object> data) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",4);
		ByteBuf buff=Unpooled.buffer();
		int index=0;//MyByteBuf mbb=new MyByteBuf(buff);
		String temp=null;
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeByte(Integer.parseInt(temp));
			}else if("WORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeShort(Integer.parseInt(temp));
			}else if("DWORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeInt(Integer.parseInt(temp));
			}else if("BYTE[2]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}
			
			index++;
		}
		
		byte bytes[]=new byte[buff.readableBytes()];
		buff.readBytes(bytes);
		buff.release();
		return Utils.bytesToHex(bytes);
	}

	public String f62320(Map<String,Object> data) {
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",5);
		ByteBuf buff=Unpooled.buffer();
		int index=0;//MyByteBuf mbb=new MyByteBuf(buff);
		String temp=null;
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeByte(Integer.parseInt(temp));
			}else if("WORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeShort(Integer.parseInt(temp));
			}else if("DWORD".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeInt(Integer.parseInt(temp));
			}else if("BYTE[2]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				temp=data.get("p"+index).toString();
				buff.writeBytes(Utils.hexToBytes(temp));
			}
			
			index++;
		}
		
		byte bytes[]=new byte[buff.readableBytes()];
		buff.readBytes(bytes);
		buff.release();
		return Utils.bytesToHex(bytes);
	}
	public String f117(Map<String,Object> map) {
		ByteBuf buff=Unpooled.buffer();
		buff.writeByte(Integer.parseInt(map.get("p0").toString()));
		buff.writeByte(Integer.parseInt(map.get("p1").toString()));
		buff.writeShort(Integer.parseInt(map.get("p2").toString()));
		buff.writeByte(Integer.parseInt(map.get("p4").toString()));
		buff.writeInt(Integer.parseInt(map.get("p5").toString()));
		buff.writeByte(Integer.parseInt(map.get("p9").toString()));
		buff.writeByte(Integer.parseInt(map.get("p10").toString()));
		buff.writeShort(Integer.parseInt(map.get("p11").toString()));
		buff.writeByte(Integer.parseInt(map.get("p13").toString()));
		buff.writeInt(Integer.parseInt(map.get("p14").toString()));
		List<Integer> list=(List<Integer>)map.get("p18");
		int sum=0;
		for(int temp:list) {
			sum+=Math.pow(2,temp);
		}
		buff.writeShort(sum);
		buff.writeByte(Integer.parseInt(map.get("p20").toString()));
		byte bytes[]=new byte[buff.readableBytes()];
		
		
		buff.readBytes(bytes);
		buff.release();
		return Utils.bytesToHex(bytes);
	}
	public Map<String,Object> p117(String temp){
		ByteBuf buff=Unpooled.copiedBuffer(Utils.hexToBytes(temp));
		int p0=buff.readByte();
		int p1=buff.readByte();
		int p2=buff.readShort();
		int p4=buff.readByte();
		int p5=buff.readInt();
		int p9=buff.readByte();
		int p10=buff.readByte();
		int p11=buff.readShort();
		int p13=buff.readByte();
		int p14=buff.readInt();
		int p18=buff.readShort();
		int p20=buff.readByte();
		Map<String,Object> map=new HashMap<>();
		map.put("p0", p0);
		map.put("p1", p1);
		map.put("p2", p2);
		map.put("p4", p4);
		map.put("p5", p5);
		map.put("p9", p9);
		map.put("p10", p10);
		map.put("p11", p11);
		map.put("p13", p13);
		map.put("p14", p14);
		List<Integer> list=new ArrayList<>();
		for(int i=0;i<6;i++) {
			int temp1=new Double(Math.pow(2,i)).intValue();
			if((p18&temp1)>0) {
				list.add(i);
			}
		}
		map.put("p18", list.toArray());
		map.put("p20", p20);
		return map;
	}
	

	
}
