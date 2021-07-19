package com.dbs.cryptowallet.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.dbs.cryptowallet.service.WalletService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WalletRunner implements ApplicationRunner {

	@Autowired
	private WalletService walletService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String walletPerformance = walletService.evaluateWalletPerformance();
		log.info("WalletPerformance: {}", walletPerformance);
	}

}
