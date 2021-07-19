package com.dbs.cryptowallet.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StringUtils;

import com.dbs.cryptowallet.dto.Assets;
import com.dbs.cryptowallet.dto.AssetsHistory;
import com.dbs.cryptowallet.dto.CoinData;
import com.dbs.cryptowallet.dto.WalletCoin;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class WalletTask implements Runnable {

	private static final String ASSETS_HISTORY_URL = "https://api.coincap.io/v2/assets/%s/history?interval=d1&start=1617753600000&end=1617753601000";

	private String name;
	private WalletCoin walletCoin;
	private Assets assets;

	public void run() {
		log.info("Executing: " + name);
		Optional<CoinData> optional = assets.getData().stream()
				.filter(coinData -> walletCoin.getSymbol().equalsIgnoreCase(coinData.getSymbol())).findFirst();
		optional.ifPresent(coinData -> walletCoin.setId(coinData.getId()));

		// get history and set position in USD
		if (StringUtils.hasText(walletCoin.getId())) {
			AssetsHistory assetsHistory = getAssetsHistory(walletCoin.getId());
			if (!assetsHistory.getData().isEmpty()) {
				Double actualPriceUsd = assetsHistory.getData().get(0).getPriceUsd();
				walletCoin.setPositionInUsd(BigDecimal.valueOf(walletCoin.getQuantity() * actualPriceUsd));
				walletCoin.setPerformance(actualPriceUsd / walletCoin.getPrice());
			}
		}
		log.info("Executed: " + name);
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