package io.vertx.fastdfs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.fastdfs.options.AbstractFdfsOptions;

/**
 * 
 * @author GengTeng
 *         <p>
 *         me@gteng.org
 * 
 * @version 3.5.0
 */
public class FdfsClientOptions extends AbstractFdfsOptions {
	
	private static final JsonObject DEFAULT_CONFIG = new JsonObject()
			.put(FdfsClientOptions.CHARSET, FdfsClientOptions.DEFAULT_CHARSET)
			.put(FdfsClientOptions.CONNECT_TIMEOUT, FdfsClientOptions.DEFAULT_CONNECT_TIMEOUT)
			.put(FdfsClientOptions.NETWORK_TIMEOUT, FdfsClientOptions.DEFAULT_NETWORK_TIMEOUT)
			.put(FdfsClientOptions.DEFAULT_EXT, FdfsClientOptions.DEFAULT_DEFAULT_EXT).put(FdfsClientOptions.TRACKERS,
					new JsonArray().add(new JsonObject().put(FdfsClientOptions.HOST, FdfsClientOptions.DEFAULT_HOST)
							.put(FdfsClientOptions.PORT, FdfsClientOptions.DEFAULT_PORT)));

	public static final String TRACKERS = "trackers";
	public static final String HOST = "host";
	public static final String PORT = "port";

	public static final String DEFAULT_HOST = "127.0.0.1";
	public static final int DEFAULT_PORT = 22122;

	private List<SocketAddress> trackers;

	public FdfsClientOptions() {
		super();
		trackers = new ArrayList<>();
	}
	
	public FdfsClientOptions(AbstractFdfsOptions other) {
		super(other);
		trackers = new ArrayList<>();
	}
	
	public static JsonObject defaultJsonConfig() {
		return DEFAULT_CONFIG.copy();
	}
	
	public List<SocketAddress> getTrackers() {
		return trackers;
	}

	public FdfsClientOptions addTrackers(SocketAddress... trackers) {
		this.trackers.addAll(Arrays.asList(trackers));
		return this;
	}

	public FdfsClientOptions addTracker(SocketAddress trackers) {
		this.trackers.add(trackers);
		return this;
	}

	public FdfsClientOptions addTracker(String host, int port) {
		this.trackers.add(SocketAddress.inetSocketAddress(port, host));
		return this;
	}

	public FdfsClientOptions delTracker(int index) {
		this.trackers.remove(index);
		return this;
	}

	@Override
	public FdfsClientOptions fromJson(JsonObject json) {

		super.fromJson(json);

		JsonArray array = json.getJsonArray(TRACKERS);

		if (array != null && array.size() > 0) {
			array.forEach(object -> {
				if (object instanceof JsonObject) {
					JsonObject tracker = (JsonObject) object;

					String host = tracker.getString(HOST, "");
					int port = tracker.getInteger(PORT, -1);

					if (!host.isEmpty() && port != -1) {
						trackers.add(SocketAddress.inetSocketAddress(port, host));
					}
				}
			});
		}
		
		return this;
	}

	@Override
	public JsonObject toJson() {
		return super.toJson().put(TRACKERS,
				new JsonArray(trackers.stream()
						.map(sockAddr -> new JsonObject().put(HOST, sockAddr.host()).put(PORT, sockAddr.port()))
						.collect(Collectors.toList())));
	}
	
	@Override
	public FdfsClientOptions setCharset(String charset) {
		super.setCharset(charset);
		return this;
	}
	
	@Override
	public FdfsClientOptions setConnectTimeout(long connectTimeout) {
		super.setConnectTimeout(connectTimeout);
		return this;
	}
	
	@Override
	public FdfsClientOptions setNetworkTimeout(long networkTimeout) {
		super.setNetworkTimeout(networkTimeout);
		return this;
	}
	
	@Override
	public FdfsClientOptions setDefaultExt(String defaultExt) {
		super.setDefaultExt(defaultExt);
		return this;
	}
}
