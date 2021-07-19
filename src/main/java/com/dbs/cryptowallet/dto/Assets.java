package com.dbs.cryptowallet.dto;

import java.util.List;

import lombok.Data;

@Data
public class Assets {

	private List<CoinData> data;
	private Long timestamp;

}