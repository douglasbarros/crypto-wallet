package com.dbs.cryptowallet.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

	private BigDecimal total;

	@JsonProperty("best_asset")
	private String bestAsset;

	@JsonProperty("best_performance")
	private BigDecimal bestPerformance;

	@JsonProperty("worst_asset")
	private String worstAsset;

	@JsonProperty("worst_performance")
	private BigDecimal worstPerformance;

	public BigDecimal getTotal() {
		return total.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getBestPerformance() {
		return bestPerformance.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getWorstPerformance() {
		return worstPerformance.setScale(2, RoundingMode.HALF_UP);
	}

}
