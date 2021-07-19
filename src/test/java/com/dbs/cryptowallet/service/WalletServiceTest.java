package com.dbs.cryptowallet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dbs.cryptowallet.dto.Assets;
import com.dbs.cryptowallet.dto.Wallet;
import com.dbs.cryptowallet.exception.AssetException;
import com.dbs.cryptowallet.util.JsonUtils;

public class WalletServiceTest {

	@Mock
	AssetService assetService;
	
	@InjectMocks
	WalletService walletService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void sholdReturnSuccess() throws AssetException {
		String assets = "{" + 
				"   \"data\":[" + 
				"      {" + 
				"         \"id\":\"bitcoin\"," + 
				"         \"rank\":\"1\"," + 
				"         \"symbol\":\"BTC\"," + 
				"         \"name\":\"Bitcoin\"," + 
				"         \"supply\":\"18743737.0000000000000000\"," + 
				"         \"maxSupply\":\"21000000.0000000000000000\"," + 
				"         \"marketCapUsd\":\"593150917762.2141458355714397\"," + 
				"         \"volumeUsd24Hr\":\"9006535300.5228721389048737\"," + 
				"         \"priceUsd\":\"31645.2859833774954181\"," + 
				"         \"changePercent24Hr\":\"-1.8700009503283712\"," + 
				"         \"vwap24Hr\":\"31684.8436049529934563\"," + 
				"         \"explorer\":\"https://blockchain.info/\"" + 
				"      }," + 
				"      {" + 
				"         \"id\":\"ethereum\"," + 
				"         \"rank\":\"2\"," + 
				"         \"symbol\":\"ETH\"," + 
				"         \"name\":\"Ethereum\"," + 
				"         \"supply\":\"116469137.4990000000000000\"," + 
				"         \"maxSupply\":null," + 
				"         \"marketCapUsd\":\"219569424150.5585256983646613\"," + 
				"         \"volumeUsd24Hr\":\"7614159349.9815151662882231\"," + 
				"         \"priceUsd\":\"1885.2155074338362058\"," + 
				"         \"changePercent24Hr\":\"-4.7873418136516729\"," + 
				"         \"vwap24Hr\":\"1934.3438625141882933\"," + 
				"         \"explorer\":\"https://etherscan.io/\"" + 
				"      }" + 
				"   ]" + 
				"}";
		when(assetService.getAssets()).thenReturn(JsonUtils.toObject(assets, Assets.class));
		String walletPerformance = walletService.evaluateWalletPerformance();
		assertNotNull(walletPerformance);
		Wallet wallet = JsonUtils.toObject(walletPerformance, Wallet.class);
		assertEquals(new BigDecimal(16984.62).setScale(2, RoundingMode.HALF_UP), wallet.getTotal());
		assertEquals("BTC", wallet.getBestAsset());
		assertEquals(new BigDecimal(1.51).setScale(2, RoundingMode.HALF_UP), wallet.getBestPerformance());
		assertEquals("ETH", wallet.getWorstAsset());
		assertEquals(new BigDecimal(1.01).setScale(2, RoundingMode.HALF_UP), wallet.getWorstPerformance());
	}

	@Test
	void sholdReturnAssetRequestError() {
		when(assetService.getAssets()).thenReturn(null);
		AssetException assetException = assertThrows(AssetException.class, () -> {
			walletService.evaluateWalletPerformance();
		});
		assertEquals("Cannot read assets", assetException.getMessage());
	}

}
