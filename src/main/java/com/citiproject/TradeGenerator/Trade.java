package com.citiproject.TradeGenerator;

import java.util.GregorianCalendar; 

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "trade")
public class Trade {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "securityname")
	private String securityname;

	@Column(name = "isin")
	private String isin;

	@Column(name = "price")
	private double price;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "date")
	private GregorianCalendar date;

	@Column(name = "tradetype")
	private String tradetype;
	
	@Column(name = "faceValue")
	private int faceValue ;
	
	@Column(name = "couponRate")
	private float couponRate;
	
	@Column(name = "couponpaymentdate")
	private GregorianCalendar couponpaymentdate;
	
	@Column(name = "bonusdate")
	private GregorianCalendar bonusdate;
	
	@Column(name = "maturity")
	private GregorianCalendar maturity;
	
	@Column(name = "dcc")
	private String dcc;

	public Trade() {
	}

	public Trade(String securityname, String isin, double price, int quantity, GregorianCalendar date,
			String tradetype, int faceValue, float couponRate, GregorianCalendar couponpaymentdate, GregorianCalendar maturity, String dcc,GregorianCalendar bonusdate) {
		super();
		//this.id = id;
		this.securityname = securityname;
		this.isin = isin;
		this.price = price;
		this.quantity = quantity;
		this.date = date;
		this.tradetype = tradetype;
		this.faceValue = faceValue;
		this.couponRate = couponRate;
		this.couponpaymentdate = couponpaymentdate;
		this.maturity = maturity;
		this.dcc = dcc;
		this.bonusdate=bonusdate;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public GregorianCalendar getBonusdate() {
		return bonusdate;
	}

	public void setBonusdate(GregorianCalendar bonusdate) {
		this.bonusdate = bonusdate;
	}

	public String getSecurityname() {
		return securityname;
	}

	public void setSecurityname(String securityname) {
		this.securityname = securityname;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public GregorianCalendar getDate() {
		return date;
	}

	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	public String getTradetype() {
		return tradetype;
	}

	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}

	public int getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(int faceValue) {
		this.faceValue = faceValue;
	}

	public float getCouponRate() {
		return couponRate;
	}

	public void setCouponRate(float couponRate) {
		this.couponRate = couponRate;
	}

	public GregorianCalendar getCouponpaymentdate() {
		return couponpaymentdate;
	}

	public void setCouponpaymentdate(GregorianCalendar couponpaymentdate) {
		this.couponpaymentdate = couponpaymentdate;
	}

	public GregorianCalendar getMaturity() {
		return maturity;
	}
	
	public void setMaturity(GregorianCalendar maturity) {
		this.maturity = maturity;
	}

	public String getDcc() {
		return dcc;
	}

	public void setDcc(String dcc) {
		this.dcc = dcc;
	}

	@Override
	public String toString() {
		return "Trade [id=" + id + ", securityname=" + securityname + ", isin=" + isin + ", price=" + price
				+ ", quantity=" + quantity + ", date=" + date + ", tradetype=" + tradetype + ", faceValue=" + faceValue + ", couponRate=" + couponRate + ", couponpaymentdate=" + couponpaymentdate
				+ ", maturity=" + maturity + ", dcc=" + dcc + "]";
	}

}