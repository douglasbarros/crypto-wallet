package com.dbs.cryptowallet.dto;

import lombok.Data;

@Data
public class CoinData {

	private String id;
	private String rank;
	private String symbol;
	private String name;
	private Double supply;
	private Double maxSupply;
	private Double marketCapUsd;
	private Double volumeUsd24Hr;
	private Double priceUsd;
	private Double changePercent24Hr;
	private Double vwap24Hr;
	private String explorer;

}