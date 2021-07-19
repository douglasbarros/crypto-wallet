package com.dbs.cryptowallet.service;

import java.io.IOException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import com.dbs.cryptowallet.dto.Assets;
import com.dbs.cryptowallet.dto.AssetsHistory;
import com.dbs.cryptowallet.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetService {

	private static final String ASSETS_URL = "http://api.coincap.io/v2/assets";
	private static final String ASSETS_HISTORY_URL = "https://api.coincap.io/v2/assets/%s/history?interval=d1&start=1617753600000&end=1617753601000";

	Assets getAssets() {
		Assets response = null;
		try (CloseableHttpClient client = HttpClients.createDefault()) {

			HttpGet request = new HttpGet(ASSETS_URL);

			response = client.execute(request,
					httpResponse -> JsonUtils.toObject(httpResponse.getEntity().getContent(), Assets.class));

		} catch (IOException e) {
			log.error("Error fetching assets", e);
		}
		return response;
	}

	/**
	 * @param id Coin ID
	 * @return history data of Coin
	 */
	public AssetsHistory getAssetsHistory(String id) {
		AssetsHistory response = null;
		try (CloseableHttpClient client = HttpClients.createDefault()) {

			HttpGet request = new HttpGet(String.format(ASSETS_HISTORY_URL, id));

			response = client.execute(request,
					httpResponse -> JsonUtils.toObject(httpResponse.getEntity().getContent(), AssetsHistory.class));

		} catch (IOException e) {
			log.error("Error fetching assets history", e);
		}
		return response;
	}

}
