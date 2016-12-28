import lab.ticket.model.SingleTrainOrderVO;
import lab.ticket.model.TicketData;
import lab.ticket.model.TrainQuery;
import lab.ticket.model.UserData;
import lab.ticket.service.HttpClientService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tyh on 2016-12-27.
 */
public class TestPost {


//    @Test
    public void testsubmitOrderRequest2() throws  Exception {
        HttpClientService h = new HttpClientService();
        Map<String, String> map = new HashMap<>();
        map.put("JSESSIONID","C6EE060AF1613ADF0B2E880A00F7B7C8");
        map.put("BIGipServerotn","602407178.24610.0000");
        map.put("current_captcha_type","Z");

        HttpClient httpClient = h.buildHttpClient();
        Map<String, String> map2 = new HashMap<>();
        map2.put("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
        HttpResponse response = h.getHttpRequest(httpClient,
                "https://kyfw.12306.cn/otn/leftTicket/init", null, null, map2);

        HttpEntity entity = response.getEntity();
        String htmlcontent = EntityUtils.toString(entity, "utf-8");
        h.extractKey(htmlcontent,map);



        TicketData ticketData = new TicketData();
        ticketData.setTrainFrom("南宁");
        ticketData.setTrainTo("贵阳");
        UserData userData = new UserData();

        userData.setCookieData(map);
        List<TrainQuery> trainQueries = h.queryTrain(ticketData, userData, "2016-12-29");

        TrainQuery trainQuery = null;
        for(TrainQuery tq : trainQueries){
            if(tq.getQueryLeftNewDTO().getStation_train_code().equals("D3566")){
                trainQuery = tq;
                break;
            }
        }
        Thread.sleep(5000);
        SingleTrainOrderVO o = new SingleTrainOrderVO();
        ticketData = new TicketData();
        ticketData.setTrainFrom("南宁");
        ticketData.setTrainTo("贵阳");
        o.setTicketData(ticketData);

        o.setTrainQuery(trainQuery);
        o.setTrainDate("2016-12-29");
        o.setCookieData(map);
        h.submitOrderRequest2(o);


    }

}
