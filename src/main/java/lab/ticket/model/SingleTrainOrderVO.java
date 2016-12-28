/*
 * 12306-hunter: Java Swing C/S版本12306订票助手
 * 本程序完全开放源代码，仅作为技术学习交流之用，不得用于任何商业用途
 */
package lab.ticket.model;

import lab.ticket.model.UserData.SeatType;

import java.util.Map;

public class SingleTrainOrderVO {

	private String loginUser;

	private Map<String, String> cookieData;
	
	private String trainDate;

	private String trainNo;

	private SeatType seatType;

	private TicketData ticketData;

	private TrainQuery trainQuery;

	private String submitOrderRequestToken;
	private String submitOrderRequestLeftTicketStr;
	
	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public Map<String, String> getCookieData() {
		return cookieData;
	}

	public void setCookieData(Map<String, String> cookieData) {
		this.cookieData = cookieData;
	}

	public String getSubmitOrderRequestToken() {
		return submitOrderRequestToken;
	}

	public void setSubmitOrderRequestToken(String submitOrderRequestToken) {
		this.submitOrderRequestToken = submitOrderRequestToken;
	}

	public String getSubmitOrderRequestLeftTicketStr() {
		return submitOrderRequestLeftTicketStr;
	}

	public void setSubmitOrderRequestLeftTicketStr(String submitOrderRequestLeftTicketStr) {
		this.submitOrderRequestLeftTicketStr = submitOrderRequestLeftTicketStr;
	}

	public String getTrainDate() {
		return trainDate;
	}

	public void setTrainDate(String trainDate) {
		this.trainDate = trainDate;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public void setSeatType(SeatType seatType) {
		this.seatType = seatType;
	}


	public TrainQuery getTrainQuery() {
		return trainQuery;
	}

	public void setTrainQuery(TrainQuery trainQuery) {
		this.trainQuery = trainQuery;
	}

	public TicketData getTicketData() {
		return ticketData;
	}

	public void setTicketData(TicketData ticketData) {
		this.ticketData = ticketData;
	}
}
