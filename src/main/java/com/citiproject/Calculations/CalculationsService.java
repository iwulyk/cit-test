package com.citiproject.Calculations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

import com.citiproject.TradeGenerator.*;

@Service
public class CalculationsService {

	@Autowired
	private CalculationsRepository calculationsRepository;

	//coupon income 
	public Map<String,Double> Couponincome(List<Trade> tradelist,List<FixedIncomeSecurity> masterdb) 
	   {
		   System.out.println("Inside coupon income");
		   System.out.println(tradelist.size());
		   
			int i=0;
		   
		   Map<String,Double> allcouponincomes = new HashMap<>();
		   int k=0;
		   String bonussecurity=null;
		   while(k<masterdb.size())
		   {
			   if(masterdb.get(k).getBonusdate()!=null)
			   {
				   bonussecurity=masterdb.get(k).getSecurityname();
			   }
			   k++;
		   }
		   
		   while(i<masterdb.size())
		   {
			   FixedIncomeSecurity currentsecurity = masterdb.get(i);
			   String securityname = currentsecurity.getSecurityname();
			   int facevalue = currentsecurity.getFaceValue();
			   float couponrate = currentsecurity.getCouponRate();
			   int netqty = currentsecurity.getOpeningqty();
			   double couponincome =0 ;
			   int bonusqty=0;
			   GregorianCalendar bonusdate=currentsecurity.getBonusdate();
			   
			   int j=0;
		   while(j<tradelist.size() && currentsecurity.getCouponpaymentdate()!= null)
			{
			   GregorianCalendar coupondate = currentsecurity.getCouponpaymentdate();
				Trade trade = new Trade();
				trade = tradelist.get(j);
				if(trade.getSecurityname().equalsIgnoreCase(bonussecurity) && trade.getDate().before(bonusdate) && bonusdate.before(coupondate))
				{
				String tradetype = trade.getTradetype();
				bonusqty=currentsecurity.getOpeningqty();
				if(tradetype.equalsIgnoreCase("buy"))
				{
					bonusqty = bonusqty + trade.getQuantity();
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{
					bonusqty = bonusqty - trade.getQuantity();
				}
				}
				if(trade.getSecurityname().equalsIgnoreCase(securityname) && trade.getDate().before(coupondate))
				{
				String tradetype = trade.getTradetype();
				if(tradetype.equalsIgnoreCase("buy"))
				{
					netqty = netqty + trade.getQuantity();
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{
					netqty = netqty - trade.getQuantity();
				}
				}
				j++;			
			}
		   
		   if(tradelist.size()!=0){
		   if(currentsecurity.getDcc() != null)
		   {
			   String dcc = currentsecurity.getDcc();
			   couponincome = (double)((netqty+bonusqty)*facevalue*0.01*couponrate);
		   }
		   }
		   
		   allcouponincomes.put(securityname, couponincome);
		   i++;
		   }
			
		   return allcouponincomes;
	   }
	   
	   public Map<String,Double> Accruedcouponincome(List<Trade> tradelist,List<FixedIncomeSecurity> masterdb)
	   {
		   int i=0;
		   
		  	   Map<String,Double> allcouponincomes = new HashMap<>();
		  	 int k=0;
			   String bonussecurity=null;
			   while(k<masterdb.size())
			   {
				   if(masterdb.get(k).getBonusdate()!=null)
				   {
					   bonussecurity=masterdb.get(k).getSecurityname();
				   }
				   k++;
			   }
		   
		   while(i<masterdb.size())
		   {
			   FixedIncomeSecurity currentsecurity = masterdb.get(i);
			   String securityname = currentsecurity.getSecurityname();
			   int facevalue = currentsecurity.getFaceValue();
			   float couponrate = currentsecurity.getCouponRate();
			   double couponincome =0 ;
			   int sellqty=0;
			   int qty=currentsecurity.getOpeningqty();
			   int bonusqty=0;
			   GregorianCalendar bonusdate=currentsecurity.getBonusdate();
			   
			   int j=0;
		   while(j<tradelist.size() && currentsecurity.getCouponpaymentdate()!= null)
			{
			   
				Trade trade = new Trade();
				trade = tradelist.get(j);
				if(trade.getSecurityname().equalsIgnoreCase(bonussecurity) && trade.getDate().before(bonusdate))
				{bonusqty=currentsecurity.getOpeningqty();
				String tradetype = trade.getTradetype();
				if(tradetype.equalsIgnoreCase("buy"))
				{
					bonusqty = bonusqty + trade.getQuantity();
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{
					bonusqty = bonusqty - trade.getQuantity();
				}
				}
				if(trade.getSecurityname().equalsIgnoreCase(securityname))
				{
				String tradetype = trade.getTradetype();
				if(tradetype.equalsIgnoreCase("buy"))
				{if(trade.getDate().after(currentsecurity.getCouponpaymentdate())){
					String dcc = currentsecurity.getDcc();
					GregorianCalendar yearend = new GregorianCalendar(2020,11,31);
					   long days = (yearend.getTimeInMillis() - trade.getDate().getTimeInMillis()) / (86400000);
					   if(dcc.equalsIgnoreCase("a/360")) {
						   
						   couponincome =  couponincome+(double)((trade.getQuantity())*facevalue*0.01*couponrate*days)/360;
					   }
					   if(dcc.equalsIgnoreCase("a/365")) {

						   couponincome =  couponincome+(double)((trade.getQuantity())*facevalue*0.01*couponrate*days)/365;
					   }
					   if(dcc.equalsIgnoreCase("a/366")) {
						   couponincome =  couponincome+(double)((trade.getQuantity())*facevalue*0.01*couponrate*days)/366;
					   }
					   if(dcc.equalsIgnoreCase("30/360")) {
						   int month= yearend.get(Calendar.MONTH)- trade.getDate().get(Calendar.MONTH);
						   int day= yearend.get(Calendar.DAY_OF_MONTH)- trade.getDate().get(Calendar.DAY_OF_MONTH);
						   int a=(month*30)+day;
						   couponincome =  couponincome+(double)((trade.getQuantity())*facevalue*0.01*couponrate*a)/360;
					   }
				}
				if(trade.getDate().before(currentsecurity.getCouponpaymentdate())) {
					qty=qty+trade.getQuantity();
				}
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{if(trade.getDate().after(currentsecurity.getCouponpaymentdate())) {
					sellqty =  sellqty+ trade.getQuantity();}
				if(trade.getDate().before(currentsecurity.getCouponpaymentdate())) {
					qty=qty-trade.getQuantity();
				}
				}
				}
				j++;	
			}
		   if(currentsecurity.getCouponpaymentdate()!= null){
		   String dcc = currentsecurity.getDcc();
		   GregorianCalendar coupondate = currentsecurity.getCouponpaymentdate();
	   GregorianCalendar yearend = new GregorianCalendar(2020,11,31);
	   long netdays = (yearend.getTimeInMillis() - coupondate.getTimeInMillis()) / (86400000);

	   if(dcc.equalsIgnoreCase("a/360")) {
		   
		   couponincome =  couponincome+(double)((qty+bonusqty-sellqty)*facevalue*0.01*couponrate*netdays)/360;
	   }
	   if(dcc.equalsIgnoreCase("a/365")) {

		   couponincome =  couponincome+(double)((qty+bonusqty-sellqty)*facevalue*0.01*couponrate*netdays)/365;
	   }
	   if(dcc.equalsIgnoreCase("a/366")) {
		   couponincome =  couponincome+(double)((qty+bonusqty-sellqty)*facevalue*0.01*couponrate*netdays)/366;
	   }
	   if(dcc.equalsIgnoreCase("30/360")) {
		   int month= yearend.get(Calendar.MONTH)- coupondate.get(Calendar.MONTH);
		   int day= yearend.get(Calendar.DAY_OF_MONTH)- coupondate.get(Calendar.DAY_OF_MONTH);
		   int a=(month*30)+day;
		   couponincome =  couponincome+(double)((qty+bonusqty-sellqty)*facevalue*0.01*couponrate*a)/360;
	   }
		   }  allcouponincomes.put(securityname, couponincome);
		   i++;
		   }
			
		   return allcouponincomes;

	   }
	   
	   public Map<String,Double> PLpersecurity(List<Trade> tradelist,List<FixedIncomeSecurity> masterdb) 
		
	   {
			int i=0;
			int k=0;
			   String bonussecurity=null;
			   while(k<masterdb.size())
			   {
				   if(masterdb.get(k).getBonusdate()!=null)
				   {
					   bonussecurity=masterdb.get(k).getSecurityname();
				   }
				   k++;
			   }
			
			
			double finalprice = 0;
			double ufinalprice=0;
			Map<String,Double> allPLS = new HashMap<>();
			while(i<masterdb.size())
			{
				FixedIncomeSecurity currentsecurity = masterdb.get(i);
				int netbuyqty=currentsecurity.getOpeningqty();
				double netbuyprice = (currentsecurity.getOpeningqty() * currentsecurity.getOpeningprice());
				int netsellqty =0 ;
				double finalmp=currentsecurity.getFinalprice();
				int bonusqty=0;
				GregorianCalendar bonusdate=currentsecurity.getBonusdate();
				
				double netsellprice =0 ;
			int j=0;
			String securityname = masterdb.get(i).getSecurityname();
			while(j<tradelist.size())
			{
				Trade trade = new Trade();
				trade = tradelist.get(j);
				if(trade.getSecurityname().equalsIgnoreCase(bonussecurity) && trade.getDate().before(bonusdate))
				{bonusqty=currentsecurity.getOpeningqty();
				String tradetype = trade.getTradetype();
				if(tradetype.equalsIgnoreCase("buy"))
				{
					bonusqty = bonusqty + trade.getQuantity();
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{
					bonusqty = bonusqty - trade.getQuantity();
				}
				}
				if(trade.getSecurityname().equalsIgnoreCase(securityname))
				{
				String tradetype = trade.getTradetype();
				if(tradetype.equalsIgnoreCase("buy"))
				{
					netbuyprice = netbuyprice + (trade.getQuantity() * trade.getPrice());
					netbuyqty = netbuyqty + trade.getQuantity();
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{
					netsellprice = netsellprice + (trade.getQuantity() * trade.getPrice());
					netsellqty = netsellqty + trade.getQuantity();
				}
				}
				j++;
			}
			
			if(netsellqty==0)
				finalprice=0;
			else
				finalprice =( (netsellprice / netsellqty) - (netbuyprice / (netbuyqty+bonusqty)) ) * (netsellqty);
			
			allPLS.put(securityname, finalprice);
			i++;	
			}
			 
			return allPLS;
		}
	 
	   public Map<String,Double> UPLpersecurity(List<Trade> tradelist,List<FixedIncomeSecurity> masterdb)
		
	   {
			int i=0;
			GregorianCalendar newyear = new GregorianCalendar();
			newyear.set(Calendar.YEAR, 2021);
			newyear.set(Calendar.MONTH, 0);
			newyear.set(Calendar.DATE, 01);
			int k=0;
			   String bonussecurity=null;
			   while(k<masterdb.size())
			   {
				   if(masterdb.get(k).getBonusdate()!=null)
				   {
					   bonussecurity=masterdb.get(k).getSecurityname();
				   }
				   k++;
			   }
			double finalprice = 0;
			double ufinalprice=0;
			Map<String,Double> UallPLS = new HashMap<>();
			while(i<masterdb.size())
			{
				FixedIncomeSecurity currentsecurity = masterdb.get(i);
				int netbuyqty=currentsecurity.getOpeningqty();
				double netbuyprice = (currentsecurity.getOpeningqty() * currentsecurity.getOpeningprice());
				int netsellqty =0 ;
				double finalmp=currentsecurity.getFinalprice();
				int bonusqty=0;
				GregorianCalendar bonusdate=currentsecurity.getBonusdate();
				
				double netsellprice =0 ;
			int j=0;
			String securityname = masterdb.get(i).getSecurityname();
			while(j<tradelist.size())
			{
				Trade trade = new Trade();
				trade = tradelist.get(j);
				if(trade.getSecurityname().equalsIgnoreCase(bonussecurity) && trade.getDate().before(bonusdate))
				{bonusqty=currentsecurity.getOpeningqty();
				String tradetype = trade.getTradetype();
				if(tradetype.equalsIgnoreCase("buy"))
				{
					bonusqty = bonusqty + trade.getQuantity();
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{
					bonusqty = bonusqty - trade.getQuantity();
				}
				}
				if(trade.getSecurityname().equalsIgnoreCase(securityname))
				{
				String tradetype = trade.getTradetype();
				if(tradetype.equalsIgnoreCase("buy"))
				{
					netbuyprice = netbuyprice + (trade.getQuantity() * trade.getPrice());
					netbuyqty = netbuyqty + trade.getQuantity();
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{
					netsellprice = netsellprice + (trade.getQuantity() * trade.getPrice());
					netsellqty = netsellqty + trade.getQuantity();
				}
				}
				j++;
			}
			
			finalprice =( (netbuyqty+bonusqty-netsellqty) *(finalmp-(netbuyprice/(netbuyqty+bonusqty))));
			UallPLS.put(securityname, finalprice);
			i++;	
			}
			 
			return UallPLS;
		}
	 
	   public double Closingfund(List<Trade> tradelist,List<FixedIncomeSecurity> masterdb)
	   {Map<String,Double> clf = new HashMap<>();
	   clf=Couponincome( tradelist, masterdb);
		   
		   double a=0;
		   Iterator<Map.Entry<String, Double>> itr = clf.entrySet().iterator(); 
	       
	       while(itr.hasNext()) 
	       { 
	            Map.Entry<String, Double> entry = itr.next();
	            a=a+entry.getValue();
	 
	       } 
		   int i=0;
		   double fund=masterdb.get(1).getOpeningfund()+a;
		   while(i<tradelist.size())
		   {
			   Trade trade = new Trade();
			   trade = tradelist.get(i);
			   String tradetype = trade.getTradetype();
			   if(tradetype.equalsIgnoreCase("buy"))
			   {
				   fund=fund-(trade.getQuantity() * trade.getPrice());
			   }
			   if(tradetype.equalsIgnoreCase("sell"))
			   {
				   fund=fund+(trade.getQuantity() * trade.getPrice());
			   }
			   i++;
		   }
		   return fund;
	   	}

	   public double MarketValuation(List<Trade> tradelist,List<FixedIncomeSecurity> masterdb)
		
	   {
			int i=0;
			int k=0;
			   String bonussecurity=null;
			   while(k<masterdb.size())
			   {
				   if(masterdb.get(k).getBonusdate()!=null)
				   {
					   bonussecurity=masterdb.get(k).getSecurityname();
				   }
				   k++;
			   }
			
			double mvalue=0;

			Map<String,Double> allPLS = new HashMap<>();
			
			while(i<masterdb.size())
			{
				FixedIncomeSecurity currentsecurity = masterdb.get(i);
				int netqty=currentsecurity.getOpeningqty();
				double finalmp=currentsecurity.getFinalprice();
				int bonusqty=0;
				GregorianCalendar bonusdate=currentsecurity.getBonusdate();
				
				int netsellprice =0 ;
			int j=0;
			String securityname = masterdb.get(i).getSecurityname();
			while(j<tradelist.size())
			{
				Trade trade = new Trade();
				trade = tradelist.get(j);
				if(trade.getSecurityname().equalsIgnoreCase(bonussecurity) && trade.getDate().before(bonusdate))
				{bonusqty=currentsecurity.getOpeningqty();
				String tradetype = trade.getTradetype();
				if(tradetype.equalsIgnoreCase("buy"))
				{
					bonusqty = bonusqty + trade.getQuantity();
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{
					bonusqty = bonusqty - trade.getQuantity();
				}
				}
				if(trade.getSecurityname().equalsIgnoreCase(securityname))
				{
				String tradetype = trade.getTradetype();
				if(tradetype.equalsIgnoreCase("buy"))
				{
					netqty = netqty + trade.getQuantity();
				}
				if(tradetype.equalsIgnoreCase("sell"))
				{
					netqty = netqty - trade.getQuantity();
				}
				
			}
			mvalue=mvalue+finalmp*(netqty+bonusqty);
			j++;	
			}
			i++;
			}	 
			return mvalue;
		}
	 
	}
