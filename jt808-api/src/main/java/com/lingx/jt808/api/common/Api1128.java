package com.lingx.jt808.api.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.lingx.utils.TokenUtils;
import com.lingx.web.api.IApi;
import com.lingx.web.api.impl.AbstractAuthApi;
import com.lingx.jt808.core.cmd.Cmd8106;
import com.lingx.jt808.core.service.JT808CommandService;
import com.lingx.jt808.core.support.MyByteBuf;
import com.lingx.jt808.core.utils.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
@Component
public class Api1128 extends AbstractAuthApi implements IApi{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JT808CommandService commandService;
	@Override
	public int getApiCode() {
		return 1128;
	}
	@Override
	public String getApiName() {
		return "查询设备参数信息";
	}
	@Override
	public String getGroupName() {
		return "车载监控";
	}
	@Override
	public Map<String, Object> api(Map<String, Object> params) {
		Map<String,Object> ret=IApi.getRetMap(1, "SUCCESS");
		if(params.containsKey("deviceId"))params.put("tid", params.get("deviceId"));//兼容API接口
		if(this.checkIsNull(params, "tid",ret))return ret; 
		if(this.checkIsNull(params, "id",ret))return ret; 
		String carid=this.getParamString(params, "tid", "");
		String id=this.getParamString(params, "id", "");
		String type=this.getParamString(params, "type", "HEXSTRING");
		String token=IApi.getParamString(params, "lingxtoken", "");
		String userid=TokenUtils.getTokenDataUserId(token);
		String tid=this.commandService.getTidByCarid(carid);
		Cmd8106 cmd=new Cmd8106(tid,new int[] {Integer.parseInt(id)});
		Map<String,Object> data=commandService.sendCommand(cmd,carid,userid,false);
		//System.out.println("测试的62308");
		//data.put("62308", "1E0600003C00C80302000100010FFF00000003001E320503023C0532050302320503021B320503021E320503021E32050302030200000000");
		if(data.containsKey("117")) {
			String temp=data.get("117").toString();
			data.put("p117", p117(temp));
		}
		if(data.containsKey("62308")) {//苏标adas设置
			String temp=data.get("62308").toString();
			data.put("form", p62308(temp));
		}
		if(data.containsKey("62309")) {//苏标dsm设置
			String temp=data.get("62309").toString();
			data.put("form", p62309(temp));
		}
		if(data.containsKey("62310")) {//苏标盲区检测设置
			String temp=data.get("62310").toString();
			data.put("form", p62310(temp));
		}
		if(data.containsKey("62311")) {//苏标胎压设置
			String temp=data.get("62311").toString();
			data.put("form", p62311(temp));
		}
		if(data.containsKey("62320")) {//激烈驾驶
			String temp=data.get("62320").toString();
			data.put("form", p62320(temp));
		}
		ret.put("data", data);
		//System.out.println(JSON.toJSONString(ret));
		//转换数据类型
		if(data.containsKey(id))//当有相关的数据再转换
		switch(type) {
		case "BYTE":
		case "WORD":
		case "DWORD":
			String temp=data.get(id).toString();
			byte[] bytes=Utils.hexToBytes(temp);
			ret.put("value", Utils.byteArrayToInt(bytes));
			break;
		case "STRING":
			String temp1=data.get(id).toString();
			byte[] bytes1=Utils.hexToBytes(temp1);
			//ret.put("value",new String(bytes1));
			try {
				ret.put("value",new String(bytes1,"GBK"));
			} catch (UnsupportedEncodingException e) {
				ret.put("value",new String(bytes1));
			}
			break;
		default:
			ret.put("value", data.get(id));
		}
		else {
			ret.put("message", "操作失败,终端设备不支持该参数");
		}
		return ret;

	}
	//7E01040040016821120125053B1C4E010000F364381E0600003C00C80302000100010FFF00000003001E320503023C0532050302320503021B320503021E320503021E32050302030200000000DA7E
	public Map<String,Object> p62308(String temp){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",1);
		ByteBuf buff=Unpooled.copiedBuffer(Utils.hexToBytes(temp));
		Map<String,Object> data=new HashMap<>();
		int index=0;MyByteBuf mbb=new MyByteBuf(buff);
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedByte());
			}else if("WORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedShort());
			}else if("DWORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedInt());
			}else if("BYTE[2]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(2));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(3));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(4));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(6));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(12));
			}
			
			index++;
		}
		return data;
	}
	public Map<String,Object> p62309(String temp){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",2);
		ByteBuf buff=Unpooled.copiedBuffer(Utils.hexToBytes(temp));
		Map<String,Object> data=new HashMap<>();
		int index=0;MyByteBuf mbb=new MyByteBuf(buff);
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedByte());
			}else if("WORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedShort());
			}else if("DWORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedInt());
			}else if("BYTE[2]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(2));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(3));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(4));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(6));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(12));
			}
			
			index++;
		}
		return data;
	}
	public Map<String,Object> p62310(String temp){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",3);
		ByteBuf buff=Unpooled.copiedBuffer(Utils.hexToBytes(temp));
		Map<String,Object> data=new HashMap<>();
		int index=0;MyByteBuf mbb=new MyByteBuf(buff);
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedByte());
			}else if("WORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedShort());
			}else if("DWORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedInt());
			}else if("BYTE[2]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(2));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(3));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(4));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(6));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(12));
			}
			
			index++;
		}
		return data;
	}
	public Map<String,Object> p62320(String temp){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",5);
		ByteBuf buff=Unpooled.copiedBuffer(Utils.hexToBytes(temp));
		Map<String,Object> data=new HashMap<>();
		int index=0;MyByteBuf mbb=new MyByteBuf(buff);
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedByte());
			}else if("WORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedShort());
			}else if("DWORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedInt());
			}else if("BYTE[2]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(2));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(3));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(4));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(6));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(12));
			}
			
			index++;
		}
		return data;
	}
	public Map<String,Object> p62311(String temp){
		List<Map<String,Object>> list=this.jdbcTemplate.queryForList("select type,input_options from tsb_param_item where param_id=? order by orderindex asc ",4);
		ByteBuf buff=Unpooled.copiedBuffer(Utils.hexToBytes(temp));
		Map<String,Object> data=new HashMap<>();
		int index=0;MyByteBuf mbb=new MyByteBuf(buff);
		for(Map<String,Object> map:list){
			if("BYTE".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedByte());
			}else if("WORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedShort());
			}else if("DWORD".equals(map.get("type").toString())){
				data.put("p"+index, buff.readUnsignedInt());
			}else if("BYTE[2]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(2));
			}else if("BYTE[3]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(3));
			}else if("BYTE[4]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(4));
			}else if("BYTE[6]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(6));
			}else if("BYTE[12]".equals(map.get("type").toString())){
				data.put("p"+index, mbb.readStringBCD(12));
			}
			
			index++;
		}
		return data;
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
		for(int i=0;i<10;i++) {
			int temp1=new Double(Math.pow(2,i)).intValue();
			if((p18&temp1)>0) {
				list.add(i);
			}
		}
		map.put("p18", list.toArray());
		map.put("p20", p20);
		return map;
	}

	public boolean isLog() {
		return false;
	}
}
