package com.dbs.cryptowallet.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CoinPrice {

	private Double priceUsd;
	private Long time;
	private Date date;

}