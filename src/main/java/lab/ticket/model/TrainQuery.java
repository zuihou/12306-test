package lab.ticket.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by tyh on 2016-12-26.
 */
public class TrainQuery {

    /**
     * train_no : 6c00000G6809
     * station_train_code : G68
     * start_station_telecode : IZQ
     * start_station_name : 广州南
     * end_station_telecode : BXP
     * end_station_name : 北京西
     * from_station_telecode : IZQ
     * from_station_name : 广州南
     * to_station_telecode : BXP
     * to_station_name : 北京西
     * start_time : 11:15
     * arrive_time : 20:56
     * day_difference : 0
     * train_class_name :
     * lishi : 09:41
     * canWebBuy : IS_TIME_NOT_BUY
     * lishiValue : 581
     * yp_info : oJMf5xlEefUggxrFkaF7%2BC8iUoNHU%2B7Qx4CH%2FlGuWGWizHOg
     * control_train_day : 20991231
     * start_train_date : 20161229
     * seat_feature : O3M393
     * yp_ex : O0M090
     * train_seat_feature : 3
     * seat_types : OM9
     * location_code : Q7
     * from_station_no : 01
     * to_station_no : 16
     * control_day : 29
     * sale_time : 1300
     * is_support_card : 0
     * controlled_train_flag : 0
     * controlled_train_message : 正常车次，不受控
     * train_type_code : 2
     * start_province_code : 16
     * start_city_code : 1502
     * end_province_code : 31
     * end_city_code : 0357
     * yz_num : --
     * rz_num : --
     * yw_num : --
     * rw_num : --
     * gr_num : --
     * zy_num : 有
     * ze_num : 有
     * tz_num : --
     * gg_num : --
     * yb_num : --
     * wz_num : --
     * qt_num : --
     * swz_num : 有
     */

    private QueryLeftNewDTOBean queryLeftNewDTO;
    /**
     * queryLeftNewDTO : {"train_no":"6c00000G6809","station_train_code":"G68","start_station_telecode":"IZQ","start_station_name":"广州南","end_station_telecode":"BXP","end_station_name":"北京西","from_station_telecode":"IZQ","from_station_name":"广州南","to_station_telecode":"BXP","to_station_name":"北京西","start_time":"11:15","arrive_time":"20:56","day_difference":"0","train_class_name":"","lishi":"09:41","canWebBuy":"IS_TIME_NOT_BUY","lishiValue":"581","yp_info":"oJMf5xlEefUggxrFkaF7%2BC8iUoNHU%2B7Qx4CH%2FlGuWGWizHOg","control_train_day":"20991231","start_train_date":"20161229","seat_feature":"O3M393","yp_ex":"O0M090","train_seat_feature":"3","seat_types":"OM9","location_code":"Q7","from_station_no":"01","to_station_no":"16","control_day":29,"sale_time":"1300","is_support_card":"0","controlled_train_flag":"0","controlled_train_message":"正常车次，不受控","train_type_code":"2","start_province_code":"16","start_city_code":"1502","end_province_code":"31","end_city_code":"0357","yz_num":"--","rz_num":"--","yw_num":"--","rw_num":"--","gr_num":"--","zy_num":"有","ze_num":"有","tz_num":"--","gg_num":"--","yb_num":"--","wz_num":"--","qt_num":"--","swz_num":"有"}
     * secretStr :
     * buttonTextInfo : 23:00-06:00系统维护时间
     */

    private String secretStr;
    private String buttonTextInfo;

    public QueryLeftNewDTOBean getQueryLeftNewDTO() {
        return queryLeftNewDTO;
    }

    public void setQueryLeftNewDTO(QueryLeftNewDTOBean queryLeftNewDTO) {
        this.queryLeftNewDTO = queryLeftNewDTO;
    }

    public String getSecretStr() {
        return secretStr;
    }

    public void setSecretStr(String secretStr) {
        this.secretStr = secretStr;
    }

    public String getButtonTextInfo() {
        return buttonTextInfo;
    }

    public void setButtonTextInfo(String buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
    }

    public static class QueryLeftNewDTOBean {
        private String train_no; // 火车编号
        private String station_train_code;  //车次
        private String start_station_telecode;
        private String start_station_name;
        private String end_station_telecode;
        private String end_station_name;
        private String from_station_telecode;
        private String from_station_name;
        private String to_station_telecode;
        private String to_station_name;
        private String start_time;
        private String arrive_time;
        private String day_difference;
        private String train_class_name;
        private String lishi;
        private String canWebBuy; //是否可以购买  Y:是 N:否 IS_TIME_NOT_BUY:不在
        private String lishiValue;
        private String yp_info;
        private String control_train_day;
        private String start_train_date;
        private String seat_feature;
        private String yp_ex;
        private String train_seat_feature;
        private String seat_types;
        private String location_code;
        private String from_station_no;
        private String to_station_no;
        private int control_day;
        private String sale_time;
        private String is_support_card;
        private String controlled_train_flag;
        private String controlled_train_message;
        private String train_type_code;
        private String start_province_code;
        private String start_city_code;
        private String end_province_code;
        private String end_city_code;
        private String yz_num;//硬座
        private String rz_num;//软座
        private String yw_num;//硬卧
        private String rw_num;//软卧
        private String gr_num;//高级软卧
        private String zy_num;//一等
        private String ze_num;//二等
        private String tz_num;//特等
        private String gg_num;//
        private String yb_num;//
        private String wz_num;//无座
        private String qt_num;//其他
        private String swz_num;//商务座

        public String getTrain_no() {
            return train_no;
        }

        public void setTrain_no(String train_no) {
            this.train_no = train_no;
        }

        public String getStation_train_code() {
            return station_train_code;
        }

        public void setStation_train_code(String station_train_code) {
            this.station_train_code = station_train_code;
        }

        public String getStart_station_telecode() {
            return start_station_telecode;
        }

        public void setStart_station_telecode(String start_station_telecode) {
            this.start_station_telecode = start_station_telecode;
        }

        public String getStart_station_name() {
            return start_station_name;
        }

        public void setStart_station_name(String start_station_name) {
            this.start_station_name = start_station_name;
        }

        public String getEnd_station_telecode() {
            return end_station_telecode;
        }

        public void setEnd_station_telecode(String end_station_telecode) {
            this.end_station_telecode = end_station_telecode;
        }

        public String getEnd_station_name() {
            return end_station_name;
        }

        public void setEnd_station_name(String end_station_name) {
            this.end_station_name = end_station_name;
        }

        public String getFrom_station_telecode() {
            return from_station_telecode;
        }

        public void setFrom_station_telecode(String from_station_telecode) {
            this.from_station_telecode = from_station_telecode;
        }

        public String getFrom_station_name() {
            return from_station_name;
        }

        public void setFrom_station_name(String from_station_name) {
            this.from_station_name = from_station_name;
        }

        public String getTo_station_telecode() {
            return to_station_telecode;
        }

        public void setTo_station_telecode(String to_station_telecode) {
            this.to_station_telecode = to_station_telecode;
        }

        public String getTo_station_name() {
            return to_station_name;
        }

        public void setTo_station_name(String to_station_name) {
            this.to_station_name = to_station_name;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getArrive_time() {
            return arrive_time;
        }

        public void setArrive_time(String arrive_time) {
            this.arrive_time = arrive_time;
        }

        public String getDay_difference() {
            return day_difference;
        }

        public void setDay_difference(String day_difference) {
            this.day_difference = day_difference;
        }

        public String getTrain_class_name() {
            return train_class_name;
        }

        public void setTrain_class_name(String train_class_name) {
            this.train_class_name = train_class_name;
        }

        public String getLishi() {
            return lishi;
        }

        public void setLishi(String lishi) {
            this.lishi = lishi;
        }

        public String getCanWebBuy() {
            return canWebBuy;
        }

        public void setCanWebBuy(String canWebBuy) {
            this.canWebBuy = canWebBuy;
        }

        public String getLishiValue() {
            return lishiValue;
        }

        public void setLishiValue(String lishiValue) {
            this.lishiValue = lishiValue;
        }

        public String getYp_info() {
            return yp_info;
        }

        public void setYp_info(String yp_info) {
            this.yp_info = yp_info;
        }

        public String getControl_train_day() {
            return control_train_day;
        }

        public void setControl_train_day(String control_train_day) {
            this.control_train_day = control_train_day;
        }

        public String getStart_train_date() {
            return start_train_date;
        }

        public void setStart_train_date(String start_train_date) {
            this.start_train_date = start_train_date;
        }

        public String getSeat_feature() {
            return seat_feature;
        }

        public void setSeat_feature(String seat_feature) {
            this.seat_feature = seat_feature;
        }

        public String getYp_ex() {
            return yp_ex;
        }

        public void setYp_ex(String yp_ex) {
            this.yp_ex = yp_ex;
        }

        public String getTrain_seat_feature() {
            return train_seat_feature;
        }

        public void setTrain_seat_feature(String train_seat_feature) {
            this.train_seat_feature = train_seat_feature;
        }

        public String getSeat_types() {
            return seat_types;
        }

        public void setSeat_types(String seat_types) {
            this.seat_types = seat_types;
        }

        public String getLocation_code() {
            return location_code;
        }

        public void setLocation_code(String location_code) {
            this.location_code = location_code;
        }

        public String getFrom_station_no() {
            return from_station_no;
        }

        public void setFrom_station_no(String from_station_no) {
            this.from_station_no = from_station_no;
        }

        public String getTo_station_no() {
            return to_station_no;
        }

        public void setTo_station_no(String to_station_no) {
            this.to_station_no = to_station_no;
        }

        public int getControl_day() {
            return control_day;
        }

        public void setControl_day(int control_day) {
            this.control_day = control_day;
        }

        public String getSale_time() {
            return sale_time;
        }

        public void setSale_time(String sale_time) {
            this.sale_time = sale_time;
        }

        public String getIs_support_card() {
            return is_support_card;
        }

        public void setIs_support_card(String is_support_card) {
            this.is_support_card = is_support_card;
        }

        public String getControlled_train_flag() {
            return controlled_train_flag;
        }

        public void setControlled_train_flag(String controlled_train_flag) {
            this.controlled_train_flag = controlled_train_flag;
        }

        public String getControlled_train_message() {
            return controlled_train_message;
        }

        public void setControlled_train_message(String controlled_train_message) {
            this.controlled_train_message = controlled_train_message;
        }

        public String getTrain_type_code() {
            return train_type_code;
        }

        public void setTrain_type_code(String train_type_code) {
            this.train_type_code = train_type_code;
        }

        public String getStart_province_code() {
            return start_province_code;
        }

        public void setStart_province_code(String start_province_code) {
            this.start_province_code = start_province_code;
        }

        public String getStart_city_code() {
            return start_city_code;
        }

        public void setStart_city_code(String start_city_code) {
            this.start_city_code = start_city_code;
        }

        public String getEnd_province_code() {
            return end_province_code;
        }

        public void setEnd_province_code(String end_province_code) {
            this.end_province_code = end_province_code;
        }

        public String getEnd_city_code() {
            return end_city_code;
        }

        public void setEnd_city_code(String end_city_code) {
            this.end_city_code = end_city_code;
        }

        public String getYz_num() {
            return yz_num;
        }

        public void setYz_num(String yz_num) {
            this.yz_num = yz_num;
        }

        public String getRz_num() {
            return rz_num;
        }

        public void setRz_num(String rz_num) {
            this.rz_num = rz_num;
        }

        public String getYw_num() {
            return yw_num;
        }

        public void setYw_num(String yw_num) {
            this.yw_num = yw_num;
        }

        public String getRw_num() {
            return rw_num;
        }

        public void setRw_num(String rw_num) {
            this.rw_num = rw_num;
        }

        public String getGr_num() {
            return gr_num;
        }

        public void setGr_num(String gr_num) {
            this.gr_num = gr_num;
        }

        public String getZy_num() {
            return zy_num;
        }

        public void setZy_num(String zy_num) {
            this.zy_num = zy_num;
        }

        public String getZe_num() {
            return ze_num;
        }

        public void setZe_num(String ze_num) {
            this.ze_num = ze_num;
        }

        public String getTz_num() {
            return tz_num;
        }

        public void setTz_num(String tz_num) {
            this.tz_num = tz_num;
        }

        public String getGg_num() {
            return gg_num;
        }

        public void setGg_num(String gg_num) {
            this.gg_num = gg_num;
        }

        public String getYb_num() {
            return yb_num;
        }

        public void setYb_num(String yb_num) {
            this.yb_num = yb_num;
        }

        public String getWz_num() {
            return wz_num;
        }

        public void setWz_num(String wz_num) {
            this.wz_num = wz_num;
        }

        public String getQt_num() {
            return qt_num;
        }

        public void setQt_num(String qt_num) {
            this.qt_num = qt_num;
        }

        public String getSwz_num() {
            return swz_num;
        }

        public void setSwz_num(String swz_num) {
            this.swz_num = swz_num;
        }

        /**
         * 检查给定的车次座位类型是否有效
         * @param trainData
         * @return
         */
        public boolean validateTrainData(TrainData trainData) {
            if (!trainData.getTrainNo().equalsIgnoreCase(this.getStation_train_code())) {
                return false;
            }
//            BUSS_SEAT("商务座", "9"),
//                    BEST_SEAT("特等座", "P"),
//                    ONE_SEAT("一等座", "M"),
//                    TWO_SEAT("二等座", "O"),
//                    VAG_SLEEPER("高级软卧", "6"),
//                    SOFT_SLEEPER("软卧", "4"),
//                    HARD_SLEEPER("硬卧", "3"),
//                    SOFT_SEAT("软座", "2"),
//                    HARD_SEAT("硬座", "1"),
//                    NONE_SEAT("无座", "-1"),
//                    OTH_SEAT("其他", "0");
            try {
                switch (trainData.getSeatType()) {
                    case BUSS_SEAT:
                        return "有".equals(this.getSwz_num()) || Integer.parseInt(this.getSwz_num()) > 0;
                    case BEST_SEAT:
                        return "有".equals(this.getTz_num()) || Integer.parseInt(this.getTz_num()) > 0;
                    case ONE_SEAT:
                        return "有".equals(this.getZy_num()) || Integer.parseInt(this.getZy_num()) > 0;
                    case TWO_SEAT:
                        return "有".equals(this.getZe_num()) || Integer.parseInt(this.getZe_num()) > 0;
                    case VAG_SLEEPER:
                        return "有".equals(this.getGr_num()) || Integer.parseInt(this.getGr_num()) > 0;
                    case SOFT_SLEEPER:
                        return "有".equals(this.getRw_num()) || Integer.parseInt(this.getRw_num()) > 0;
                    case HARD_SLEEPER:
                        return "有".equals(this.getYw_num()) || Integer.parseInt(this.getYw_num()) > 0;
                    case SOFT_SEAT:
                        return "有".equals(this.getRz_num()) || Integer.parseInt(this.getRz_num()) > 0;
                    case HARD_SEAT:
                        return "有".equals(this.getYz_num()) || Integer.parseInt(this.getYz_num()) > 0;
                    case NONE_SEAT:
                        return "有".equals(this.getWz_num()) || Integer.parseInt(this.getWz_num()) > 0;
                    case OTH_SEAT:
                        return "有".equals(this.getQt_num()) || Integer.parseInt(this.getQt_num()) > 0;
                }
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }

//            String st = seatDatas.get(trainData.getSeatType());
//            if (StringUtils.isBlank(st) || st.equals(TicketUtil.INVALID_SEAT_TYPE)) {
//                return false;
//            }
//            if(!this.seat_types.equals(trainData.getSeatType())){
//                return false;
//            }
            return true;
        }

        @Override
        public String toString() {
            return "QueryLeftNewDTOBean{" +
                    "train_no='" + train_no + '\'' +
                    ", station_train_code='" + station_train_code + '\'' +
                    ", start_station_telecode='" + start_station_telecode + '\'' +
                    ", start_station_name='" + start_station_name + '\'' +
                    ", end_station_telecode='" + end_station_telecode + '\'' +
                    ", end_station_name='" + end_station_name + '\'' +
                    ", from_station_telecode='" + from_station_telecode + '\'' +
                    ", from_station_name='" + from_station_name + '\'' +
                    ", to_station_telecode='" + to_station_telecode + '\'' +
                    ", to_station_name='" + to_station_name + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", arrive_time='" + arrive_time + '\'' +
                    ", day_difference='" + day_difference + '\'' +
                    ", train_class_name='" + train_class_name + '\'' +
                    ", lishi='" + lishi + '\'' +
                    ", canWebBuy='" + canWebBuy + '\'' +
                    ", lishiValue='" + lishiValue + '\'' +
                    ", yp_info='" + yp_info + '\'' +
                    ", control_train_day='" + control_train_day + '\'' +
                    ", start_train_date='" + start_train_date + '\'' +
                    ", seat_feature='" + seat_feature + '\'' +
                    ", yp_ex='" + yp_ex + '\'' +
                    ", train_seat_feature='" + train_seat_feature + '\'' +
                    ", seat_types='" + seat_types + '\'' +
                    ", location_code='" + location_code + '\'' +
                    ", from_station_no='" + from_station_no + '\'' +
                    ", to_station_no='" + to_station_no + '\'' +
                    ", control_day=" + control_day +
                    ", sale_time='" + sale_time + '\'' +
                    ", is_support_card='" + is_support_card + '\'' +
                    ", controlled_train_flag='" + controlled_train_flag + '\'' +
                    ", controlled_train_message='" + controlled_train_message + '\'' +
                    ", train_type_code='" + train_type_code + '\'' +
                    ", start_province_code='" + start_province_code + '\'' +
                    ", start_city_code='" + start_city_code + '\'' +
                    ", end_province_code='" + end_province_code + '\'' +
                    ", end_city_code='" + end_city_code + '\'' +
                    ", yz_num='" + yz_num + '\'' +
                    ", rz_num='" + rz_num + '\'' +
                    ", yw_num='" + yw_num + '\'' +
                    ", rw_num='" + rw_num + '\'' +
                    ", gr_num='" + gr_num + '\'' +
                    ", zy_num='" + zy_num + '\'' +
                    ", ze_num='" + ze_num + '\'' +
                    ", tz_num='" + tz_num + '\'' +
                    ", gg_num='" + gg_num + '\'' +
                    ", yb_num='" + yb_num + '\'' +
                    ", wz_num='" + wz_num + '\'' +
                    ", qt_num='" + qt_num + '\'' +
                    ", swz_num='" + swz_num + '\'' +
                    '}';
        }
    }



    @Override
    public String toString() {
        return "TrainQuery{" +
                "queryLeftNewDTO=" + JSONObject.toJSONString(queryLeftNewDTO) +
                ", secretStr='" + secretStr + '\'' +
                ", buttonTextInfo='" + buttonTextInfo + '\'' +
                '}';
    }
}
