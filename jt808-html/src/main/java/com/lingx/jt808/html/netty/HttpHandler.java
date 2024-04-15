package com.lingx.jt808.html.netty;

import com.lingx.jt808.html.utils.Utils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Sharable
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	public static final String BASE_PATH = "support/http";

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		if (msg instanceof FullHttpRequest) {

			FullHttpRequest request = msg;
			String uri = request.uri();
			uri = uri.indexOf("?") >= 0 ? uri.substring(0, uri.indexOf("?")) : uri;
			if (uri.endsWith(".html")) {
				byte[] responseBytes = Utils.readByteArrayFromResource(getPage(uri));
				int contentLength = responseBytes.length;
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(responseBytes));
				addHeaders(uri, contentLength, response);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			} else if (uri.endsWith(".css")) {
				byte[] responseBytes = Utils.readByteArrayFromResource(getPage(uri));
				int contentLength = responseBytes.length;
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(responseBytes));
				addHeaders(uri, contentLength, response);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			} else if (uri.endsWith(".js")) {
				byte[] responseBytes = Utils.readByteArrayFromResource(getPage(uri));
				int contentLength = responseBytes.length;
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(responseBytes));
				response.headers().set("Content-Type", "application/javascript");
				response.headers().set("Content-Length", Integer.toString(contentLength));
			
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			} else if (uri.endsWith(".svg")) {
				byte[] responseBytes = Utils.readByteArrayFromResource(getPage(uri));
				int contentLength = responseBytes.length;
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(responseBytes));
				addHeaders(uri, contentLength, response);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			} else if (uri.endsWith(".woff2")) {
				byte[] responseBytes = Utils.readByteArrayFromResource(getPage(uri));
				int contentLength = responseBytes.length;
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(responseBytes));
				addHeaders(uri, contentLength, response);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			} else if (uri.endsWith(".ttf")) {
				byte[] responseBytes = Utils.readByteArrayFromResource(getPage(uri));
				int contentLength = responseBytes.length;
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(responseBytes));
				addHeaders(uri, contentLength, response);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			} else if (uri.endsWith(".ico")) {
				byte[] responseBytes = Utils.readByteArrayFromResource(getPage(uri));
				int contentLength = responseBytes.length;
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(responseBytes));
				addHeaders(uri, contentLength, response);
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			} else if (uri.endsWith("/")) {
				String responseHtml=Utils.readFromResource(getPage(uri));
				byte[] responseBytes =responseHtml.getBytes("UTF-8") ;
				int contentLength = responseBytes.length;
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(responseBytes));
				addHeaders(uri, contentLength, response);
				response.headers().set("cors-headers", "accept,content-type,authorization,origin");
				response.headers().set("Access-Control-Allow-Origin", "*");
				response.headers().set("Access-Control-Allow-Headers", "Content-Type");
				response.headers().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			} else {

				byte[] responseBytes = Utils.readByteArrayFromResource(getPage(uri));
				int contentLength = responseBytes.length;
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(responseBytes));
				addHeaders(uri, contentLength, response);
				response.headers().set("cors-headers", "accept,content-type,authorization,origin");
				response.headers().set("Access-Control-Allow-Origin", "*");
				response.headers().set("Access-Control-Allow-Headers", "Content-Type");
				response.headers().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
				ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			}


		}
	}

	private void addHeaders(String uri, int length, FullHttpResponse response) {
		String ext = uri;
		if (ext.indexOf(".") >= 0)
			ext = uri.toLowerCase().substring(uri.lastIndexOf("."));

		switch (ext) {
		case ".json":
		case ".html":
		case "/":
		case "":
		case ".txt":
		case ".api":
			response.headers().set("Content-Type", "text/html; charset=utf-8");
			break;
		case ".jpg":
		case ".jpeg":
		case ".jpe":
			response.headers().set("Content-Type", "image/jpeg; charset=utf-8");
			break;
		case ".png":
			response.headers().set("Content-Type", "image/png; charset=utf-8");
			break;
		case ".ico":
			response.headers().set("Content-Type", "image/x-icon; charset=utf-8");
			break;
		case ".ttf":
		case ".woff2":
			response.headers().set("Content-Type", "application/octet-stream");
			break;
		case ".svg":
			response.headers().set("Content-Type", "image/svg+xml");
			break;
		case ".js":
			response.headers().set("Content-Type", "application/javascript; charset=utf-8");
			break;
		case ".css":
			response.headers().set("Content-Type", "text/css; charset=utf-8");
			break;
		case ".gif":
			response.headers().set("Content-Type", "image/gif");
			break;
		case ".mp3":
			response.headers().set("Content-Type", "audio/mp3");
			break;
		case ".mp4":
			response.headers().set("Content-Type", "video/mpeg4");
			break;
		case ".mov":
		case ".movie":
			response.headers().set("Content-Type", "video/x-sgi-movie");
			break;
		case ".pdf":
			response.headers().set("Content-Type", "application/pdf");
			break;
		default: {
			response.headers().set("Content-Type", "application/octet-stream");
		}
		}
		response.headers().set("Content-Length", Integer.toString(length));
	}

	private String getPage(String uri) {
		uri = ("/".equals(uri) || "".equals(uri)) ? "/index.html" : uri;
		return BASE_PATH + uri;
	}

	private Map<String, String> getRequestParams(ChannelHandlerContext ctx, FullHttpRequest req) {
		// System.out.println("璇锋眰鏂瑰紡:"+req.getMethod());
		Map<String, String> requestParams = new HashMap<>();
		// 澶勭悊get璇锋眰
		if (req.getMethod() == HttpMethod.GET) {
			QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
			Map<String, List<String>> parame = decoder.parameters();
			Iterator<Entry<String, List<String>>> iterator = parame.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, List<String>> next = iterator.next();
				requestParams.put(next.getKey(), next.getValue().get(0));
			}
		}
		// 澶勭悊POST璇锋眰
		if (req.getMethod() == HttpMethod.POST) {
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), req);
			decoder.offer(req);
			List<InterfaceHttpData> postData = decoder.getBodyHttpDatas(); //
			for (InterfaceHttpData data : postData) {
				if (data.getHttpDataType() == HttpDataType.Attribute) {
					MemoryAttribute attribute = (MemoryAttribute) data;
					requestParams.put(attribute.getName(), attribute.getValue());
				}
			}
		}
		return requestParams;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// System.out.println("鍏抽棴");
		super.channelInactive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// log.error(cause.getMessage());
		ctx.close();

	}

}
