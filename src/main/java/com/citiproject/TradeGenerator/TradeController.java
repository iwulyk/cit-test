package com.citiproject.TradeGenerator;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citiproject.Calculations.CalculationsService;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TradeController {

	@Autowired
	private TradeService tradeService;
	
	@Autowired
	private CalculationsService calculationsService;
	
	List<FixedIncomeSecurity> masterDB = Arrays.asList(
			new FixedIncomeSecurity("IN1015467857", 100, "GOVERNMENT OF INDIA T-BILL", 0, 2020, 9, 30),
			new FixedIncomeSecurity("INE002A14F01", 1000, "RELIANCE INDUSTRIES COMMERCIAL PAPER", 6, 2020, 02, 15, 2021,
					02, 15, "a/360"),
			new FixedIncomeSecurity("INE002A14F02", 1000, "STATE BANK OF INDIA CD", 4, 2020, 06, 30, 2021, 04, 30,
					"a/365"),
			new FixedIncomeSecurity("INE002A14F03", 1000, "ABG SHIPYARD LTD. DEBENTURE", 5, 2020, 04, 10, 2026, 8, 10,
					"a/366"),
			new FixedIncomeSecurity("INE002A14F04", 100, "GOVERNMENT OF INDIA BOND", 4.5f, 2020, 10, 25, 2021, 06, 27,
					"30/360"));

	List<Trade> tradelist;
	
	RandomValueGenerator rvg = new RandomValueGenerator();
	@GetMapping("/masterdb")
	public List<FixedIncomeSecurity> getMasterDB() {
		return masterDB;
	}
	
	@GetMapping("/trades")
	public ResponseEntity<List<Trade>> getAllTrades() {
		tradelist = tradeService.getAllTrades().getBody();
		return tradeService.getAllTrades();
	}

	@GetMapping("/trades/{id}")
	public ResponseEntity<Trade> getTradeById(@PathVariable("id") long id) {
		return tradeService.getTradeById(id);
	}

	
	@GetMapping(value = "/trades/list")
	public List<Trade> getTradeList() {
		 return tradelist;	
	}
	
	@PostMapping(value = "/trades/add")
	public ResponseEntity<Trade> addTrade(@RequestBody Trade newTrade) {
		return tradeService.addTradeByUser(tradelist, newTrade, masterDB);
	}
	
	@GetMapping("/couponincome")
	public Map<String, Double> getCouponIncomes(){
		return calculationsService.Couponincome(tradelist,masterDB);
	}
	
	@GetMapping("/acccouponincome")
	public Map<String, Double> getAccruedCouponIncomes(){
		return calculationsService.Accruedcouponincome(tradelist,masterDB);
	}
	
	@GetMapping("/closingfund")
	public Double getClosingFund(){
		return calculationsService.Closingfund(tradelist,masterDB);
	}

	@GetMapping("/pnl")
	public Map<String, Double> getProfitAndLoss(){
		return calculationsService.PLpersecurity(tradelist,masterDB);
	}
	
	@GetMapping("/upnl")
	public Map<String, Double> getUnrealisedProfitAndLoss(){
		return calculationsService.UPLpersecurity(tradelist,masterDB);
	}
	
	@GetMapping("/mvalue")
	public double getMarketValuation(){
		return calculationsService.MarketValuation(tradelist, masterDB);
	}
	

	@PostMapping(value = "/trades/all")
	public List<Trade> createTradeList() {
		masterDB=rvg.UpdateMasterDb(masterDB);
		int i=0;
		while(i<masterDB.size()) {
			if(masterDB.get(i).getBonusdate()!=null) {
				System.out.println(masterDB.get(i).getSecurityname()+ " " + masterDB.get(i).getBonusdate());
			}
			i++;
		}
		
		tradelist = tradeService.generateInitialTrades(masterDB);
		
		return tradelist;
	}
	
	@GetMapping("/logout")
	public void userLogout() {
		tradelist.clear();
		
		System.out.println("Inside logout");


	}
	
}