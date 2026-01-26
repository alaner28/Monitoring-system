package com.sipc.monitoringsystem;

// 对用户身份进行认证
import com.huaweicloud.sdk.cbs.v1.CbsClient;
// 导入cbs sdk
import com.huaweicloud.sdk.cbs.v1.model.ExecuteQaChatRequest;
import com.huaweicloud.sdk.cbs.v1.model.ExecuteQaChatResponse;
import com.huaweicloud.sdk.cbs.v1.model.PostRequestsReq;
import com.huaweicloud.sdk.cbs.v1.model.QaBotAnswer;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
// Http配置
import com.huaweicloud.sdk.core.http.HttpConfig;
import com.huaweicloud.sdk.core.region.Region;

import java.util.List;


public class Cbs {
    public static void main(String[] args) {
        // 认证用的ak和sk硬编码到代码中或者明文存储都有很大的安全风险，建议在配置文件或者环境变量中密文存放，使用时解密，确保安全；
        // 本示例以ak和sk保存在环境变量中来实现身份认证为例，运行示例前请先在本地环境中设置环境变量HUAWEICLOUD_SDK_AK和HUAWEICLOUD_SDK_SK。

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
        body.setQuestion("如何避免中暑");
        System.out.println(body.getQuestion());
        request.withBody(body);
        // 设置botId,可以参考前提条件的获取botid方法
        request.setQabotId("2ac36073-ae7d-4c36-b259-a2302ed441ff"); // bot id
        ExecuteQaChatResponse response = client.executeQaChat(request);



        System.out.println(body.getQuestion());
        System.out.println("\n\n\n\n\n");

        System.out.println(response.toString());

        System.out.println("\n\n\n\n\n");
        List<QaBotAnswer> answers = response.getQabotAnswers().getAnswers();
        String answer = answers.get(0).getAnswer();
        System.out.println(answer);

    }
}
