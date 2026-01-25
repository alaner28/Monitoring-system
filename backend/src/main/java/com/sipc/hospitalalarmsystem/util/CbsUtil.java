package com.sipc.hospitalalarmsystem.util;

import com.huaweicloud.sdk.cbs.v1.CbsClient;
import com.huaweicloud.sdk.cbs.v1.model.ExecuteQaChatRequest;
import com.huaweicloud.sdk.cbs.v1.model.ExecuteQaChatResponse;
import com.huaweicloud.sdk.cbs.v1.model.PostRequestsReq;
import com.huaweicloud.sdk.cbs.v1.model.QaBotAnswer;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CbsUtil {


public String cbs(String  question){

    String ak = "QHXOVBGHRQ3ZXKTFIZEP";
    String sk = "YLDZv807hVMlCHWlrXh5tJfIxgtMO1uZN1TcQOR3";
    String projectid = "bab5d3203fc04e22b399ea3259d59736";


    // 获取iam 站点 https://support.huaweicloud.com/api-iam/iam_16_0005.html
    String iamEndpoint = "https://iam.cn-north-4.myhuaweicloud.com"; //
    // 获取cbs 站点信息 https://support.huaweicloud.com/api-cbs/cbs_03_0102.html
    String endpoint = "https://cbs-ext.cn-north-4.myhuaweicloud.com"; //
    // 网络配置信息
    HttpConfig config = new HttpConfig();
    config.withIgnoreSSLVerification(true);
    // 鉴权信息
    ICredential auth = new BasicCredentials()
            .withIamEndpoint(iamEndpoint)
            .withProjectId(projectid)
            .withAk(ak)
            .withSk(sk);
    // 初始化cbsClient
    CbsClient client = CbsClient.newBuilder()
            .withCredential(auth)
            .withHttpConfig(config)
            .withRegion(new Region("cn-north-4", endpoint))
            .build();
    ExecuteQaChatRequest request = new ExecuteQaChatRequest();
    PostRequestsReq body = new PostRequestsReq();
    // 设置请求问题 相关参数可以参考：https://support.huaweicloud.com/api-cbs/cbs_03_0115.html
    body.setQuestion(question);
    System.out.println(body.getQuestion());
    request.withBody(body);
    // 设置botId,可以参考前提条件的获取botid方法
    request.setQabotId("2ac36073-ae7d-4c36-b259-a2302ed441ff"); // bot id
    ExecuteQaChatResponse response = client.executeQaChat(request);
    //获取答案
    List<QaBotAnswer> answers = response.getQabotAnswers().getAnswers();
    String answer = answers.get(0).getAnswer();
    //  log.info(answer);
    return  answer;
}


}
