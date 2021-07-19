package com.dbs.cryptowallet.dto;

import java.util.List;

import lombok.Data;

@Data
public class AssetsHistory {

	private List<CoinPrice> data;
	private Long timestamp;

}