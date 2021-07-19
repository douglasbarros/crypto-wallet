package com.dbs.cryptowallet.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WalletCoin {

	private String symbol;
	private Double quantity;
	private Double price;
	private String id;
	private BigDecimal positionInUsd;
	private Double performance;

}
