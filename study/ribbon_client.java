package so.dian.apollo.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.netflix.config.ConfigurationManager;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.hystrix.HystrixFeign;
import feign.ribbon.RibbonClient;
import lombok.experimental.Builder;
import so.dian.apollo.api.desktop.ApolloDesktopApi;

import java.util.Date;
import java.util.Properties;

/**
 * Created with apollo-all
 *
 * @author: damao
 * @date: Created in 2017-11-22 14:37
 * @description:
 */
@Builder
public class ApolloClient {

    private String token;

    private String appName;

    private String appSecret;

    private String env;

    public ApolloApiFactory getApolloApi() {

        //TODO STEP1 vaild token

        // STEP2 load apollo ribbon properties
        Properties ribbonProperties = new Properties();
        ribbonProperties.setProperty("apollo.ribbon.NFLoadBalancerPingClassName", "com.netflix.loadbalancer.PingConstant");
        ribbonProperties.setProperty("apollo.ribbon.NFLoadBalancerClassName", "com.netflix.loadbalancer.DynamicServerListLoadBalancer");
        ribbonProperties.setProperty("apollo.ribbon.NIWSServerListClassName", "com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList");
        ribbonProperties.setProperty("apollo.ribbon.NFLoadBalancerRuleClassName", "com.netflix.loadbalancer.AvailabilityFilteringRule");
        ribbonProperties.setProperty("apollo.ribbon.DeploymentContextBasedVipAddresses", "apollo");
        ConfigurationManager.loadProperties(ribbonProperties);

        // STEP3 init apollo HystrixFeign API
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        Date.class,
                        (JsonDeserializer<Date>) (jsonElement, type, context) -> new Date(jsonElement.getAsJsonPrimitive().getAsLong()))
                .create();
        ApolloDesktopApi apolloDesktopApi = HystrixFeign.builder()
                .encoder(new GsonEncoder(gson))
                .decoder(new GsonDecoder(gson))
                .client(RibbonClient.create())
                .target(ApolloDesktopApi.class, "http://apollo", new ApolloDesktopApi.ApolloDesktopApiFallback());

        return ApolloApiFactory.builder().apolloDesktopApi(apolloDesktopApi).build();
    }

}

