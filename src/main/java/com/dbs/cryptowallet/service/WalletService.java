package com.dbs.cryptowallet.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbs.cryptowallet.dto.Assets;
import com.dbs.cryptowallet.dto.Wallet;
import com.dbs.cryptowallet.dto.WalletCoin;
import com.dbs.cryptowallet.exception.AssetException;
import com.dbs.cryptowallet.util.JsonUtils;
import com.dbs.cryptowallet.util.WalletTask;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WalletService {

	private static final String WALLET_CSV = "wallet.csv";

	private static final Integer THREAD_POOL_SIZE = 3;

	@Autowired
	private AssetService assetService;
	
	public String evaluateWalletPerformance() throws AssetException {
		
		Wallet wallet = Wallet.builder().build();
		
		// read wallet
		List<WalletCoin> walletCoins = readWallet();

		// get assets
		Assets assets = assetService.getAssets();
		
		if(assets == null) {
			throw new AssetException("Cannot read assets");
		}

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newScheduledThreadPool(THREAD_POOL_SIZE);
		walletCoins.forEach(walletCoin -> {
			WalletTask walletTask = WalletTask.builder().name(walletCoin.getSymbol()).walletCoin(walletCoin).assets(assets).build();
			executor.submit(walletTask);
		});
		executor.shutdown();
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			log.error("Timeout while waiting executor", e);
		}

		WalletCoin coinWithBestPerformance = walletCoins.stream().max(Comparator.comparing(WalletCoin::getPerformance))
				.orElseThrow(NoSuchElementException::new);
		WalletCoin coinWithWorstPerformance = walletCoins.stream().min(Comparator.comparing(WalletCoin::getPerformance))
				.orElseThrow(NoSuchElementException::new);

		wallet.setTotal(walletCoins.stream().map(WalletCoin::getPositionInUsd).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
		wallet.setBestAsset(coinWithBestPerformance.getSymbol());
		wallet.setBestPerformance(BigDecimal.valueOf(coinWithBestPerformance.getPerformance()));
		wallet.setWorstAsset(coinWithWorstPerformance.getSymbol());
		wallet.setWorstPerformance(BigDecimal.valueOf(coinWithWorstPerformance.getPerformance()));

		return JsonUtils.toJson(wallet);
	}

	private List<WalletCoin> readWallet() {
		List<WalletCoin> walletCoins = new ArrayList<>();
		InputStream is = getClass().getClassLoader().getResourceAsStream(WALLET_CSV);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				WalletCoin walletCoin = WalletCoin.builder()
						.symbol(values[0])
						.quantity(Double.valueOf(values[1]))
						.price(Double.valueOf(values[2]))
						.build();
				walletCoins.add(walletCoin);
			}
		} catch (IOException e) {
			log.error("Error reading wallet", e);
		}
		return walletCoins;
	}

}
